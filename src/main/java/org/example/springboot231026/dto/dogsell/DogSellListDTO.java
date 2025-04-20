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
public class DogSellListDTO {
    //String gender, type, health; Double weight, age; LocalDateTime createdDate;DogSellImageDTO dsiDto;
    private Long dno;
    private String gender, type, health;
    private Double weight, age;
    //private Long inum;

    //private Boolean complete;
    private String complete;

    //추가
    private String membername;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdDate, modifiedDate;

    private List<DogSellImageDTO> dsiDtoList;

    //추가
    private String content,name;
    private int price;


}
