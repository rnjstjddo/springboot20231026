package org.example.springboot231026.web;


import org.example.springboot231026.dto.dogsell.reply.DogSellReplyDTO;
import org.example.springboot231026.service.dogsell.DogSellReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dogsell/reply")
public class DogSellReplyController {

    @Autowired
    private DogSellReplyService dsrs;

    @PostMapping("/register/{dno}")
    public Long register(@RequestBody DogSellReplyDTO rDto, @PathVariable Long dno){
        //System.out.println("DogSellReplyController register() 진입 파라미터 출력 -> "+ rDto.toString()+", dno -> "+dno);
        Long drno =dsrs.dsReplyRegister(rDto, dno);
        return drno;
    }

    @PostMapping("/remove")
    public @ResponseBody Long remove(@RequestBody Long drno) {
        //System.out.println("DogSellReplyController remove() 진입 파라미터 출력 -> " + drno);
        dsrs.dsReplyRemove(drno);
        return drno;
    }
    @PutMapping("/modify/{dno}")
    public Long modify(@RequestBody DogSellReplyDTO rDto, @PathVariable Long dno) {
        //System.out.println("DogSellReplyController modify() 진입 @PathVariable -> " + dno);
        //DogSellReplyDTO -> Long drno; / String text, replyer;/ DogSell dno;     private LocalDateTime createdDate, modifiedDate;
        dsrs.dsReplyModify(rDto, dno);
        return rDto.getDrno();
    }


}
