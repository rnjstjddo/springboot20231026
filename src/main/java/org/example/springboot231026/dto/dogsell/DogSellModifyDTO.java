package org.example.springboot231026.dto.dogsell;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DogSellModifyDTO {
//Long dno; double weight, age, price;  String writer, content,title, gender, type, name, health;
//LocalDateTime createdDate, modifiedDate;
// List<DogSellImageDTO> dsiDtoList;

    private Long dno;
    private double weight, age;
    private int price;
    private String writer, content,title, gender, type, name, health;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate, modifiedDate;

    private List<DogSellImageDTO> dsiDtoList;


}
