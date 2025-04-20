package org.example.springboot231026.web;


import lombok.AllArgsConstructor;
import org.example.springboot231026.dto.dogsell.DogSellListDTO;
import org.example.springboot231026.dto.guestbook.GuestPageRequestDTO;
import org.example.springboot231026.dto.guestbook.GuestPageResultDTO;
import org.example.springboot231026.dto.member.MemberDTO;
import org.example.springboot231026.dto.page.PageRequestDTO;
import org.example.springboot231026.dto.page.PageResponseDTO;
import org.example.springboot231026.service.dogsell.DogSellService;
import org.example.springboot231026.service.guestbook.GuestbookReplyService;
import org.example.springboot231026.service.guestbook.GuestbookService;
import org.example.springboot231026.service.inquiry.InquiryService;
import org.example.springboot231026.service.member.MemberService;
import org.example.springboot231026.service.posts.PostReplyService;
import org.example.springboot231026.service.posts.PostsService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/tab")
@AllArgsConstructor
public class AdminTabController {

    //분양글
    private final DogSellService dogSellService;
    //방명록
    private final GuestbookService gs;
    private final GuestbookReplyService grs;
    //게시글
    private final PostsService ps;
    private final PostReplyService prs;

    //문의
    private final InquiryService is;
    //회원
    private final MemberService ms;


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/dogsell") //admin/tab/dogsell
    public String adminDogList(@ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO, Model model,
                               @AuthenticationPrincipal MemberDTO memberDTO,
                               @RequestParam(required = false) String yearmonth) {
        //System.out.println("관리자컨트롤러AdminTabController /admin/tab/dogsell 진입 ");

        PageResponseDTO pResponseDto = dogSellService.list(pageRequestDTO);

        if (pResponseDto.getDtoList().size() > 0 && pResponseDto.getEnd() != 0) {
            //System.out.println("관리자컨트롤러AdminTabController /admin/tab/dogsell 진입 " +
                   // " PageResponseDTO 존재할경우 getTotal() - > " + pResponseDto.getTotal()+", getSize() -> "+pResponseDto.getSize());

            model.addAttribute("responseDtoList", pResponseDto.getDtoList());
            model.addAttribute("responseDto", pResponseDto);
        }

        return "admin/tab/admin_tab_dogsell";
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/post")  //admin/tab/post
    public String adminTabPost(@ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO, Model model,
                               @AuthenticationPrincipal MemberDTO memberDTO,
                               @RequestParam(required = false) String yearmonth) {
        //System.out.println("관리자컨트롤러AdminTabController /admin/tab/post 진입");

        PageResponseDTO pResponseDto = ps.list(pageRequestDTO);

        if (pResponseDto.getDtoList().size() > 0 && pResponseDto.getEnd() != 0) {
            //System.out.println("관리자컨트롤러AdminTabController /admin/tab/post 진입 " +
                   // " PageResponseDTO 존재할경우 getTotal() - > " + pResponseDto.getTotal()+", getSize() -> "+pResponseDto.getSize());

            model.addAttribute("responseDtoList", pResponseDto.getDtoList());
            model.addAttribute("responseDto", pResponseDto);
        }

        return "admin/tab/admin_tab_post";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/postreply")  //admin/tab/postreply
    public String adminTabPostReply(@ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO, Model model,
                               @AuthenticationPrincipal MemberDTO memberDTO,
                               @RequestParam(required = false) String yearmonth) {
        //System.out.println("관리자컨트롤러AdminTabController /admin/tab/postreply 진입");

        PageResponseDTO pResponseDto = prs.getListAdmin(pageRequestDTO);

        if (pResponseDto.getDtoList().size() > 0 && pResponseDto.getEnd() != 0) {
            //System.out.println("관리자컨트롤러AdminTabController /admin/tab/postreply 진입 " +
                    //" PageResponseDTO 존재할경우 getTotal() - > " + pResponseDto.getTotal()+", getSize() -> "+pResponseDto.getSize());

            model.addAttribute("responseDtoList", pResponseDto.getDtoList());
            model.addAttribute("responseDto", pResponseDto);
        }

        return "admin/tab/admin_tab_postreply";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/guestbookreply") //admin/tab/guestbookreply
    public String adminTabGuestbookReply(@ModelAttribute("pageRequestDTO") GuestPageRequestDTO pageRequestDTO, Model model,
                                          @RequestParam(required = false)String yearmonth ,
                                          @AuthenticationPrincipal MemberDTO memberDTO) {
        //System.out.println("관리자컨트롤러AdminTabController /admin/tab/postreply 진입");
        GuestPageResultDTO pResponseDto = grs.getReplyListAdmin(pageRequestDTO);

        if (pResponseDto.getDtoList().size() > 0 && pResponseDto.getEnd() != 0) {
            //System.out.println("관리자컨트롤러AdminTabController /admin/tab/postreply 진입 " +
                    //" GuestPageResultDTO getSize() - > " + pResponseDto.getSize()+", getTotalPage() -> "+ pResponseDto.getTotalPage());

            model.addAttribute("pResponseDtoList", pResponseDto.getDtoList());
            model.addAttribute("pResponseDto", pResponseDto);
        }

        return "admin/tab/admin_tab_guestbookreply";
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/guestbook") //admin/tab/guestbook
    public String adminTabGuestbook(@ModelAttribute("pageRequestDTO") GuestPageRequestDTO pageRequestDTO, Model model,
                                         @RequestParam(required = false)String yearmonth ,
                                         @AuthenticationPrincipal MemberDTO memberDTO) {
        //System.out.println("관리자컨트롤러AdminTabController /admin/tab/post 진입");
        GuestPageResultDTO pResponseDto = gs.getListAdmin(pageRequestDTO);

        if (pResponseDto.getDtoList().size() > 0 && pResponseDto.getEnd() != 0) {
            //System.out.println("관리자컨트롤러AdminTabController /admin/tab/post 진입 " +
                    //" GuestPageResultDTO getSize() - > " + pResponseDto.getSize()+", getTotalPage() -> "+ pResponseDto.getTotalPage());

            model.addAttribute("pResponseDtoList", pResponseDto.getDtoList());
            model.addAttribute("pResponseDto", pResponseDto);
        }

        return "admin/tab/admin_tab_guestbook";
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/inquiry") //admin/tab/inquiry
    public String adminInguiryList(Model model, @ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO,
                                   @AuthenticationPrincipal MemberDTO memberDTO,
                                   @RequestParam(required = false) String yearmonth) {
        //System.out.println("관리자컨트롤러AdminTabController /admin/tab/inquiry 진입");

        PageResponseDTO pResponseDto = is.getListAdmin(pageRequestDTO);

        if (pResponseDto.getDtoList().size() > 0 && pResponseDto.getEnd() != 0) {
            //System.out.println("관리자컨트롤러AdminTabController /admin/tab/inquiry 진입 " +
                    //" PageResponseDTO getSize() -> " + pResponseDto.getSize()+" , getTotal() -> "+ pResponseDto.getTotal());

            model.addAttribute("responseDtoList", pResponseDto.getDtoList());
            model.addAttribute("pResponseDto", pResponseDto);
        }

        return "admin/tab/admin_tab_inquiry";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/member") //admin/tab/member
    public String adminMemberList(Model model, @ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO,
                                  @AuthenticationPrincipal MemberDTO memberDTO,
                                  @RequestParam(required = false) String yearmonth) {
        //System.out.println("관리자컨트롤러AdminTabController /admin/tab/member 진입");

        PageResponseDTO pResponseDto = ms.getListAdmin(pageRequestDTO);

        if(pResponseDto.getDtoList().size() >0 && pResponseDto.getEnd() !=0) {
            //System.out.println("관리자컨트롤러AdminTabController /admin/tab/member 진입 " +
                  //  " PageResponseDTO getTotal() -> " + pResponseDto.getTotal()+", getSize() -> "+pResponseDto.getSize());

            model.addAttribute("responseDtoList", pResponseDto.getDtoList());
            model.addAttribute("responseDto", pResponseDto);
        }

        return "admin/tab/admin_tab_member";

    }

}
