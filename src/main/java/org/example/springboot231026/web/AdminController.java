package org.example.springboot231026.web;


import lombok.AllArgsConstructor;
import org.example.springboot231026.domain.Count;
import org.example.springboot231026.dto.dogsell.DogSellListDTO;
import org.example.springboot231026.dto.guestbook.GuestPageRequestDTO;
import org.example.springboot231026.dto.guestbook.GuestPageResultDTO;
import org.example.springboot231026.dto.guestbook.GuestbookDTO;
import org.example.springboot231026.dto.guestbook.GuestbookReplyDTO;
import org.example.springboot231026.dto.inquiry.InquiryDto;
import org.example.springboot231026.dto.member.MemberDTO;
import org.example.springboot231026.dto.page.PageRequestDTO;
import org.example.springboot231026.dto.page.PageResponseDTO;
import org.example.springboot231026.dto.post.PostReplyDTO;
import org.example.springboot231026.dto.post.PostsListResponseDto;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

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


    //관리자페이지 메인
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/home/home") //admin/home/home
    public String adminHome(@ModelAttribute("pageRequestDTO") GuestPageRequestDTO pageRequestDTO, Model model,
                            @AuthenticationPrincipal MemberDTO memberDTO,
                            @RequestParam(required = false) String yearmonth,
                            @RequestParam(required = false) String tabtitle) {
        //System.out.println("관리자컨트롤러 /admin/home/home 진입");

        if (tabtitle != null) {
            //System.out.println("관리자컨트롤러 /admin/home/home 진입 tabtitle 존재할때 진입 ->  " + tabtitle);
            model.addAttribute("tabtitle", tabtitle);
        } else {
            //System.out.println("관리자컨트롤러 /admin/home/home 진입 tabtitle 존재하지 않을때 진입 ->  " + tabtitle);
            model.addAttribute("tabtitle", "guestbook");
        }

        if (yearmonth != null) {
            //System.out.println("관리자컨트롤러 /admin/home/home 진입 쿼리스트링 yearmonth 있을때 진입 -> "+ yearmonth);
        }else{
            yearmonth = LocalDate.now().toString();
            //System.out.println("관리자컨트롤러 /admin/home/home 진입 쿼리스트링 yearmonth 없을때 진입 오늘날짜로 넣는다-> "+ yearmonth);
        }
//방명록-----------------------------------------

        //방명록 목록전체
        List<GuestbookDTO> guestbookDTOList = gs.findByAll();

        //방명록댓글 목록전체
        List<GuestbookReplyDTO> guestbookReplyDTOList = grs.findAll();

        if ((guestbookDTOList != null && guestbookDTOList.size() > 0) ||
                (guestbookReplyDTOList != null && guestbookReplyDTOList.size() > 0)) {
            //System.out.println("관리자컨트롤러 /admin/home/home 진입 방명록 목록과 방명록댓글 목록 모두 존재할경우 진입");
            //System.out.println("관리자컨트롤러 /admin/home/home 방명록 총 개수 -> " + guestbookDTOList.size());
            //System.out.println("관리자컨트롤러 /admin/home/home 방명록댓글 총 개수 -> " + guestbookReplyDTOList.size());

            //방명록 날짜필드 중복안되게 들고오기
            List<LocalDateTime> guestbookDateList = new ArrayList<>();

            //방명록댓글 날짜필드 중복안되게 들고오기
            List<LocalDateTime> guestbookReplyDateList = new ArrayList<>();

            if (yearmonth != null) {
                LocalDate afterYearMonth = LocalDate.parse(yearmonth).with(TemporalAdjusters.firstDayOfMonth());
                //달의 첫일자로 맞춘다
                model.addAttribute("yearmonth", afterYearMonth);

                LocalDate plusAfterYearMonth = afterYearMonth.withDayOfMonth(afterYearMonth.lengthOfMonth()); //afterYearMonth.plusMonths(1);

                //System.out.println("관리자컨트롤러 /admin/home/home 진입 원본받은 날짜 -> " + yearmonth + ", 해당 월의 첫일 -> " + afterYearMonth + ", 해당 월의 마지막일 -> " + plusAfterYearMonth);
                //방명록날짜만 중복안되게 들고오기
                guestbookDateList = guestbookDTOList.stream()
                        //.filter(r -> r.getModifiedDate().toLocalDate().isAfter(afterYearMonth))
                        //.filter(r -> r.getModifiedDate().toLocalDate().isBefore(plusAfterYearMonth))
                        //.map(GuestbookDTO::getModifiedDate)
                        //.distinct().collect(Collectors.toList());
                        .filter(r -> r.getCreatedDate().toLocalDate().isAfter(afterYearMonth))
                        .filter(r -> r.getCreatedDate().toLocalDate().isBefore(plusAfterYearMonth))
                        .map(GuestbookDTO::getCreatedDate)
                        .distinct().collect(Collectors.toList());

                //방명록댓글 날짜만 중복안되게 들고오기
                guestbookReplyDateList = guestbookReplyDTOList.stream()
                        //.filter(r -> r.getModifiedDate().toLocalDate().isAfter(afterYearMonth))
                        //.filter(r -> r.getModifiedDate().toLocalDate().isBefore(plusAfterYearMonth))
                        //.map(GuestbookReplyDTO::getModifiedDate)
                        //.distinct().collect(Collectors.toList());
                        .filter(r -> r.getCreatedDate().toLocalDate().isAfter(afterYearMonth))
                        .filter(r -> r.getCreatedDate().toLocalDate().isBefore(plusAfterYearMonth))
                        .map(GuestbookReplyDTO::getCreatedDate)
                        .distinct().collect(Collectors.toList());
            }

            //최종결과인 방명록 날짜와 글수 Map만듬
            Map<LocalDate, Long> guestbookDateCount = new HashMap<>();

            //최종결과인 방명록댓글 날짜와 글수 Map만듬
            Map<LocalDate, Long> guestbookReplyDateCount = new HashMap<>();

            //방명록 날짜별 등록개수
            for (LocalDateTime guestbookDate : guestbookDateList) {

                LocalDate localDate = guestbookDate.toLocalDate();

                //방명록
                Long guestCount = (Long) guestbookDTOList.stream()
                        //.filter(r -> r.getModifiedDate().toLocalDate().isEqual(localDate)).count();
                        .filter(r -> r.getCreatedDate().toLocalDate().isEqual(localDate)).count();
                guestbookDateCount.put(localDate, guestCount);
                //System.out.println("관리자컨트롤러 /admin/home/home " +localDate +" 일자의 방명록 갯수 guestCount ->  " + guestCount);


            }//for문

            //방명록댓글 날짜별 등록개수
            for (LocalDateTime guestbookDate : guestbookReplyDateList) {

                LocalDate localDate = guestbookDate.toLocalDate();

                //방명록댓글
                Long guestReplyCount = (Long) guestbookReplyDTOList.stream()
                        //.filter(r -> r.getModifiedDate().toLocalDate().isEqual(localDate)).count();
                        .filter(r -> r.getCreatedDate().toLocalDate().isEqual(localDate)).count();

                //System.out.println("관리자컨트롤러 /admin/home/home " +localDate +" 일자의 방명록 댓글갯수 guestReplyCount ->  " + guestReplyCount);

                guestbookReplyDateCount.put(localDate, guestReplyCount);
            }

            //방명록갯수 날짜별
            model.addAttribute("guestbookDateCount", guestbookDateCount);

            //방명록댓글갯수 날짜별
            model.addAttribute("guestbookReplyDateCount", guestbookReplyDateCount);
        }

//게시판 ___________________________________________________________________________________

        //게시판
        List<PostsListResponseDto> postsListResponseDtoList = ps.findAll();
        //게시판댓글
        List<PostReplyDTO> postReplyDTOList = prs.findAll();

        if ((postsListResponseDtoList != null && postsListResponseDtoList.size() > 0) ||
                (postReplyDTOList != null && postReplyDTOList.size() > 0)) {
            //System.out.println("관리자컨트롤러 /admin/home/home 게시판 총 개수 -> " + guestbookDTOList.size());
            //System.out.println("관리자컨트롤러 /admin/home/home 게시판 총 댓글개수 -> " + guestbookReplyDTOList.size());

            //게시판 날짜필드만 중복안되게 들고오기
            List<LocalDateTime> postsDateList = new ArrayList<>();

            //게시판댓글 날짜필드만 중복안되게 들고오기
            List<LocalDateTime> postReplyDateList = new ArrayList<>();

            if (yearmonth != null) {
                LocalDate afterYearMonth = LocalDate.parse(yearmonth).with(TemporalAdjusters.firstDayOfMonth());
                ; //달의 첫일자로 맞춘다

                LocalDate plusAfterYearMonth = afterYearMonth.withDayOfMonth(afterYearMonth.lengthOfMonth());  //afterYearMonth.plusMonths(1);
                //System.out.println("관리자컨트롤러 /admin/home/home 진입 원본받은 날짜 -> " + yearmonth + ", 해당 월의 첫일 -> " + afterYearMonth + ", 해당 월의 마지막일 -> " + plusAfterYearMonth);//받은 날짜 -> 2024-04-01, 한달더한 날짜 -> 2024-05-01

                //게시판날짜만 중복안되게 들고오기
                postsDateList = postsListResponseDtoList.stream()
                        //.filter(r -> r.getModifiedDate().toLocalDate().isAfter(afterYearMonth))
                        //.filter(r -> r.getModifiedDate().toLocalDate().isBefore(plusAfterYearMonth))
                        //.map(PostsListResponseDto::getModifiedDate)
                        //.distinct().collect(Collectors.toList());
                        .filter(r -> r.getCreatedDate().toLocalDate().isAfter(afterYearMonth))
                        .filter(r -> r.getCreatedDate().toLocalDate().isBefore(plusAfterYearMonth))
                        .map(PostsListResponseDto::getCreatedDate)
                        .distinct().collect(Collectors.toList());

                //게시판댓글 날짜만 중복안되게 들고오기
                postReplyDateList = postReplyDTOList.stream()
                        //.filter(r -> r.getModifiedDate().toLocalDate().isAfter(afterYearMonth))
                        //.filter(r -> r.getModifiedDate().toLocalDate().isBefore(plusAfterYearMonth))
                        //.map(PostReplyDTO::getModifiedDate)
                        //.distinct().collect(Collectors.toList());
                        .filter(r -> r.getCreatedDate().toLocalDate().isAfter(afterYearMonth))
                        .filter(r -> r.getCreatedDate().toLocalDate().isBefore(plusAfterYearMonth))
                        .map(PostReplyDTO::getCreatedDate)
                        .distinct().collect(Collectors.toList());

            }//if 전달되어온 yearmonth존재할때

            //최종결과인 게시판날짜와 글수 Map만듬
            Map<LocalDate, Long> postsDateCount = new HashMap<>();

            //최종결과인 게시판댓글 날짜와 글수 Map만듬
            Map<LocalDate, Long> postReplyDateCount = new HashMap<>();

            //게시판 날짜별 등록개수
            for (LocalDateTime postsDate : postsDateList) {

                LocalDate localDate = postsDate.toLocalDate();

                //게시판개수
                Long postCount = (Long) postsListResponseDtoList.stream()
                        //.filter(r -> r.getModifiedDate().toLocalDate().isEqual(localDate)).count();
                        .filter(r -> r.getCreatedDate().toLocalDate().isEqual(localDate)).count();

                //System.out.println("관리자컨트롤러 /admin/home/home " +postsDate +" 일자의 게시판갯수 postCount ->  " + postCount);
                postsDateCount.put(localDate, postCount);

            }//for문

            //게시판댓글 날짜별 등록개수
            for (LocalDateTime postReplyDate : postReplyDateList) {

                LocalDate localDate = postReplyDate.toLocalDate();

                //게시판댓글수
                Long postReplyCount = (Long) postReplyDTOList.stream()
                        //.filter(r -> r.getModifiedDate().toLocalDate().isEqual(localDate)).count();
                        .filter(r -> r.getCreatedDate().toLocalDate().isEqual(localDate)).count();

                //System.out.println("관리자컨트롤러 /admin/home/home " + postReplyDate + " 일자의 게시판댓글수 postReplyCount ->  " + postReplyCount);

                postReplyDateCount.put(localDate, postReplyCount);

            }

            //방명록갯수 날짜별
            model.addAttribute("postsDateCount", postsDateCount);
            //postsDateCount.forEach((k, v) -> System.out.println("게시판 키 -> " + k + ", 값 -> " + v));

            //방명록댓글갯수 날짜별
            model.addAttribute("postReplyDateCount", postReplyDateCount);
            //postReplyDateCount.forEach((k, v) -> System.out.println("게시판댓글 키 -> " + k + ", 값 -> " + v));

        }//전체 게시판목록이 있을때

//문의글 ___________________________________________________________________________________

        List<InquiryDto> inquiryDtoList = is.findAll();

        if (inquiryDtoList.size() > 0 && inquiryDtoList != null) {
            //System.out.println("관리자컨트롤러 /admin/home/home 총 문의글 개수 -> " + inquiryDtoList.size());

            if (yearmonth != null) {
                LocalDate afterYearMonth = LocalDate.parse(yearmonth).with(TemporalAdjusters.firstDayOfMonth());
                LocalDate plusAfterYearMonth = afterYearMonth.withDayOfMonth(afterYearMonth.lengthOfMonth()); //afterYearMonth.plusMonths(1);

                //방명록 날짜필드만 중복안되게 들고오기
                List<LocalDateTime> inquiryDateList = inquiryDtoList.stream()
                        //.filter(r -> r.getModifiedDate().toLocalDate().isAfter(afterYearMonth))
                        //.filter(r -> r.getModifiedDate().toLocalDate().isBefore(plusAfterYearMonth))
                        //.map(InquiryDto::getModifiedDate)
                        //.distinct().peek(System.out::println).collect(Collectors.toList());
                        .filter(r -> r.getCreatedDate().toLocalDate().isAfter(afterYearMonth))
                        .filter(r -> r.getCreatedDate().toLocalDate().isBefore(plusAfterYearMonth))
                        .map(InquiryDto::getCreatedDate)
                        .distinct().peek(System.out::println).collect(Collectors.toList());


                //Model객체
                Map<LocalDate, Long> inquiryDateCount = new HashMap<>();

                for (LocalDateTime inquiryDate : inquiryDateList) {

                    LocalDate localDate = inquiryDate.toLocalDate();

                    //일자별 문의글수
                    Long inquiryCount = (Long) inquiryDtoList.stream()
                            //.filter(r -> r.getModifiedDate().toLocalDate().isEqual(localDate)).count();
                            .filter(r -> r.getCreatedDate().toLocalDate().isEqual(localDate)).count();

                    //System.out.println("관리자컨트롤러 /admin/home/home " + inquiryDate+" 일자의 문의글수 inquiryCount ->  " + inquiryCount);

                    inquiryDateCount.put(localDate, inquiryCount);

                }//for종료 문의글 날짜별
                model.addAttribute("inquiryDateCount", inquiryDateCount);
                //inquiryDateCount.forEach((k, v) -> System.out.println("문의글 키 -> " + k + ", 값 -> " + v));

            }//if종료 yearmonth 존재할때
        }//전체문의글이 존재할때

//회원___________________________________________________________________________________

        List<MemberDTO> memberDTOList = ms.findAll();

        if (memberDTOList.size() > 0 && memberDTOList != null) {
            //System.out.println("관리자컨트롤러 /admin/home/home 총 회원 개수 -> " + memberDTOList.size());

            if (yearmonth != null) {
                LocalDate afterYearMonth = LocalDate.parse(yearmonth).with(TemporalAdjusters.firstDayOfMonth());
                LocalDate plusAfterYearMonth = afterYearMonth.withDayOfMonth(afterYearMonth.lengthOfMonth()); //afterYearMonth.plusMonths(1);

                //System.out.println("VIEW컨트롤러 /home/home 원본받은 날짜 -> " + yearmonth + ", 해당 월의 첫일-> " + afterYearMonth + ",해당 월의 마지막일 -> " + plusAfterYearMonth);//받은 날짜 -> 2024-04-01, 한달더한 날짜 -> 2024-05-01

                //날짜별List만들기
                List<LocalDateTime> memberDateList = memberDTOList.stream().filter(i -> i.getCreatedDate().toLocalDate().isAfter(afterYearMonth))
                        .filter(i -> i.getCreatedDate().toLocalDate().isBefore(plusAfterYearMonth))
                        .map(MemberDTO::getCreatedDate).distinct().peek(System.out::println).collect(Collectors.toList());

                //Model객체
                Map<LocalDate, Long> memberDateCount = new HashMap<>();

                for (LocalDateTime memberDate : memberDateList) {

                    LocalDate localDate = memberDate.toLocalDate();
                    //해당일자에 가입한 회원수
                    Long memberCount = (Long) memberDTOList.stream()
                            .filter(r -> r.getCreatedDate().toLocalDate().isEqual(localDate)).count();
                    //System.out.println("관리자컨트롤러 /admin/home/home " +memberDate + " 일자의 회원수 memberCount ->  " + memberCount);

                    memberDateCount.put(localDate, memberCount);

                }//for종료 문의글 날짜별
                model.addAttribute("memberDateCount", memberDateCount);
                //memberDateCount.forEach((k, v) -> System.out.println("회원 Map담긴 키 -> " + k + ", 값 -> " + v));

            }//if yearmonth null 아닐경우

        }//if문 회원List존재할때

//분양글-------------------------------------------------

        List<DogSellListDTO> dogSellListDto = dogSellService.list();

        if (dogSellListDto.size() > 0 && dogSellListDto != null) {

            //System.out.println("관리자컨트롤러 /admin/home/home 총 분양글 개수 -> " + dogSellListDto.size());

            if (yearmonth != null) {
                LocalDate afterYearMonth = LocalDate.parse(yearmonth).with(TemporalAdjusters.firstDayOfMonth());
                LocalDate plusAfterYearMonth = afterYearMonth.withDayOfMonth(afterYearMonth.lengthOfMonth()); //afterYearMonth.plusMonths(1);

                //System.out.println("VIEW컨트롤러 /home/home 원본받은 날짜 -> " + yearmonth + ", 해당 월의 첫일-> " + afterYearMonth + ",해당 월의 마지막일 -> " + plusAfterYearMonth);//받은 날짜 -> 2024-04-01, 한달더한 날짜 -> 2024-05-01

                //날짜별List만들기
                List<LocalDateTime> dogsellDateList = dogSellListDto.stream().filter(i -> i.getCreatedDate().toLocalDate().isAfter(afterYearMonth))
                        .filter(i -> i.getCreatedDate().toLocalDate().isBefore(plusAfterYearMonth))
                        .map(DogSellListDTO::getCreatedDate).distinct().peek(System.out::println).collect(Collectors.toList());

                //Model객체
                Map<LocalDate, Long> dogsellDateCount = new HashMap<>();



                for (LocalDateTime dogsellDate : dogsellDateList) {

                    LocalDate localDate = dogsellDate.toLocalDate();
                    //해당일자에 등록한 분양글수
                    Long dogsellCount = (Long) dogSellListDto.stream()
                            .filter(r -> r.getCreatedDate().toLocalDate().isEqual(localDate)).count();

                    dogsellDateCount.put(localDate, dogsellCount);
                    //System.out.println("관리자컨트롤러 /admin/home/home"+ dogsellDate+ " 일자의 분양글수 dogsellCount ->  " + dogsellCount);

                }//for종료 분양글 날짜별
                model.addAttribute("dogsellDateCount", dogsellDateCount);
                //dogsellDateCount.forEach((k, v) -> System.out.println("분양글 Map담긴 키 -> " + k + ", 값 -> " + v));

            }//if yearmonth null 아닐경우

        }//if문 분양글List존재할때


        return "admin/admin_home";
    }


    //분양글
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/dogsell/list")
    public String adminDogList(@ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO, Model model,
                               @RequestParam(required = false) String yearmonth,
                               @AuthenticationPrincipal MemberDTO memberDTO,
                               @RequestParam(required = false) String tabtitle,
                               @ModelAttribute("count") Count count) {
        //System.out.println("관리자컨트롤러 /admin/dogsell/list 진입 ");
        if (tabtitle != null) {
            //System.out.println("관리자컨트롤러 /admin/dogsell/list 진입 파라미터 tabtitle -> " + tabtitle);
            model.addAttribute("tabtitle", tabtitle);
        }
        if (yearmonth != null) {
            //System.out.println("관리자컨트롤러 /admin/dogsell/list 진입 파라미터 yearmonth -> "+yearmonth);
            LocalDate localDate = LocalDate.parse(yearmonth);
            model.addAttribute("localDate", yearmonth);

            PageResponseDTO<DogSellListDTO> pResponseDto = dogSellService.getListAdminCreatedDate(pageRequestDTO, localDate);

            if (pResponseDto.getDtoList().size() > 0 && pResponseDto.getEnd() != 0) {
                //System.out.println("관리자컨트롤러 /admin/dogsell/list 진입 " +
                        //" GuestPageResultDTO getSize() -> " + pResponseDto.getPage() + ", getTotalPage() -> " + pResponseDto.getTotal());

                model.addAttribute("pResponseDtoList", pResponseDto.getDtoList());
                pResponseDto.getDtoList().forEach(v -> {
                    System.out.println("관리자컨트롤러에서 complete 출력해봄 -> "+v.getComplete()+", dno 출력 -> "+v.getDno()+" , name 출력 -> "+v.getName());
                });

                model.addAttribute("pResponseDto", pResponseDto);
            }

            if(count.getGuestcount() ==null) {
                count = this.returnCount(localDate);
                model.addAttribute("count", count);
                //System.out.println("관리자컨트롤러 /admin/dogsell/list 진입 Count출력 -> "+ count.toString());
            }

        }//yearmonth존재할경우
        else {
            //System.out.println("관리자컨트롤러 /admin/dogsell/list 진입 쿼리스트링으로 yearmonth 존재하지 않을때 진입");
            return "redirect:/admin/home/home";
        }

        if (memberDTO != null) {
            model.addAttribute("loginMember", memberDTO.getName());
        }
        return "admin/admin_dogsell_list";
    }
    
    //관리자페이지에서 품종검색
