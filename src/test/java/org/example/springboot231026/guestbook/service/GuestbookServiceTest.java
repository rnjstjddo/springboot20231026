package org.example.springboot231026.guestbook.service;

import org.example.springboot231026.domain.guestbook.Guestbook;
import org.example.springboot231026.dto.guestbook.GuestbookDTO;
import org.example.springboot231026.dto.guestbook.GuestPageRequestDTO;
import org.example.springboot231026.dto.guestbook.GuestPageResultDTO;
import org.example.springboot231026.service.guestbook.GuestbookService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class GuestbookServiceTest {

    @Autowired
    private GuestbookService gs;

    @Test

    public void testRegister(){
        System.out.println("테스트 GuestbookServiceTest 시작");

        GuestbookDTO dto = GuestbookDTO.builder()
                .title("서비스테스트")
                .content("서비스테스트")
                .writer("서비스테스트")
                .build();
        Long gno = gs.register(dto);
        System.out.println("결과 gno -> "+ gno);
    }

    @Test
    public void testList(){
        System.out.println("테스트 GuestbookServiceTest 시작");

        GuestPageRequestDTO pDto =GuestPageRequestDTO.builder()
                .page(1).size(10)
                .build();

        GuestPageResultDTO<GuestbookDTO, Guestbook> rDto = gs.getList(pDto);

        System.out.println("결과 페이지처리후 ToString -> "+ rDto.toString());

    }
    @Test
    public void testSerch() {
        System.out.println("테스트 GuestbookServiceTest 시작 검색조건테스트");
        GuestPageRequestDTO pDto =GuestPageRequestDTO.builder()
                .page(1).size(10)
                .keyword("1").type("twc")
                .build();

        GuestPageResultDTO<GuestbookDTO, Guestbook> rDto = gs.getList(pDto);

        for(GuestbookDTO dto: rDto.getDtoList()){
            System.out.println("결과 -> "+dto.getGno());
        }


    }
//        System.out.println("테스트 GuestbookServiceTest 시작");


    }