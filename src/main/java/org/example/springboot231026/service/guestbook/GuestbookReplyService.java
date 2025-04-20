package org.example.springboot231026.service.guestbook;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.example.springboot231026.domain.guestbook.*;
import org.example.springboot231026.domain.member.Member;
import org.example.springboot231026.domain.member.MemberRepository;
import org.example.springboot231026.domain.posts.PostReply;
import org.example.springboot231026.dto.guestbook.GuestPageRequestDTO;
import org.example.springboot231026.dto.guestbook.GuestPageResultDTO;
import org.example.springboot231026.dto.guestbook.GuestbookDTO;
import org.example.springboot231026.dto.member.MemberDTO;
import org.example.springboot231026.dto.post.PostReplyDTO;
import org.example.springboot231026.service.member.MemberService;
import org.example.springboot231026.dto.guestbook.GuestbookReplyDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.codehaus.groovy.runtime.DefaultGroovyMethods.collect;

@Service
public class GuestbookReplyService {

    @Autowired
    private GuestbookRepository gr;

    @Autowired
    private GuestbookReplyService grs;

    @Autowired
    private GuestbookReplyRepository grr;

    @Autowired
    private MemberRepository mr;

    @Autowired
    private MemberService ms;

    @Autowired
    private ModelMapper mm;


    //댓글목록
    public List<GuestbookReplyDTO> list(Long gno) {
        //System.out.println("service-guestbook패키지 GuestbookReplyService클래스 list() 진입 파라미터 gno -> " + gno);

        List<GuestbookReply> list = grr.list(gno);


////GuestbookReplyDTO -> Long grno; /Long gno; / MemberDTO mDto; /String name; /String comment;/LocalDateTime modifiedDate, createdDate;
       /* List<GuestbookReplyDTO> dtoList = list.stream().map(entity -> new GuestbookReplyDTO(
                        entity.getGrno(), entity.getGno(), entity.getMember().getName(), entity.getComment(),
                        entity.getModifiedDate() , entity.getCreatedDate()))
                .collect(Collectors.toList());*/

        List<GuestbookReplyDTO> dtoList = list.stream()
                .map((e) -> {
                    return
                            new GuestbookReplyDTO(
                                    e.getGrno(), e.getGno(), ms.toMemberDto(e.getMember()), e.getMember().getName(),
                                    e.getComment(),
                                    e.getModifiedDate(), e.getCreatedDate()
                            );
                })
                .collect(Collectors.toList());

        //System.out.println("service-guestbook패키지 GuestbookReplyService클래스 list() 진입 List<GuestbookReplyDTO> -> " + dtoList.toString());

        if (dtoList != null) {
            //System.out.println("service-guestbook패키지 GuestbookReplyService클래스 list() 진입 -반환타입 List<GuestbookReplyDTO> null 아닐경우 -> " + dtoList.toString());
        }
        return dtoList;

    }

    //등록
    public GuestbookReplyDTO save(GuestbookReplyDTO dto) {
        //System.out.println("service-guestbook패키지 GuestbookReplyService클래스 save() 진입 파라미터 GuestbookReplyDTO -> " + dto.toString());

        GuestbookReply entity = GuestbookReply.builder()
                .comment(dto.getComment())
                .member(ms.dtoToEntity(dto.getMDto()))
                .gno(dto.getGno())
                .build();
        Long grno = grr.save(entity).getGrno();
        //System.out.println("service-guestbook패키지 GuestbookReplyService클래스 save() 진입 파라미터 생성한 GuestbookReply -> " + entity.toString());

        Optional<GuestbookReply> or = grr.findById(grno);

        return entityToDto(ms.dtoToEntity(dto.getMDto()), or.get());
    }


    //삭제
    public boolean guestbookReplyDeleteGno(Long gno) {
        //System.out.println("service-guestbook패키지 GuestbookReplyService클래스 guestbookReplyDeleteGno() 진입");
        grr.deleteByGno(gno);
        return true;
    }

    //삭제
    public boolean guestbookReplyDelete(Long grno) {
        //System.out.println("service-guestbook패키지 GuestbookReplyService클래스 guestbookReplyDelete() 진입");
        grr.deleteById(grno);
        return true;
    }


    //entity ->  dto
    public GuestbookReplyDTO entityToDto(Member mentity, GuestbookReply pentity) {
        //System.out.println("service-guestbook패키지 GuestbookReplyService클래스 entityToDto() 진입 파라미터 GuestbookReply -> " + pentity);
        //System.out.println("service-guestbook패키지 GuestbookReplyService클래스 entityToDto() 진입 파라미터 Member -> " + mentity);

        MemberDTO mDto = ms.toMemberDto(mentity);

        GuestbookReplyDTO dto = new GuestbookReplyDTO(
                pentity.getGrno(), pentity.getGno(), pentity.getComment(), mDto.getName(),
                pentity.getModifiedDate(), pentity.getCreatedDate());

        //System.out.println("service-guestbook패키지 GuestbookReplyService클래스 entityToDto() 진입 생성한 GuestbookReplyDTO -> " + dto.toString());

        return dto;
    }

    public GuestbookReplyDTO toDto(GuestbookReply entity) {
        //System.out.println("service-guestbook패키지 GuestbookReplyService클래스 toDto() 진입");

        GuestbookReplyDTO dto = new GuestbookReplyDTO(
                entity.getGrno(), entity.getGno(), entity.getComment(), entity.getMember().getName(),
                entity.getModifiedDate(), entity.getCreatedDate());
        return dto;
    }


