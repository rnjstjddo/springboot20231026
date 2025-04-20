package org.example.springboot231026.domain.dog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DogSellReplyRepository extends JpaRepository<DogSellReply, Long> {


    @Query("select r.drno from DogSellReply r where r.replyer=:replyer")
    List<Long> getDogSellReplyDrnoByWriter(@Param("replyer") String replyer);

    @Query("select r from DogSellReply r where r.dogsell.dno=:dno")
    Optional<List<DogSellReply>> dsReplyListDno(@Param("dno") Long dno);
}
