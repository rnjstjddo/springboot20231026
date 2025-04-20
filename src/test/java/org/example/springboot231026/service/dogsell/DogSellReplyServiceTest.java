package org.example.springboot231026.service.dogsell;

import org.example.springboot231026.domain.dog.DogSellReplyRepository;
import org.example.springboot231026.dto.dogsell.reply.DogSellReplyDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;




@SpringBootTest
@RunWith(SpringRunner.class)
public class DogSellReplyServiceTest {

    @Autowired
    private DogSellReplyService dsrs;

    @Autowired
    private DogSellReplyRepository dsrr;

    @Autowired
    private DogSellService dss;

    /*@Test
    @Transactional
    public void testRead(){
        System.out.println("service-dogsell클래스 DogSellReplyServiceTest 시작");
        DogSellReplyDTO rDto = dsrs.dsReplyRead(1L);
        System.out.println("service-dogsell클래스 DogSellReplyServiceTest - 반환 DTO -> "+rDto.toString());

    }*/

/*    @Test
    @Transactional
    public void testModify(){
        System.out.println("service-dogsell클래스 DogSellReplyServiceTest 시작");
        Optional<DogSellReply> o = dsrr.findById(1L);
        DogSellReply entity =o.get();
        entity.change("수정합니다.");
        System.out.println("service-dogsell클래스 DogSellReplyServiceTest - 반환 DTO -> "+entity.toString());

    }*/

    /*@Test
    public void testRemove(){
        System.out.println("service-dogsell클래스 DogSellReplyServiceTest 시작");

        dsrs.dsReplyRemove(1L);
    }*/

    @Test
    @Transactional
    public void testList(){
        System.out.println("service-dogsell클래스 DogSellReplyServiceTest 시작");

        List<DogSellReplyDTO> result = dsrs.dsReplyList();
        System.out.println("service-dogsell클래스 DogSellReplyServiceTest - 결과 -> "+ result.toString());
    }
}