package org.example.springboot231026.service.guestbook;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.example.springboot231026.domain.guestbook.Guestbook;
import org.example.springboot231026.domain.guestbook.GuestbookReplyRepository;
import org.example.springboot231026.domain.guestbook.GuestbookRepository;
import org.example.springboot231026.domain.guestbook.QGuestbook;
import org.example.springboot231026.dto.guestbook.GuestbookDTO;
import org.example.springboot231026.dto.guestbook.GuestPageRequestDTO;
import org.example.springboot231026.dto.guestbook.GuestPageResultDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class GuestbookService {

    @Autowired
    private GuestbookRepository gr;
    @Autowired
    private GuestbookReplyService grs;

    @Autowired
    private ModelMapper mm;

    public Long register(GuestbookDTO gDto) {
        //System.out.println("service-guestbook패키지 GuestbookService클래스 register() 진입");

        Guestbook entity = dtoToEntity(gDto);
        return gr.save(entity).getGno();
    }

    
    //방명록목록
    public GuestPageResultDTO<GuestbookDTO, Guestbook> getList(GuestPageRequestDTO requestDTO){
        //System.out.println("service-guestbook패키지 GuestbookService클래스 getList() 진입 - 파라미터GuestPageRequestDTO -> "+ requestDTO);

        Pageable p = requestDTO.getPageable(Sort.by("gno").descending());

        BooleanBuilder bb = getSearch(requestDTO);
        //검색조건결과인 BooleanBuilder를 파라미터로 추가한다.
        Page<Guestbook> result = gr.findAll(bb, p);

        Function<Guestbook, GuestbookDTO> fn =(entity -> entityToDto(entity));

        return new GuestPageResultDTO<>(result, fn);
    }

    //방명록조회
    public GuestbookDTO read(Long gno){
        //System.out.println("service-guestbook패키지 GuestbookService클래스 read() 진입 파라미터 -> "+gno);
        Guestbook entity =gr.findByGno(gno).orElseGet( () -> {return new Guestbook();});

        return entityToDto(entity);
    }


    //방명록수정
    @Transactional
    public GuestbookDTO modify(GuestbookDTO gDto){
        //System.out.println("service-guestbook패키지 GuestbookService클래스 read() 진입 파라미터 GuestbookDTO -> "+gDto);
        Guestbook entity =gr.findByGno(gDto.getGno()).orElseGet( () -> {return new Guestbook();});

        entity.changeGuestbook(gDto.getTitle(), gDto.getContent());
        return entityToDto(entity);
    }


    @Transactional
    //방명록삭제
    public void remove(Long gno){
        //System.out.println("service-guestbook패키지 GuestbookService클래스 remove() 진입 파라미터 -> "+gno);

        //관련댓글부터 삭제
        grs.guestbookReplyDeleteGno(gno);
        gr.deleteById(gno);
    }


    @Transactional
    //회원탈퇴시 방명록삭제
    public boolean removeDeleteMember(String name){
        //System.out.println("service-guestbook패키지 GuestbookService클래스 removeDeleteMember() 진입 -> "+name);

        List<Long> gnoList = gr.getGnoByWriter(name);

        boolean result = false;
        if(gnoList.size() !=0 && gnoList !=null){

            for(Long gno : gnoList) {
               // System.out.println("service-guestbook패키지 GuestbookService클래스 removeDeleteMember() 진입 -> " +
                       // " 탈퇴할 회원의 삭제할 방명록이 존재할경우 진입  -> " + gno);
                this.remove(gno);
            }
            result = true;
        }else{
            //System.out.println("service-guestbook패키지 GuestbookService클래스 removeDeleteMember() 진입 -> "+
                   // " 탈퇴할 회원의 삭제할 방명록이 없을경우 진입");
            result= true;

        }
        return result;
    }
    
    //방명록검색
    private BooleanBuilder getSearch(GuestPageRequestDTO pageRequestDTO){
        //System.out.println("service-guestbook패키지 GuestbookService클래스 getSearch() 진입");

        String type = pageRequestDTO.getType();
        String keyword = pageRequestDTO.getKeyword();

        BooleanBuilder bb = new BooleanBuilder();

        QGuestbook qg = QGuestbook.guestbook;

        BooleanExpression be = qg.gno.gt(0L);
        bb.and(be);

        if(type ==null || type.trim().length()==0){
            //System.out.println("service-guestbook패키지 GuestbookService클래스 getSearch() 진입 - 검색조건이 없는 경우 진입");
            return bb;
        }

        BooleanBuilder searchbb = new BooleanBuilder();
        if(type.contains("t")){
            searchbb.or(qg.title.contains(keyword));
        }

        if(type.contains("c")){
            searchbb.or(qg.content.contains(keyword));
        }
        if(type.contains("w")){
            searchbb.or(qg.writer.contains(keyword));
        }

        bb.and(searchbb);
        return bb;
    }

    //mypage에서 내가 쓴 방명록보기
    public List<GuestbookDTO> getListMyPage(String name) {
        //System.out.println("service-guestbook패키지 GuestbookService클래스 getListMyPage() 진입 - 회원name -> " + name);

        Optional<List<Guestbook>> listOptional = gr.findByWriter(name);
        //반환타입생성
        List<GuestbookDTO> guestbookDTOList = new ArrayList<>();

        if(listOptional.isPresent()){
            //System.out.println("service-guestbook패키지 GuestbookService클래스 getListMyPage() 진입 - 방명록이 존재할경우 진입");
            guestbookDTOList = listOptional.get().stream().map(entity -> mm.map(entity, GuestbookDTO.class))
                    .collect(Collectors.toList());
        }
        return guestbookDTOList;
    }

//System.out.println("guestbook-service클래스 dtoToEntity() 진입");
//

    public Guestbook dtoToEntity(GuestbookDTO gDto){
        //System.out.println("service-guestbook패키지 GuestbookService클래스 dtoToEntity() 진입");
        Guestbook entity = Guestbook.builder()
                .gno(gDto.getGno())
                .writer(gDto.getWriter())
                .title(gDto.getTitle())
                .content(gDto.getContent())
                .build();
        return entity;
    }


    public GuestbookDTO entityToDto(Guestbook entity){
        //System.out.println("service-guestbook패키지 GuestbookService클래스 entityToDto() 진입");
        GuestbookDTO dto = GuestbookDTO.builder()
                .gno(entity.getGno())
                .writer(entity.getWriter())
                .title(entity.getTitle())
                .content(entity.getContent())
                .gno(entity.getGno())
                .createdDate(entity.getCreatedDate())
                .modifiedDate(entity.getModifiedDate())
                .build();
        return dto;
    }

    //관리자페이지에서 방명록 목록볼때 수정날짜이용해서 보기
    public List<GuestbookDTO> findByModifiedDate(LocalDate date){
        //System.out.println("service-guestbook패키지 GuestbookService클래스 findByModifiedDate() 진입 -> "+date);


        LocalDateTime before = date.atStartOfDay();
        LocalDateTime after = date.atTime(LocalTime.MAX);

        List<Guestbook> list = gr.findByModifiedDate(before.toLocalDate().atStartOfDay(), after.toLocalDate().atTime(LocalTime.MAX));

        List<GuestbookDTO> resultList =new ArrayList<>();
        for(Guestbook dto: list){

            resultList.add(entityToDto(dto));
        }
        return resultList;
    }

    //관리자페이지에서 방명록 전체 가져오기 페이지처리없이
    public List<GuestbookDTO> findByAll(){
        //System.out.println("service-guestbook패키지 GuestbookService클래스 findByAll() 진입");

        List<Guestbook> all = gr.findAll();
        List<GuestbookDTO> guestbookDTOList=new ArrayList<>();
        for(Guestbook entity : all){
            guestbookDTOList.add(entityToDto(entity));
        }
        return guestbookDTOList;
    }

    //동적검색만
    private BooleanBuilder getSearchAdmin(GuestPageRequestDTO pageRequestDTO){
        //System.out.println("service-guestbook패키지 GuestbookService클래스 getSearchAdmin() 진입 ");

        String type = pageRequestDTO.getType();
        String keyword = pageRequestDTO.getKeyword();

        BooleanBuilder bb = new BooleanBuilder();

        QGuestbook qg = QGuestbook.guestbook;

        BooleanExpression be = qg.gno.gt(0L);
        bb.and(be);

        if(type ==null || type.trim().length()==0){
            //System.out.println("service-guestbook패키지 GuestbookService클래스 getSearch() 진입 - 검색조건이 없는 경우 진입");
            return bb;
        }

        BooleanBuilder searchbb = new BooleanBuilder();
        if(type.contains("t")){
            searchbb.or(qg.title.contains(keyword));
        }

        if(type.contains("c")){
            searchbb.or(qg.content.contains(keyword));
        }
        if(type.contains("w")){
            searchbb.or(qg.writer.contains(keyword));
        }

        bb.and(searchbb);
        return bb;
    }

    //동적검색+특정날짜추가
    private BooleanBuilder getSearchAdminModifiedDate(GuestPageRequestDTO pageRequestDTO, LocalDate localDate){
        //System.out.println("service-guestbook패키지 GuestbookService클래스 getSearchAdmin() 진입 ");

        String type = pageRequestDTO.getType();
        String keyword = pageRequestDTO.getKeyword();

        BooleanBuilder bb = new BooleanBuilder();

        QGuestbook qg = QGuestbook.guestbook;

        BooleanExpression be = qg.gno.gt(0L)
                .and(qg.modifiedDate.between(localDate.atTime(LocalTime.MIN),localDate.atTime(LocalTime.MAX)));
        bb.and(be);

        if(type ==null || type.trim().length()==0){
            //System.out.println("service-guestbook패키지 GuestbookService클래스 getSearch() 진입 - 검색조건이 없는 경우 진입");
            return bb;
        }

        BooleanBuilder searchbb = new BooleanBuilder();
        if(type.contains("t")){
            searchbb.or(qg.title.contains(keyword));
        }

        if(type.contains("c")){
            searchbb.or(qg.content.contains(keyword));
        }
        if(type.contains("w")){
            searchbb.or(qg.writer.contains(keyword));
        }

        bb.and(searchbb);
        return bb;
    }


    private BooleanBuilder getSearchAdminCreatedDate(GuestPageRequestDTO pageRequestDTO, LocalDate localDate){
        //System.out.println("service-guestbook패키지 GuestbookService클래스 getSearchAdminCreatedDate() 진입 ");

        String type = pageRequestDTO.getType();
        String keyword = pageRequestDTO.getKeyword();

        BooleanBuilder bb = new BooleanBuilder();

        QGuestbook qg = QGuestbook.guestbook;

        BooleanExpression be = qg.gno.gt(0L)
                .and(qg.createdDate.between(localDate.atTime(LocalTime.MIN),localDate.atTime(LocalTime.MAX)));
        bb.and(be);

        if(type ==null || type.trim().length()==0){
            //System.out.println("service-guestbook패키지 GuestbookService클래스 getSearchAdminCreatedDate() 진입 - 검색조건이 없는 경우 진입");
            return bb;
        }

        BooleanBuilder searchbb = new BooleanBuilder();
        if(type.contains("t")){
            searchbb.or(qg.title.contains(keyword));
        }

        if(type.contains("c")){
            searchbb.or(qg.content.contains(keyword));
        }
        if(type.contains("w")){
            searchbb.or(qg.writer.contains(keyword));
        }

        bb.and(searchbb);
        return bb;
    }


    //동적검색 + 특정날짜만 조회 수정날짜 기준
    public GuestPageResultDTO<GuestbookDTO, Guestbook> getListAdminModifiedDate(GuestPageRequestDTO requestDTO, LocalDate localDate){
        //System.out.println("service-guestbook패키지 GuestbookService클래스 getListAdminModifiedDate() 진입 - 파라미터GuestPageRequestDTO -> "+ requestDTO);

        Pageable p = requestDTO.getPageable(Sort.by("gno").descending());

        BooleanBuilder bb = getSearchAdminModifiedDate(requestDTO,localDate);
        //검색조건결과인 BooleanBuilder를 파라미터로 추가한다.
        //Page<Guestbook> result = gr.findAll(bb, p);
        Page<Guestbook> result = gr.findAll(bb, p);

        Function<Guestbook, GuestbookDTO> fn =(entity -> entityToDto(entity));

        return new GuestPageResultDTO<>(result, fn);
    }

    public GuestPageResultDTO<GuestbookDTO, Guestbook> getListAdminCreatedDate(GuestPageRequestDTO requestDTO, LocalDate localDate){
        //System.out.println("service-guestbook패키지 GuestbookService클래스 getListAdminCreatedDate() 진입 - 파라미터GuestPageRequestDTO -> "+ requestDTO);

        Pageable p = requestDTO.getPageable(Sort.by("gno").descending());

        //BooleanBuilder bb = getSearchAdminModifiedDate(requestDTO,localDate);
        BooleanBuilder bb = getSearchAdminCreatedDate(requestDTO,localDate);

        //검색조건결과인 BooleanBuilder를 파라미터로 추가한다.
        //Page<Guestbook> result = gr.findAll(bb, p);
        Page<Guestbook> result = gr.findAll(bb, p);

        Function<Guestbook, GuestbookDTO> fn =(entity -> entityToDto(entity));

        return new GuestPageResultDTO<>(result, fn);
    }


    //전체 페이징처리
    public GuestPageResultDTO<GuestbookDTO, Guestbook> getListAdmin(GuestPageRequestDTO requestDTO){
        //System.out.println("service-guestbook패키지 GuestbookService클래스 getListAdmin() 진입 - 파라미터GuestPageRequestDTO -> "+ requestDTO);

        Pageable p = requestDTO.getPageable(Sort.by("gno").descending());

        BooleanBuilder bb = getSearchAdmin(requestDTO);
        //검색조건결과인 BooleanBuilder를 파라미터로 추가한다.
        //Page<Guestbook> result = gr.findAll(bb, p);
        Page<Guestbook> result = gr.findAll(bb, p);

        Function<Guestbook, GuestbookDTO> fn =(entity -> entityToDto(entity));

        return new GuestPageResultDTO<>(result, fn);
    }


    //특정날짜만 조회 수정날짜 기준
    public List<GuestbookDTO> findByModifiedDateLocalDate(LocalDate localDate){
        //System.out.println("service-guestbook패키지 GuestbookService클래스 findByModifiedDate() 진입 -> "+localDate);
        List<Guestbook> list = gr.findByModifiedDateLocalDate(localDate);

        //반환타입
        List<GuestbookDTO> result = new ArrayList<>();
        for(Guestbook dto: list){
            result.add(entityToDto(dto));
        }

        return result;
    }



    //특정날짜방명록수
    public Long getCountLocalDate(LocalDate localDate){
        //System.out.println("service-guestbook패키지 GuestbookReplyService클래스 getCountLocalDate() 진입");

        Long count = gr.getCountLocalDate(localDate.atTime(LocalTime.MIN), localDate.atTime(LocalTime.MAX));
        //System.out.println("service-guestbook패키지 GuestbookReplyService클래스 getCountLocalDate() 진입 localDate 방명록수 -> "+ count);
        return count;
    }
}
