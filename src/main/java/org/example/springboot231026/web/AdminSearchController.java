/*
package org.example.springboot231026.web;

import lombok.AllArgsConstructor;
import org.example.springboot231026.domain.Count;
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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminSearchController {


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

    //방명록글 특정날짜만 쿼리만들자.
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/guestbook/list")
    public String adminGuestbookList(@ModelAttribute("pageRequestDTO") GuestPageRequestDTO pageRequestDTO, Model model,
                                     @RequestParam(required = false) String yearmonth,
                                     @AuthenticationPrincipal MemberDTO memberDTO,
                                     @RequestParam(required = false) String tabtitle,
                                     @ModelAttribute("count") Count count) {
        System.out.println("관리자컨트롤러PostController /admin/guestbook/list 진입 String타입 날짜 -> " + yearmonth);
        System.out.println("관리자컨트롤러PostController /admin/guestbook/list 진입 Count -> " + count.toString());
        System.out.println("관리자컨트롤러PostController /admin/guestbook/list 진입 tabtitle -> " + tabtitle);
        System.out.println("관리자컨트롤러PostController /admin/guestbook/list 진입 GuestPageRequestDTO -> " + pageRequestDTO.toString());


        if (tabtitle != null) {

            System.out.println("tabtitle 존재할때 진입 ->  " + tabtitle);
            model.addAttribute("tabtitle", tabtitle);
        }
        if (yearmonth != null) {
            LocalDate localDate = LocalDate.parse(yearmonth);
            model.addAttribute("localDate", yearmonth);

            //GuestPageResultDTO pResponseDto = gs.getListAdminModifiedDate(pageRequestDTO, localDate);
            GuestPageResultDTO pResponseDto = gs.getListAdminCreatedDate(pageRequestDTO, localDate);


            if (pResponseDto.getDtoList().size() > 0 && pResponseDto.getEnd() != 0) {
                System.out.println("관리자컨트롤러PostController /admin/guestbook/list 진입 " +
                        " GuestPageResultDTO getSize() -> " + pResponseDto.getPage() + ", getTotalPage() -> " + pResponseDto.getTotalPage());

                model.addAttribute("pResponseDtoList", pResponseDto.getDtoList());
                model.addAttribute("pResponseDto", pResponseDto);
            }

           *//* if(count.getGuestcount() ==null) {
                count = this.returnCount(localDate);
                model.addAttribute("count", count);
                System.out.println("Count출력 -> "+ count.toString());
            }*//*

        }//yearmonth존재할경우
        else {
            System.out.println("관리자컨트롤러PostController /admin/guestbook/list 진입 쿼리스트링으로 yearmonth 존재하지 않을때 진입");
            return "redirect:/admin/home/home";
        }

        if (memberDTO != null) {
            model.addAttribute("loginMember", memberDTO.getName());
        }

        return "admin/admin_guestbook_list";
    }


    //방명록댓글 특정날짜만 쿼리만들자.
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/guestbookreply/list")
    public String adminGuestbookReplyList(@ModelAttribute("pageRequestDTO") GuestPageRequestDTO pageRequestDTO, Model model,
                                          @RequestParam(required = false) String yearmonth,
                                          @AuthenticationPrincipal MemberDTO memberDTO,
                                          @RequestParam(required = false) String tabtitle,
                                          @ModelAttribute Count count) {
        System.out.println("관리자컨트롤러PostController /admin/guestbookreply/list 진입 String타입 날짜 -> " + yearmonth);
        System.out.println("관리자컨트롤러PostController /admin/guestbookreply/list 진입 String타입 날짜 -> " + yearmonth);
        System.out.println("관리자컨트롤러PostController /admin/guestbookreply/list 진입 Count -> " + count.toString());
        System.out.println("관리자컨트롤러PostController /admin/guestbookreply/list 진입 tabtitle -> " + tabtitle);
        System.out.println("관리자컨트롤러PostController /admin/guestbookreply/list 진입 GuestPageRequestDTO -> " + pageRequestDTO.toString());

        if (tabtitle != null) {

            System.out.println("tabtitle 존재할때 진입 ->  " + tabtitle);
            model.addAttribute("tabtitle", tabtitle);
        }

        if (yearmonth != null) {
            LocalDate localDate = LocalDate.parse(yearmonth);
            model.addAttribute("localDate", yearmonth);

            GuestPageResultDTO pResponseDto = grs.getReplyListAdmin(pageRequestDTO, localDate);

            if (pResponseDto.getDtoList().size() > 0 && pResponseDto.getEnd() != 0) {
                System.out.println("관리자컨트롤러관리자컨트롤러PostController /admin/guestbookreply/list 진입 " +
                        " GuestPageResultDTO getSize() -> " + pResponseDto.getSize() + ", getTotalPage() -> " + pResponseDto.getTotalPage());

                model.addAttribute("pResponseDtoList", pResponseDto.getDtoList());
                model.addAttribute("pResponseDto", pResponseDto);
            }

        }//yearmonth존재할경우
        else {
            System.out.println("관리자컨트롤러 /admin/guestbookreply/list 진입 쿼리스트링으로 yearmonth 존재하지 않을때 진입");
            return "redirect:/admin/home/home";
        }

        if (memberDTO != null) {
            model.addAttribute("loginMember", memberDTO.getName());
        }

        return "admin/admin_guestbookreply_list";

    }

    //게시글 날짜처리
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/post/list")
    public String adminPostList(@ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO, Model model,
                                @AuthenticationPrincipal MemberDTO memberDTO,
                                @RequestParam(required = false) String yearmonth,
                                @RequestParam(required = false) String tabtitle,
                                @ModelAttribute Count count) {
        System.out.println("관리자컨트롤러PostController /admin/post/list 진입 String타입 날짜 -> " + yearmonth);
        System.out.println("관리자컨트롤러PostController /admin/post/list 진입 Count -> " + count.toString());
        System.out.println("관리자컨트롤러PostController /admin/post/list 진입 tabtitle -> " + tabtitle);
        System.out.println("관리자컨트롤러PostController /admin/post/list 진입 GuestPageRequestDTO -> " + pageRequestDTO.toString());


        if (tabtitle != null) {

            System.out.println("tabtitle 존재할때 진입 ->  " + tabtitle);
            model.addAttribute("tabtitle", tabtitle);
        }
        if (yearmonth != null) {
            LocalDate localDate = LocalDate.parse(yearmonth);
            model.addAttribute("localDate", yearmonth);

            //PageResponseDTO pResponseDto = ps.getListAdminModifiedDate(pageRequestDTO, localDate);
            PageResponseDTO pResponseDto = ps.getListAdminCreatedDate(pageRequestDTO, localDate);

            if (pResponseDto.getDtoList().size() > 0 && pResponseDto.getEnd() != 0) {
                System.out.println("관리자컨트롤러 /admin/post/list 진입 " +
                        " PageResponseDTO getSize() -> " + pResponseDto.getSize() + ", getTotal() -> " + pResponseDto.getTotal());

                model.addAttribute("responseDtoList", pResponseDto.getDtoList());
                model.addAttribute("pResponseDto", pResponseDto);
            }

        }//yearmonth존재할경우
        else {
            System.out.println("관리자컨트롤러 /admin/post/list 진입 쿼리스트링으로 yearmonth 존재하지 않을때 진입");
            return "redirect:/admin/home/home";
        }

        if (memberDTO != null) {
            model.addAttribute("loginMember", memberDTO.getName());
        }

        return "admin/admin_post_list";
    }


    //게시글댓글 날짜처리
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/postreply/list") //admin/postreply/list
    public String adminPostReplyList(@ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO, Model model,
                                     @AuthenticationPrincipal MemberDTO memberDTO,
                                     @RequestParam(required = false) String yearmonth,
                                     @RequestParam(required = false) String tabtitle,
                                     @ModelAttribute Count count) {
        System.out.println("관리자컨트롤러PostController /admin/postreply/list 진입 String타입 날짜 -> " + yearmonth);
        System.out.println("관리자컨트롤러PostController /admin/postreply/list 진입 Count -> " + count.toString());
        System.out.println("관리자컨트롤러PostController /admin/postreply/list 진입 tabtitle -> " + tabtitle);
        System.out.println("관리자컨트롤러PostController /admin/postreply/list 진입 GuestPageRequestDTO -> " + pageRequestDTO.toString());


        if (tabtitle != null) {

            System.out.println("tabtitle 존재할때 진입 ->  " + tabtitle);
            model.addAttribute("tabtitle", tabtitle);
        }

        if (yearmonth != null) {
            LocalDate localDate = LocalDate.parse(yearmonth);
            model.addAttribute("localDate", yearmonth);

            //PageResponseDTO pResponseDto = prs.getListAdminModifiedDate(pageRequestDTO, localDate);
            PageResponseDTO pResponseDto = prs.getListAdminCreatedDate(pageRequestDTO, localDate);

            if (pResponseDto.getDtoList().size() > 0 && pResponseDto.getEnd() != 0) {
                System.out.println("관리자컨트롤러 /admin/postreply/list 진입 " +
                        " PageResponseDTO getSize() -> " + pResponseDto.getSize() + ", getTotal() -> " + pResponseDto.getTotal());

                model.addAttribute("responseDtoList", pResponseDto.getDtoList());
                model.addAttribute("pResponseDto", pResponseDto);
            }

        }//yearmonth존재할경우
        else {
            System.out.println("관리자컨트롤러 /admin/postreply/list 진입 쿼리스트링으로 yearmonth 존재하지 않을때 진입");
            return "redirect:/admin/home/home";
        }

        if (memberDTO != null) {
            model.addAttribute("loginMember", memberDTO.getName());
        }

        return "admin/admin_postreply_list";
    }


    //문의글
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/inquiry/list")
    public String adminInguiryList(Model model, @ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO,
                                   @AuthenticationPrincipal MemberDTO memberDTO,
                                   @RequestParam(required = false) String yearmonth,
                                   @RequestParam(required = false) String tabtitle,
                                   @ModelAttribute Count count) {
        System.out.println("관리자컨트롤러PostController /admin/inquiry/list 진입 String타입 날짜 -> " + yearmonth);
        System.out.println("관리자컨트롤러PostController /admin/inquiry/list 진입 Count -> " + count.toString());
        System.out.println("관리자컨트롤러PostController /admin/inquiry/list 진입 tabtitle -> " + tabtitle);
        System.out.println("관리자컨트롤러PostController /admin/inquiry/list 진입 GuestPageRequestDTO -> " + pageRequestDTO.toString());


        model.addAttribute("tabtitle", tabtitle);

        if (yearmonth != null) {
            LocalDate localDate = LocalDate.parse(yearmonth);
            model.addAttribute("localDate", yearmonth);

            //PageResponseDTO pResponseDto = is.getListAdminModifiedDate(pageRequestDTO, localDate);
            PageResponseDTO pResponseDto = is.getListAdminCreatedDate(pageRequestDTO, localDate);

            if (pResponseDto.getDtoList().size() > 0 && pResponseDto.getEnd() != 0) {
                System.out.println("관리자컨트롤러 /admin/inquiry/list 진입 " +
                        " PageResponseDTO getSize() -> " + pResponseDto.getSize());

                model.addAttribute("responseDtoList", pResponseDto.getDtoList());
                model.addAttribute("pResponseDto", pResponseDto);
            }

        }//yearmonth존재할경우
        else {
            System.out.println("관리자컨트롤러 /admin/inquiry/list 진입 쿼리스트링으로 yearmonth 존재하지 않을때 진입");
            return "redirect:/admin/home/home";
        }


        if (memberDTO != null) {
            model.addAttribute("loginMember", memberDTO.getName());
        }

        return "admin/admin_inquiry_list";
    }


    //회원
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/member/list")
    public String adminMemberList(Model model, @ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO,
                                  @AuthenticationPrincipal MemberDTO memberDTO,
                                  @RequestParam(required = false) String yearmonth,
                                  @RequestParam(required = false) String tabtitle,
                                  @ModelAttribute Count count) {
        System.out.println("관리자컨트롤러PostController /admin/member/list 진입 String타입 날짜 -> " + yearmonth);
        System.out.println("관리자컨트롤러PostController /admin/member/list 진입 Count -> " + count.toString());
        System.out.println("관리자컨트롤러PostController /admin/member/list 진입 tabtitle -> " + tabtitle);
        System.out.println("관리자컨트롤러PostController /admin/member/list 진입 GuestPageRequestDTO -> " + pageRequestDTO.toString());

        if (tabtitle != null) {
            model.addAttribute("tabtitle", tabtitle);
        }

        if (yearmonth != null) {
            LocalDate localDate = LocalDate.parse(yearmonth);

            //PageResponseDTO pResponseDto = ms.getListAdminModifiedDate(pageRequestDTO, localDate);
            PageResponseDTO pResponseDto = ms.getListAdminCreatedDate(pageRequestDTO, localDate);

            if (pResponseDto.getDtoList().size() > 0 && pResponseDto.getEnd() != 0) {
                System.out.println("관리자컨트롤러 /admin/member/list 진입 " +
                        " PageResponseDTO getSize() -> " + pResponseDto.getSize());

                model.addAttribute("responseDtoList", pResponseDto.getDtoList());
                model.addAttribute("pResponseDto", pResponseDto);
            }
        }//if존재시
        else {
            System.out.println("관리자컨트롤러 /admin/member/list 진입 쿼리스트링으로 yearmonth 존재하지 않을때 진입");
            return "redirect:/admin/home/home";
        }

        return "admin/admin_member_list";
    }

    //분양글
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/dogsell/list")
    public String adminDogSellList(Model model, @ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO,
                                  @AuthenticationPrincipal MemberDTO memberDTO,
                                   @RequestParam(required = false) String yearmonth,
                                   @RequestParam(required = false) String tabtitle,
                                  @ModelAttribute Count count) {
        System.out.println("관리자컨트롤러PostController /admin/dogsell/list 진입 String타입 날짜 -> " + yearmonth);
        System.out.println("관리자컨트롤러PostController /admin/dogsell/list 진입 Count -> " + count.toString());
        System.out.println("관리자컨트롤러PostController /admin/dogsell/list 진입 tabtitle -> " + tabtitle);
        System.out.println("관리자컨트롤러PostController /admin/dogsell/list 진입 GuestPageRequestDTO -> " + pageRequestDTO.toString());

        if (tabtitle != null) {
            model.addAttribute("tabtitle", tabtitle);
        }

        if (yearmonth != null) {
            LocalDate localDate = LocalDate.parse(yearmonth);

            //PageResponseDTO pResponseDto = ms.getListAdminModifiedDate(pageRequestDTO, localDate);
            PageResponseDTO pResponseDto = dogSellService.getListAdminCreatedDate(pageRequestDTO, localDate);

            if (pResponseDto.getDtoList().size() > 0 && pResponseDto.getEnd() != 0) {
                System.out.println("관리자컨트롤러 /admin/dogsell/list 진입 " +
                        " PageResponseDTO getSize() -> " + pResponseDto.getSize());

                model.addAttribute("responseDtoList", pResponseDto.getDtoList());
                model.addAttribute("pResponseDto", pResponseDto);
            }
        }//if존재시
        else {
            System.out.println("관리자컨트롤러 /admin/dogsell/list 진입 쿼리스트링으로 yearmonth 존재하지 않을때 진입");
            return "redirect:/admin/home/home";
        }

        //return "admin/admin_dogsell_list?yearmonth="+yearmonth+"&tabtitle="+tabtitle;
        return "admin/admin_dogsell_list";
    }


    //일자에 맞는 개수반환 현재 회원은 오류로 제외시킴
    public Count returnCount(LocalDate localDate) {
        System.out.println("관리자컨트롤러 returnCount() 진입 Count 객체반환");

        //반환타입
        Count count = new Count();

        //문의글
        count.setInquirycount(is.getCountLocalDate(localDate));
        //게시글댓글
        count.setPostreplycount(prs.getCountLocalDate(localDate));
        //게시글
        count.setPostcount(prs.getCountLocalDate(localDate));
        //방명록
        count.setGuestcount(gs.getCountLocalDate(localDate));
        //방명록댓글
        count.setGuestreplycount(grs.getCountLocalDate(localDate));

        System.out.println("관리자컨트롤러 returnCount() 진입 Count 객체반환 -> "+ count.toString());

        return count;
    }

}
*/