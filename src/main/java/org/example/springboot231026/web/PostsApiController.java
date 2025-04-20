package org.example.springboot231026.web;


import lombok.RequiredArgsConstructor;
import org.example.springboot231026.dto.member.MemberDTO;
import org.example.springboot231026.dto.post.PostReplyDTO;
import org.example.springboot231026.dto.post.PostsSaveRequestDto;
import org.example.springboot231026.dto.post.PostsUpdateRequestDto;
import org.example.springboot231026.service.posts.PostReplyService;
import org.example.springboot231026.service.posts.PostsService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostsApiController {


    private final PostsService ps;

    private final PostReplyService prs;


    //게시글저장
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/api/v1/posts")
    public Long save(@RequestBody PostsSaveRequestDto dto) {
        //System.out.println("REST컨트롤러 PostsApiController save() 진입");

        return ps.save(dto);
    }


    //게시글삭제
    @PreAuthorize("#memberDTO != null && #memberDTO.name == #dto.author")
    @DeleteMapping("/api/v1/posts/{id}")
    public Long delete(@PathVariable("id") Long id, @AuthenticationPrincipal MemberDTO memberDTO,
                       @RequestBody PostsUpdateRequestDto dto) {
        //System.out.println("REST컨트롤러 PostsApiController delete() 진입 파라미터 게시글번호 -> "+ id);

        ps.delete(id);
        return id;
    }

    //게시글수정
    @PutMapping("/api/v1/posts/{id}")
    @PreAuthorize("#memberDTO != null && #memberDTO.name == #dto.author")
    public Long update(@PathVariable("id") Long id, @RequestBody PostsUpdateRequestDto dto
                 , @AuthenticationPrincipal MemberDTO memberDTO) {
        //System.out.println("REST컨트롤러 PostsApiController update() 진입 파라미터 -> "+dto.toString());
        return ps.update(id, dto);
    }


    //게시글 댓글저장
    @PostMapping("/post/reply/save")
    @PreAuthorize("isAuthenticated()")
    //@PreAuthorize("#memberDTO != null")
    public PostReplyDTO replysave(@RequestBody PostReplyDTO rDto, @AuthenticationPrincipal MemberDTO memberDTO){
        //System.out.println("REST컨트롤러 PostsApiController replysave() 진입 파라미터 -> "+rDto.toString());
        rDto.setMDto(memberDTO);
        return prs.save(rDto);
    }

    //게시글 댓글삭제
    @DeleteMapping("/post/reply/delete")
    @PreAuthorize("isAuthenticated()")
    public String replydelete(Long prno, @AuthenticationPrincipal MemberDTO memberDTO){
        //System.out.println("REST컨트롤러 PostsApiController replydelete() 진입 파라미터 -> "+prno);
        prs.postReplyDelete(prno);
        return "success";
    }


}