package org.example.springboot231026.dto.post;


import lombok.*;
import org.example.springboot231026.domain.posts.Posts;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PostsListResponseDto {

    private Long id;
    private String title, author, content;
    private LocalDateTime createdDate, modifiedDate;

    public PostsListResponseDto(Posts entity){
        //System.out.println("dto클래스 PostsListsResponseDto 생성자진입");
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.author=entity.getAuthor();
        this.content=entity.getContent();
        this.createdDate=entity.getCreatedDate();
        this.modifiedDate=entity.getModifiedDate();
        //System.out.println("dto클래스 PostsListsResponseDto 생성자진입 -> "+ this.toString());

    }
}
