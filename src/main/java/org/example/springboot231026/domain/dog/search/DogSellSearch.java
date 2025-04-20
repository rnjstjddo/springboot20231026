package org.example.springboot231026.domain.dog.search;

import org.example.springboot231026.domain.dog.DogSell;
import org.example.springboot231026.domain.inquiry.Inquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface DogSellSearch {


    //관리자페이지 게시글 댓글날자까지추가
    Page<DogSell> searchDogSellAllModifiedDate(String [] types, String keyword, Pageable pageable, LocalDate localDate);

    Page<DogSell> searchDogSellAllCreatedDate(String [] types, String keyword, Pageable pageable, LocalDate localDate);

    Page<DogSell> searchDogSellAll(String [] types, String keyword, Pageable pageable);


}
