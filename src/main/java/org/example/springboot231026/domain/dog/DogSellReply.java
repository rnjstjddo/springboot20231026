package org.example.springboot231026.domain.dog;


import lombok.*;
import org.example.springboot231026.domain.posts.BaseTimeEntity;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude="dogsell")
public class DogSellReply extends BaseTimeEntity {
// Long drno;String text; String replyer;  @ManyToOne DogSell dogsell;  LocalDateTime createdDate, modifiedDate;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long drno;

    private String text;

    private String replyer;


    @ManyToOne(fetch = FetchType.LAZY)
    private DogSell dogsell;


//수정용
    public DogSellReply change(String text){
        //System.out.println("domain-dog클래스 DogSellReply 엔티티 change() 진입");
        this.text=text;
        return this;
    }
    
}
