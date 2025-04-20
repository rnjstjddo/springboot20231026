package org.example.springboot231026.web;


import org.example.springboot231026.domain.guestbook.GuestbookReplyRepository;
import org.example.springboot231026.dto.guestbook.GuestPageResultDTO;
import org.example.springboot231026.dto.guestbook.GuestbookReplyDTO;
import org.example.springboot231026.dto.member.MemberDTO;
import org.example.springboot231026.dto.guestbook.GuestbookDTO;
import org.example.springboot231026.dto.guestbook.GuestPageRequestDTO;
import org.example.springboot231026.dto.post.PostReplyDTO;
import org.example.springboot231026.service.guestbook.GuestbookReplyService;
import org.example.springboot231026.service.guestbook.GuestbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/guestbook")
public class GuestbookController {

    @Autowired
    private GuestbookService gs;

    @Autowired
    private GuestbookReplyService grs;

    @Autowired
    private GuestbookReplyRepository grrepo;

    @GetMapping("")
    public String index() {
        //System.out.println("View컨트롤러 GuestbookController index() 진입");

        return "redirect:/guestbook/list";
    }

    @GetMapping("/list")
    public void list(@ModelAttribute("pageRequestDTO") GuestPageRequestDTO pageRequestDTO, Model model,
                     @AuthenticationPrincipal MemberDTO memberDTO){
        //System.out.println("View컨트롤러 GuestbookController list() 진입");
        GuestPageResultDTO pResponseDto = gs.getList(pageRequestDTO);
        if(pResponseDto.getDtoList().size() >0 && pResponseDto.getEnd() !=0) {
            //System.out.println("View컨트롤러 GuestbookController list() 진입 "+
                   // " 방명록이 존재할 경우 진입 GuestPageResultDTO - > "+ pResponseDto.toString());

            model.addAttribute("pResponseDtoList", pResponseDto.getDtoList());
            model.addAttribute("pResponseDto", pResponseDto);

            //해당 방명록의 댓글수 (GuestbookDTO)
            Map<Long, Long> gnoReplyCount =null;
            List<Map<Long, Long>> gnoReplyCountList =null;


            List<GuestbookDTO> guestbookDTOList = pResponseDto.getDtoList();
            List<Long> gnoList = guestbookDTOList.stream()
                    .map(GuestbookDTO::getGno).collect(Collectors.toList());
/*


            model.addAttribute("replyCount", ( Map<Long, Long>)gnoList.stream()
                    //.map( i-> gnoReplyCount.put(i, grrepo.gnoCount(i))).collect(Collectors.toMap()));
                    .flatMap(gno -> grrepo.gnoCount(gno)) // Stream<Item>
                    .collect(Collectors.toMap(, Function.identity()));
*/


            gnoReplyCount = gnoList.stream()
				.collect(Collectors.toMap(
						k -> k, v -> grrepo.gnoCount((v))
				)); //Map<Long,Long>
/*

            gnoReplyCount =gnoReplyCount.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .map(
                        entry-> {
                            System.out.println("[key]:" + entry.getKey() + ", " +
                                    " [value]:" + entry.getValue());
                            return entry;
                        }).collect(Map<>);

*/

            gnoReplyCountList = List.of(gnoReplyCount); //List<Map<Long,Long>>

            model.addAttribute("map",gnoReplyCount);

            for(Map<Long, Long> map : gnoReplyCountList){
                map.forEach( (k,v) ->
                {

                    //model.addAttribute("map",map);
                    //System.out.println("View컨트롤러 GuestbookController list() 진입 " +
                            //" 방명록이 존재할 경우 진입 Map 키-> "+ k+ ", 값 -> "+ v );
                });
            }//for문
        }


        //System.out.println("차이 GuestPageRequestDTO.getPage() -> "+pageRequestDTO.getPage()+
                //", GuestPageResultDTO -> "+pResponseDto.getPage());
        if(memberDTO !=null) {
            model.addAttribute("loginMember", memberDTO.getName());
        }
    }

    @GetMapping("/register")
    public String register(@ModelAttribute("pageRequestDTO") GuestPageRequestDTO pageRequestDTO, Model model,
                         @AuthenticationPrincipal MemberDTO memberDTO){
        //System.out.println("View컨트롤러 GuestbookController register() 진입");

        if(memberDTO ==null) {
            return "redirect:/member/login";
        }

        if(memberDTO !=null) {
            model.addAttribute("loginMember", memberDTO.getName());

        }
        return null;
    }