    //관리자페이지 댓글전체 수
    public List<GuestbookReplyDTO> findAll() {
        //System.out.println("service-guestbook패키지 GuestbookReplyService클래스 findAll() 진입");
        List<GuestbookReply> list = grr.findAll();

        //반환객체
        List<GuestbookReplyDTO> result = new ArrayList<>();

        for (GuestbookReply reply : list) {
            result.add(toDto(reply));
        }
        return result;

    }

    //댓글동적검색으로 일치하는 날짜조회
    public GuestPageResultDTO<GuestbookReplyDTO, GuestbookReply> getReplyListAdmin(GuestPageRequestDTO requestDTO, LocalDate localDate) {
        //System.out.println("service-guestbook패키지 GuestbookReplyService클래스 getReplyListAdmin() 진입");

        Pageable p = requestDTO.getPageable(Sort.by("grno").descending());

        BooleanBuilder bb = getReplySearchAdmin(requestDTO, localDate);
        //검색조건결과인 BooleanBuilder를 파라미터로 추가한다.
        //Page<Guestbook> result = gr.findAll(bb, p);
        Page<GuestbookReply> result = grr.findAll(bb, p);

        Function<GuestbookReply, GuestbookReplyDTO> fn = (entity -> toDto(entity));

        return new GuestPageResultDTO<>(result, fn);


    }


    //댓글동적검색+특정날짜
    private BooleanBuilder getReplySearchAdmin(GuestPageRequestDTO pageRequestDTO, LocalDate localDate) {
        //System.out.println("service-guestbook패키지 GuestbookReplyService클래스 getReplySearchAdmin() 진입");

        String type = pageRequestDTO.getType();
        String keyword = pageRequestDTO.getKeyword();

        BooleanBuilder bb = new BooleanBuilder();

        QGuestbookReply qg = QGuestbookReply.guestbookReply;

        BooleanExpression be = qg.grno.gt(0L)
                //일치하는 날짜추가
                .and(qg.modifiedDate.between(localDate.atTime(LocalTime.MIN), localDate.atTime(LocalTime.MAX)));
        bb.and(be);

        if (type == null || type.trim().length() == 0) {
            //System.out.println("service-guestbook패키지 GuestbookReplyService클래스 getSearch() 진입 - 검색조건이 없는 경우 진입");
            return bb;
        }

        BooleanBuilder searchbb = new BooleanBuilder();
        if (type.contains("n")) {
            searchbb.or(qg.gno.stringValue().contains(keyword));
        }

        if (type.contains("c")) {
            searchbb.or(qg.comment.contains(keyword));
        }
        if (type.contains("w")) {
            searchbb.or(qg.member.name.contains(keyword));
        }

        bb.and(searchbb);
        return bb;
    }


    //관리자페이지 탭 -> 댓글동적검색으로 일치하는 날짜조회 미포함
    public GuestPageResultDTO<GuestbookReplyDTO, GuestbookReply> getReplyListAdmin(GuestPageRequestDTO requestDTO) {
        //System.out.println("service-guestbook패키지 GuestbookReplyService클래스 getReplyListAdmin() 진입");

        Pageable p = requestDTO.getPageable(Sort.by("grno").descending());

        BooleanBuilder bb = getReplySearchAdmin(requestDTO);
        //검색조건결과인 BooleanBuilder를 파라미터로 추가한다.
        //Page<Guestbook> result = gr.findAll(bb, p);
        Page<GuestbookReply> result = grr.findAll(bb, p);

        Function<GuestbookReply, GuestbookReplyDTO> fn = (entity -> toDto(entity));

        return new GuestPageResultDTO<>(result, fn);

    }

    //댓글동적검색
    private BooleanBuilder getReplySearchAdmin(GuestPageRequestDTO pageRequestDTO) {
        //System.out.println("service-guestbook패키지 GuestbookReplyService클래스 getReplySearchAdmin() 진입");

        String type = pageRequestDTO.getType();
        String keyword = pageRequestDTO.getKeyword();

        BooleanBuilder bb = new BooleanBuilder();

        QGuestbookReply qg = QGuestbookReply.guestbookReply;

        BooleanExpression be = qg.grno.gt(0L);
        bb.and(be);

        if (type == null || type.trim().length() == 0) {
            //System.out.println("service-guestbook패키지 GuestbookReplyService클래스 getSearch() 진입 - 검색조건이 없는 경우 진입");
            return bb;
        }

        BooleanBuilder searchbb = new BooleanBuilder();
        if (type.contains("n")) {
            searchbb.or(qg.gno.stringValue().contains(keyword));
        }

        if (type.contains("c")) {
            searchbb.or(qg.comment.contains(keyword));
        }
        if (type.contains("w")) {
            searchbb.or(qg.member.name.contains(keyword));
        }

        bb.and(searchbb);
        return bb;
    }

    //특정날짜방명록댓글수
    public Long getCountLocalDate(LocalDate localDate){
        //System.out.println("service-guestbook패키지 GuestbookReplyService클래스 getCountLocalDate() 진입");

        Long count = grr.getCountLocalDate(localDate.atTime(LocalTime.MIN), localDate.atTime(LocalTime.MAX));
        //System.out.println("service-guestbook패키지 GuestbookReplyService클래스 getCountLocalDate() 진입 localDate 방명록댓글수 -> "+ count);
        return count;
    }
}