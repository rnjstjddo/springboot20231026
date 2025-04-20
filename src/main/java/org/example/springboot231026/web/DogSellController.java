package org.example.springboot231026.web;


import org.example.springboot231026.domain.dog.DogSellReply;
import org.example.springboot231026.domain.dog.WishNum;
import org.example.springboot231026.dto.dogsell.*;
import org.example.springboot231026.dto.dogsell.cart.WishNumDTO;
import org.example.springboot231026.dto.dogsell.reply.DogSellReplyDTO;
import org.example.springboot231026.dto.member.MemberDTO;
import org.example.springboot231026.service.dogsell.DogSellReplyService;
import org.example.springboot231026.service.dogsell.DogSellService;
import org.example.springboot231026.service.dogsell.WishNumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/dogsell")
public class DogSellController {

    @Autowired
    private DogSellService dss;

    //댓글Service추가
    @Autowired
    private DogSellReplyService dsrs;

    @Autowired
    private WishNumService wns;

    @GetMapping("/list") //dogsell/list
    public void list(@AuthenticationPrincipal MemberDTO memberDTO, Model model){
        //System.out.println("컨트롤러DogSellController list() 진입");

        try {
            if (memberDTO != null) {
                //System.out.println("컨트롤러DogSellController list() ");
                //model.addAttribute("loginMember", memberDTO.getName());
                model.addAttribute("loginMember", memberDTO.getName());
                //model.addAttribute("loginMemberEmail", memberDTO.getEmail());
            }

            List<DogSellListDTO> dsListDto = dss.list();
            if(dsListDto !=null) {

                //System.out.println("컨트롤러DogSellController list() 진입 List<DogSellListDTO> getDno()-> "
                        //+ dsListDto.stream().map(ds -> ds.getDno()).collect(Collectors.toList()).toString());
                model.addAttribute("dsListDto", dsListDto);
            }

            List<String> completeList= dsListDto.stream().map(dto -> dto.getComplete())
                    .collect(Collectors.toList());
            //System.out.println("컨트롤러DogSellController list() 진입 complete 값 -> "+ completeList.toString());

            Map<String, Object> map = wns.wishNumListGet(memberDTO.getName());
            //Map<String, Object> map = wns.wishNumListGet(memberDTO.getEmail());


            List<WishNumDTO> wishNumDTOList = (List<WishNumDTO>) map.get("wishNumDTOList");
            //System.out.println("컨트롤러DogSellController list() 진입 List<WishNumDTO> getWishNum()-> "
            //        + wishNumDTOList.stream().map(ds -> ds.getWishNum()).collect(Collectors.toList()).toString());
            //wishNum는 중복이므로 불가능.

                //1번째 위시리스트 담긴상품의 경우
                List<DogSellListDTO> dsListDtoWishNum = new ArrayList<>();
                //2번째 위시리스트 담기지 않은 상품의 경우
                List<DogSellListDTO> dsListDtoWishNumNot = new ArrayList<>();

                if (wishNumDTOList != null && wishNumDTOList.size() != 0) {
                    //System.out.println("컨트롤러DogSellController list() - List<WishNumDTO> 존재할경우 진입 size() -> " + wishNumDTOList.size());
                    //System.out.println("컨트롤러DogSellController list() - List<WishNumDTO> 존재할경우 진입 toString() -> " + wishNumDTOList.toString());

                    Long countWishNum = (Long) map.get("countWishNum");

                    for (WishNumDTO wishNumDTO : wishNumDTOList) {
                        for (DogSellListDTO dogSellListDTO : dsListDto) { //List<DogSellListDTO> dsListDto = dss.list()
                            if (dogSellListDTO.getDno() == wishNumDTO.getWishNum()) {
                                dsListDtoWishNum.add(dogSellListDTO);
                            }
                        }
                    }//바깥for문


                    //중복안되는 번호 가려내기 A가 기준으로 차집합
                    dsListDtoWishNumNot = dsListDto.stream().filter(first -> wishNumDTOList.stream()
                            .noneMatch(second -> {
                                return first.getDno().equals(second.getWishNum());
                            })).collect(Collectors.toList());

                    //System.out.println("컨트롤러DogSellController list()  " +
                           // "- List<WishNumDTO> 존재할경우 진입 - 위시리스트에 담긴상품 List<WishNumDTO> getDno()-> "
                            //+ dsListDtoWishNum.stream().map(ds -> ds.getDno()).collect(Collectors.toList())
                            //.toString());

                    //System.out.println("컨트롤러DogSellController list() " +
                            //"- List<WishNumDTO> 존재할경우 진입 - 위시리스트에 담기지 않은 상품 List<WishNumDTO> getDno() -> "
                            //+ dsListDtoWishNumNot.stream().map(ds -> ds.getDno()).collect(Collectors.toList())
                            //.toString());

                    model.addAttribute("countWishNum", countWishNum);
                    model.addAttribute("dsListDtoWishNumNot", dsListDtoWishNumNot);// 담기지않은상품

                    model.addAttribute("dsListDtoWishNum", dsListDtoWishNum);// 담긴상품

                    //model.addAttribute("wishNumDTOList", wishNumDTOList);
                    //model.addAttribute("wishNumDTOListanother", false);
                }//if문 위시번호 존재할경우 List<WishNumDTO>

        }catch (Exception e){
            //System.out.println("컨트롤러DogSellController list() - catch() 진입");
            e.printStackTrace();
        }

        //model.addAttribute("wishNumDTOListboolean", false);
    }


