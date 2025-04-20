package org.example.springboot231026.service.dogsell;

import org.example.springboot231026.domain.dog.WishNum;
import org.example.springboot231026.domain.dog.WishNumRepository;
import org.example.springboot231026.domain.member.Member;
import org.example.springboot231026.domain.member.MemberRepository;
import org.example.springboot231026.dto.dogsell.DogSellDTO;
import org.example.springboot231026.dto.dogsell.DogSellListDTO;
import org.example.springboot231026.dto.dogsell.DogSellReadDTO;
import org.example.springboot231026.dto.dogsell.cart.WishNumDTO;
import org.example.springboot231026.dto.member.MemberDTO;
import org.example.springboot231026.service.member.MemberService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class WishNumService {

    @Autowired
    private WishNumRepository wnr;

    @Autowired
    private MemberRepository mr;

    @Autowired
    private MemberService ms;

    @Autowired
    private ModelMapper mm;

    @Autowired
    private DogSellService dss;


    @Transactional
    public Map<String, Object> wishNumInsert(Long wishNum, String name, MemberDTO memberDTO){
    //public Map<String, Object> wishNumInsert(Long wishNum, String email, MemberDTO memberDTO){

        //System.out.println("service-dogsell클래스 WishNumService wishNumInsert() 진입");

        //반환타입생성
        Map<String, Object> map = new HashMap<>();

        Optional<Member> mentity = mr.findByName(name);
        //Optional<Member> mentity = mr.findByEmail(email);


        //존재하는 번호인지 먼저확인
        Optional<WishNum> own =wnr.getWishNum(mentity.get(), wishNum);
        if(own.isPresent()) {
            //System.out.println("service-dogsell클래스 WishNumService wishNumInsert() 진입 - WishNum 존재할때 진입 -이미 존재하는 분양글번호 ->" + own.get().getWishnum());

            map.put("overlap", true);
            return map;
        }
        
        WishNum wnentity = WishNum.builder()
                .wishnum(wishNum)
                .member(mentity.get())
                .build();

        Long id =wnr.save(wnentity).getId();

        //System.out.println("service-dogsell클래스 WishNumService WishNum 테이블 save() 후 id -> "+id);

        //출력해보자
        Optional<WishNum> o =wnr.findById(id);
        //System.out.println("service-dogsell클래스 WishNumService wishNumInsert() 진입 Optional<WishNum> -> "+ o.toString());

        Object oc = wnr.getWishNumCount(mentity.get());
        Long count = (Long)oc;
        //System.out.println("service-dogsell클래스 WishNumService wishNumInsert() 진입 현재 위시리스트개수-> "+ count);

        map.put("count",count);
        return map;
    }


    //위시목록전달
    public Map<String, Object> wishNumListGet(String name) {
    //public Map<String, Object> wishNumListGet(String email) {

        //System.out.println("service-dogsell클래스 WishNumService wishNumListGet() 진입 파라미터 name -> "+ name);
        //System.out.println("service-dogsell클래스 WishNumService wishNumListGet() 진입 파라미터 email -> "+ email);

        //반환타입
        Map<String, Object> map =new HashMap<>();

        Optional<Member> o = mr.findByName(name);
        //Optional<Member> o = mr.findByEmail(email);

        Optional<List<WishNum>> wishNumO= wnr.findByWishNum(o.get());
        List<WishNumDTO> wishNumDTOList = new ArrayList<>();

        List<DogSellReadDTO> DogSellReadDTOList = new ArrayList<>();

        //위시리스트목록
        if(wishNumO.isPresent()) {
            //System.out.println("service-dogsell클래스 WishNumService wishNumListGet() 진입 - List<WishNum> 존재할경우 진입 -> "+ wishNumO.toString());


            if (wishNumO.get().size() != 0) {
                for(WishNum wishNumList : wishNumO.get()) {
                    wishNumDTOList.add(mm.map(wishNumList, WishNumDTO.class));

                    Long wishnum = wishNumList.getWishnum();
                    //System.out.println("service-dogsell클래스 WishNumService wishNumListGet() 진입 - List<WishNum> 존재할경우 진입 - 해당 회원의 위시번호 -> "+ wishnum);

                    DogSellReadDTO dogSellReadDTO = dss.read(wishnum);
                    DogSellReadDTOList.add(dogSellReadDTO);

                }
            }
            //System.out.println("service-dogsell클래스 WishNumService wishNumListGet() 진입 - List<WishNum> 존재할경우 진입 -> "+ wishNumDTOList.toString());

            map.put("wishNumDTOList",wishNumDTOList);
            map.put("dogSellReadDTOList",DogSellReadDTOList);
        }//if 위시리스트목록이존재할때

        //위시리스트 목록개수
        Object oc = wnr.getWishNumCount(o.get());

        if((Long) oc != 0 ) {
            //System.out.println("service-dogsell클래스 WishNumService wishNumListGet() 진입 - 위시리스트 개수가 존재할경우 -> " +(Long)oc);

            Long count = (Long) oc;
            map.put("countWishNum",count);
        }

        return map;
    }

    //위시번호삭제 회원과 위시번호 동시에 일치
    @Transactional
    public boolean wishlistremove(Long wishnum, String name){
        //System.out.println("service-dogsell클래스 WishNumService wishlistremove() 진입 ");
        Optional<Member> o =mr.findByName(name);
        if(o.isPresent()){
            //System.out.println("service-dogsell클래스 WishNumService wishlistremove() 진입 Member -> "+ o.get());
        }
        Optional<WishNum> own =wnr.getWishNum(o.get(), wishnum);
        //System.out.println("삭제진입전");
        if(own.isPresent()) {
            //System.out.println("service-dogsell클래스 WishNumService wishlistremove() 진입 - WishNum 존재할때 진입 삭제처리할 분양글번호->" + own.get().getWishnum());
            wnr.deleteById(own.get().getId());
            return true;
        }
        //System.out.println("삭제진입후");
        return false;

    }


    //위시번호삭제 위시번호만 일치
    @Transactional
    public void wishlistremoveAll(Long wishnum){
        //System.out.println("service-dogsell클래스 WishNumService wishlistremove() 진입 -게시글삭제에 의한 위시번호삭제처리");

        List<WishNum> wishNumList = wnr.findByWishnum(wishnum);
        //System.out.println("위시번호전체 삭제진입전");

        for(WishNum entity: wishNumList){
            //System.out.println("위시번호전체 삭제 id -> "+entity.getId());
            wnr.deleteById(entity.getId());
        }
    }



    //위시번호삭제 위시번호만 일치
    @Transactional
    public boolean wishlistremoveAllDeleteMember(Member member){
        //System.out.println("service-dogsell클래스 WishNumService wishlistremoveAllDeleteMember() 진입 "+
                        //" 회원탈퇴시 위시번호삭제처리 ");

        List<Long> wnrIdList = wnr.getWithNumIdByMember(member);

        boolean result = false;
        if(wnrIdList !=null && wnrIdList.size() != 0) {

            for (Long wnrId : wnrIdList) {
                //System.out.println("service-dogsell클래스 WishNumService wishlistremoveAllDeleteMember() 진입 "+
                         //" 삭제할 위시번호가 존재할떄 진입 -> "+ wnrId );
                wnr.deleteById(wnrId);
            }
            result = true;
        }else{
            //System.out.println("service-dogsell클래스 WishNumService wishlistremoveAllDeleteMember() 진입 "+
                  //  " 삭제할 위시번호가 없을때 진입");
            result = true;
        }
        return result;
    }


}
