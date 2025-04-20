package org.example.springboot231026.dto.guestbook;


import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Data
public class GuestPageResultDTO<DTO,EN> {

    private List<DTO> dtoList;

    private int totalPage, page, size, start, end;
    private boolean prev, next;

    private List<Integer> pageList;
    

    public GuestPageResultDTO(Page<EN> result, Function<EN, DTO> fn){
        //System.out.println("guestbook-dto-page클래스 GuestbookPageResultDTO 생성자진입");
        dtoList = result.stream().map(fn).collect(Collectors.toList());
        
        //초기화미리
        totalPage= result.getTotalPages();
        makePageList(result.getPageable());
    }

    private void makePageList(Pageable p) {
        //System.out.println("guestbook-dto-page클래스 GuestbookPageResultDTO makePageList() 진입 파라미터 Pageable -> "+ p);

        this.page = p.getPageNumber()+1; // 0부터 시작하기에
        this.size= p.getPageSize();

        int tempEnd =(int)(Math.ceil(page/10.0))*10; //올림을 위한 10.0 나눈다.

        start= tempEnd-9;

        prev = start>1;
        end = totalPage > tempEnd ? tempEnd :totalPage;

        next =totalPage > tempEnd;

        pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());

        //System.out.println("guestbook-dto-page클래스 GuestbookPageResultDTO makePageList() 진입 멤버필드값 지정후 클래스 ToString -> "+ this.toString());
        //System.out.println("guestbook-dto-page클래스 GuestbookPageResultDTO makePageList() 진입 멤버필드값 지정후 getPageList() -> "+ this.getPageList());

    }

    //System.out.println("guestbook-dto-page클래스 GuestbookPageResultDTO 생성자진입");

}
