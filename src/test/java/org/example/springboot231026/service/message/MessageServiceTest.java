package org.example.springboot231026.service.message;



   /*
@SpringBootTest
@RunWith(SpringRunner.class)
public class MessageServiceTest {


    @Autowired
    private MessageService ms;

    @Autowired
    private MemberService mems;

    @Autowired
    private MemberRepository mr;

    @Test
    public void sendTest(){
        System.out.println("테스트시작 MessaegServiceTest sendTest()");

        Optional<Member> o= mr.findById("rnjstjddo88@naver.com");
        Member recipient = o.get();

        Optional<Member> oo= mr.findById("rnjstjddo88@naver.com");
        Member sender =oo.get();

        MessageDTO mDto =MessageDTO.builder()
                .name(o.get().getName())
                .email("rnjstjddo88@naver.com")
                .recipient(mems.toMemberDto(recipient))
                .sender(mems.toMemberDto(sender))
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .content("강아지동영상")
                .number("01088889999")
                .build();

        MessageDTO dto = ms.send(mDto);
        System.out.println("결과 -> "+ dto);
    }

}*/