/*    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/dogsell/list/search")
    public String adminDogListPost(Model model, @AuthenticationPrincipal MemberDTO memberDTO,
                               @RequestParam(required = false) String yearmonth,
                               @RequestParam(required = false) String tabtitle,
                                   @ModelAttribute("searchBreed") String searchBreed) {
        System.out.println("관리자컨트롤러 /admin/dogsell/list 진입 품종검색 searchBread-> " + searchBreed);
        if (tabtitle != null) {

            System.out.println("tabtitle 존재할때 진입 ->  " + tabtitle);
            model.addAttribute("tabtitle", tabtitle);
        }
        //List<DogSellListDTO> dogSellList = dogSellService.adminDogSellList();
        List<DogSellListDTO> dogSellList = dogSellService.searchBreed(searchBreed);

        List<LocalDateTime> localDateTimeList = dogSellList.stream().map(DogSellListDTO::getModifiedDate)
                .collect(Collectors.toList());


        model.addAttribute("dogSellList", dogSellList);
        model.addAttribute("localDateTimeList", localDateTimeList);


        model.addAttribute("loginMember", memberDTO.getName());
        return "admin/admin_dog_list";
        //return "redirect:/admin/dogsell/list";
    }*/


    //방명록글 특정날짜만 쿼리만들자.
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/guestbook/list")
    public String adminGuestbookList(@ModelAttribute("pageRequestDTO") GuestPageRequestDTO pageRequestDTO, Model model,
                                     @RequestParam(required = false) String yearmonth,
                                     @AuthenticationPrincipal MemberDTO memberDTO,
                                     @RequestParam(required = false) String tabtitle,
                                     @ModelAttribute("count") Count count) {
        //System.out.println("관리자컨트롤러 /admin/guestbook/list 진입 String타입 날짜 -> " + yearmonth);
        if (tabtitle != null) {

            //System.out.println("tabtitle 존재할때 진입 ->  " + tabtitle);
            model.addAttribute("tabtitle", tabtitle);
        }
        if (yearmonth != null) {
            LocalDate localDate = LocalDate.parse(yearmonth);
            model.addAttribute("localDate", yearmonth);

            //GuestPageResultDTO pResponseDto = gs.getListAdminModifiedDate(pageRequestDTO, localDate);
            GuestPageResultDTO pResponseDto = gs.getListAdminCreatedDate(pageRequestDTO, localDate);

            if (pResponseDto.getDtoList().size() > 0 && pResponseDto.getEnd() != 0) {
                //System.out.println("관리자컨트롤러 /admin/guestbook/list 진입 " +
                        //" GuestPageResultDTO getSize() -> " + pResponseDto.getPage() + ", getTotalPage() -> " + pResponseDto.getTotalPage());

                model.addAttribute("pResponseDtoList", pResponseDto.getDtoList());
                model.addAttribute("pResponseDto", pResponseDto);
            }

            if(count.getGuestcount() ==null) {
                count = this.returnCount(localDate);
                model.addAttribute("count", count);
                //System.out.println("관리자컨트롤러 /admin/guestbook/list 진입 Count출력 -> "+ count.toString());
            }

        }//yearmonth존재할경우
        else {
            //System.out.println("관리자컨트롤러 /admin/guestbook/list 진입 쿼리스트링으로 yearmonth 존재하지 않을때 진입");
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
        //System.out.println("관리자컨트롤러 /admin/guestbookreply/list 진입 String타입 날짜 -> " + yearmonth);
        if (tabtitle != null) {

            //System.out.println("tabtitle 존재할때 진입 ->  " + tabtitle);
            model.addAttribute("tabtitle", tabtitle);
        }

        if (yearmonth != null) {
            LocalDate localDate = LocalDate.parse(yearmonth);
            model.addAttribute("localDate", yearmonth);

            GuestPageResultDTO pResponseDto = grs.getReplyListAdmin(pageRequestDTO, localDate);

            if (pResponseDto.getDtoList().size() > 0 && pResponseDto.getEnd() != 0) {
                //System.out.println("관리자컨트롤러 /admin/guestbookreply/list 진입 " +
                      //  " GuestPageResultDTO getSize() -> " + pResponseDto.getSize() + ", getTotalPage() -> " + pResponseDto.getTotalPage());

                model.addAttribute("pResponseDtoList", pResponseDto.getDtoList());
                model.addAttribute("pResponseDto", pResponseDto);
            }

            if(count.getGuestreplycount() ==null) {
                count = this.returnCount(localDate);
                model.addAttribute("count", count);
                //System.out.println("관리자컨트롤러 /admin/guestbookreply/list 진입 Count출력 -> "+ count.toString());

            }

        }//yearmonth존재할경우
        else {
            //System.out.println("관리자컨트롤러 /admin/guestbookreply/list 진입 쿼리스트링으로 yearmonth 존재하지 않을때 진입");
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
        //System.out.println("관리자컨트롤러 /admin/post/list 진입");
        if (tabtitle != null) {

            //System.out.println("관리자컨트롤러 /admin/post/list 진입 tabtitle 존재할때 진입 ->  " + tabtitle);
            model.addAttribute("tabtitle", tabtitle);
        }
        if (yearmonth != null) {
            LocalDate localDate = LocalDate.parse(yearmonth);
            //System.out.println("관리자컨트롤러 /admin/post/list 진입 yearmonth -> "+ yearmonth);
            //System.out.println("관리자컨트롤러 /admin/post/list 진입 LocalDate로 변경 -> "+ localDate);
            
            model.addAttribute("localDate", yearmonth);

            //PageResponseDTO pResponseDto = ps.getListAdminModifiedDate(pageRequestDTO, localDate);
            PageResponseDTO pResponseDto = ps.getListAdminCreatedDate(pageRequestDTO, localDate);

            if (pResponseDto.getDtoList().size() > 0 && pResponseDto.getEnd() != 0) {
                //System.out.println("관리자컨트롤러 /admin/post/list 진입 " +
                        //" PageResponseDTO 게시글 존재하는 경우 진입 getSize() -> " + pResponseDto.getSize() + ", getTotal() -> " + pResponseDto.getTotal());

                model.addAttribute("pResponseDtoList", pResponseDto.getDtoList());
                model.addAttribute("pResponseDto", pResponseDto);
            }

            if(count.getPostcount() ==null) {
                //System.out.println("관리자컨트롤러 /admin/post/list 진입 게시글 갯수가 null 인경우 진입 ");
                count = this.returnCount(localDate);
                model.addAttribute("count", count);
                //System.out.println("관리자컨트롤러 /admin/post/list 진입 Count출력 -> "+ count.toString());
            }

        }//yearmonth존재할경우
        else {
            //System.out.println("관리자컨트롤러 /admin/post/list 진입 쿼리스트링으로 yearmonth 존재하지 않을때 진입");
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
        //System.out.println("관리자컨트롤러 /admin/postreply/list 진입");
        if (tabtitle != null) {

            //System.out.println("tabtitle 존재할때 진입 ->  " + tabtitle);
            model.addAttribute("tabtitle", tabtitle);
        }

        if (yearmonth != null) {
            LocalDate localDate = LocalDate.parse(yearmonth);
            model.addAttribute("localDate", yearmonth);

            //PageResponseDTO pResponseDto = prs.getListAdminModifiedDate(pageRequestDTO, localDate);

            PageResponseDTO pResponseDto = prs.getListAdminCreatedDate(pageRequestDTO, localDate);

            if (pResponseDto.getDtoList().size() > 0 && pResponseDto.getEnd() != 0) {
                //System.out.println("관리자컨트롤러 /admin/postreply/list 진입 " +
                        //" PageResponseDTO getSize() -> " + pResponseDto.getSize() + ", getTotal() -> " + pResponseDto.getTotal());

                model.addAttribute("pResponseDtoList", pResponseDto.getDtoList());
                model.addAttribute("pResponseDto", pResponseDto);
            }


            if(count.getPostreplycount() ==null) {
                count = this.returnCount(localDate);
                model.addAttribute("count", count);
                //System.out.println("관리자컨트롤러 /admin/postreply/list 진입 Count출력 -> "+ count.toString());

            }

        }//yearmonth존재할경우
        else {
            //System.out.println("관리자컨트롤러 /admin/postreply/list 진입 쿼리스트링으로 yearmonth 존재하지 않을때 진입");
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
        //System.out.println("관리자컨트롤러 /admin/inquiry/list 진입");

        model.addAttribute("tabtitle", tabtitle);
        model.addAttribute("localDate", yearmonth);

        if (yearmonth != null) {
            LocalDate localDate = LocalDate.parse(yearmonth);
            model.addAttribute("localDate", yearmonth);

            //PageResponseDTO pResponseDto = is.getListAdminModifiedDate(pageRequestDTO, localDate);
            PageResponseDTO pResponseDto = is.getListAdminCreatedDate(pageRequestDTO, localDate);


            if (pResponseDto.getDtoList().size() > 0 && pResponseDto.getEnd() != 0) {
                //System.out.println("관리자컨트롤러 /admin/inquiry/list 진입 " +
                        //" PageResponseDTO getSize() -> " + pResponseDto.getSize());

                model.addAttribute("pResponseDtoList", pResponseDto.getDtoList());
                model.addAttribute("pResponseDto", pResponseDto);
            }


            if(count.getInquirycount() ==null) {
                count = this.returnCount(localDate);
                model.addAttribute("count", count);
                //System.out.println("관리자컨트롤러 /admin/inquiry/list 진입 Count출력 -> "+ count.toString());
            }
        }//yearmonth존재할경우
        else {
            //System.out.println("관리자컨트롤러 /admin/inquiry/list 진입 쿼리스트링으로 yearmonth 존재하지 않을때 진입");
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
        //System.out.println("관리자컨트롤러 /admin/member/list 진입-> " + pageRequestDTO.toString());

        if (tabtitle != null) {
            model.addAttribute("tabtitle", tabtitle);
        }

        if (yearmonth != null) {
            LocalDate localDate = LocalDate.parse(yearmonth);
            model.addAttribute("localDate", yearmonth);

            //PageResponseDTO pResponseDto = ms.getListAdminModifiedDate(pageRequestDTO, localDate);
            PageResponseDTO pResponseDto = ms.getListAdminCreatedDate(pageRequestDTO, localDate);


            if (pResponseDto.getDtoList().size() > 0 && pResponseDto.getEnd() != 0) {
                //System.out.println("관리자컨트롤러 /admin/member/list 진입 " +
                        //" PageResponseDTO getSize() -> " + pResponseDto.getSize());

                model.addAttribute("pResponseDtoList", pResponseDto.getDtoList());
                model.addAttribute("pResponseDto", pResponseDto);
            }

            if(count.getMembercount() ==null) {
                count = this.returnCount(localDate);
                model.addAttribute("count", count);
                //System.out.println("관리자컨트롤러 /admin/member/list 진입 Count출력 -> "+ count.toString());

            }
        }//if존재시
        else {
            //System.out.println("관리자컨트롤러 /admin/member/list 진입 쿼리스트링으로 yearmonth 존재하지 않을때 진입");
            return "redirect:/admin/home/home";
        }

        return "admin/admin_member_list";
    }


    //일자에 맞는 개수반환 현재 회원은 오류로 제외시킴
    public Count returnCount(LocalDate localDate) {
        //System.out.println("관리자컨트롤러 returnCount() 진입 Count 객체반환 파라미터로 받은 날짜 -> "+localDate);

        //반환타입
        Count count = new Count();

        //분양글
        count.setDogsellcount(dogSellService.getCountLocalDate(localDate));

        //문의글
        count.setInquirycount(is.getCountLocalDate(localDate));
        //게시글댓글
        count.setPostreplycount(prs.getCountLocalDate(localDate));
        //게시글
        count.setPostcount(ps.getCountLocalDate(localDate));
        //방명록
        count.setGuestcount(gs.getCountLocalDate(localDate));
        //방명록댓글
        count.setGuestreplycount(grs.getCountLocalDate(localDate));
        //회원
        count.setMembercount(ms.getCountLocalDate(localDate));

        if(count !=null) {
            //System.out.println("관리자컨트롤러 returnCount() 진입 Count -> " + count.toString());
        }
        return count;
    }

}