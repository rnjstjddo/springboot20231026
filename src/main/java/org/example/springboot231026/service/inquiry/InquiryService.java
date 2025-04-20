package org.example.springboot231026.service.inquiry;


import lombok.AllArgsConstructor;
import org.example.springboot231026.domain.inquiry.Inquiry;
import org.example.springboot231026.domain.inquiry.InquiryRepository;
import org.example.springboot231026.dto.inquiry.InquiryDto;
import org.example.springboot231026.dto.page.PageRequestDTO;
import org.example.springboot231026.dto.page.PageResponseDTO;
import org.example.springboot231026.dto.post.PostsListResponseDto;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class InquiryService {

    private final InquiryRepository irepo;
    private final ModelMapper mm;

    public boolean inquiryRegister(InquiryDto inquiryDto){
        //System.out.println("service-inquiry패키지 InquiryService클래스 inquiryRegister() 진입");

        Inquiry entity =irepo.toEntity(inquiryDto);
        Long id = irepo.save(entity).getInnum();

        //System.out.println("service-inquiry패키지 InquiryService클래스 inquiryRegister() 진입 등록한문의글번호 확인-> "+ id);

        return id >0 ? true: false;
    }

    //회원의 문의글 가져오기
    public List<InquiryDto> getListforMember(String writer) {
        //System.out.println("service-inquiry패키지 InquiryService클래스 getListforMember() 진입");
        Optional<List<Inquiry>> o =irepo.getListforMember(writer);

        List<InquiryDto> resultList = null;

        if(o.isPresent()){
            //System.out.println("service-inquiry패키지 InquiryService클래스 getListforMember() 진입 작성한 게시글이 존재한경우 진입");

            List<Inquiry> list =o.get();
            resultList = list.stream().map(i-> irepo.toDto(i)).collect(Collectors.toList());
        }
        return resultList;
    }


        //관리자 페이지 전체문의글 가져오기
    public List<InquiryDto> findAll(){
        //System.out.println("service-inquiry패키지 InquiryService클래스 findAll() 진입");

        List<Inquiry> list =irepo.findAll();
        //반환타입
        List<InquiryDto> result = new ArrayList<>();
        for(Inquiry entity: list){
            result.add(irepo.toDto(entity));
        }
        return result;
    }


    //동적검색+특정날짜추가
    @Transactional
    public PageResponseDTO<InquiryDto> getListAdminModifiedDate(PageRequestDTO requestDTO, LocalDate localDate){
        //System.out.println("service-inquiry패키지 InquiryService클래스 getListAdminModifiedDate() 진입");

        String [] types = requestDTO.getTypes();
        String keyword = requestDTO.getKeyword();
        Pageable p = requestDTO.getPageable("innum");

        //날짜까지 추가
        Page<Inquiry> page = irepo.searchInquiryAllModifiedDate(types, keyword, p,localDate);

        //System.out.println("service-posts패키지 PostsService클래스 public Page<PostsListResponseDto> getListAdminModifiedDate(LocalDate localDate){\n() 진입 파라미터 Posts엔티티출력 -> "+ page.getContent());

        //entity->dto
        List<InquiryDto> list = page.getContent().stream()
                .map(entity -> new InquiryDto(entity.getInnum(), entity.getWriter(),entity.getContent(),
                        entity.getPhone(),entity.getEmail(), entity.getComplete(), entity.getCreatedDate(),
                        entity.getModifiedDate()))
                .collect(Collectors.toList());

        return PageResponseDTO.<InquiryDto>withAll()
                .requestDTO(requestDTO)
                .dtoList(list)
                .total((int)page.getTotalElements())
                .build();
    }


    @Transactional
    public PageResponseDTO<InquiryDto> getListAdminCreatedDate(PageRequestDTO requestDTO, LocalDate localDate){
        //System.out.println("service-inquiry패키지 InquiryService클래스 getListAdminCreatedDate() 진입");

        String [] types = requestDTO.getTypes();
        String keyword = requestDTO.getKeyword();
        Pageable p = requestDTO.getPageable("innum");

        //날짜까지 추가
        //Page<Inquiry> page = irepo.searchInquiryAllModifiedDate(types, keyword, p,localDate);
        Page<Inquiry> page = irepo.searchInquiryAllCreatedDate(types, keyword, p,localDate);

        //System.out.println("service-posts패키지 PostsService클래스 public Page<PostsListResponseDto> getListAdminCreatedDate() 진입 파라미터 Posts엔티티출력 -> "+ page.getContent());

        //entity->dto
        List<InquiryDto> list = page.getContent().stream()
                .map(entity -> new InquiryDto(entity.getInnum(), entity.getWriter(),entity.getContent(),
                        entity.getPhone(),entity.getEmail(), entity.getComplete(), entity.getCreatedDate(),
                        entity.getModifiedDate()))
                .collect(Collectors.toList());

        return PageResponseDTO.<InquiryDto>withAll()
                .requestDTO(requestDTO)
                .dtoList(list)
                .total((int)page.getTotalElements())
                .build();
    }


    //동적검색+페이지처리
    @Transactional
    public PageResponseDTO<InquiryDto> getListAdmin(PageRequestDTO requestDTO){
        //System.out.println("service-inquiry패키지 InquiryService클래스 getListAdmin() 진입");

        String [] types = requestDTO.getTypes();
        String keyword = requestDTO.getKeyword();
        Pageable p = requestDTO.getPageable("innum");

        //날짜까지 추가
        Page<Inquiry> page = irepo.searchInquiryAll(types, keyword, p);


        //entity->dto
        List<InquiryDto> list = page.getContent().stream()
                .map(entity -> new InquiryDto(entity.getInnum(), entity.getWriter(),entity.getContent(),
                        entity.getPhone(),entity.getEmail(), entity.getComplete(), entity.getCreatedDate(),
                        entity.getModifiedDate()))
                .collect(Collectors.toList());

        return PageResponseDTO.<InquiryDto>withAll()
                .requestDTO(requestDTO)
                .dtoList(list)
                .total((int)page.getTotalElements())
                .build();
    }


    //특정날짜문의글수
    public Long getCountLocalDate(LocalDate localDate){
        //System.out.println("service-inquiry패키지 InquiryService클래스 getCountLocalDate() 진입");

        Long count = irepo.getCountLocalDate(localDate.atTime(LocalTime.MIN), localDate.atTime(LocalTime.MAX));
        //System.out.println("service-inquiry패키지 InquiryService클래스 getCountLocalDate() 진입 localDate 문의글수 -> "+ count);
        return count;
    }


//System.out.println("service-inquiry패키지 InquiryService클래스 inquiryRegister() 진입");


}
