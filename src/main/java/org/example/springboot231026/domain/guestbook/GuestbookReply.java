package org.example.springboot231026.domain.guestbook;

import lombok.*;
import org.example.springboot231026.domain.member.Member;
import org.example.springboot231026.domain.posts.BaseTimeEntity;

import javax.persistence.*;


@Entity
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GuestbookReply extends BaseTimeEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long grno;

    private String comment;

    private Long gno; // 해당 필드와 Guestbook의 gno 일치하는것

    @ManyToOne(fetch = FetchType.EAGER)
    private Member member;//db에서는 member_id 자동 컬럼생성

}
