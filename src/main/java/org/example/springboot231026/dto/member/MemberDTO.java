package org.example.springboot231026.dto.member;

import lombok.*;
import org.example.springboot231026.domain.dog.WishNum;
import org.example.springboot231026.domain.member.Member;
import org.example.springboot231026.domain.member.MemberRepository;
import org.example.springboot231026.domain.member.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@ToString
//@Builder
public class MemberDTO extends User implements OAuth2User {
//MemberDTO ->  String name, password, email; /RoleType role;/boolean fromSocial;
// /LocalDateTime createdDate, modifiedDate; private Map<String, Object> attrs;
// List<WishNum> wishNum 9개

    @Autowired
    private MemberRepository mr;

    private String name, password, email,fromSocial;
    private RoleType role;
    //private boolean fromSocial;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdDate, modifiedDate;
    
    //소셜로그인 멤버필드추가
    private Map<String, Object> attrs;

    public MemberDTO(String username,String password, String fromSocial,
                     Collection<? extends GrantedAuthority> authorities,
                     String name, String email, RoleType role,
                     LocalDateTime createdDate, LocalDateTime modifiedDate,
                     Map<String, Object> attrs, List<WishNum> wishNum){
        this(username,password, fromSocial, authorities);
        //System.out.println("dto-member클래스 MemberDTO 생성자진입 -User 상속 ");

        this.name=name;
        //this.password=password;
        this.email=email;
        this.role=role;
        //this.fromSocial =fromSocial;
        this.createdDate=createdDate;
        this.modifiedDate=modifiedDate;
        this.attrs=attrs;
    }

    public MemberDTO(String username, String password, Collection<? extends GrantedAuthority> authorities, String fromSocial,
                     Map<String, Object> attrs, String name, String email) { //7개
        //super(username, password, authorities);
        this(username, password, fromSocial, authorities);
        //System.out.println("dto-member클래스 MemberDTO 생성자진입 ajax용 회원가입시 컨트롤러파라미터 - OAuth2User 상속 ");
        this.name = name;
        this.email=email;
        this.attrs=attrs;
        //System.out.println("dto-member클래스 MemberDTO 생성자진입 ajax용 회원가입시 컨트롤러파라미터 - OAuth2User 상속 ->"+this.toString());

    }

    @Override
    public  Map<String, Object> getAttributes() {
        //System.out.println("dto-member클래스 MemberDTO 오버라이딩 getAttributes() 진입 반환타입은 Map<String, Object> ");

        return this.attrs;
    }

    //카카오
    public MemberDTO(String username, String password, Collection<? extends GrantedAuthority> authorities,
                     String name, String email) {//5개
        super(username, password, authorities);

        //System.out.println("dto-member클래스 MemberDTO 생성자진입 ajax용 회원가입시 컨트롤러파라미터 - User상속");
        this.name=name;
        this.password =password;
        this.email=email;

        //System.out.println("dto-member클래스 MemberDTO 생성자진입  ajax용 회원가입시 컨트롤러파라미터 - User상속 -> "+this.toString());

    }

    public MemberDTO(String username, String password, String fromSocial,
                     Collection<? extends GrantedAuthority> authorities) {//4개
        super(username, password, authorities);
        //System.out.println("dto-member클래스 MemberDTO 생성자진입 - User상속");
        this.email=username;
        this.password=password;
        this.fromSocial=fromSocial;

        //System.out.println("dto-member클래스 MemberDTO 생성자진입 - User상속 - >"+ this.toString());
    }



    public static Collection<? extends GrantedAuthority> setAuthorities(String role){
        //System.out.println("dto-member클래스 MemberDTO setAuthorities 진입 파라미터 -> "+ role);
        Collection<GrantedAuthority> roleList = new ArrayList<>();
        roleList.add( () -> {
            return "ROLE_"+role;
        });
        //System.out.println("dto-member클래스 MemberDTO setAuthorities 진입 Collection<GrantedAuthority> 출력 -> "+ roleList);
        return roleList;
    }


    public MemberDTO setMember(Member entity){
        //System.out.println("dto-member클래스 MemberDTO setMember() 진입 회원정보 수정후 세션갱신");
        //this.name=entity.getName(); 닉네임은 일반회원은 바꾸지 않음
        this.password=entity.getPassword();
        return this;
    }

    //관리자페이지에서 사용
    public MemberDTO(String username,String password,
                     Collection<? extends GrantedAuthority> authorities, String fromSocial,
                     String name, String email, RoleType role,
                     LocalDateTime createdDate, LocalDateTime modifiedDate){
        super(username, password, authorities);
        //System.out.println("dto-member클래스 MemberDTO 생성자진입");
        this.password=password;
        this.fromSocial =fromSocial;
        this.name=name;
        this.email=email;
        this.role=role;
        this.createdDate=createdDate;
        this.modifiedDate=modifiedDate;
    }
}
