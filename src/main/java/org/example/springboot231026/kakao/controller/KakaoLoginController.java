package org.example.springboot231026.kakao.controller;


import org.example.springboot231026.domain.member.Member;
import org.example.springboot231026.kakao.service.KakaoLoginService;
import org.example.springboot231026.service.member.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class KakaoLoginController {

    @Autowired
    private KakaoLoginService ks;

    //사용자인증정보 얻고 신규회원등록후 인증처리
    @Autowired
    private MemberService ms;

    @Autowired
    private AuthenticationManager am;


    @Value("${kakao.default.password}")
    private String kakaoPassword;


    @GetMapping("/oauth/kakao")
    public String kakaoCallback(String code){
        //System.out.println("컨트롤러 KakaoLoginController kakaoCallback() 진입");

        String at = ks.getAccessToken(code);
        //System.out.println("컨트롤러 KakaoLoginController kakaoCallback() 진입 - 카카오 엑세스토큰 얻기 -> "+ at);

        //엑세스토큰 이용해서 사용자정보 얻어온다. 이때 Member엔티티로 만들어반환한다.
        Member m = ks.getUserInfo(at);

        //System.out.println("컨트롤러 KakaoLoginController kakaoCallback() 진입 - 받은 사용자정보로 Member엔티티생성 -> "+ m.toString());

        Member entity = ms.getUser(m.getEmail());
        //System.out.println("컨트롤러 KakaoLoginController kakaoCallback() 진입 - DB에 사용자 존재하는지 확인 후 엔티티 반환 -> "+ entity.toString());

        if(entity.getEmail() == null){
            //System.out.println("컨트롤러 KakaoLoginController kakaoCallback() 진입 - DB에 존재하지 않는 사용자일경우 DB데이터 넣는다.");
            String getName = ms.insertUser(m);
            //System.out.println("컨트롤러 KakaoLoginController kakaoCallback() 진입 - DB에 존재하지 않는 사용자일경우 DB데이터 넣는다. - 결과로 getName() 얻기 -> "+ getName);

        }

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(m.getEmail(), kakaoPassword);

        //System.out.println("컨트롤러 KakaoLoginController kakaoCallback() 진입 - UsernamePasswordAuthenticationToken 생성 -> "+token);

        Authentication auth = am.authenticate(token);

        //System.out.println("컨트롤러 KakaoLoginController kakaoCallback() 진입 - Authentication 생성 -> "+auth);
        SecurityContextHolder.getContext().setAuthentication(auth);

        return "redirect:/member/updateSocialJoin";
        //return "redirect:/home/home";
    }


}
