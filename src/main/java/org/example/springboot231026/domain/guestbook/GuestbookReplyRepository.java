package org.example.springboot231026.domain.guestbook;

import org.example.springboot231026.domain.posts.PostReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface GuestbookReplyRepository extends JpaRepository<GuestbookReply,Long>,QuerydslPredicateExecutor<GuestbookReply> {

    @Query("select r from GuestbookReply r where r.gno =:gno")
    List<GuestbookReply> list(@Param("gno") Long gno);

    @Modifying
    @Query("delete from GuestbookReply r where r.gno =:gno")
    void deleteByGno(@Param("gno") Long gno);

    @Query("select count(r.grno) from GuestbookReply r where r.gno =:gno")
    Long gnoCount(@Param("gno") Long gno);


    //관리자페이지 방명록댓글수
   // @Query("select count(r) from GuestbookReply r where r.modifiedDate like localDate%")
    //Long getCountLocalDate(@Param("localDate") LocalDate localDate);

    @Query("select count(g) from GuestbookReply g where g.createdDate >= :before and g.createdDate < :after")
    Long getCountLocalDate(@Param("before") LocalDateTime before, @Param("after") LocalDateTime after);


}