    @PostMapping("/register")
    public String registerPost(@ModelAttribute("pageRequestDTO") GuestPageRequestDTO pageRequestDTO, GuestbookDTO gDto,
                               @AuthenticationPrincipal MemberDTO memberDTO, RedirectAttributes rttrs){
        //System.out.println("View컨트롤러 GuestbookController registerPost() 진입 파라미터 GuestPageRequestDTO -> "+pageRequestDTO);

        Long gno =gs.register(gDto);

        rttrs.addFlashAttribute("gno", gno);
        rttrs.addAttribute("page", pageRequestDTO.getPage());
        rttrs.addAttribute("size", pageRequestDTO.getSize());

        rttrs.addAttribute("keyword", pageRequestDTO.getKeyword());
        rttrs.addAttribute("type", pageRequestDTO.getType());

        if(memberDTO ==null) {
            return "redirect:/member/login";
        }

        if(memberDTO !=null) {
            rttrs.addAttribute("loginMember", memberDTO.getName());
        }
        return "redirect:/guestbook/list";
    }

    @GetMapping("/read")
    public String read(@ModelAttribute("pageRequestDTO") GuestPageRequestDTO pageRequestDTO, Long gno,
                     @AuthenticationPrincipal MemberDTO memberDTO, Model model){
        //System.out.println("View컨트롤러 GuestbookController read() 진입 파라미터 -> "+gno);
        GuestbookDTO guestbookDTO=gs.read(gno);
        model.addAttribute("gDto", guestbookDTO);

        //댓글조회 함께 보여주기
        List<GuestbookReplyDTO> guestbookReplyDTOList = grs.list(gno);

        if(memberDTO ==null) {
            //System.out.println("View컨트롤러 GuestbookController read() 진입 -> 방명록작성자 -> "+guestbookDTO.getWriter());
            return "redirect:/member/login";
        }

        if(memberDTO !=null) {
            //System.out.println("View컨트롤러 GuestbookController read() 진입 - 현재 로그인한 회원 -> "+memberDTO.getName()+", -> 방명록작성자 -> "+guestbookDTO.getWriter());
            if(guestbookReplyDTOList !=null){
                //System.out.println("VIEW컨트롤러 GuestbookController read()진입 - 댓글목록 List<GuestbookReplyDTO>  null 아닐 경우 출력 -> "+ guestbookReplyDTOList.toString());
                model.addAttribute("rDtoList", guestbookReplyDTOList);
            }
            model.addAttribute("loginMember", memberDTO.getName());
        }
        return null;
    }

    @PostMapping("/modify")
    public String modify(@ModelAttribute("pageRequestDTO") GuestPageRequestDTO pageRequestDTO,
                         @AuthenticationPrincipal MemberDTO memberDTO,GuestbookDTO gDto, RedirectAttributes rttrs){
        //System.out.println("View컨트롤러 GuestbookController modify() 진입");

        gs.modify(gDto);

        rttrs.addFlashAttribute("modifygno", gDto.getGno());
        rttrs.addAttribute("page", pageRequestDTO.getPage());
        rttrs.addAttribute("size", pageRequestDTO.getSize());
        rttrs.addAttribute("keyword", pageRequestDTO.getKeyword());
        rttrs.addAttribute("type", pageRequestDTO.getType());

        if(memberDTO ==null) {
            return "redirect:/member/login";
        }

        if(memberDTO !=null) {
            rttrs.addAttribute("loginMember", memberDTO.getName());
        }
        return "redirect:/guestbook/list";
    }

    @PostMapping("/remove")
    public String remove(@ModelAttribute("pageRequestDTO") GuestPageRequestDTO pageRequestDTO,
                         @AuthenticationPrincipal MemberDTO memberDTO, Long gno, RedirectAttributes rttrs){
        //System.out.println("View컨트롤러 GuestbookController remove() 진입");

        gs.remove(gno);
        rttrs.addAttribute("page", pageRequestDTO.getPage());
        rttrs.addAttribute("size", pageRequestDTO.getSize());

        rttrs.addAttribute("keyword", pageRequestDTO.getKeyword());
        rttrs.addAttribute("type", pageRequestDTO.getType());

        if(memberDTO ==null) {
            return "redirect:/member/login";
        }

        if(memberDTO !=null) {
            rttrs.addAttribute("loginMember", memberDTO.getName());
        }
        return "redirect:/guestbook/list";
    }
}
