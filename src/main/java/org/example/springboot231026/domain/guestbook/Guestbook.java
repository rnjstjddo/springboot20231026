package org.example.springboot231026.domain.guestbook;

import lombok.*;
import org.example.springboot231026.domain.posts.BaseTimeEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@ToString(exclude = "grList")
@ToString
public class Guestbook extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gno;

    @Column(length= 100, nullable = false)
    private String title;

    @Column(length= 2000, nullable = false)
    private String content;

    @Column(length = 100, nullable = false)
    private String writer;


    //@OneToMany(fetch = FetchType.LAZY)
    //@Builder.Default
    //private List<GuestbookReply> grList =new ArrayList<>();

    //수정
    public Guestbook changeGuestbook(String title, String content){
        //System.out.println("guestbook-domain클래스 Guestbook엔티티 changeGuestbook() 진입");
        this.title=title;
        this.content=content;
        return this;
    }

}
