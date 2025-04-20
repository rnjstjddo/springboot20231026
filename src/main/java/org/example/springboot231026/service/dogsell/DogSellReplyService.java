package org.example.springboot231026.service.dogsell;

import org.example.springboot231026.domain.dog.DogSell;
import org.example.springboot231026.domain.dog.DogSellReply;
import org.example.springboot231026.domain.dog.DogSellReplyRepository;
import org.example.springboot231026.domain.dog.DogSellRepository;
import org.example.springboot231026.dto.dogsell.DogSellDTO;
import org.example.springboot231026.dto.dogsell.reply.DogSellReplyDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DogSellReplyService {

    @Autowired
    private DogSellReplyRepository dsrr;

    @Autowired
    private DogSellRepository  dsr;

    @Autowired
    private ModelMapper mm;

    //DogSellReply 등록
    @Transactional
    public Long dsReplyRegister(DogSellReplyDTO rDto, Long dno){
        //System.out.println("service-dogsell클래스 DogSellReplyService dsReplyRegister() 진입 댓글작성하려는 게시글번호 -> "+dno);

        Optional<DogSell> o = dsr.findById(dno);
        if(o.isPresent()){
            DogSell entity =o.get();
        }
        rDto.setDno(mm.map(o.get(), DogSellDTO.class));

        DogSellReply entity = dtoToEntity(rDto);

        dsrr.save(entity);
        return entity.getDrno();
    }


    //DogSellReply
    @Transactional
    public DogSellReplyDTO dsReplyRead(Long drno) {
        //System.out.println("service-dogsell클래스 DogSellReplyService dsReplyRead() 진입");

        Optional<DogSellReply> o = dsrr.findById(drno);

        DogSellReply entity =o.get();

        DogSellReplyDTO resultDto = entityToDto(entity);
        return resultDto;
    }


    //DogSellReply 수정
    @Transactional
    public DogSellReplyDTO dsReplyModify(DogSellReplyDTO rDto, Long dno) {
        //System.out.println("service-dogsell클래스 DogSellReplyService dsReplyModify() 진입");

        Optional<DogSellReply> o = dsrr.findById(rDto.getDrno());

        DogSellReply entity =o.get();
        DogSellReply result = entity.change(rDto.getText());
        //System.out.println("service-dogsell클래스 DogSellReplyService dsReplyModify() 진입 - 변경한 entity - >" + result.toString());

        DogSellReplyDTO resultDto = entityToDto(result);

        return resultDto;

    }

    //DogSellReply 삭제
    public void dsReplyRemove(Long drno) {
        //System.out.println("service-dogsell클래스 DogSellReplyService dsReplyRemove() 진입");

        Optional<DogSellReply> o = dsrr.findById(drno);
        if(o.get().getDrno() !=null) {
            //System.out.println("service-dogsell클래스 DogSellReplyService dsReplyRemove() 파라미터번호 -> "+ drno +", 엔티티로 가져온번호 -> "+o.get().getDrno());

            dsrr.deleteById(o.get().getDrno());
        }
    }

    //회원탈퇴시 관련 분양댓글삭제처리
    public boolean dsReplyRemoveDeleteMember(String writer) {
        //System.out.println("service-dogsell클래스 DogSellReplyService dsReplyRemoveDeleteMember() 진입"+
                //" 삭제할 회원 name -> "+writer);
        List<Long> drnoList = dsrr.getDogSellReplyDrnoByWriter(writer);

        boolean result = false;
        if(drnoList !=null && drnoList.size() !=0){

            for(Long drno : drnoList) {
                //System.out.println("service-dogsell클래스 DogSellReplyService dsReplyRemoveDeleteMember() 진입" +
                       // " 탈퇴할 회원의 분양댓글이 존재할 경우 진입 drno -> "+ drno);
                dsrr.deleteById(drno);
            }
            result = true;

        }else{
            //System.out.println("service-dogsell클래스 DogSellReplyService dsReplyRemoveDeleteMember() 진입"+
                    //" 탈퇴할 회원의 분양댓글이 없을경우 진입");
            result = true;
        }
        return result;
    }

    //DogSellReply 목록
    @Transactional
    public List<DogSellReplyDTO> dsReplyList() {
        //System.out.println("service-dogsell클래스 DogSellReplyService dsReplyList() 진입");

        List<DogSellReply> o =dsrr.findAll();

        List<DogSellReplyDTO> result =o.stream().map(e -> entityToDto(e)).collect(Collectors.toList());
        return result;
    }


    //분양글에 대한 댓글만 가져오기
    @Transactional
    public List<DogSellReplyDTO> dsReplyListDno(Long dno) {
        //System.out.println("service-dogsell클래스 DogSellReplyService dsReplyListDno() 진입 - 분양글번호 -> "+ dno);

        Optional<List<DogSellReply>> o =dsrr.dsReplyListDno(dno);
        List<DogSellReplyDTO> result = new ArrayList<>();

        if(o.isPresent()){
            //System.out.println("service-dogsell클래스 DogSellReplyService dsReplyListDno() 진입 분양글에 대한 댓글이 존재할경우 진입");

            List<DogSellReply> list = o.get();

            result = list.stream().map(e -> entityToDto(e)).collect(Collectors.toList());

            //System.out.println("service-dogsell클래스 DogSellReplyService dsReplyListDno() 진입 분양글에 대한 댓글 출력 -> "+result.toString());
        }
        return result;
    }


        //DogSellReplyDTO -> entity
    public DogSellReply dtoToEntity(DogSellReplyDTO rDto){
        //System.out.println("service-dogsell클래스 DogSellReplyService dtoToEntity() 진입");


// Long drno;String text; String replyer;  @ManyToOne DogSell dogsell;  LocalDateTime createdDate, modifiedDate;
        DogSellReply entity = DogSellReply.builder()
                .replyer(rDto.getReplyer())
                .text(rDto.getText())
                .dogsell(mm.map(rDto.getDno(), DogSell.class))
                .drno(rDto.getDrno())
                .build();


        //System.out.println("service-dogsell클래스 DogSellReplyService dtoToEntity() 진입 생성한 엔티티 -> "+entity.toString());

        return entity;
    }

    //entity -> DogSellReplyDTO
    public DogSellReplyDTO entityToDto(DogSellReply entity){
        //System.out.println("service-dogsell클래스 DogSellReplyService entityToDto() 진입");

//엔티티멤버변수 Long drno;String text;replyer;  @ManyToOne DogSell dogsell; LocalDateTime createdDate, modifiedDate;

//DTO멤버변수 Long drno;String text, replyer;DogSell dno;LocalDateTime createdDate, modifiedDate;
        DogSellReplyDTO rDto = DogSellReplyDTO.builder()
                .replyer(entity.getReplyer())
                .createdDate(entity.getCreatedDate())
                .dno(mm.map(entity.getDogsell(), DogSellDTO.class))
                .drno(entity.getDrno())
                .text(entity.getText())
                .modifiedDate(entity.getModifiedDate())
                .build();

        //System.out.println("service-dogsell클래스 DogSellReplyService dtoToEntity() 진입 생성한 DTO -> "+rDto.toString());

        return rDto;
    }


}
