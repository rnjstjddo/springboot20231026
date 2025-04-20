package org.example.springboot231026.upload.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UploadViewController {

    @GetMapping("/uploadEx")
    public void uploadEx(){
        //System.out.println("upload-controller클래스 UploadViewController uploadEx() 진입");
    }




}
