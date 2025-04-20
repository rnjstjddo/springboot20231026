package org.example.springboot231026.web;


import org.example.springboot231026.dto.dogsell.DogSellListDTO;
import org.example.springboot231026.dto.guestbook.GuestPageRequestDTO;
import org.example.springboot231026.dto.guestbook.GuestPageResultDTO;
import org.example.springboot231026.dto.guestbook.GuestbookDTO;
import org.example.springboot231026.dto.member.MemberDTO;
import org.example.springboot231026.dto.page.PageRequestDTO;
import org.example.springboot231026.dto.page.PageResponseDTO;
import org.example.springboot231026.service.dogsell.DogSellService;
import org.example.springboot231026.service.guestbook.GuestbookService;
import org.example.springboot231026.service.posts.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private GuestbookService gs;

    @Autowired
    private PostsService ps;

    @Autowired
    private DogSellService dss;


    @GetMapping("/home")
    public String home(@ModelAttribute("pageRequestDTO") GuestPageRequestDTO pageRequestDTO,
                     @AuthenticationPrincipal MemberDTO memberDTO, Model model,

                     @ModelAttribute("pageRequestDto") PageRequestDTO requestDTO){
        //System.out.println("컨트롤러 HomeController home() 진입");

        //분양글
        List<DogSellListDTO> dsListDto = dss.list();
        //System.out.println("HomeController home() 진입 List<DogSellListDTO> getDno()-> "
               // + dsListDto.stream().map(ds -> ds.getDno()).collect(Collectors.toList()).toString());

        if(dsListDto !=null) {
            //System.out.println("컨트롤러 HomeController home() 진입 - 분양글 List<DogSellListDTO> 존재할때 -> ");
                model.addAttribute("dsListDto", dsListDto);
        }

        //방명록
        GuestPageResultDTO guestPageResultDTO= gs.getList(pageRequestDTO);

        if(guestPageResultDTO.getDtoList().size() > 0 && guestPageResultDTO.getDtoList() !=null) {
            //System.out.println("컨트롤러 HomeController home() 진입 - 방명록 GuestPageResultDTO 존재할때 -> "+
                   // guestPageResultDTO.getDtoList().toString()
            //);
            model.addAttribute("pResponseDto", guestPageResultDTO.getDtoList()); //GuestPageResultDTO 들어간다.
            model.addAttribute("pResponseDtoPage", guestPageResultDTO.getPage());
        }

        if(memberDTO !=null) {
            //System.out.println("컨트롤러 HomeController home() 진입 - MemberDTO 회원존재할때 ");
            model.addAttribute("loginMember", memberDTO.getName());
        }
        //게시판
        PageResponseDTO responseDto= ps.list(requestDTO);

        if(responseDto.getDtoList().size() > 0 && responseDto.getDtoList()!=null) {
            //System.out.println("컨트롤러 HomeController home() 진입 - 게시판 PageResponseDTO 존재할때 -> "
              //      +responseDto.getDtoList().toString());
            model.addAttribute("responseDto", responseDto.getDtoList()); //PageResponseDTO 들어간다.
        }

        return "home/home";
    }

}
