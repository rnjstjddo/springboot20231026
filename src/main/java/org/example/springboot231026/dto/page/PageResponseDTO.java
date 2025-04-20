package org.example.springboot231026.dto.page;


import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor
public class PageResponseDTO<E> {

    private int page, size, total, start, end;
    private boolean prev, next;

    private List<E> dtoList = new ArrayList<>();

    @Builder(builderMethodName= "withAll")
    public PageResponseDTO(PageRequestDTO requestDTO, List<E> dtoList, int total){
        //System.out.println("dto-page클래스 PageResponseDTO<E> 생성자 진입 3개 파라미터 갖는다.");

        if(total<=0){
            //System.out.println("dto-page클래스 PageResponseDTO<E> 생성자 진입 3개 파라미터 갖는다. - 게시글 수 0개 이하일때 return");

            return;
        }

        this.page = requestDTO.getPage();
        this.size=requestDTO.getSize();

        this.total=total;
        this.dtoList =dtoList;

        this.end=(int)(Math.ceil(this.page/10.0))*10; //화면마지막
        this.start = this.end -9;

        int last =(int)(Math.ceil( (total/(double)size))); // 총수에 맞는 마지막번호
        this.end = end>last ? last:end;

        this.prev=this.start>1;

        this.next= total>this.end*this.size;

    }

}
