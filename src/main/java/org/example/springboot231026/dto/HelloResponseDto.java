package org.example.springboot231026.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class HelloResponseDto {

    private String name;
    private int amount;

}
