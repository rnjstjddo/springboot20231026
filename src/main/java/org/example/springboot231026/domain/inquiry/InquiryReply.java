package org.example.springboot231026.domain.inquiry;


import lombok.*;
import org.example.springboot231026.domain.posts.BaseTimeEntity;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class InquiryReply extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inrenum;


    @Column(name = "content",nullable = false)
    private String content;

    @OneToOne(fetch=FetchType.EAGER)
    //@JoinColumn(name="inquiry_innum", unique = true)
    @JoinColumn(name="inquiry_innum", referencedColumnName = "innum")
    private Inquiry inquiry;

}
