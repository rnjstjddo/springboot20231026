package org.example.springboot231026.dto.dogsell.reply;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.springboot231026.domain.dog.DogSell;
import org.example.springboot231026.dto.dogsell.DogSellDTO;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DogSellReplyDTO {
//DogSellReplyDTO -> Long drno; / String text, replyer;/ DogSell dno;     private LocalDateTime createdDate, modifiedDate;
//엔티티멤버변수 Long drno;String text;replyer;  @ManyToOne DogSell dogsell; LocalDateTime createdDate, modifiedDate;
//DTO멤버변수 Long drno;String text, replyer;DogSell dno;LocalDateTime createdDate, modifiedDate;
    private Long drno;

    private String text, replyer;

    private DogSellDTO dno; //연관관계에 가진 엔티티의 컬럼중 1개를 추가한다.

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdDate, modifiedDate;

}
