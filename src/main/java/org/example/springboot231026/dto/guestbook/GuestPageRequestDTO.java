package org.example.springboot231026.dto.guestbook;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Builder
@AllArgsConstructor
@Data
public class GuestPageRequestDTO {

    private int page;
    private int size;

    //추가
    private String type;
    private String keyword;


    public GuestPageRequestDTO(){
        //System.out.println("guestbook-dto-page클래스 GuestPageRequestDTO 생성자진입");
        this.page=1;
        this.size=10;
    }

    public Pageable getPageable(Sort sort){
        //System.out.println("guestbook-dto-page클래스 GuestPageRequestDTO getPageable() 진입 - 파라미터 Sort -> "+ sort);

        return PageRequest.of(page-1, size, sort);
    }

}
