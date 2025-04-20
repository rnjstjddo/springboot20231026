package org.example.springboot231026.dto.main;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.springboot231026.dto.dogsell.DogSellImageDTO;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MainDogSellDTO {

    private String gender, type, health;
    private Double weight, age;

    private List<DogSellImageDTO> dsiDto;


}
