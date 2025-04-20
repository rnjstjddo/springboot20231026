package org.example.springboot231026.dto.member;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.springboot231026.domain.member.Member;
import org.example.springboot231026.domain.member.RoleType;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class JoinDTO {

    private String name, password, email;
    private RoleType role;
    //private boolean fromSocial;
    private String formSocial;

    private LocalDateTime createdDate, modifiedDate;


}
