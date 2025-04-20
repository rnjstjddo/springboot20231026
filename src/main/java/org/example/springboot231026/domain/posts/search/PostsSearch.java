package org.example.springboot231026.domain.posts.search;

import org.example.springboot231026.domain.posts.PostReply;
import org.example.springboot231026.domain.posts.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface PostsSearch {


    //게시글 검색결과 타임지정
    Page<Posts> searchAll(String [] types, String keyword, Pageable pageable);


    //관리자페이지 게시글 날자까지추가
    Page<Posts> searchAllModifiedDate(String [] types, String keyword, Pageable pageable, LocalDate localDate);

    Page<Posts> searchAllCreatedDate(String [] types, String keyword, Pageable pageable, LocalDate localDate);


    //관리자페이지 게시글댓글 동적검색+페이지 댓글날자까지추가
    Page<PostReply> searchReplyAllModifiedDate(String [] types, String keyword, Pageable pageable, LocalDate localDate);
    Page<PostReply> searchReplyAllCreatedDate(String [] types, String keyword, Pageable pageable, LocalDate localDate);


    //관리자페이지 탭 게시글댓글 동적검색+페이지
    Page<PostReply> searchReplyAll(String [] types, String keyword, Pageable pageable);

}
