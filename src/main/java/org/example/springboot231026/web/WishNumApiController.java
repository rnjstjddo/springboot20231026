package org.example.springboot231026.web;


import org.example.springboot231026.dto.member.MemberDTO;
import org.example.springboot231026.service.dogsell.WishNumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/wishnum")
public class WishNumApiController {

    @Autowired
    private WishNumService wns;

    @GetMapping("/insert")
    public @ResponseBody Object wishNumInsert(Model model, Long wishNum, @AuthenticationPrincipal MemberDTO memberDTO) {
        //System.out.println("컨트롤러 WishNumController wishNumInsert() 진입 ");

        if(memberDTO != null){
            //System.out.println("컨트롤러 WishNumController wishNumInsert() 진입 MemberDTO -> "+ memberDTO);
        }

        Map<String, Object> map = wns.wishNumInsert(wishNum, memberDTO.getName(), memberDTO);
        Long count =(Long) map.get("count");

        if ((Boolean) map.get("overlap") !=null && (Boolean) map.get("overlap") ==true) {
            return false;
        }
        //System.out.println("컨트롤러 WishNumController wishNumInsert() 진입 찜한 상품의 수 -> "+count );
        return count;
    }

}
