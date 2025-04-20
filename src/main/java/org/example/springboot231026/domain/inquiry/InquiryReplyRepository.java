package org.example.springboot231026.domain.inquiry;

import org.example.springboot231026.domain.inquiry.search.InquirySearch;
import org.example.springboot231026.dto.inquiry.InquiryDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface InquiryReplyRepository extends JpaRepository<InquiryReply, Long> {


    @Query("select ir from InquiryReply ir where ir.inquiry.innum =:innum")
    Optional<InquiryReply> getInquiryReply(@Param("innum") Long innum);

}
