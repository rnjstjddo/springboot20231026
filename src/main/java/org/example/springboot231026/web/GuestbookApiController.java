package org.example.springboot231026.web;

import org.example.springboot231026.dto.guestbook.GuestbookReplyDTO;
import org.example.springboot231026.dto.member.MemberDTO;
import org.example.springboot231026.dto.post.PostReplyDTO;
import org.example.springboot231026.service.guestbook.GuestbookReplyService;
import org.example.springboot231026.service.guestbook.GuestbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping
public class GuestbookApiController {

    @Autowired
    private GuestbookService gs;

    @Autowired
    private GuestbookReplyService grs;


    //방명록댓글저장
    @PostMapping("/guestbook/reply/save")
    @PreAuthorize("isAuthenticated()")
    //@PreAuthorize("#memberDTO != null")
    public GuestbookReplyDTO replysave(@RequestBody GuestbookReplyDTO rDto, @AuthenticationPrincipal MemberDTO memberDTO){
        //System.out.println("REST컨트롤러 GuestbookApiController replysave() 진입 파라미터 -> "+rDto.toString());
        rDto.setMDto(memberDTO);
        return grs.save(rDto);
    }

    //방명록댓글삭제
    @DeleteMapping("/guestbook/reply/delete")
    @PreAuthorize("isAuthenticated()")
    public boolean replydelete(Long gno, Long grno, @AuthenticationPrincipal MemberDTO memberDTO){

        if(gno !=null && gno > 0) {
            //System.out.println("REST컨트롤러 GuestbookApiController replydelete() 진입 파라미터 gno -> " + gno);
            boolean result =grs.guestbookReplyDeleteGno(gno);

            if(result){
                return result;
            }
            return  false;
        }

        if(grno !=null && grno > 0 ) {
            //System.out.println("REST컨트롤러 GuestbookApiController replydelete() 진입 파라미터 grno -> " + grno);
            boolean result =grs.guestbookReplyDelete(grno);

            if(result){
                return result;
            }
            return  false;
        }
        return false;
    }
}
