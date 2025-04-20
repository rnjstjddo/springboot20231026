package org.example.springboot231026.dto.post;


import lombok.*;
import org.example.springboot231026.domain.posts.Posts;
import org.example.springboot231026.dto.member.MemberDTO;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostReplyDTO {
    //PostReplyDTO ->Long prno; pno; /MemberDTO mDto;/ String comment;/ LocalDateTime modifiedDate, createdDate;

    //PostReply-> Member member; / Long pno; / String  comment; /@Id Long prno   private String comment;
    private Long prno;
    private Long pno;
    private MemberDTO mDto;
    private String name;
    private String comment;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime modifiedDate, createdDate;

    public PostReplyDTO(Long prno, Long pno, String comment, String name, LocalDateTime modifiedDate, LocalDateTime createdDate){
        //System.out.println("dto-posts클래스 PostReplyDTO 생성자진입");
        this.prno=prno;
        this.pno=pno;
        this.comment=comment;
        this.name=name;
        this.modifiedDate=modifiedDate;
        this.createdDate=createdDate;
    }
}
