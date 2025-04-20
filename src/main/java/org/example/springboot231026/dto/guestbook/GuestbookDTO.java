package org.example.springboot231026.dto.guestbook;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GuestbookDTO {


    private Long gno;
    private String title, content, writer;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdDate, modifiedDate;

}
