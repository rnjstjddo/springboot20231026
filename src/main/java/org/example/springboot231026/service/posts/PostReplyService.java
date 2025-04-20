package org.example.springboot231026.service.posts;


import org.example.springboot231026.domain.member.Member;
import org.example.springboot231026.domain.member.MemberRepository;
import org.example.springboot231026.domain.posts.PostReply;
import org.example.springboot231026.domain.posts.PostReplyRepository;
import org.example.springboot231026.domain.posts.Posts;
import org.example.springboot231026.domain.posts.PostsRepository;
import org.example.springboot231026.domain.posts.search.PostsSearchImpl;
import org.example.springboot231026.dto.member.MemberDTO;
import org.example.springboot231026.dto.page.PageRequestDTO;
import org.example.springboot231026.dto.page.PageResponseDTO;
import org.example.springboot231026.dto.post.PostReplyDTO;
import org.example.springboot231026.dto.post.PostsListResponseDto;
import org.example.springboot231026.dto.post.PostsResponseDto;
import org.example.springboot231026.service.member.MemberService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostReplyService {

    @Autowired
    private PostReplyRepository prr;

    @Autowired
    private PostsRepository pr;

    @Autowired
    private PostsService ps;

    @Autowired
    private MemberRepository mr;

    @Autowired
    private MemberService ms;

    @Autowired
    private ModelMapper mm;

    @Autowired
    private PostsSearchImpl psi;
    
    //댓글목록
    public List<PostReplyDTO> list(Long pno){
        //System.out.println("service-postsposts패키지 PostReplyService클래스 list() 진입 파라미터 pno -> "+ pno);

        List<PostReply> list =prr.list(pno);


        List<PostReplyDTO> dtoList = list.stream().map(entity -> new PostReplyDTO(
                entity.getPrno(), entity.getPno(), entity.getComment(),  entity.getMember().getName(),
                        entity.getModifiedDate() , entity.getCreatedDate()))
                .collect(Collectors.toList());
        return dtoList;

    }

    //등록
    public PostReplyDTO save(PostReplyDTO dto){
        //System.out.println("service-postsposts패키지 PostReplyService클래스 save() 진입 파라미터 PostReplyDTO -> "+ dto.toString());

        //PostReply-> Member member; / Long pno; / String  comment; /@Id Long prno
        PostReply entity = PostReply.builder()
                .comment(dto.getComment())
                .member(ms.dtoToEntity(dto.getMDto()))
                .pno(dto.getPno())
                .build();
        Long prno =prr.save(entity).getPrno();
        //System.out.println("service-postsposts패키지 PostReplyService클래스 save() 진입 파라미터 생성한 PostReply -> "+ entity.toString());

        Optional<PostReply> or = prr.findById(prno);

        return entityToDto(ms.dtoToEntity(dto.getMDto()), or.get());
    }


    //삭제
    public void postReplyDelete(Long prno) {
        //System.out.println("service-postsposts패키지 PostReplyService클래스 postReplyDelete() 진입");
        prr.deleteById(prno);
    }


    //entity ->  dto
    public PostReplyDTO entityToDto(Member mentity, PostReply pentity){
        //System.out.println("service-postsposts패키지 PostReplyService클래스entityToDto() 진입 파라미터 PostReply -> "+pentity);
        //System.out.println("service-postsposts패키지 PostReplyService클래스 entityToDto() 진입 파라미터 Member -> "+mentity);
        //PostReplyDTO ->Long prno; pno; /MemberDTO mDto;/ String comment;/ LocalDateTime modifiedDate, createdDate;

        MemberDTO mDto = ms.toMemberDto(mentity);

        PostReplyDTO dto = new PostReplyDTO(
                 pentity.getPrno(), pentity.getPno(),  pentity.getComment(),mDto.getName(),
                pentity.getModifiedDate(), pentity.getCreatedDate());

        //System.out.println("service-posts패키지 PostReplyService클래스 entityToDto() 진입 생성한 PostReplyDTO -> "+ dto.toString() );

        return dto;
    }

    //관리자페이지 게시글댓글전체 들고오기
    public List<PostReplyDTO> findAll(){
        //System.out.println("service-postsposts패키지 PostReplyService클래스 findAll() 진입");

        List<PostReply> list = prr.findAll();

        //반환타입
        List<PostReplyDTO> dtoList = new ArrayList<>();

        for(PostReply entity : list){
            dtoList.add(prr.entityToDto(entity));
        }
        return dtoList;
    }



    //관리자 탭 페이지 동적검색 + 페이지처리
    @Transactional
    public PageResponseDTO<PostReplyDTO> getListAdmin(PageRequestDTO requestDTO) {
        //System.out.println("service-posts패키지 PostReplyService클래스 getListAdmin() 진입");

        String [] types = requestDTO.getTypes();
        String keyword = requestDTO.getKeyword();
        Pageable p = requestDTO.getPageable("id");

        Page<PostReply> page = psi.searchReplyAll(requestDTO.getTypes(),requestDTO.getKeyword(),p);


        //entity->dto
        List<PostReplyDTO> list = page.getContent().stream()
                //에러발생.map(entity -> mm.map(entity, PostReplyDTO.class))
                .map(entity -> {return new PostReplyDTO(entity.getPrno(), entity.getPno(), entity.getComment(),
                        entity.getMember().getName(), entity.getModifiedDate(), entity.getCreatedDate());})
                .collect(Collectors.toList());

        return PageResponseDTO.<PostReplyDTO>withAll()
                .requestDTO(requestDTO)
                .dtoList(list)
                .total((int)page.getTotalElements())
                .build();

    }

    //관리자 페이지 게시글댓글특정날짜 + 동적검색 + 페이지처리
    @Transactional
    public PageResponseDTO<PostReplyDTO> getListAdminModifiedDate(PageRequestDTO requestDTO, LocalDate localDate){
        //System.out.println("service-posts패키지 PostReplyService클래스 getListAdminModifiedDate() 진입");

        String [] types = requestDTO.getTypes();
        String keyword = requestDTO.getKeyword();
        Pageable p = requestDTO.getPageable("id");

        //날짜까지 추가
        Page<PostReply> page = psi.searchReplyAllModifiedDate(types, keyword, p,localDate);

        //System.out.println("service-posts패키지 PostsService클래스 public Page<PostsListResponseDto> getListAdminModifiedDate(LocalDate localDate){\n() 진입 파라미터 Posts엔티티출력 -> "+ page.getContent());

        //entity->dto
        List<PostReplyDTO> list = page.getContent().stream()
                //에러발생.map(entity -> mm.map(entity, PostReplyDTO.class))
                .map(entity -> {return new PostReplyDTO(entity.getPrno(), entity.getPno(), entity.getComment(),
                        entity.getMember().getName(), entity.getModifiedDate(), entity.getCreatedDate());})
                .collect(Collectors.toList());

        return PageResponseDTO.<PostReplyDTO>withAll()
                .requestDTO(requestDTO)
                .dtoList(list)
                .total((int)page.getTotalElements())
                .build();
    }


    //관리자 페이지 게시글댓글특정날짜 + 동적검색 + 페이지처리
    @Transactional
    public PageResponseDTO<PostReplyDTO> getListAdminCreatedDate(PageRequestDTO requestDTO, LocalDate localDate){
        //System.out.println("service-posts패키지 PostReplyService클래스 getListAdminCreatedDate() 진입");

        String [] types = requestDTO.getTypes();
        String keyword = requestDTO.getKeyword();
        Pageable p = requestDTO.getPageable("id");

        //날짜까지 추가
        //Page<PostReply> page = psi.searchReplyAllModifiedDate(types, keyword, p,localDate);
        Page<PostReply> page = psi.searchReplyAllCreatedDate(types, keyword, p,localDate);

        //System.out.println("service-posts패키지 PostsService클래스 public Page<PostsListResponseDto> getListAdminCreatedDate() 진입 파라미터 Posts엔티티출력 -> "+ page.getContent());

        //entity->dto
        List<PostReplyDTO> list = page.getContent().stream()
                //에러발생.map(entity -> mm.map(entity, PostReplyDTO.class))
                .map(entity -> {return new PostReplyDTO(entity.getPrno(), entity.getPno(), entity.getComment(),
                        entity.getMember().getName(), entity.getModifiedDate(), entity.getCreatedDate());})
                .collect(Collectors.toList());

        return PageResponseDTO.<PostReplyDTO>withAll()
                .requestDTO(requestDTO)
                .dtoList(list)
                .total((int)page.getTotalElements())
                .build();
    }


    //특정날짜게시글수
    public Long getCountLocalDate(LocalDate localDate){
        //System.out.println("service-posts패키지 PostReplyService클래스 getCountLocalDate() 진입");

        Long count = prr.getCountLocalDate(localDate.atTime(LocalTime.MIN), localDate.atTime(LocalTime.MAX));
        //System.out.println("service-posts패키지 PostReplyService클래스 getCountLocalDate() 진입 localDate 게시글댓글수 -> "+ count);
        return count;
    }

//System.out.println("service-posts패키지 PostReplyService클래스 list() 진입 파라미터 prno -> "+ prno);

}
