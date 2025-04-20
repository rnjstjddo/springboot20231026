package org.example.springboot231026.web;


import org.example.springboot231026.dto.HelloResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello(){
        //System.out.println("REST컨트롤러 hello() 진입");
        return "hello";
    }

    @GetMapping("/hello/dto")
    public HelloResponseDto helloDto(@RequestParam("name") String name, @RequestParam("amount") int amount){
        //System.out.println("REST컨트롤러 helloDto() 진입");

        return new HelloResponseDto(name, amount);
    }


}

