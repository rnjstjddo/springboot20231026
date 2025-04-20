package org.example.springboot231026.domain.member;


import lombok.*;
import org.example.springboot231026.domain.posts.BaseTimeEntity;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@ToString
@Table(name="members")
//@DynamicInsert
public class Member extends BaseTimeEntity {
// Member->  String email, name, password; /boolean fromSocial; /RoleType role;
    @Id
    @Column(length = 100, nullable = false)
    @NotNull(message = "이메일 입력란이 비었습니다.")
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private String email;

    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    //private String mno;

    @Column(nullable = false, length=50, unique = true)
    @NotNull(message = "이름 입력란이 비었습니다.")
    private String name;

    @NotNull(message = "비밀번호 입력란이 비었습니다.")
    @Column(length = 100,nullable = false)
    private String password;

    //@Column( nullable = false)
    //private boolean fromSocial;
    //@ColumnDefault("'FALSE'")
    @Column(nullable = false)
    @Builder.Default
    private String fromSocial = "false";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleType role;

    public RoleType changeRole(RoleType role){
        return role;
    }
    

    //회원정보수정시
    public Member updateMember(String name, String password){
        //System.out.println("domain-member클래스 Member updateMember() 진입");
        this.name=name;
        this.password=password;//암호된 값으로 넣음

        //System.out.println("domain-member클래스 Member updateMember() 진입 -> "+ this.toString());
        return this;
    }

    public Member updateSocialMember(String name){
        //System.out.println("domain-member클래스 Member updateSocialMember() 진입");
        this.name=name;

        //System.out.println("domain-member클래스 Member updateSocialMember() 진입 -> "+ this.toString());
        return this;
    }
}
