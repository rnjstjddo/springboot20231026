package org.example.springboot231026.domain.dog;


import lombok.*;
import org.example.springboot231026.domain.posts.BaseTimeEntity;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@DynamicInsert

public class DogSell extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dno;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private Double weight;

    @Column(nullable = false, length = 2000)
    private String content;

    @Column(nullable = false, length = 200)
    private String type;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false)
    private Double age;

    @Column(nullable = false, length = 2000)
    private String health;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private String writer;
//Long dno; LocalDateTime createdDate, modifiedDate, String writer 4개는 변경하지 않아도된다.
// String title,gender, weight, content,type, name,health, Double age, int price


    //분양완료경우
    //@Column(nullable = false)
    //@Convert(converter = BooleanToYNConverter.class)
    //@Builder.Default
    //private Boolean complete=false;
    @ColumnDefault("'FALSE'")
    @Column(name="complete",nullable = false)
    @Builder.Default
    //private Boolean complete = false;
    private String complete = "false";

    public DogSell change(String title, String gender, String type, String name,String content,
                          String health, Double age, Double weight, int price){
        //System.out.println("domain-dog클래스 DogSell 엔티티 change() 진입");
        this.title=title;
        this.gender=gender;
        this.type=type;
        this.name=name;
        this.content=content;
        this.health=health;
        this.age=age;
        this.weight=weight;
        this.price=price;

        return this;
    }

    public void changeComplete(String value){
        //System.out.println("domain-dog패키지 DogSell클래스 changeComplete()진입 분양완료된 분양강아지");
        this.complete=value;
    }
}