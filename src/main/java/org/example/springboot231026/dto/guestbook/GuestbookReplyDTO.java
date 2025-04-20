package org.example.springboot231026.dto.guestbook;

import lombok.*;
import org.example.springboot231026.dto.member.MemberDTO;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GuestbookReplyDTO {
//GuestbookReplyDTO -> Long grno; /Long gno; / MemberDTO mDto; /String name; /String comment;/LocalDateTime modifiedDate, createdDate;

    private Long grno;
    private Long gno;
    private MemberDTO mDto;
    private String name;
    private String comment;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime modifiedDate, createdDate;

    public GuestbookReplyDTO(Long grno, Long gno, String comment, String name, LocalDateTime modifiedDate, LocalDateTime createdDate){
        //System.out.println("dto-posts클래스 GuestbookReplyDTO 생성자진입");
        this.grno=grno;
        this.gno=gno;
        this.comment=comment;
        this.name=name;
        this.modifiedDate=modifiedDate;
        this.createdDate=createdDate;
    }
}
