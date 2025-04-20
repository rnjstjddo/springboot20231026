package org.example.springboot231026.web;


import org.example.springboot231026.dto.dogsell.DogSellReadDTO;
import org.example.springboot231026.dto.dogsell.cart.WishNumDTO;
import org.example.springboot231026.dto.guestbook.GuestbookDTO;
import org.example.springboot231026.dto.inquiry.InquiryDto;
import org.example.springboot231026.dto.inquiry.InquiryReplyDto;
import org.example.springboot231026.dto.member.MemberDTO;
import org.example.springboot231026.dto.message.MessageDTO;
import org.example.springboot231026.dto.post.PostsResponseDto;
import org.example.springboot231026.service.dogsell.WishNumService;
import org.example.springboot231026.service.guestbook.GuestbookService;
import org.example.springboot231026.service.inquiry.InquiryReplyService;
import org.example.springboot231026.service.inquiry.InquiryService;
import org.example.springboot231026.service.message.MessageService;
import org.example.springboot231026.service.posts.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.util.MapUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/mypage")
public class MypageController {

    @Autowired
    private MessageService ms;

    @Autowired
    private WishNumService wns;

    @Autowired
    private GuestbookService gs;

    @Autowired
    private PostsService ps;

    @Autowired
    private InquiryService is;

