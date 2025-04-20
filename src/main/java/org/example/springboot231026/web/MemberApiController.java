package org.example.springboot231026.web;

import org.example.springboot231026.domain.member.Member;
import org.example.springboot231026.domain.member.MemberRepository;
import org.example.springboot231026.domain.member.RoleType;
import org.example.springboot231026.dto.member.JoinDTO;
import org.example.springboot231026.dto.member.MemberDTO;
import org.example.springboot231026.dto.page.PageRequestDTO;
import org.example.springboot231026.service.member.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
public class MemberApiController {

    @Autowired
    private MemberService ms;
    @Autowired
    private MemberRepository mrepo;


    @PostMapping("/member/join")
    public String joinMember(@RequestBody JoinDTO dto){
        //System.out.println("컨트롤러클래스 MemberApiController joinMember() 진입 파라미터 JoinDTO -> "+ dto.toString());
        String name =ms.memberJoin(dto);

        return name;
    }

    @PostMapping("/member/join/check")
    public String joinMemberCheck(@RequestBody JoinDTO dto){
        //System.out.println("컨트롤러클래스 MemberApiController joinMemberCheck() 진입 파라미터 JoinDTO.getName() 아이디중복확인 -> "+ dto.getName());
        String result = ms.checkName(dto);

        return result;
    }

    @PutMapping("/member/updateJoin")
    public String updateJoin(@RequestBody JoinDTO joinDTO, @AuthenticationPrincipal MemberDTO memberDTO){
        //System.out.println("컨트롤러클래스 MemberApiController updateJoin() 진입 파라미터 JoinDTO -> "+ joinDTO.toString());
        Member entity = ms.updateJoin(joinDTO, memberDTO);

        MemberDTO updateMemberDTO =memberDTO.setMember(entity); //세션갱신
        //System.out.println("컨트롤러클래스 MemberApiController updateJoin() 진입 파라미터 Member 엔티티수정 트랜잭션후 출력 -> "+ entity.getPassword());
        //System.out.println("컨트롤러클래스 MemberApiController updateJoin() 진입 파라미터 MemberDTO 세션 비밀번호수정 전 -> "+ memberDTO.getPassword());
        //System.out.println("컨트롤러클래스 MemberApiController updateJoin() 진입 파라미터 MemberDTO 세션 비밀번호수정 후 -> "+ updateMemberDTO.getPassword());

        return entity.getName();
    }

    @PutMapping("/member/updateSocialJoin")
    public String updateSocialJoin(@RequestBody JoinDTO joinDTO, @AuthenticationPrincipal MemberDTO memberDTO){

        //System.out.println("컨트롤러클래스 MemberApiController updateSocialJoin() 진입 파라미터 JoinDTO -> "+ joinDTO.getName());//joinDTO.getName(
        String originName= memberDTO.getName();

        Member checkName = ms.nameCheck(joinDTO.getName());

        if(checkName.getName() !=null){
            //System.out.println("컨트롤러클래스 MemberApiController updateSocialJoin() 진입 소셜로그인시 닉네임중복확인시 동일한 닉네임 존재할때");

            return "exist";
        }
        //DB는 변경
        Member entity = ms.updateSocialJoin(joinDTO.getName(), memberDTO); //트랜잭션적용

        //memberDTO.setMember(joinDTO); //세션갱신자동
        //System.out.println("컨트롤러클래스 MemberApiController updateSocialJoin() 진입 파라미터 서비스클래스에서 트랜잭션 적용후 "+
                        //"Member의 name 변경확인  -> "+ entity.getName()+" 기존 name -> "+ originName);
        //세션변경
        memberDTO.setMember(entity);
        //System.out.println("컨트롤러클래스 MemberApiController updateSocialJoin() 진입 MemberDTO변경후 name확인 -> "+ memberDTO.getName());

        return entity.getName();//변경된 이름전달
    }


    @DeleteMapping("/member/delete")
    public void deleteMember(@RequestBody String name) {
        //System.out.println("컨트롤러클래스 MemberApiController updateSocialJoin() 진입 삭제할 회원name -> "+name);
        Member entity = mrepo.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("회원탈퇴시 해당 회원이 존재하지 않습니다."));
        ms.deleteMember(name, entity);
    }


}
