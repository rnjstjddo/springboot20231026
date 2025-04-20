package org.example.springboot231026.web;


import lombok.AllArgsConstructor;
import org.example.springboot231026.dto.inquiry.InquiryReplyDto;
import org.example.springboot231026.service.inquiry.InquiryReplyService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AdminApiController {


    private final InquiryReplyService irs;

    @PostMapping("/admin/inquiry/register")
    public String inquiryReplyRegister(@RequestBody InquiryReplyDto inquiryReplyDto){
        //System.out.println("API컨트롤러 AdminApiController inquiryReplyRegister() 진입");
        Boolean result =irs.registerInquiryReply(inquiryReplyDto);

        if(result){
            //System.out.println("API컨트롤러 AdminApiController inquiryReplyRegister() 진입 문의글에 대한 답변등록 성공진입");
            return "true";
        }else{
            //System.out.println("API컨트롤러 AdminApiController inquiryReplyRegister() 진입 문의글에 대한 답변등록 실패진입");
            return "false";
        }
    }


    @PostMapping("/admin/inquiry/{innum}")
    public InquiryReplyDto inquiryReplyGet(@PathVariable("innum") Long innum){
        //System.out.println("API컨트롤러 AdminApiController inquiryReplyGet() 진입 문의글 번호 -> "+innum);
        InquiryReplyDto inquiryReplyDto = irs.getInquiryReply(innum);

        if(inquiryReplyDto ==null){
            //System.out.println("API컨트롤러 AdminApiController inquiryReplyGet() 진입 문의글에 대한 답글 " +
                   // " 가져오기 실패 진입");
            return null;
        }
        return inquiryReplyDto;
    }



}
