package org.example.springboot231026.dto.post;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class PostsUpdateRequestDto {

    private String title, content, author;

    @Builder
    public PostsUpdateRequestDto(String title, String content, String author){
        //System.out.println("dto클래스 PostsUpdateRequestDto 생성자진입 ");
        this.title=title;
        this.content=content;
        this.author=author;
        //System.out.println("dto클래스 PostsUpdateRequestDto 생성자진입 -> "+ this.toString());

    }

}
