package org.example.springboot231026.web;

import org.example.springboot231026.domain.member.RoleType;
import org.example.springboot231026.dto.ajax.ResponseDTO;
import org.example.springboot231026.dto.member.MemberDTO;
import org.example.springboot231026.dto.page.PageRequestDTO;
import org.example.springboot231026.service.member.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;


@Controller
public class MemberController {

    @Autowired
    private MemberService ms;

    @GetMapping("/member/login")
    public String login(@ModelAttribute("pageRequestDto") PageRequestDTO requestDTO, Model model,
                      @RequestParam(required = false) String error,
                      @RequestParam(required = false) String exception,
                      RedirectAttributes rttrs) {
        //System.out.println("컨트롤러클래스 MemberController login() 진입");
        //System.out.println(error);
        //System.out.println(exception);
        //if(error == "true") {
        if (error !=null && error.equals("true")){
            //System.out.println("컨트롤러클래스 MemberController login() 진입 로그인시 에러존재할때 진입");

            rttrs.addFlashAttribute("exception", exception);
            rttrs.addFlashAttribute("error", error);
            //model.addAttribute("exception", exception);
            //model.addAttribute("error", error);
            return "redirect:/member/login";
        }
        return "member/login";
    }

    @GetMapping("/member/join")
    public void joinGet(@ModelAttribute("pageRequestDto") PageRequestDTO requestDTO, Model model){
        //System.out.println("컨트롤러클래스 MemberController joinGet() 진입");

    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/member/updateJoin")
    public void updateJoin(@ModelAttribute("pageRequestDto") PageRequestDTO requestDTO, Model model,
                           @AuthenticationPrincipal MemberDTO memberDTO){
        //System.out.println("컨트롤러클래스 MemberController updateJoin() 진입");
        model.addAttribute("memberDTO", memberDTO);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/member/updateSocialJoin")
    public String updateSocialJoin(@ModelAttribute("pageRequestDto") PageRequestDTO requestDTO, Model model,
                                   @AuthenticationPrincipal MemberDTO memberDTO, HttpServletRequest request){
        //System.out.println("컨트롤러클래스 MemberController updateSocialJoin() 진입");
        model.addAttribute("memberDTO", memberDTO);

        //System.out.println(memberDTO.getCreatedDate());
        //System.out.println(memberDTO.getModifiedDate());
        //System.out.println(memberDTO.getName());
        //System.out.println(memberDTO.getEmail());

        if(memberDTO.getCreatedDate()!=memberDTO.getModifiedDate() &&memberDTO.getName()!=memberDTO.getEmail()){
            //System.out.println("컨트롤러클래스 MemberController updateSocialJoin() 진입 닉네임을 수정한 적이 있는 소셜로그인회원이기에 홈으로 바로 홈으로 이동한다.");
            String referer = request.getHeader("Referer");
            //System.out.println("컨트롤러클래스 MemberController updateSocialJoin() 진입 referer -> "+ referer);
            //referer.indexOf("/");

            return "redirect:/home/home";
        }

        return "member/updateSocialJoin";
    }

}
