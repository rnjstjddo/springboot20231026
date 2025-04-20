package org.example.springboot231026.upload.controller;


import net.coobird.thumbnailator.Thumbnailator;
import org.example.springboot231026.upload.dto.UploadResultDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.plaf.multi.MultiListUI;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class UploadController {

    @Value("${org.example.upload231026.path}")
    private String cPath;


    @PostMapping("/uploadAjax")
    //public void uploadFile(MultipartFile[] multipartFiles){
    public ResponseEntity<List<UploadResultDTO>> uploadFile(MultipartFile[] multipartFiles){
        //System.out.println("upload-controller클래스 UploadController uploadFile() 진입");


        //반환타입 객체생성
        List<UploadResultDTO> dtoList= new ArrayList<>();

        for(MultipartFile multipartFile: multipartFiles){

            //파일검사
            if(multipartFile.getContentType().startsWith("image") == false){
                //System.out.println("upload-controller클래스 UploadController uploadFile() 진입 - 파일검사 image 시작하는 않는 파일의 경우");
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);

            }


            String originName = multipartFile.getOriginalFilename();
            //System.out.println("upload-controller클래스 UploadController uploadFile() 진입 getOriginalFilename() -> "+ originName);

            originName = originName.substring(originName.lastIndexOf("\\")+1);
            //System.out.println("upload-controller클래스 UploadController uploadFile() 진입 lastIndexOf(\\) -> "+ originName);

            //날짜폴더생성
            String datePath = makeFolder(); //c경로까지 포함
            //System.out.println("upload-controller클래스 UploadController uploadFile() 진입 날짜폴더생성 String타입-> "+ datePath);

            String uuid= UUID.randomUUID().toString();

            String saveName = cPath+ File.separator+datePath+ File.separator+uuid+"_"+originName;
            //System.out.println("upload-controller클래스 UploadController uploadFile() 진입 저장할파일경로 String타입 -> "+ saveName);

            Path savePath = Paths.get(saveName);

            //System.out.println("upload-controller클래스 UploadController uploadFile() 진입 저장할파일경로 Path타입 -> "+ savePath);

            try {

                multipartFile.transferTo(savePath);
                //썸네일생성
                String thumb = cPath+ File.separator+ datePath+File.separator+
                        "s_"+ uuid+"_"+originName;

                //System.out.println("upload-controller클래스 UploadController uploadFile() 진입 썸네일저장할파일경로 String타입 -> "+ thumb);

                File thumbFile = new File(thumb);

                Thumbnailator.createThumbnail(savePath.toFile(), thumbFile, 100,100);

                dtoList.add(new UploadResultDTO(originName, uuid, datePath));


            }catch (IOException e){
                e.printStackTrace();
            }

        }//for문
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    private String makeFolder() {
        //System.out.println("upload-controller클래스 UploadController makeFolder() 진입 - 날짜생성폴더 만들기");

        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        //System.out.println("upload-controller클래스 UploadController makeFolder() 진입 - 날짜생성폴더 만들기 DateTimeFormatter.ofPattern() -> "+ date);
        //2023/11/08
        String folderPath = date.replace("/", File.separator);

        File folder = new File(cPath, folderPath); //parent, child

        //System.out.println("upload-controller클래스 UploadController makeFolder() 진입 - 날짜생성폴더 만들기 File객체로 폴더생성 -> "+ folder);
        // C: \ upload231026 \ 2023 \ 11 \ 08
        if(folder.exists() == false){
            folder.mkdirs();
        }

        return folderPath;
    }

    @GetMapping("/display")
    private ResponseEntity<byte []> getFile(String fileName) {
        //System.out.println("upload-controller클래스 UploadController getFile() 진입 - 파일이미지 json 전송 반환타입 byte[] 파라미터 -> "+ fileName);

        ResponseEntity<byte []> result = null;

        try {

            String srcdecode = URLDecoder.decode(fileName, "utf-8"); //try-catch필수

            File file = new File(cPath + File.separator + srcdecode);

            //System.out.println("upload-controller클래스 UploadController getFile() 진입 - URLDecoder 후에 File객체생성 -> "+ file);

            HttpHeaders headers= new HttpHeaders();

            headers.add("Content-Type", Files.probeContentType(file.toPath()));

            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), headers, HttpStatus.OK);


        }catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }//서버에서 이미지 불러오기

    //업로드파일삭제 서버폴더 삭제처리
    @PostMapping("/removeFile")
    public ResponseEntity<Boolean> removeFile(String fileName){
        //System.out.println("upload-controller클래스 UploadController removeFile() 진입 - 파라미터 String -> "+ fileName);

        String srcName = null;

        try {

            srcName = URLDecoder.decode(fileName, "utf-8");
            File file = new File(cPath+File.separator+srcName); //디코딩된 이름으로 File객체생성
            boolean result = file.delete(); //서버폴더에서 삭제

            File thumb = new File(file.getParent(), "s_"+ file.getName());
            result = thumb.delete();//이미지까지 서버폴더에서 삭제한다.

            return new ResponseEntity<>(result, HttpStatus.OK);

        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

//System.out.println("upload-controller클래스 UploadController getFile() 진입 - URLDecoder 후에 File객체생성 -> "+ file);
}
