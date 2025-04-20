package org.example.springboot231026.domain.dog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DogSellImageRepository extends JpaRepository<DogSellImage, Long> {


    @Query("select di from DogSellImage di where di.dogsell.dno = :dno")
    List<DogSellImage> findByDno(@Param("dno") Long dno);
}