    @GetMapping("/register")
    public void register(@AuthenticationPrincipal MemberDTO memberDTO, Model model){
        //System.out.println("컨트롤러DogSellController register() 진입");
        try {
            if (memberDTO != null) {
                //System.out.println("컨트롤러DogSellController registerPost()  ");
                model.addAttribute("loginMember", memberDTO.getName());
                //model.addAttribute("loginMemberEmail", memberDTO.getEmail());
            }
        }catch (Exception e){
            //System.out.println("컨트롤러DogSellController registerPost() - catch() 진입");
            e.printStackTrace();
        }

    }
    
    //분양글등록
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/register")
    public @ResponseBody Long registerPost(@RequestBody DogSellDTO dsDto){
        //System.out.println("컨트롤러DogSellController registerPost() 진입 파라미터 DogSellDTO -> "+ dsDto.toString());
        //System.out.println("컨트롤러DogController registerPost() 진입 파라미터 DogSellDTO -> "+ dsDto.toString());
        dsDto.setComplete("false");//처음등록시에는 false 분양완료 안된상태로 값이 들어가야한다.
        Long dno =dss.register(dsDto);
        return dno;
    }

    //분양글 수정
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify")
    public String modifyPost(DogSellModifyDTO dsModifyDto,RedirectAttributes rttrs){
        //System.out.println("컨트롤러DogSellController modifyPost() 진입 파라미터 DogSellModifyDTO -> "+ dsModifyDto.toString());
        rttrs.addFlashAttribute("modifySuccess", dsModifyDto.getDno());
        dss.modify(dsModifyDto);
       return "redirect:/dogsell/list";
    }

    
    //분양글삭제
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/remove")
    public String removePost(Long dno,RedirectAttributes rttrs, @AuthenticationPrincipal MemberDTO memberDTO){
        //System.out.println("컨트롤러DogSellController removePost() 진입 파라미터 게시글번호 -> "+dno);
        dss.remove(dno, memberDTO.getName());
        //dss.remove(dno, memberDTO.getEmail());

        rttrs.addFlashAttribute("removeSuccess", dno);
        //return "redirect:/dogsell/list/"+dno;
        return "redirect:/dogsell/list?dno="+dno;
    }


