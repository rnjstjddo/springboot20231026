package org.example.springboot231026.web;


import lombok.RequiredArgsConstructor;
import org.example.springboot231026.domain.posts.PostReplyRepository;
import org.example.springboot231026.domain.posts.Posts;
import org.example.springboot231026.domain.posts.PostsRepository;
import org.example.springboot231026.dto.member.MemberDTO;
import org.example.springboot231026.dto.page.PageRequestDTO;
import org.example.springboot231026.dto.page.PageResponseDTO;
import org.example.springboot231026.dto.post.PostReplyDTO;
import org.example.springboot231026.dto.post.PostsListResponseDto;
import org.example.springboot231026.dto.post.PostsResponseDto;
import org.example.springboot231026.service.posts.PostReplyService;
import org.example.springboot231026.service.posts.PostsService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostsController {

    private final PostsService ps;

    private final PostsRepository prepo;

    //private final PostReplyRepository prrepo;

    //댓글보여주기
    private final PostReplyService prs;


    //게시글목록
    @GetMapping("/list")
    public String index(@ModelAttribute("pageRequestDto") PageRequestDTO requestDTO, Model model,
                         @AuthenticationPrincipal MemberDTO memberDTO) {
        //System.out.println("VIEW컨트롤러 index()진입 파라미터 PageRequestDTO -> "+requestDTO);

        if(memberDTO !=null) {
            model.addAttribute("loginMember", memberDTO.getName());
        }

        try {
            //PageResponseDTO<PostsListResponseDto> dto= ps.list(requestDTO);

            PageResponseDTO responseDto= ps.list(requestDTO);
            //System.out.println("VIEW컨트롤러 index()진입 파라미터 PageResponseDTO -> "+responseDto);

            //System.out.println("VIEW컨트롤러 index()진입 파라미터 PageResponseDTO .getDtoList().size() -> "+responseDto.getDtoList().size());
            //System.out.println("VIEW컨트롤러 index()진입 파라미터 PageResponseDTO .getDtoList() -> "+responseDto.getDtoList());

            if(responseDto.getDtoList().size() ==0){
// VIEW컨트롤러 index()진입 파라미터 PageResponseDTO ->
// //PageResponseDTO(page=0, size=0, total=0, start=0, end=0, prev=false, next=false, dtoList=[])
                //System.out.println("VIEW컨트롤러 index()진입 PageResponseDTO 게시글이 없을경우 진입");
                //PageResponseDTO nullDto = new PageResponseDTO();
                //model.addAttribute("responseDto", nullDto);
                PageResponseDTO nullDto = new PageResponseDTO();
                model.addAttribute("responseDto", nullDto);

                if (memberDTO != null) {
                    //System.out.println("VIEW컨트롤러 index()진입 PageResponseDTO 게시글이 없을경우 진입 - 로그인한 회원일경우 getName() 같이전달 ");
                    model.addAttribute("loginMember", memberDTO.getName());
                    return "post/list";
                }
            }else{

                //System.out.println("VIEW컨트롤러 index()진입 PageResponseDTO 게시글이 존재할 경우 진입");
                model.addAttribute("responseDto", responseDto);
                
                //게시글 댓글
                List<Object []> oarraysList = prepo.getReplyCount();

                //List<Object []> oarraysList = prrepo.getPostReplyCount();

                Map<Long, Long> map =new HashMap<>(); //게시글번호와 댓글수 put() 넣는다.

                for(Object [] arrays: oarraysList){
                    map.put((Long) arrays[0], (Long) arrays[1]);
                    //System.out.println("게시글번호 -> "+arrays[0]+", 댓글수 -> "+arrays[1]);
                }
                
                //댓글수 model담기
                model.addAttribute("replyCntMap", map);


                if (memberDTO != null) {
                    //System.out.println("VIEW컨트롤러 index()진입 PageResponseDTO 게시글이 존재할 경우 진입 - 로그인한 회원일경우 getName() 같이전달 ");
                    model.addAttribute("loginMember", memberDTO.getName());
                    return "post/list";
                }
            }
        }catch(Exception e) {
            //System.out.println("VIEW컨트롤러 index()진입 - catch() 진입");
                e.printStackTrace();
            return "post/list";
        }
        return "post/list";
   }

    //게시글저장
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/save")
    public void postsSave(@ModelAttribute("pageRequestDto") PageRequestDTO requestDTO, Model model,
                             @AuthenticationPrincipal MemberDTO memberDTO){
        //System.out.println("VIEW컨트롤러 postsSave()진입");

        try {
            if (memberDTO != null) {
                //System.out.println("VIEW컨트롤러 postsSave()진입 - 로그인한 회원일경우 getName() 같이전달 ");
                model.addAttribute("loginMember", memberDTO.getName());
            }
        }catch (Exception e){
            //System.out.println("VIEW컨트롤러 postsSave()진입 - catch() 진입");
            e.printStackTrace();
        }
    }

    //게시글조회
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/update")
    public void postsUpdate(@ModelAttribute("pageRequestDto") PageRequestDTO requestDTO, Model model,
                              Long id, @AuthenticationPrincipal MemberDTO memberDTO){
        //System.out.println("VIEW컨트롤러 postsUpdate()진입 파라미터번호 -> "+id);

        PostsResponseDto postsResponseDto = ps.findById(id);
        model.addAttribute("posts", postsResponseDto);

        //System.out.println("VIEW컨트롤러 postsUpdate()진입 현재 로그인사용자 -> "+memberDTO.getName()+", 게시글작성자 -> "+postsResponseDto.getAuthor());


        //댓글조회 함께 보여주기
        List<PostReplyDTO> replydtoList = prs.list(id);
        //System.out.println("VIEW컨트롤러 postsUpdate()진입 댓글출력 -> "+ replydtoList.toString());
        model.addAttribute("rDtoList", replydtoList);

        try {
            if (memberDTO != null) {
                //System.out.println("VIEW컨트롤러 postsUpdate()진입 - 로그인한 회원일경우 getName() 같이전달 ");
                model.addAttribute("loginMember", memberDTO.getName());

            }
        }catch (Exception e){
            //System.out.println("VIEW컨트롤러 postsUpdate()진입 - catch() 진입");
            e.printStackTrace();
        }
    }




}