    @Autowired
    private InquiryReplyService irs;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/message")
    public void list(Model model, @AuthenticationPrincipal MemberDTO memberDTO){
        //System.out.println("컨트롤러 MypageController list() 진입");

        List<MessageDTO> senderResult = ms.senderList(memberDTO.getEmail());
        if (senderResult != null && senderResult.size() > 0) {
            //System.out.println("컨트롤러 MypageController list() 진입 보낸쪽지 개수 -> "+ senderResult.size());
            model.addAttribute("seDtoList",senderResult);
            model.addAttribute("countSender",senderResult.size());

        }

        List<MessageDTO> recipientResult = ms.recipientList(memberDTO.getEmail());
        if (recipientResult != null && recipientResult.size() > 0) {

            //System.out.println("컨트롤러 MypageController list() 진입 받은쪽지 개수 -> "+ recipientResult.size());
            model.addAttribute("reDtoList",recipientResult);
            model.addAttribute("countRecipient",recipientResult.size());
        }

        if(memberDTO !=null){
            //System.out.println("컨트롤러 MypageController list() 진입- MemberDTO 존재할때 진입");
            model.addAttribute("memberDTO",memberDTO);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/post")
    public void post(Model model, @AuthenticationPrincipal MemberDTO memberDTO) {
        //System.out.println("컨트롤러 MypageController post() 진입");
        List<PostsResponseDto> postsResponseDtoList = ps.listMyPage(memberDTO.getName());

        if(memberDTO !=null){
            //System.out.println("컨트롤러 MypageController guestbook() 진입- MemberDTO 존재할때 진입");
            model.addAttribute("memberDTO",memberDTO);
        }

        if(postsResponseDtoList !=null && postsResponseDtoList.size() > 0) {
            model.addAttribute("pDtoList", postsResponseDtoList);
        }

    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/guestbook")
    public void guestbook(Model model, @AuthenticationPrincipal MemberDTO memberDTO) {
        //System.out.println("컨트롤러 MypageController guestbook() 진입");
        List<GuestbookDTO> guestbookDTOList = gs.getListMyPage(memberDTO.getName());


        if(memberDTO !=null){
            //System.out.println("컨트롤러 MypageController guestbook() 진입- MemberDTO 존재할때 진입");
            model.addAttribute("memberDTO",memberDTO);
        }

        if(guestbookDTOList !=null && guestbookDTOList.size() > 0){
            //System.out.println("컨트롤러 MypageController guestbook() 진입- List<GuestbookDTO> 존재할때 진입");
            model.addAttribute("gDtoList",guestbookDTOList);
        }
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/wishlist")
    public void wishnumlist(Model model, @AuthenticationPrincipal MemberDTO memberDTO){
        //System.out.println("컨트롤러 MypageController wishnumlist() 진입");

        if(memberDTO !=null) {
            //System.out.println("컨트롤러 MypageController wishnumlist() 진입 -> " + memberDTO.getName());
            //System.out.println("컨트롤러 MypageController wishnumlist() 진입 -> " + memberDTO.toString());

        }
        Map<String, Object> map = wns.wishNumListGet(memberDTO.getName());
        //Map<String, Object> map = wns.wishNumListGet(memberDTO.getEmail());

        if(!MapUtils.isEmpty(map)) {
            //System.out.println("컨트롤러 MypageController wishnumlist() 진입 -Map<String,Object> 존재할경우 진입");

            List<WishNumDTO> wishNumDTOList = (List<WishNumDTO>) map.get("wishNumDTOList");
            model.addAttribute("wishNumDTOList", wishNumDTOList);

            Long countWishNum = (Long) map.get("countWishNum");
            model.addAttribute("countWishNum", countWishNum);

            model.addAttribute("memberDTO", memberDTO);

            List<DogSellReadDTO> dogSellReadDTOList = (List<DogSellReadDTO>) map.get("dogSellReadDTOList");
            model.addAttribute("dogSellReadDTOList", dogSellReadDTOList);
            //Long dno; double weight, age, price;  String writer, content,title, gender, type, name, health;
            //LocalDateTime createdDate, modifiedDate;
            // List<DogSellImageDTO> dsiDtoList;
        }else{
            //System.out.println("컨트롤러 MypageController wishnumlist() 진입 -Map<String,Object> 존재하지 않을경우 진입");

            model.addAttribute("memberDTO", memberDTO);

        }

    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/inquiry")
    public void inquiry(Model model, @AuthenticationPrincipal MemberDTO memberDTO) {
        //System.out.println("컨트롤러 MypageController inquiry() 진입");

        List<InquiryDto> inquiryDTOlist = is.getListforMember(memberDTO.getName());

        if(memberDTO !=null){
            //System.out.println("컨트롤러 MypageController inquiry() 진입- MemberDTO 존재할때 진입");
            model.addAttribute("memberDTO",memberDTO);
        }

        if(inquiryDTOlist !=null && inquiryDTOlist.size() > 0){
            //System.out.println("컨트롤러 MypageController inquiry() 진입- List<InquiryDto> 존재할때 진입");

            List<Long> innumList = inquiryDTOlist.stream().map(InquiryDto::getInnum)
                    .collect(Collectors.toList());
            //문의답변글
            List<InquiryReplyDto> inquiryReplyDTOlist = new ArrayList<>();
            
            for(Long innum : innumList) {
                InquiryReplyDto inquiryReplyDto = irs.getInquiryReply(innum);

                if(inquiryReplyDto !=null) {
                    inquiryReplyDTOlist.add(inquiryReplyDto);
                }
            }
            //문의답변글 Model담기
            if(inquiryReplyDTOlist.size() > 0) {
                //System.out.println("컨트롤러 MypageController inquiry() 진입- List<InquiryDto> 존재할때 진입 " +
                       // " List<InquiryReplyDto> 존재할때 진입 ");
                model.addAttribute("irDTOList", inquiryReplyDTOlist);

            }

           // inquiryDTOlist.stream().forEach(i->
                //System.out.println("문의글 중복 filter처리전 "+i.getInnum())
            //);

            inquiryDTOlist = inquiryDTOlist.stream().filter(i -> i.getComplete().equals("false"))
                    .collect(Collectors.toList());


            //inquiryDTOlist.stream().forEach(i->
                    //System.out.println("문의글 중복 filter처리후 "+i.getInnum())
            //);
            //답변글과 중복된다면 위의 코드에서 List에서 제외시키고 담았다
            model.addAttribute("iDTOList",inquiryDTOlist);

        }//문의글이 있을경우
    }


}
