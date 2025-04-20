package org.example.springboot231026.dto.inquiry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.springboot231026.domain.inquiry.Inquiry;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class InquiryReplyDto {

//Long inrenum / String content / InquiryDto inquiryDto / Long innum / LocalDateTime createdDate, modifiedDate;
    private Long inrenum;

    private String content;

    private InquiryDto inquiryDto;

    //여기서 Inquiry 필드가 꼭 필요할까...
    private Long innum;

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDateTime createdDate, modifiedDate;


}
