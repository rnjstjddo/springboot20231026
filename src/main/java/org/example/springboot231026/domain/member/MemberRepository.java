package org.example.springboot231026.domain.member;

import org.example.springboot231026.domain.member.search.MemberSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, String>, MemberSearch {


    @Query("select m from Member m where m.email= :email and m.fromSocial =:fromSocial")
    Optional<Member> findByUsername(@Param("email") String email, @Param("fromSocial") String fromSocial);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByName(String name);

    //특정날짜 회원등록수 추출
    @Query("select count(m) from Member m where m.createdDate >= :before and m.createdDate < :after")
    Long getCountLocalDate(@Param("before") LocalDateTime before, @Param("after") LocalDateTime after);



}
