package org.example.springboot231026.guestbook.domain;


import org.example.springboot231026.domain.guestbook.Guestbook;
import org.example.springboot231026.domain.guestbook.GuestbookRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.stream.IntStream;
@RunWith(SpringRunner.class)
@SpringBootTest
public class GuestbookRepositoryTest {


    @Autowired
    private GuestbookRepository rr;

    @Test
    public void insertDummies(){
        System.out.println("테스트시작 GuestbookRepositoryTest ");
            IntStream.rangeClosed(1,150).forEach(i -> {
                Guestbook entity = Guestbook.builder()
                        .content(i+"내용")
                        .title(i+"제목")
                        .writer(i+"작성자")
                        .build();
                rr.save(entity);

            });
    }






}
