package org.example.springboot231026.dto.dogsell;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.springboot231026.domain.dog.DogSellImage;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class DogSellImageDTO {
    //Long inum; String uuid,imgName,path; getImageURL() getThumbnailURL()

    private Long inum;

    private String uuid,imgName,path;

    public DogSellImageDTO(String uuid, String imgName, String path){
        //System.out.println("dto-dogsell클래스 DogSellImageDTO 생성자진입");
        
        this.imgName=imgName;
        this.path=path;
        this.uuid=uuid;
        //System.out.println("dto-dogsell클래스 DogSellImageDTO 생성자진입 결과 ->"+this.toString());
    }

    public String getImageURL(){
        //System.out.println("dto-dogsell클래스 DogSellImageDTO getImageURL() 진입");
        try {

            return URLEncoder.encode(path+"/"+uuid+"_"+imgName,"utf-8");
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return "";
    }


    public String getThumbnailURL(){
        //System.out.println("dto-dogsell클래스 DogSellImageDTO getThumbnailURL() 진입");
        try {

            return URLEncoder.encode(path+"/s_"+uuid+"_"+imgName,"utf-8");
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return "";
    }



}
