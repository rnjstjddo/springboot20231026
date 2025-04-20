package org.example.springboot231026.service.dogsell;

import org.example.springboot231026.dto.dogsell.DogSellListDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class DogSellServiceTest {

    @Autowired
    private DogSellService dsr;

    @Test
    @Transactional
    public void getDogSellAllImageTest() {
        System.out.println("테스트시작 domain-dog클래스 DogSellServiceTest getDogSellAllImageTest() ");

        List<DogSellListDTO> list = dsr.list();
        for(DogSellListDTO dto : list) {
            System.out.println("결과 -> " +dto.toString());
        }
    }

}