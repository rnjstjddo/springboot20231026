package org.example.springboot231026.web;


import lombok.AllArgsConstructor;
import org.example.springboot231026.dto.inquiry.InquiryDto;
import org.example.springboot231026.dto.member.MemberDTO;
import org.example.springboot231026.service.inquiry.InquiryService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@AllArgsConstructor
public class InquiryController {

    private final InquiryService inquiryService;


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/inquiry/inquiry")
    public String inquiryGet( @AuthenticationPrincipal MemberDTO memberDTO, Model model) {
        //System.out.println("VIEW컨트롤러 InquiryController inquiryGet() 진입");
        try {
            if (memberDTO != null) {
                //System.out.println("VIEW컨트롤러 InquiryController inquiryGet() 진입 MemberDTO -> "+ memberDTO);
                model.addAttribute("loginMember", memberDTO);

            }
        }catch (Exception e){
            //System.out.println("VIEW컨트롤러 InquiryController inquiryGet() 진입");
            e.printStackTrace();
        }

        return "inquiry/inquiry";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/inquiry/register")
    public String inquiryRegister(InquiryDto inquiryDto, RedirectAttributes rttr) {
        //System.out.println("VIEW컨트롤러 InquiryController inquiryRegister() 진입 -> "+ inquiryDto.toString());

        boolean result = inquiryService.inquiryRegister(inquiryDto);

        //System.out.println("VIEW컨트롤러 InquiryController inquiryRegister() 진입 문의글 등록후 boolean 값 -> "+ result);

        if(result){

            //System.out.println("VIEW컨트롤러 InquiryController inquiryRegister() 진입 문의글 등록 성공진입");

            rttr.addFlashAttribute("register","success");
            return "redirect:/inquiry/inquiry";

        }else{
            //System.out.println("VIEW컨트롤러 InquiryController inquiryRegister() 진입 문의글 등록 성공진입");

            rttr.addFlashAttribute("register","fail");
            return "redirect:/inquiry/inquiry";

        }
    }
}
