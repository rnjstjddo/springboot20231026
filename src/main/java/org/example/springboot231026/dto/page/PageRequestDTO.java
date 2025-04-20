package org.example.springboot231026.dto.page;


import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PageRequestDTO {

    @Builder.Default
    private int page=1;

    @Builder.Default
    private int size=10;

    private String type;

    private String keyword;

    private String link;

    public String getLink(){

        //System.out.println("dto-page클래스 PageRequestDTO getLink() 진입");
        if(link ==null){
            //System.out.println("dto-page클래스 PageRequestDTO getLink() 진입 -필드변수 link 가 존재하지 않을때");

            StringBuilder sb = new StringBuilder();
            sb.append("page="+this.page);
            sb.append("&size="+this.size);

            //System.out.println("dto-page클래스 PageRequestDTO getLink() 진입 -필드변수 link 가 존재하지 않을때 StringBuilder -> "+sb.toString());

            if(type != null && type.length()>0){

                //System.out.println("dto-page클래스 PageRequestDTO getLink() 진입 -필드변수 type 존재할때");

                sb.append("&type="+type);
            }

            if(keyword!=null){

                //System.out.println("dto-page클래스 PageRequestDTO getLink() 진입 -필드변수 keyword 존재할때");

                try {

                    sb.append("&keyword="+ URLEncoder.encode(keyword,"utf-8"));
                }catch(UnsupportedEncodingException e){

                    //System.out.println("dto-page클래스 PageRequestDTO getLink() 진입 -필드변수 keyword 한글인코딩 에러발생");

                }
            }//if keyword
            link =sb.toString();

            //System.out.println("dto-page클래스 PageRequestDTO getLink() 진입 - 생성한 link필드변수 -> "+ sb.toString());

        }
        return link;
    }

    public String [] getTypes(){
        //System.out.println("dto-page클래스 PageRequestDTO getTypes() 진입");
        if (type == null  || type.isEmpty() ){
            //System.out.println("dto-page클래스 PageRequestDTO getTypes() 진입 -필드변수 type이 존재하지 않는경우");
            return null;
        }
        return type.split("");
    }

    public Pageable getPageable(String... props){

        //System.out.println("dto-page클래스 PageRequestDTO getPageable() 진입");
        return PageRequest.of(this.page-1, this.size, Sort.by(props).descending());
    }




}
