package org.example.springboot231026.upload.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Data
@AllArgsConstructor
public class UploadResultDTO implements Serializable {


    private  String fileName, uuid, folderPath; //"2023\\11\\08" 브라우저에서


    public String getImageURL(){
        //System.out.println("upload-dto클래스 getImageURL() 진입 folderPath -> "+ folderPath);
        // 서버에서 저장할때 C: \ upload231026\2023\11\08

        try {

            return URLEncoder.encode(folderPath+"/"+uuid+"_"+fileName,"utf-8");
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return "";
    }

    public String getThumbnailURL(){
        //System.out.println("upload-dto클래스 getThumbnailURL() 진입");

        try {

            return URLEncoder.encode(folderPath+"/s_"+uuid+"_"+fileName,"utf-8");
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return "";
    }
}
