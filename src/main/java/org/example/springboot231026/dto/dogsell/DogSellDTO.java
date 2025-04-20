package org.example.springboot231026.dto.dogsell;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.springboot231026.domain.dog.DogSellImage;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DogSellDTO {

    private Long dno;
    private double weight, age;
    private int price;
    private String writer, content,title, gender, type, name, health;

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDateTime createdDate, modifiedDate;

    private String complete;

    @Builder.Default
    private List<DogSellImageDTO> dsiDtoList = new ArrayList<>();

    public void setDsiDtoList(List<DogSellImageDTO> dsiDtoList) {
        //System.out.println("dogsell-dto클래스 DogSellDTO List<DogSellImageDTO> setDsiDtoList() 진입");
        this.dsiDtoList =dsiDtoList;

    }

    public void addDiDto(DogSellImageDTO dsiDto){
        //System.out.println("dogsell-dto클래스 DogSellDTO List<DogSellImageDTO> addDiDto() 진입");
        dsiDtoList.add(dsiDto);
    }

}
