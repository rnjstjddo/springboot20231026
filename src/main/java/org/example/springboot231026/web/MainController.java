package org.example.springboot231026.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping()
public class MainController {

    @GetMapping("/main")
    public String main(){
        //System.out.println("컨트롤러MainController main() 진입 html.html 이동 ");
        return "redirect:/home/home";
    }

    @GetMapping("/")
    public String basic(){
        //System.out.println("컨트롤러MainController basic() 진입 /home/home 이동");
        return "redirect:/home/home";
    }


}
