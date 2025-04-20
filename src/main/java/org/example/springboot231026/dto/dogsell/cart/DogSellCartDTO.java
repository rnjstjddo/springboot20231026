package org.example.springboot231026.dto.dogsell.cart;

import lombok.*;
import org.example.springboot231026.domain.member.Member;
import org.example.springboot231026.dto.dogsell.DogSellDTO;
import org.example.springboot231026.dto.member.MemberDTO;

import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
public class DogSellCartDTO {

    //DogSellCartDTO -> MemberDTO mDto; / List<Long> wishnumList; /List<DogSellDTO>
    private MemberDTO mDto;

    private List<Long> wishnumList;

    private List<DogSellDTO> dsDtoList = new ArrayList<>();


    public DogSellCartDTO(MemberDTO memberDTO){
        //System.out.println("dogsell-dto클래스 DogSellCartDTO 생성자 진입");
        this.mDto=memberDTO;
    }
    public void addDsDto(DogSellDTO dsDto){
        //System.out.println("dogsell-dto클래스 DogSellCartDTO List<DogSellDTO> 추가 addDsDto() 진입");
        dsDtoList.add(dsDto);
    }

    public void addWishnumList(Long wishnum){
        //System.out.println("dogsell-dto클래스 DogSellCartDTO List<DogSellDTO> 추가 addWishnumList() 진입");
        wishnumList.add(wishnum);
    }

}
