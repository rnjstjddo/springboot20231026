package org.example.springboot231026.domain.posts;


import lombok.*;
import org.example.springboot231026.domain.member.Member;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Builder
public class PostReply extends BaseTimeEntity{
//PostReply -> Long prno;/ String comment; / Long pno; / Member member;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long prno;


    private String comment;

    //@ManyToOne(fetch = FetchType.EAGER)
    //private Posts posts;

    private Long pno; // 해당 필드와 Posts의 id 일치하는것

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    private Member member;//Member

    //댓글
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    private Posts posts;

}
