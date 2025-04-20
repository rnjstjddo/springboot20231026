package org.example.springboot231026.domain.member.search;

import org.example.springboot231026.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface MemberSearch {


    //관리자페이지 날자까지추가
    Page<Member> searchMemberAllModifiedDate(String [] types, String keyword, Pageable pageable, LocalDate localDate);

    Page<Member> searchMemberAllCreatedDate(String [] types, String keyword, Pageable pageable, LocalDate localDate);


    //관리자페이지 동적검색만+페이징
    Page<Member> searchMemberAll(String [] types, String keyword, Pageable pageable);

}
