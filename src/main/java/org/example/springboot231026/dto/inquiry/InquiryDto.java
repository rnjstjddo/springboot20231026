package org.example.springboot231026.dto.inquiry;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class InquiryDto {

    private Long innum;
    private String writer, content,phone,email,complete;
    //private Boolean complete;

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDateTime createdDate, modifiedDate;


}
