package org.example.springboot231026.domain.inquiry;

import org.example.springboot231026.domain.inquiry.search.InquirySearch;
import org.example.springboot231026.dto.inquiry.InquiryDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface InquiryRepository extends JpaRepository<Inquiry, Long>, InquirySearch {


    @Query("select count(g) from Inquiry g where g.createdDate >= :before and g.createdDate < :after")
    Long getCountLocalDate(@Param("before") LocalDateTime before, @Param("after") LocalDateTime after);

    //회원이 작성한 문의글 가져오기
    @Query("select i from Inquiry i where i.writer= :writer")
    Optional<List<Inquiry>> getListforMember(@Param("writer") String writer);


    //dto-> entity
    default Inquiry toEntity(InquiryDto inquiryDto){
        //System.out.println("repository-inquiry패키지 dto -> entity 변환 진입");

        Inquiry entity = Inquiry.builder()
                .email(inquiryDto.getEmail())
                .innum(inquiryDto.getInnum())
                .phone(inquiryDto.getPhone())
                .complete(inquiryDto.getComplete())
                .content(inquiryDto.getContent())
                .writer(inquiryDto.getWriter())
                .build();
        return entity;
    }

    //entity -> dto
    default InquiryDto toDto(Inquiry entity){
        //System.out.println("repository-inquiry패키지 entity -> dto 변환 진입");
        InquiryDto inquiryDto = new InquiryDto(
                entity.getInnum(),entity.getWriter(), entity.getContent(), entity.getPhone(),
                entity.getEmail(),entity.getComplete(), entity.getCreatedDate(), entity.getModifiedDate()
        );
        //System.out.println("repository-inquiry패키지 entity -> dto 변환 진입 InquiryDto toString() -> " +inquiryDto.toString());
        return inquiryDto;
    }


}
