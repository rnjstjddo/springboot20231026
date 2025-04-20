package org.example.springboot231026.domain.dog;

import org.example.springboot231026.domain.dog.search.DogSellSearch;
import org.example.springboot231026.dto.dogsell.DogSellListDTO;
import org.example.springboot231026.service.dogsell.DogSellService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DogSellRepository extends JpaRepository<DogSell,Long> , DogSellSearch {


    //DogSell만 전체들고오기
    List<DogSell> findAll();

    //전체목록
    @Query("select d, di from DogSell d left join DogSellImage di on di.dogsell = d " +
            " and di.inum =(select max(di2.inum) from DogSellImage di2 where di2.dogsell =d) group by d ")
    //@Query("select d, di from DogSell d left join DogSellImage di on di.dogsell = d ")
    List<Object[]> getDogSellAll();

    //전체목록
    @Query("select d, di from DogSell d left join DogSellImage di on di.dogsell = d group by d")
    List<Object[]> getDogSellAllImage();

    //강아지번호에 맞는 이미지 1개만 불러오기 /dogsell/list 목록화면처리
    @Query("select d, di from DogSell d left join DogSellImage di on di.dogsell = d " +
            "where d.dno=:dno")
    Object getDogSellOne(@Param("dno") Long dno);

    //name은 회원Member엔티티의 name멤버변수이다.
    @Query("select d.dno from DogSell d where d.name=:name")
    List<Long> getDogSellDnoByName(@Param("name") String name);


    //@Query("select d.dno from DogSell d where d.type like concat('%',:type,'%')")
    //@Query("select d.dno from DogSell d where d.type like %:type%")
    @Query("select d, di from DogSell d left join DogSellImage di on di.dogsell = d where d.type like %:type% group by d ")
    //@Query("select d, di from DogSell d left join DogSellImage di on di.dogsell = d group by d")
    List<Object[]> getDogSellByType(@Param("type") String type);


    //관리자페이지에서 날짜별 분류
    @Query("select d, di from DogSell d left join DogSellImage di on di.dogsell = d "+
            " group by d order by d.createdDate ")
    //@Query("select d, di from DogSell d left join DogSellImage di on di.dogsell = d group by d")
    List<Object []> getDogSellByModifiedDate();

    //관리자페이지 특정날짜의 분양글가져오기
    @Query("select count(g) from DogSell g where g.createdDate >= :before and g.createdDate < :after")
    Long getCountLocalDate(@Param("before") LocalDateTime before, @Param("after") LocalDateTime after);


}
