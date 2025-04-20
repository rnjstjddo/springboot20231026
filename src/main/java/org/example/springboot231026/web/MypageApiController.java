package org.example.springboot231026.web;


import org.example.springboot231026.domain.member.Member;
import org.example.springboot231026.domain.member.MemberRepository;
import org.example.springboot231026.dto.member.MemberDTO;
import org.example.springboot231026.dto.message.MessageDTO;
import org.example.springboot231026.service.dogsell.WishNumService;
import org.example.springboot231026.service.member.MemberService;
import org.example.springboot231026.service.message.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/mypage")
public class MypageApiController {

    @Autowired
    private MemberRepository mr;

    @Autowired
    private MemberService mems;

    @Autowired
    private MessageService ms;


    @Autowired
    private WishNumService wns;


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/message/send")
    public void sendPost( @RequestBody MessageDTO mDto, @AuthenticationPrincipal MemberDTO memberDTO){
        //System.out.println("API컨트롤러 MessageApiController sendPost() 진입 ");

        //Message String email; content; number; name; Member sender;recipient; BaseTimeEntity

        Optional<Member> osender = mr.findByName(mDto.getSenderString());
        //Optional<Member> oreci = mr.findByName(memberDTO.getName());
        Optional<Member> oreci = mr.findByName(mDto.getRecipientString());

        MemberDTO s = mems.toMemberDto(osender.get());
        MemberDTO r = mems.toMemberDto(oreci.get());

        mDto.setSender(s);
        mDto.setRecipient(r);
        //System.out.println("API컨트롤러 MessageApiController sendPost() 진입 MessageDTO -> "+mDto.toString());
        ms.send(mDto);

    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/wishlist/remove/{wishnum}")
    public @ResponseBody Boolean wishlistremove( @RequestParam String name, @PathVariable Long wishnum, @AuthenticationPrincipal MemberDTO memberDTO) {
        //System.out.println("API컨트롤러 MessageApiController wishlistremove() 진입 파라미터 -> "+name+", "+ wishnum);
        // {"name":"rnjstjddo88@naver.com"}, 2 @RequestBody
        boolean result = wns.wishlistremove(wishnum, name);//rnjstjddo88@naver.com, 2
        if(!result){
            return false;
        }
        return true;
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/message/removeRecipient")
    public String removeRecipientPost(@AuthenticationPrincipal MemberDTO memberDTO
            , @RequestBody Map<String, String> map){
        //System.out.println("API컨트롤러 MessageApiController removeRecipientPost() 진입 "+
                    //"삭제할 받은쪽지의 Map<String, String> -> "+ map);
        String name = map.get("name");
        String number = map.get("number");

        Long messageid = Long.valueOf(map.get("messageid"));
        ms.removeRecipient(messageid);
        return "success";
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/message/removeSender")
    public String removeSenderPost(@AuthenticationPrincipal MemberDTO memberDTO,
                                   @RequestBody Map<String, String> map){
        //System.out.println("API컨트롤러 MessageApiController removeSenderPost() 진입 " +
                //"삭제할 보낸쪽지의 Map<String,String> -> "+map);
        String name = map.get("name");
        String number = map.get("number");
        Long messageid = Long.valueOf(map.get("messageid"));

        ms.removeSender(messageid);

        return "success";
    }

}
