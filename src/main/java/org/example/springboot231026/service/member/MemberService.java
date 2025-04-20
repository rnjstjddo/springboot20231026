package org.example.springboot231026.service.member;


import org.example.springboot231026.domain.inquiry.Inquiry;
import org.example.springboot231026.domain.member.Member;
import org.example.springboot231026.domain.member.MemberRepository;
import org.example.springboot231026.domain.member.RoleType;
import org.example.springboot231026.dto.dogsell.cart.DogSellCartDTO;
import org.example.springboot231026.dto.inquiry.InquiryDto;
import org.example.springboot231026.dto.member.JoinDTO;
import org.example.springboot231026.dto.member.MemberDTO;
import org.example.springboot231026.dto.page.PageRequestDTO;
import org.example.springboot231026.dto.page.PageResponseDTO;
import org.example.springboot231026.service.dogsell.DogSellReplyService;
import org.example.springboot231026.service.dogsell.DogSellService;
import org.example.springboot231026.service.dogsell.WishNumService;
import org.example.springboot231026.service.guestbook.GuestbookService;
import org.example.springboot231026.service.posts.PostsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MemberService {

    @Autowired
    private MemberRepository mr;

    @Autowired
    private PasswordEncoder pe;

    @Autowired
    private DogSellReplyService dsrs;
    //새로운 메소드 생성 작성자를 기준으로 댓글번호를 받아서 댓글삭제처리

    @Autowired
    private DogSellService dss;
    //탈퇴를 위한 메소드를 다시 만들어야겠다. 회원이름만 받아서 관련 분양글과 분양글이미지 같이 트랜잭션으로 삭제처리

    @Autowired
    private WishNumService wms;

    //@Autowired
    //private GuestbookReplyService grs; public boolean guestbookReplyDeleteGno(Long gno) {
    // 방명록번호로 방명록댓글삭제 -> 방명록삭제에서 이미 같이 처리하기에 이 메소드 불필요.
    @Autowired
    private GuestbookService gbs;

    @Autowired
    private PostsService ps;

    @Autowired
    private ModelMapper mm;

    @Transactional
    public String memberJoin(JoinDTO dto){
        //System.out.println("service-member패키지 MemberService클래스 memberJoin() 진입 - 일반회원가입 경우" );

        if(dto.getName().equals("admin")){

            //System.out.println("service-member패키지 MemberService클래스 memberJoin() 진입 admin회원가입 경우 진입 -> "+ dto.getName());
            dto.setPassword(pe.encode(dto.getPassword()));
            dto.setFormSocial("false");
            dto.setRole(RoleType.ADMIN);
            Member m = this.toEntity(dto);
            mr.save(m);
            return m.getName();
        }else {

            //System.out.println("service-member패키지 MemberService클래스 memberJoin() 진입 admin회원가입 아닌 경우 진입");
            dto.setPassword(pe.encode(dto.getPassword()));
            dto.setFormSocial("false");
            dto.setRole(RoleType.USER);
            Member m = this.toEntity(dto);
            mr.save(m);
            return m.getName();
        }
    }

    //아이디체크
    public String checkName(JoinDTO joinDTO){
        //System.out.println("service-member패키지 MemberService클래스 checkName() 진입 - 일반회원가입시 아이디중복확인");

        Optional<Member> o = mr.findByName(joinDTO.getName());

        if(o.isPresent()){
            //System.out.println("service-member패키지 MemberService클래스 checkName() 진입 아이디 중복되는 경우 진입");
            return "false";
        }
        //System.out.println("service-member패키지 MemberService클래스 checkName() 진입 아이디 중복되지 않아서 true 전달");
        return "true";
    }


    @Transactional
    public Member updateJoin(JoinDTO joinDTO, MemberDTO memberDTO){
        //System.out.println("service-member패키지 MemberService클래스 updateJoin() 진입 - 일반회원이 비밀번호 변경한 경우 진입");

        Optional<Member> o = mr.findByUsername(memberDTO.getEmail(), memberDTO.getFromSocial());
                //.orElseThrow(() -> new IllegalArgumentException("존재하는 않는 회원입니다."));
        Member m = o.get();
        m.updateMember(joinDTO.getName(), pe.encode(joinDTO.getPassword()));

        return m;
    }

    @Transactional
    //@Commit
    public Member updateSocialJoin(String name, MemberDTO memberDTO){
        //System.out.println("service-member패키지 MemberService클래스 updateSocialJoin() 진입 ");

        Optional<Member> o = mr.findByUsername(memberDTO.getEmail(), memberDTO.getFromSocial());
        //.orElseThrow(() -> new IllegalArgumentException("존재하는 않는 회원입니다."));
        Member m = o.get();
        m.updateSocialMember(name);
        mr.save(m);

        return m;
    }

    public Member toEntity(JoinDTO dto) {
        //System.out.println("dto-member패키지 MemberService클래스 toEntity() 진입");

        Member m = Member.builder()
                .password(dto.getPassword())
                .email(dto.getEmail())
                .fromSocial(dto.getFormSocial())
                .name(dto.getName())
                .role(dto.getRole())
                .build();
        return m;
    }
    public MemberDTO toMemberDto(Member entity) {
        //System.out.println("dto-member패키지 MemberService클래스 toMemberDto() 진입");

        MemberDTO dto = new MemberDTO(
                 entity.getEmail(),
                 entity.getPassword(),
                 MemberDTO.setAuthorities(entity.getRole().toString()),
                 entity.getName(),
                 entity.getEmail()
                );
        dto.setRole(entity.getRole());
        return dto;
    }

    public Member dtoToEntity(MemberDTO mDto) {
        //System.out.println("dto-member패키지 MemberService클래스 dtoToEntity() 진입");
        Member entity = Member.builder()
                .name(mDto.getName())
                .email(mDto.getEmail())
                .fromSocial(mDto.getFromSocial())
                .password(pe.encode(mDto.getPassword()))
                .role(mDto.getRole())
                //.wishNumList(null)
                .build();

        //System.out.println("dto-member패키지 MemberService클래스 toMemberDto() 진입 MemberDTO -> "+ entity.toString());
        return entity;
    }


    //카카오 서버로부터 사용자정보얻고 DB에 존재하는 회원인지 확인
    @Transactional(readOnly = true)
    public Member getUser(String email){
        //System.out.println("dto-member패키지 MemberService클래스 getUser() 진입 파라미터 username -> "+ email);

        return mr.findByUsername(email, "true").orElseGet(() ->{return new Member();});
        //빈엔티티반환
    }

    @Transactional
    public String insertUser(Member m){
        //System.out.println("service-member패키지 MemberService클래스 insertUser() 진입 - DB에 저장한다.");

        mr.save(m);
        return m.getName();
    }

    @Transactional
    public Member nameCheck(String name){
        //System.out.println("service-member패키지 MemberService클래스 nameCheck() 진입 - 소셜로그인회원은 닉네임수정시 중복을 먼저확인한다.");
        Member entity = mr.findByName(name).orElseGet(() -> {return new Member();});
        //System.out.println("service-member패키지 MemberService클래스 nameCheck() 진입 - 소셜로그인회원은 닉네임수정시 중복을 먼저확인한다. Member 가져온것 확인->"+entity);

        return entity;
    }



    @Transactional
    public void deleteMember(String name, Member member){
        //System.out.println("service-member패키지 MemberService클래스 deleteMember() 진입 회원탈퇴 메소드 진입 "+
                //"탈퇴할 회원 name -> "+ name);
        //분양글 삭제처리
        boolean result = dss.removeDeleteMember(name);
        //System.out.println("service-member패키지 MemberService클래스 deleteMember() 진입 회원탈퇴 메소드 진입 "+
                //" 분양글과 이미지 삭제처리 결과 -> "+ result);

        boolean resultDSR = dsrs.dsReplyRemoveDeleteMember(name);
        //System.out.println("service-member패키지 MemberService클래스 deleteMember() 진입 회원탈퇴 메소드 진입 "+
                //" 분양댓글 삭제처리 결과 -> "+ resultDSR);

        boolean resultWN = wms.wishlistremoveAllDeleteMember(member);
        //System.out.println("service-member패키지 MemberService클래스 deleteMember() 진입 회원탈퇴 메소드 진입 "+
                //" 위시번호 삭제처리 결과 -> "+ resultWN);
        
        boolean resultGB = gbs.removeDeleteMember(name);
        //System.out.println("service-member패키지 MemberService클래스 deleteMember() 진입 회원탈퇴 메소드 진입 "+
                //" 방명록 삭제처리 결과 -> "+ resultGB);

        boolean resultPS = ps.deleteMessageDeleteMember(name);
        //System.out.println("service-member패키지 MemberService클래스 deleteMember() 진입 회원탈퇴 메소드 진입 "+
               // " 게시글과 게시글댓글 삭제처리 결과 -> "+ resultPS);

    }
    public Member toEntity(MemberDTO dto) {
        //System.out.println("dto-member패키지 MemberService클래스 toEntity() 진입");

        Member m = Member.builder()
                .password(dto.getPassword())
                .email(dto.getEmail())
                .fromSocial("false")
                .name(dto.getName())
                .role(RoleType.USER)
                //.wishNumList(null)
                .build();


        //System.out.println("dto-member패키지 MemberService클래스 toEntity() 진입 엔티티Member -> "+ m.toString());
        return m;
    }

    //관리자 페이지 회원전체 DTO로 반환
    public List<MemberDTO> findAll(){
        //System.out.println("dto-member패키지 MemberService클래스 findAll() 진입 ");
        List<Member> list =mr.findAll();

        //반환타입
        List<MemberDTO> result = new ArrayList<>();
        for(Member entity: list){
            result.add(new MemberDTO(entity.getName(), entity.getPassword(),
                    MemberDTO.setAuthorities(entity.getRole().name()),
                    entity.getFromSocial(),entity.getName(), entity.getEmail(), entity.getRole(),
                    entity.getCreatedDate(),entity.getModifiedDate()));
        }
        return result;
    }


    //관리자 페이지 날짜에 맞는 회원조회+동적검색+페이징처리
    @Transactional
    public PageResponseDTO<MemberDTO> getListAdminModifiedDate(PageRequestDTO requestDTO, LocalDate localDate) {
        //System.out.println("service-member패키지 MemberService클래스 getListAdminModifiedDate() 진입 ");

        String [] types = requestDTO.getTypes();
        String keyword = requestDTO.getKeyword();

        //Pageable p = requestDTO.getPageable("createdDate");

        Pageable p = PageRequest.of(requestDTO.getPage()-1, requestDTO.getSize());

        //날짜까지 추가
        Page<Member> page = mr.searchMemberAllModifiedDate(types, keyword, p,localDate);
        //Page<Member> page = mr.searchMemberAllCreatedDate(types, keyword, p,localDate);

        //System.out.println("여기까지오나");

        //entity->dto
        List<MemberDTO> list = page.getContent().stream()
                .map(entity ->new MemberDTO(entity.getName(), entity.getPassword(),
                        MemberDTO.setAuthorities(entity.getRole().name()),
                        entity.getFromSocial(),entity.getName(), entity.getEmail(), entity.getRole(),
                        entity.getCreatedDate(),entity.getModifiedDate()) )
                .collect(Collectors.toList());

        return PageResponseDTO.<MemberDTO>withAll()
                .requestDTO(requestDTO)
                .dtoList(list)
                .total((int)page.getTotalElements())
                .build();

    }


    @Transactional
    public PageResponseDTO<MemberDTO> getListAdminCreatedDate(PageRequestDTO requestDTO, LocalDate localDate) {
        //System.out.println("service-member패키지 MemberService클래스 getListAdminCreatedDate() 진입 ");

        String [] types = requestDTO.getTypes();
        String keyword = requestDTO.getKeyword();

        //Pageable p = requestDTO.getPageable("createdDate");

        Pageable p = PageRequest.of(requestDTO.getPage()-1, requestDTO.getSize());

        //날짜까지 추가
        //Page<Member> page = mr.searchMemberAllModifiedDate(types, keyword, p,localDate);
        Page<Member> page = mr.searchMemberAllCreatedDate(types, keyword, p,localDate);

        //entity->dto
        List<MemberDTO> list = page.getContent().stream()
                .map(entity ->new MemberDTO(entity.getName(), entity.getPassword(),
                        MemberDTO.setAuthorities(entity.getRole().name()),
                        entity.getFromSocial(),entity.getName(), entity.getEmail(), entity.getRole(),
                        entity.getCreatedDate(),entity.getModifiedDate()) )
                .collect(Collectors.toList());

        return PageResponseDTO.<MemberDTO>withAll()
                .requestDTO(requestDTO)
                .dtoList(list)
                .total((int)page.getTotalElements())
                .build();

    }



    //관리자 페이지 동적검색+페이징처리
    @Transactional
    public PageResponseDTO<MemberDTO> getListAdmin(PageRequestDTO requestDTO) {
        //System.out.println("service-member패키지 MemberService클래스 getListAdmin() 진입");

        String [] types = requestDTO.getTypes();
        String keyword = requestDTO.getKeyword();
        Pageable p = PageRequest.of(requestDTO.getPage()-1, requestDTO.getSize());


        Page<Member> page = mr.searchMemberAll(types, keyword, p);

        //entity->dto
        List<MemberDTO> list = page.getContent().stream()
                .map(entity ->new MemberDTO(entity.getName(), entity.getPassword(),
                        MemberDTO.setAuthorities(entity.getRole().name()),
                        entity.getFromSocial(),entity.getName(), entity.getEmail(), entity.getRole(),
                        entity.getCreatedDate(),entity.getModifiedDate()) )
                .collect(Collectors.toList());

        return PageResponseDTO.<MemberDTO>withAll()
                .requestDTO(requestDTO)
                .dtoList(list)
                .total((int)page.getTotalElements())
                .build();

    }

    //특정날짜회원등록수
    public Long getCountLocalDate(LocalDate localDate){
        //System.out.println("service-member패키지 MemberService클래스 getCountLocalDate() 진입");

        Long count = mr.getCountLocalDate(localDate.atTime(LocalTime.MIN), localDate.atTime(LocalTime.MAX));
        //System.out.println("service-member패키지 MemberService클래스 getCountLocalDate() 진입 localDate 등록한 회원수 -> "+ count);
        return count;
    }


}
