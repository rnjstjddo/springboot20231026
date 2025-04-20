package org.example.springboot231026.domain.inquiry.search;

import org.example.springboot231026.domain.inquiry.Inquiry;
import org.example.springboot231026.domain.posts.PostReply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface InquirySearch {


    //관리자페이지 게시글 댓글날자까지추가
    Page<Inquiry> searchInquiryAllModifiedDate(String [] types, String keyword, Pageable pageable, LocalDate localDate);

    Page<Inquiry> searchInquiryAllCreatedDate(String [] types, String keyword, Pageable pageable, LocalDate localDate);

    Page<Inquiry> searchInquiryAll(String [] types, String keyword, Pageable pageable);


}
