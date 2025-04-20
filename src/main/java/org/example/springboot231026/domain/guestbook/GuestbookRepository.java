package org.example.springboot231026.domain.guestbook;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface GuestbookRepository extends JpaRepository<Guestbook, Long>, QuerydslPredicateExecutor<Guestbook> {


    Optional<Guestbook> findByGno(Long gno);

    @Query("select g from Guestbook g where g.writer=:writer")
    Optional<List<Guestbook>> findByWriter(@Param("writer") String writer);

    @Query("select g.gno from Guestbook g where g.writer=:writer")
    List<Long> getGnoByWriter(@Param("writer") String writer);

    //댓글수
    //@Query("select count(g.grList) from Guestbook g where g.writer=:writer group by g")
    //List<Long> getGuestbookReplyCountByWriter(@Param("writer") String writer);

    //관리자 페이지에서 날짜를 통해
    @Query("select g from Guestbook g where g.createdDate >= :before and g.createdDate < :after")
    List<Guestbook> findByModifiedDate(@Param("before") LocalDateTime before,@Param("after") LocalDateTime after);

    //관리자페이지 특정날짜조회
    @Query("select g from Guestbook g where g.modifiedDate like :localDate%")
    List<Guestbook> findByModifiedDateLocalDate(@Param("localDate") LocalDate localDate);

    //관리자페이지 방명록수
    //@Query("select count(g) from Guestbook g where g.modifiedDate like localDate%")
    //Long getCountLocalDate(@Param("localDate") LocalDate localDate);

    @Query("select count(g) from Guestbook g where g.createdDate >= :before and g.createdDate < :after")
    Long getCountLocalDate(@Param("before") LocalDateTime before,@Param("after") LocalDateTime after);




}
