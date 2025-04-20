package org.example.springboot231026.web;


/*

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = HelloController.class)
public class HelloControllerTest {

    @Autowired
    private MockMvc mm;
*/
/*
    @Test
    public void hello가_리턴된다() throws Exception{
        System.out.println("컨트롤러테스트 HelloControllerTest 진입");

        String hello ="hello";

        mm.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string(hello));
    }

    @Test
    public void helloDto가_리턴된다() throws Exception {
        System.out.println("컨트롤러테스트 HelloControllerTest 진입");

        int amount =1000;
        String name = "hello";

        mm.perform(get("/hello/dto")
                .param("name", name)
                .param("amount", String.valueOf(amount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.amount", is(amount)));

    }*//*

}*/
