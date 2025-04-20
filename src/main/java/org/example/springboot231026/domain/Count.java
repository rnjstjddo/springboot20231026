package org.example.springboot231026.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Count {

    private Long dogsellcount;
    private Long guestcount;
    private Long guestreplycount;
    private Long postcount;
    private Long postreplycount;
    private Long inquirycount;
    private Long membercount;

}
