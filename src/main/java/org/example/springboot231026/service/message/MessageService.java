package org.example.springboot231026.service.message;


import org.example.springboot231026.domain.member.Member;
import org.example.springboot231026.domain.member.MemberRepository;
import org.example.springboot231026.domain.message.Message;
import org.example.springboot231026.domain.message.MessageRepository;
import org.example.springboot231026.dto.message.MessageDTO;
import org.example.springboot231026.service.member.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MessageService {

    @Autowired
    private MessageRepository mr;

    @Autowired
    private MemberRepository memr;

    @Autowired
    private MemberService mems;

    //쪽지보내기
    public MessageDTO send(MessageDTO mDto){
        //System.out.println("service-message클래스 MessageService send() 진입");


        Message entity = Message.builder()
                .email(mDto.getEmail())
                .name(mDto.getName())
                .content(mDto.getContent())
                .number(mDto.getNumber())
                .recipient(mems.toEntity(mDto.getRecipient()))
                .sender(mems.toEntity(mDto.getSender()))
                .messageid(mDto.getMessageid())
                .build();
        Long messageid =mr.save(entity).getMessageid();

        //System.out.println("service-message클래스 MessageService send() 진입 쪽지 save() getMessageid() -> "
                      //  + messageid);
        return entityToDto(entity,mDto.getName());

    }

    //쪽지받는사람기준 쪽지목록보기
    public List<MessageDTO> recipientList(String email){
        //System.out.println("service-message클래스 MessageService recipientList() 진입");

        Optional<Member> o =memr.findByEmail(email);
        Optional<List<Message>> entityListO = mr.findByRecipient(o.get());

        List<MessageDTO> mDtoList = new ArrayList<>();

        if(entityListO.isPresent()) {
            //System.out.println("service-message클래스 MessageService recipientList() 진입" +
                    //" 쪽지받는사람 기준으로 목록보기 쪽지가 존재할때 진입 ");
            List<Message> entityList = entityListO.get();
            mDtoList = entityList.stream().map(entity -> entityToDto(entity, o.get().getName())).collect(Collectors.toList());
            //System.out.println("service-message클래스 MessageService recipientList() 진입 반환타입 List<MessageDTO> -> " + mDtoList.toString());
        }else{
            //System.out.println("service-message클래스 MessageService recipientList() 진입" +
                  //  " 쪽지받는사람 기준으로 목록보기 쪽지가 존재할때 진입 ");
        }
        return mDtoList;
    }


    //쪽지보내는사람기준 쪽지목록보기
    public List<MessageDTO> senderList(String email){
        //System.out.println("service-message클래스 MessageService senderList() 진입");

        Optional<Member> o =memr.findByEmail(email);
        Optional<List<Message>> entityListO = mr.findBySender(o.get());

        List<MessageDTO> mDtoList = new ArrayList<>();

        if(entityListO.isPresent()) {
        List<Message> entityList = entityListO.get();
            //System.out.println("service-message클래스 MessageService recipientList() 진입" +
                    //" 쪽지보내는사람 기준으로 목록보기 쪽지가 존재할때 진입 ");

            mDtoList = entityList.stream().map(entity -> entityToDto(entity, o.get().getName())).collect(Collectors.toList());
            //System.out.println("service-message클래스 MessageService recipientList() 진입 반환타입 List<MessageDTO> -> " + mDtoList.toString());

        }else{
            //System.out.println("service-message클래스 MessageService recipientList() 진입" +
                  //  " 쪽지보내는사람 기준으로 목록보기 쪽지가 존재할때 진입 ");
        }
        return mDtoList;
    }


    @Transactional
    //메시지 삭제 받는사람기준
    //public void removeRecipient(Member recipient, String name, String number) {
    public void removeRecipient(Long messageid){
        //System.out.println("service-message클래스 MessageService removeRecipient() 진입 삭제할 쪽지번호 -> "+ messageid);
        mr.deleteById(messageid);
    }

    @Transactional
    //메시지 삭제 보내는사람기준
    //public void removeSender(Member sender, String name, String number) {
    public void removeSender(Long messageid) {
        //System.out.println("service-message클래스 MessageService removeSender() 진입 삭제할 쪽지번호 -> "+ messageid);
        mr.deleteById(messageid);
    }

    @Transactional
    //회원탈퇴시 메시지=쪽지 관련 삭제
    public boolean removeMessageDeleteMember(Member member) {
        //System.out.println("service-message클래스 MessageService removeMessageDeleteMember() 진입");

        List<Long> emailRecipientList =mr.getEmailByRecipient(member);
        List<Long> emailSenderList = mr.getEmailBySender(member);

        boolean result = false;
        if(emailRecipientList !=null || emailSenderList !=null){
            for(Long emailRecipient : emailRecipientList) {
                //System.out.println("service-message클래스 MessageService removeMessageDeleteMember() 진입 "+
                            //" 탈퇴회원이 쓴 메시지가 존재할경우 진입 -> "+ emailRecipient);

                mr.findById(emailRecipient);
                result = true;
            }
        }

        if(emailRecipientList ==null || emailSenderList ==null){
            //System.out.println("service-message클래스 MessageService removeMessageDeleteMember() 진입 "+
                   // " 탈퇴회원이 쓴 메시지가 없을경우 진입");
            result = true;

        }
        return result;
    }


    public MessageDTO entityToDto(Message entity, String name){
        //System.out.println("service-message클래스 MessageService entityToDto() 진입");
        MessageDTO mDto = MessageDTO.builder()
                .createdDate(entity.getCreatedDate())
                .messageid(entity.getMessageid())
                .modifiedDate(entity.getModifiedDate())
                .name(name)
                .sender(mems.toMemberDto(entity.getSender()))
                .recipient(mems.toMemberDto(entity.getRecipient()))
                .number(entity.getNumber())
                .content(entity.getContent())
                .email(entity.getEmail())
                .build();

        return mDto;
    }

    public Message dtoToEntity(MessageDTO mDto){
        //System.out.println("service-message클래스 MessageService dtoToEntity() 진입");
        Message entity = Message.builder()
                .content(mDto.getContent())
                .recipient(mems.dtoToEntity(mDto.getRecipient()))
                .email(mDto.getEmail())
                .number(mDto.getNumber())
                .sender(mems.dtoToEntity(mDto.getSender()))
                .name(mDto.getName())
                .messageid(mDto.getMessageid())
                .build();
        return entity;
    }

}
