package org.example.springboot231026.domain.dog;

import org.example.springboot231026.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.ManyToOne;
import java.util.List;
import java.util.Optional;

public interface WishNumRepository extends JpaRepository<WishNum, Long> {

    @Query("select wn from WishNum wn where wn.member=:member")
    Optional<List<WishNum>> findByWishNum(@Param("member") Member member);

    List<WishNum> findByWishnum(Long wishnum);

    @Query("select count(wn) from WishNum wn where wn.member=:member")
    Object getWishNumCount(@Param("member") Member member);


    @Query("select wn from WishNum wn where wn.member=:member and wn.wishnum=:wishnum")
    Optional<WishNum> getWishNum(@Param("member") Member member , @Param("wishnum") Long wishnum);

    @Query("select wn.id from WishNum wn where wn.member=:member")
    List<Long> getWithNumIdByMember(@Param("member") Member member);




}