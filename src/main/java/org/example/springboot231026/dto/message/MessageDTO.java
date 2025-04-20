package org.example.springboot231026.dto.message;


import lombok.*;
import org.example.springboot231026.domain.member.Member;
import org.example.springboot231026.dto.member.MemberDTO;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Data
@Builder
public class MessageDTO {
//Message String email; content; number; name; MemberDTO sender;recipient; BaseTimeEntity

    private Long messageid;

    private MemberDTO recipient, sender;
    private String recipientString, senderString;

    private String email, content, number, name;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdDate,modifiedDate;


}
