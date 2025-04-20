package org.example.springboot231026.service.posts;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.example.springboot231026.domain.guestbook.Guestbook;
import org.example.springboot231026.domain.guestbook.QGuestbook;
import org.example.springboot231026.domain.posts.PostReplyRepository;
import org.example.springboot231026.domain.posts.Posts;
import org.example.springboot231026.domain.posts.PostsRepository;
import org.example.springboot231026.dto.guestbook.GuestPageRequestDTO;
import org.example.springboot231026.dto.guestbook.GuestPageResultDTO;
import org.example.springboot231026.dto.guestbook.GuestbookDTO;
import org.example.springboot231026.dto.post.PostsListResponseDto;
import org.example.springboot231026.dto.post.PostsResponseDto;
import org.example.springboot231026.dto.post.PostsSaveRequestDto;
import org.example.springboot231026.dto.post.PostsUpdateRequestDto;
import org.example.springboot231026.dto.page.PageRequestDTO;
import org.example.springboot231026.dto.page.PageResponseDTO;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository pr;

    private final ModelMapper mm;

    private final PostReplyRepository prr;


    @Transactional
    public Long save(PostsSaveRequestDto dto){
        //System.out.println("service-posts패키지 PostsService클래스 save() 진입");
        return pr.save(dto.toEntity()).getId();
    }


    //게시글수정
    @Transactional
    public Long update(Long id, PostsUpdateRequestDto dto){
        //System.out.println("service-posts패키지 PostsService클래스 udpate() 진입");
        Posts entity = pr.findById(id).orElseThrow(() -> new IllegalArgumentException("해당게시글이 없습니다."));
        entity.update(dto.getTitle(), dto.getContent());

        //System.out.println("service-posts클래스 PostsService udpate() 진입 변경된 Posts엔티티 -> "+entity.toString());
        return id;
    }

    //게시글조회
    public PostsResponseDto findById(Long id){
        //System.out.println("service-posts패키지 PostsService클래스 findById() 진입");
        Posts entity = pr.findById(id).orElseThrow(() -> new IllegalArgumentException("해당게시글이없습니다."));

        return new PostsResponseDto(entity);
    }
    
    //게시글목록
    @Transactional
    public PageResponseDTO<PostsListResponseDto> list(PageRequestDTO requestDTO){
        //System.out.println("service-posts패키지 PostsService클래스 list() 진입 파라미터 PageRequestDTO -> "+ requestDTO.toString());


        String [] types = requestDTO.getTypes();
        String keyword = requestDTO.getKeyword();
        Pageable p = requestDTO.getPageable("id");

        Page<Posts> page = pr.searchAll(types, keyword, p);

        //System.out.println("service-posts패키지 PostsService클래스 list() 진입 파라미터 Posts엔티티출력 -> "+ page.getContent());

        //entity->dto
        List<PostsListResponseDto> list = page.getContent().stream()
                .map(entity -> mm.map(entity, PostsListResponseDto.class))
                .collect(Collectors.toList());

        return PageResponseDTO.<PostsListResponseDto>withAll()
                .requestDTO(requestDTO)
                .dtoList(list)
                .total((int)page.getTotalElements())
                .build();
    }

    //게시글삭제
    @Transactional
    public Long delete(Long id){
        //System.out.println("service-posts패키지 PostsService클래스 delete() 진입");
        prr.deleteByPno(id);
        pr.deleteById(id);
        return id;
    }


    //회원탈퇴에 의한 게시글삭제
    @Transactional
    public boolean deleteMessageDeleteMember(String name){
        //System.out.println("service-posts패키지 PostsService클래스 deleteMessageDeleteMember() 진입");

        List<Long> idList= pr.getIdByAuthor(name);

        boolean result = false;

        if(idList !=null && idList.size() !=0){
            for(Long id: idList) {
                //System.out.println("service-posts패키지 PostsService클래스 deleteMessageDeleteMember() 진입 " +
                       // " 탈퇴할 회원의 게시글과 댓글이 존재할경우 -> "+ id);
                prr.deleteByPno(id);
                pr.deleteById(id);
                result =true;
            }
        }else{
            //System.out.println("service-posts패키지 PostsService클래스 deleteMessageDeleteMember() 진입 " +
                    //" 탈퇴할 회원의 게시글과 댓글이 없을경우 진입");

            result = true;
        }
        return result;
    }


    //mypage
    @Transactional
    public List<PostsResponseDto> listMyPage(String author) {
        //System.out.println("service-posts패키지 PostsService클래스 listMyPage() 진입 - MyPage author -> " +author);

        Optional<List<Posts>> postsList = pr.findByAuthor(author);

        //반환타입
        List<PostsResponseDto> postsResponseDtoList = new ArrayList<>();

        if(postsList.isPresent()){
            //System.out.println("service-post패키지 PostsService클래스 listMyPage() 진입 - 회원이 작성한 Posts 존재할경우 진입");
            for(Posts posts : postsList.get()){
                postsResponseDtoList.add(mm.map(posts, PostsResponseDto.class));
            }
        }

        return postsResponseDtoList;
    }

    //관리자페이지 전체 게시글 조회
    public List<PostsListResponseDto> findAll(){
        //System.out.println("service-posts패키지 PostsService클래스 findAll() 진입");

        List<Posts> o = pr.findAll();

        //반환타입
        List<PostsListResponseDto> dtoList = new ArrayList<>();
        for(Posts p : o){
            dtoList.add(pr.entityToDto(p));
        }
        return dtoList;
    }

    //동적검색+특정날짜추가
    @Transactional
    public PageResponseDTO<PostsListResponseDto> getListAdminModifiedDate(PageRequestDTO requestDTO,LocalDate localDate){
        //System.out.println("service-posts패키지 PostsService클래스 getListAdminModifiedDate() 진입 ");


        String [] types = requestDTO.getTypes();
        String keyword = requestDTO.getKeyword();
        Pageable p = requestDTO.getPageable("id");

        //날짜까지 추가
        Page<Posts> page = pr.searchAllModifiedDate(types, keyword, p,localDate);

        //System.out.println("service-posts패키지 PostsService클래스 public Page<PostsListResponseDto> getListAdminModifiedDate(LocalDate localDate){\n() 진입 파라미터 Posts엔티티출력 -> "+ page.getContent());

        //entity->dto
        List<PostsListResponseDto> list = page.getContent().stream()
                .map(entity -> mm.map(entity, PostsListResponseDto.class))
                .collect(Collectors.toList());

        return PageResponseDTO.<PostsListResponseDto>withAll()
                .requestDTO(requestDTO)
                .dtoList(list)
                .total((int)page.getTotalElements())
                .build();
    }

    //동적검색+특정날짜추가
    @Transactional
    public PageResponseDTO<PostsListResponseDto> getListAdminCreatedDate(PageRequestDTO requestDTO,LocalDate localDate){
        //System.out.println("service-posts패키지 PostsService클래스 getListAdminCreatedDate() 진입 ");

        String [] types = requestDTO.getTypes();
        String keyword = requestDTO.getKeyword();
        Pageable p = requestDTO.getPageable("id");

        //날짜까지 추가
        Page<Posts> page = pr.searchAllCreatedDate(types, keyword, p,localDate);

        //System.out.println("service-posts패키지 PostsService클래스 public Page<PostsListResponseDto> getListAdminCreatedDate() 진입 파라미터 Posts엔티티출력 -> "+ page.getContent());

        //entity->dto
        List<PostsListResponseDto> list = page.getContent().stream()
                .map(entity -> mm.map(entity, PostsListResponseDto.class))
                .collect(Collectors.toList());

        return PageResponseDTO.<PostsListResponseDto>withAll()
                .requestDTO(requestDTO)
                .dtoList(list)
                .total((int)page.getTotalElements())
                .build();
    }


    //특정날짜게시글수
    public Long getCountLocalDate(LocalDate localDate){
        //System.out.println("service-posts패키지 PostsService클래스 getCountLocalDate() 진입 시작시간 -> "+localDate.atTime(LocalTime.MIN)+"종료시간 -> "+localDate.atTime(LocalTime.MAX));

        Long count = pr.getCountLocalDate(localDate.atTime(LocalTime.MIN), localDate.atTime(LocalTime.MAX));
        //System.out.println("service-posts패키지 PostsService클래스 getCountLocalDate() 진입 localDate 게시글수 -> "+ count);
        return count;
    }


        //System.out.println("service-posts패키지 PostsService클래스 save() 진입");
}
