package org.example.springboot231026.web;



/*
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostsApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private PostsRepository pr;

    @Autowired
    private TestRestTemplate trt;

  */
/*  @After
    public void tearDown() throws Exception{
        System.out.println("테스트 PostsApiControllerTest @After 진입");
        pr.deleteAll();
    }

    @Test
    public void Posts_등록된다() throws Exception {
        System.out.println("테스트 PostsApiControllerTest Posts_등록된다() 진입");

        //given
        String title = "title";
        String content = "content";
        String author = "author";

        PostsSaveRequestDto dto = PostsSaveRequestDto.builder()
                .title(title)
                .author(author)
                .content(content)
                .build();

        String url = "http://localhost:"+port+"/api/v1/posts";

        //when
        ResponseEntity<Long> entity = trt.postForEntity(url, dto, Long.class);//Long반환타입

        //then
        assertThat(entity.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        assertThat(entity.getBody())
                .isGreaterThan(0L);

        List<Posts> list = pr.findAll();

        assertThat(list.get(0).getTitle()).isEqualTo(title);
        assertThat(list.get(0).getContent()).isEqualTo(content);
    }



    @Test
    public void Posts_수정된다() throws Exception {
        System.out.println("테스트 PostsApiControllerTest Posts_수정된다() 진입");


        //given
        Posts entity = pr.save(Posts.builder()
                .autor("author")
                .title("title")
                .content("content")
                .build());

        Long updateId = entity.getId();

        String updateTitle ="title2";
        String updateContent ="content2";

        PostsUpdateRequestDto dto = PostsUpdateRequestDto.builder()
                .content(updateContent).title(updateTitle)
                .build();

        String url ="http://localhost:"+port+"/api/v1/posts/"+ updateId;

        HttpEntity<PostsUpdateRequestDto> http = new HttpEntity<>(dto);

        //when
        ResponseEntity<Long> response = trt.exchange(url, HttpMethod.PUT, http, Long.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isGreaterThan(0L);

        List<Posts> list = pr.findAll();
        assertThat(list.get(0).getTitle()).isEqualTo(updateTitle);
        assertThat(list.get(0).getContent()).isEqualTo(updateContent);

    }*//*

        //    System.out.println("테스트 PostsApiControllerTest @After 진입");

}*/