    @GetMapping("/read")
    public void read(@AuthenticationPrincipal MemberDTO memberDTO, Long dno, Model model){
        //System.out.println("컨트롤러DogSellController read() 진입 - 파라미터 강아지분양번호 -> "+ dno);

        DogSellReadDTO readDto = dss.read(dno);
        //System.out.println("컨트롤러DogSellController read() 진입 - DogSellReadDTO "+ readDto.toString());

        model.addAttribute("readDto", readDto);
        
        //댓글도 전달
        List<DogSellReplyDTO> rDtoList =dsrs.dsReplyListDno(dno);

        model.addAttribute("rDtoList", rDtoList);

        try {
            if (memberDTO != null) {
                //System.out.println("VIEW컨트롤러 index()진입  ");
                model.addAttribute("loginMember", memberDTO.getName());
                //model.addAttribute("loginMemberEmail", memberDTO.getEmail());
            }
        }catch (Exception e){
            //System.out.println("VIEW컨트롤러 index()진입 - catch() 진입");
            e.printStackTrace();
        }

    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify")
    public void modify(@AuthenticationPrincipal MemberDTO memberDTO, Long dno, Model model){
        //System.out.println("컨트롤러DogSellController modify() 진입 - 파라미터 강아지분양번호 -> "+ dno);

        DogSellReadDTO modifyDto = dss.read(dno);
        //System.out.println("컨트롤러DogSellController modify() 진입 - DogSellReadDTO "+ modifyDto.toString());

        model.addAttribute("modifyDto", modifyDto);

        try {
            if (memberDTO != null) {
                //System.out.println("컨트롤러DogSellController modify() 진입 ");
                model.addAttribute("loginMember", memberDTO.getName());
                //model.addAttribute("loginMemberEmail", memberDTO.getEmail());

            }
        }catch (Exception e){
            //System.out.println("컨트롤러DogSellController modify() 진입 - catch() 진입");
            e.printStackTrace();
        }

    }


    //분양완료
    @ResponseBody
    @PostMapping("/complete")
    public boolean complete(@RequestBody Map<String,Long> completeMap){
        //System.out.println("컨트롤러DogSellController complete() 진입 ");

        Long compateData = completeMap.get("readDno");
        //System.out.println("컨트롤러DogSellController complete() 진입 분양완료하려는 분양글번호 Map으로 얻기 -> "+ compateData);

        if(dss.complete(compateData)){
            return true;
        }
        return false;
    }



    @GetMapping("/list/search") //dogsell/list/search
    public String searchBreed(@AuthenticationPrincipal MemberDTO memberDTO, Model model,
                            @ModelAttribute("searchBreed") String searchBreed){
        //System.out.println("컨트롤러DogSellController searchBreed() get방식 진입 품종검색 -> "+ searchBreed);

        try {
            if (memberDTO != null) {
                //System.out.println("컨트롤러DogSellController registerPost() ");
                model.addAttribute("loginMember", memberDTO.getName());
                //model.addAttribute("loginMemberEmail", memberDTO.getEmail());
            }
            List<DogSellListDTO> dsListDto = dss.searchBreed(searchBreed);
            if(dsListDto !=null) {

                //System.out.println("컨트롤러DogSellController searchBreed() 진입 List<DogSellListDTO> getDno()-> "
                       // + dsListDto.stream().map(ds -> ds.getDno()).collect(Collectors.toList()).toString());
                model.addAttribute("dsListDto", dsListDto);
            }

            List<String> completeList= dsListDto.stream().map(dto -> dto.getComplete()).collect(Collectors.toList());

            Map<String, Object> map = wns.wishNumListGet(memberDTO.getName());
            //Map<String, Object> map = wns.wishNumListGet(memberDTO.getEmail());

            List<WishNumDTO> wishNumDTOList = (List<WishNumDTO>) map.get("wishNumDTOList");
            //System.out.println("컨트롤러DogSellController list() 진입 List<WishNumDTO> getWishNum()-> "
            //        + wishNumDTOList.stream().map(ds -> ds.getWishNum()).collect(Collectors.toList()).toString());
            //wishNum는 중복이므로 불가능.

            //1번째 위시리스트 담긴상품의 경우
            List<DogSellListDTO> dsListDtoWishNum = new ArrayList<>();
            //2번째 위시리스트 담기지 않은 상품의 경우
            List<DogSellListDTO> dsListDtoWishNumNot = new ArrayList<>();

            if (wishNumDTOList != null && wishNumDTOList.size() != 0) {
                //System.out.println("컨트롤러DogSellController searchBreed() - List<WishNumDTO> 존재할경우 진입 size() -> " + wishNumDTOList.size());

                Long countWishNum = (Long) map.get("countWishNum");
                //System.out.println("컨트롤러DogSellController searchBreed() - List<WishNumDTO> 존재할경우 진입 찜한상품의 개수확인 -> " +countWishNum);


                for (WishNumDTO wishNumDTO : wishNumDTOList) {
                    for (DogSellListDTO dogSellListDTO : dsListDto) { //List<DogSellListDTO> dsListDto = dss.list()
                        if (dogSellListDTO.getDno() == wishNumDTO.getWishNum()) {
                            dsListDtoWishNum.add(dogSellListDTO);
                        }
                    }
                }//바깥for문


                //중복안되는 번호 가려내기 A가 기준으로 차집합
                dsListDtoWishNumNot = dsListDto.stream().filter(first -> wishNumDTOList.stream()
                        .noneMatch(second -> {
                            return first.getDno().equals(second.getWishNum());
                        })).collect(Collectors.toList());

                //System.out.println("컨트롤러DogSellController searchBreed() " +
                       // "- List<WishNumDTO> 존재할경우 진입 - 위시리스트에 담긴상품 List<WishNumDTO> getDno()-> "
                        //+ dsListDtoWishNum.stream().map(ds -> ds.getDno()).collect(Collectors.toList())
                        //.toString());

                //System.out.println("컨트롤러DogSellController searchBreed() " +
                        //"- List<WishNumDTO> 존재할경우 진입 - 위시리스트에 담기지 않은 상품 List<WishNumDTO> getDno() -> "
                        //+ dsListDtoWishNumNot.stream().map(ds -> ds.getDno()).collect(Collectors.toList())
                        //.toString());

                model.addAttribute("countWishNum", countWishNum);
                model.addAttribute("dsListDtoWishNumNot", dsListDtoWishNumNot);// 담기지않은상품

                model.addAttribute("dsListDtoWishNum", dsListDtoWishNum);// 담긴상품

                //model.addAttribute("wishNumDTOList", wishNumDTOList);
                //model.addAttribute("wishNumDTOListanother", false);
            }//if문 위시번호 존재할경우 List<WishNumDTO>

        }catch (Exception e){
            //System.out.println("컨트롤러DogSellController registerPost() - catch() 진입");
            e.printStackTrace();
            return "home/home";
        }

        return "dogsell/list"; //redirect없으면 일반페이지로 이동
    }//품종검색


}
