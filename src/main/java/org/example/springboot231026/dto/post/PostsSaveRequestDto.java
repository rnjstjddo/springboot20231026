package org.example.springboot231026.dto.post;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.springboot231026.domain.posts.Posts;

@Getter
@ToString
@NoArgsConstructor
public class PostsSaveRequestDto {

    private String title, content, author;

    @Builder
    public PostsSaveRequestDto(String title, String content, String author){
        //System.out.println("dto클래스 PostsSaveRequestDto 생성자진입");

        this.title=title;
        this.content=content;
        this.author=author;
        //System.out.println("dto클래스 PostsSaveRequestDto 생성자진입 -> "+ this.toString());
    }

    public Posts toEntity(){
        //System.out.println("dto클래스 PostsSaveRequestDto toEntity() 진입");

        return Posts.builder()
                .title(this.title)
                .autor(this.author)
                .content(this.content)
                .build();
    }
    //System.out.println("dto클래스 PostsSaveRequestDto 생성자진입");

}
