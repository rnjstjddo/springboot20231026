package org.example.springboot231026.google.service;

import org.example.springboot231026.domain.member.Member;
import org.example.springboot231026.domain.member.MemberRepository;
import org.example.springboot231026.domain.member.RoleType;
import org.example.springboot231026.dto.dogsell.cart.DogSellCartDTO;
import org.example.springboot231026.dto.member.MemberDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;


@Service
public class Member0Auth2UserDetailsService extends DefaultOAuth2UserService {

    @Autowired
    private PasswordEncoder pe;

    @Autowired
    private MemberRepository mr;

    @Value("${google.default.password}")
    private String googlePassword;

    //@Autowired
    //private DogSellCartRepository dscr;

    @Autowired
    private ModelMapper mm;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User ou = super.loadUser(userRequest);
        //System.out.println("google-service클래스 MemberOAuth2UserDetailsService 오버라이딩 loadUser() 진입 파라미터 OAuth2UserRequest ");

        //System.out.println("google-service클래스 MemberOAuth2UserDetailsService 오버라이딩 loadUser() 진입 -가져온 사용자정보 OAuth2User -> "+ ou);
// Name: [114017297734871077662],
// Granted Authorities: [ROLE_USER],
// User Attributes: [
//      sub=114017297734871077662,
//      name=권성애,
//      given_name=성애,
//      family_name=권,
//      picture=https://lh3.googleusercontent.com/a/ACg8ocIMFI7TTPU3Y9_cmEjBZmtZVrhordqBqxmH-Ttu3f6Z=s96-c,
//      email=kwonsungae88@gmail.com,
//      email_verified=true,
//      locale=ko]

        //책에는 없다. 개정판 부트 p545
        String clientId = userRequest.getClientRegistration().getClientName();
        //System.out.println("google-service클래스 MemberOAuth2UserDetailsService 오버라이딩 loadUser() 진입" +
                //" -가져온 사용자정보 소셜로그인 인증서버이름 -> "+ clientId);

        //System.out.println(userRequest.getAdditionalParameters());

        String email =null;


        if(clientId.equals("Google")){
            email = (String) ou.getAttributes().get("email");
         }

        if(clientId.equals("Naver")){
            Map<String,Object> navermap = (Map<String, Object>) ou.getAttributes();
            Map<String,Object> map  = (Map<String,Object>) navermap.get("response");
            email = (String) map.get("email");

            navermap.forEach( (k,v) ->{
                //System.out.println("네이버로그인 키명 -> "+k+",  값-> "+v);
            });
        }
        //System.out.println("google-service클래스 MemberOAuth2UserDetailsService 오버라이딩 loadUser() 진입 -가져온 사용자정보 소셜로그인 회원의 email -> "+ email);


        //회원가입된 사용자인지 확인하자.
        //Member entity = mr.findByUsername(email, true).orElseGet(() -> {return new Member();});

        //Optional<Member> o = mr.findByUsername(email, true);
        Member o = mr.findByUsername(email, "true")
                .orElseGet(() -> {return new Member();});

        //System.out.println("google-service클래스 MemberOAuth2UserDetailsService 오버라이딩 loadUser() 진입 -가져온 사용자정보 소셜로그인 회원 DB확인 엔티티 Member -> "+ o.get());

        //반환객체 미리생성
        MemberDTO dto = null;

        if(o.getEmail() == null){

            //System.out.println("google-service클래스 MemberOAuth2UserDetailsService 오버라이딩 loadUser() 진입 -가져온 사용자정보 소셜로그인 회원 DB확인 email, fromSocial ==true 아닌경우 엔티티생성후 DB저장");

            Member entity = Member.builder()
                    .name(email)
                    .fromSocial("true")
                    .role(RoleType.USER)
                    .email(email)
                    .password(pe.encode(googlePassword)) //소셜로그인사용자는 패스워드고정
                    //.wishNumList(null)
                    .build();
            mr.save(entity);

            Optional<Member> omember = mr.findById(email);


//MemberDTO ->  String name, password, email; /RoleType role;/boolean fromSocial;
// /LocalDateTime createdDate, modifiedDate; private Map<String, Object> attrs;
// List<WishNum> wishNum 9개

            dto = new MemberDTO(
                    omember.get().getName(), omember.get().getPassword(), MemberDTO.setAuthorities("USER"), omember.get().getFromSocial(),
                    ou.getAttributes() ,omember.get().getName(), omember.get().getEmail()
            );
            dto.setRole(RoleType.USER);
            dto.setCreatedDate(omember.get().getCreatedDate());
            dto.setModifiedDate(omember.get().getModifiedDate());
            //dto.setWishNum(null);
            //System.out.println("google-service클래스 MemberOAuth2UserDetailsService 오버라이딩 loadUser() 진입 -가져온 사용자정보 소셜로그인 회원 DB확인 email, fromSocial ==true 아닌경우 엔티티생성후 DB저장 -생성한 반환타입 MemberDTO -> "+this.toString());

            return dto;
        }

        if(o.getEmail() !=null) {
            //System.out.println("google-service클래스 MemberOAuth2UserDetailsService 오버라이딩 loadUser() 진입 - 존재하는 email 있다면 화면에 보여줄 DTO바로 생성");
            //존재하는 email 있다면 화면에 보여줄 DTO바로 생성
           /* dto = new MemberDTO(
                    o.get().getName(), o.get().getPassword(), MemberDTO.setAuthorities("USER"), o.get().isFromSocial(),
                    ou.getAttributes(), o.get().getName(), o.get().getEmail()
            );*/
            dto = new MemberDTO(
                    o.getName(), o.getPassword(), MemberDTO.setAuthorities("USER"), o.getFromSocial(),
                    ou.getAttributes(), o.getName(), o.getEmail()
            );


            dto.setRole(RoleType.USER);
            /*dto.setCreatedDate(o.get().getCreatedDate());
            dto.setModifiedDate(o.get().getModifiedDate());
            */
            dto.setCreatedDate(o.getCreatedDate());
            dto.setModifiedDate(o.getModifiedDate());

            //dto.setWishNum(null);

            //Optional<DogSellCart> dogSellCart = dscr.findByMember(o.get());
            //DogSellCartDTO dogSellCartDTO = mm.map(dogSellCart.get(), DogSellCartDTO.class);
            //dto.setDogSellCartDTO(dogSellCartDTO);


            //System.out.println("google-service클래스 MemberOAuth2UserDetailsService 오버라이딩 loadUser() 진입 - 반환타입 자식 MemberDTO -> " + dto.toString());
// MemberDTO(name=kwonsungae88@gmail.com, password=$2a$10$rUbJjUffrBXk1ckWMZECtu3k7bjUPD1mxdOws43zW5NN8Oq0qpAtW, email=kwonsungae88@gmail.com, role=USER, fromSocial=true, createdDate=2023-11-05T19:15:16, modifiedDate=2023-11-05T19:15:16, attrs={sub=114017297734871077662, name=권성애, given_name=성애, family_name=권, picture=https://lh3.googleusercontent.com/a/ACg8ocIMFI7TTPU3Y9_cmEjBZmtZVrhordqBqxmH-Ttu3f6Z=s96-c, email=kwonsungae88@gmail.com, email_verified=true, locale=ko})
            return dto;
        }

        return dto;
    }
}
