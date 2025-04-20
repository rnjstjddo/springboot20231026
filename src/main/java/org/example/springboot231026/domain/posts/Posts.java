package org.example.springboot231026.domain.posts;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@ToString
public class Posts extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length=500, nullable = false)
    private String title;

    @Column(length=5000, nullable = false)
    private String content;

    @Column(length=500, nullable = false)
    private String author;

/*
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "posts")
    private List<PostReply> prList = new ArrayList<>();
*/

    @Builder
    public Posts(String title, String content, String autor){
        //System.out.println("domain-posts클래스 Posts 생성자진입");
        this.title=title;
        this.content=content;
        this.author=autor;
        //System.out.println("domain-posts클래스 Posts 생성자진입 -> "+ this.toString());

    }

    //제목과 내용 수정가능
    public void update(String title, String content){
        //System.out.println("domain-posts클래스 Posts update() 진입");

        this.title=title;
        this.content=content;
    }

    //System.out.println("domain-posts클래스 Posts 생성자진입");
    //
}
