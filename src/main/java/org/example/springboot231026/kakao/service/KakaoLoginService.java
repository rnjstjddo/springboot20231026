package org.example.springboot231026.kakao.service;

import com.google.gson.Gson;

import org.example.springboot231026.domain.member.Member;
import org.example.springboot231026.domain.member.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class KakaoLoginService {

    @Autowired
    private PasswordEncoder pe;


    @Value("${kakao.default.password}")
    private String kakaoPassword;

    public String getAccessToken(String code){
        //System.out.println("kakao-service클래스 KakaoLoginService getAccessToken() 진입");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        //HttpBody설정
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id","fc9d360917b2b071e45d69e915598dbe");
        //body.add("redirect_uri", "http://localhost:8080/oauth/kakao");
        //이전aws계정시 body.add("redirect_uri", "http://ec2-3-39-29-255.ap-northeast-2.compute.amazonaws.com/oauth/kakao");
        //body.add("redirect_uri", "http://ec2-3-39-29-255.ap-northeast-2.compute.amazonaws.com:8080/oauth/kakao");
        body.add("redirect_uri", "http://merrydog2024.store/oauth/kakao");

        body.add("code", code);


        //HttpEntity객체생성
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, httpHeaders);

        RestTemplate rest = new RestTemplate();

        ResponseEntity<String> response = rest.exchange("https://kauth.kakao.com/oauth/token",
                HttpMethod.POST, entity, String.class);

        //응답본문정보반환
        String jsonData= response.getBody();

        Gson g = new Gson();
        Map<?,?> map = g.fromJson(jsonData, Map.class);

        String ac= (String) map.get("access_token");

        return ac;

    }

    public Member getUserInfo(String accessToken) {
        //System.out.println("kakao-service클래스 KakaoLoginService getUserInfo() 진입 파라미터 액세스토큰 -> "+accessToken);
        //HttpHeader생성
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer "+ accessToken);
        httpHeaders.add("Content-Type","application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(httpHeaders);

        RestTemplate rest = new RestTemplate();

        ResponseEntity<String> response = rest.exchange("https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST, entity, String.class);

        String userInfo = response.getBody();

        Gson gson =new Gson();
        Map<?,?> map = gson.fromJson(userInfo, Map.class);
        //System.out.println("kakao-service클래스 KakaoLoginService getUserInfo() 진입 - gson() 에서 Map변환 -> "+ map);
//        {id=3.110723466E9,
//        connected_at=2023-10-22T06:12:39Z,
//        properties={nickname=권성애},
//        kakao_account={profile_nickname_needs_agreement=false,
//
//              profile={nickname=권성애},
//              has_email=true,
//              email_needs_agreement=false,
//              is_email_valid=true,
//              is_email_verified=true,
//              email=sungaekwon88@gmail.com}
//              }

        Double id = (Double) (map.get("id"));
        String nickname = (String)((Map<?,?>) (map.get("properties"))).get("nickname");
        String email = (String)((Map<?,?>) (map.get("kakao_account"))).get("email");

        Member m =  Member.builder()
                .name(nickname)
                .password(pe.encode(kakaoPassword))
                .email(email)
                .role(RoleType.USER)
                .fromSocial("true")
                .build();
        //System.out.println("kakao-service클래스 KakaoLoginService getUserInfo() 진입 - 사용자정보로 생성한 엔티티 Member-> "+ m.toString());
// Member(email=sungaekwon88@gmail.com, name=권성애, password=kakao123, fromSocial=true, role=USER)
        return m;
    }

}
