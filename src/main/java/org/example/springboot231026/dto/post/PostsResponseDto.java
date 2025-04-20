package org.example.springboot231026.dto.post;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.springboot231026.domain.posts.Posts;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PostsResponseDto {

    private Long id;
    private String title,content, author;

    private LocalDateTime createdDate, modifiedDate;

    public PostsResponseDto(Posts entity){
        //System.out.println("dto클래스 PostsResponseDto 생성자진입 ");
        this.id = entity.getId();
        this.title=entity.getTitle();
        this.content=entity.getContent();
        this.author=entity.getAuthor();
        this.modifiedDate=entity.getModifiedDate();
        this.createdDate=entity.getCreatedDate();
        //System.out.println("dto클래스 PostsResponseDto 생성자진입 ->"+ this.toString());
    }

    //System.out.println("dto클래스 PostsResponseDto 생성자진입 ");
}
