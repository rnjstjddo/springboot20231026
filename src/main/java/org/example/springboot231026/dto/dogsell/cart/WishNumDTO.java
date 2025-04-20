package org.example.springboot231026.dto.dogsell.cart;

import lombok.*;
import org.example.springboot231026.domain.member.Member;


@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
@Builder
public class WishNumDTO {
    //WishNumDTO -> WishNumDTO /Long wishNum; /Member member;
    private Long id;

    private Long wishNum;

    private Member member;


}
