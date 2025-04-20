package org.example.springboot231026.domain.message;


import lombok.*;
import org.example.springboot231026.domain.member.Member;
import org.example.springboot231026.domain.posts.BaseTimeEntity;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Builder
public class Message extends BaseTimeEntity {
//Message String email; content; number; name; sender;recipient; BaseTimeEntity

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageid;

    @Column(nullable = false)
    private String email;


    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String number;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    private Member sender;

    @ManyToOne
    private Member recipient;

}
