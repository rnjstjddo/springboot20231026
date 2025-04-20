package org.example.springboot231026.domain.posts;

import org.apache.tomcat.jni.Local;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.stream.IntStream;



@RunWith(SpringRunner.class)
@SpringBootTest
public class PostsRepositoryTest {

    @Autowired
    private PostsRepository pr;

 /*   @After
    public void cleanup(){
        System.out.println("테스트 PostsRepositoryTest @After 진입");
        pr.deleteAll();

    }

    @Test
    public void 게시글저장_불러오기(){
        System.out.println("테스트 PostsRepositoryTest 게시글저장_불러오기() 진입");

        //given
        String title ="제목";
        String content ="본문";
        String author="저자";
        
        pr.save(Posts.builder()
                .title(title)
                .content(content)
                .autor(author)
                .build());

        //when
        List<Posts> list = pr.findAll();

        //then
        Posts entity = list.get(0);

        assertThat(entity.getTitle()).isEqualTo(title);
        assertThat(entity.getContent()).isEqualTo(content);

    }

    @Test
    public void BaseTimeEntity_등록() {
        System.out.println("테스트 PostsRepositoryTest  BaseTimeEntity_등록() 진입");

        //given
        LocalDateTime now = LocalDateTime.of(2023,10,26,0,0,0);

        pr.save(Posts.builder()
                .title("title").content("content").autor("content").build());

        //when
        List<Posts> list= pr.findAll();

        //then
        Posts entity = list.get(0);

        System.out.println("테스트 PostsRepositoryTest  BaseTimeEntity_등록() 진입 ->"+ entity.toString());

        assertThat(entity.getCreatedDate()).isAfter(now);
        assertThat(entity.getModifiedDate()).isAfter(now);
    }*/

    @Test
    public void 게시글더미데이터_저장하기() {
        System.out.println("테스트 PostsRepositoryTest 게시글더미데이터_저장하기() 진입");

        //given
        String title = "제목";
        String content = "본문";
        String author = "저자";

        IntStream.rangeClosed(1,100).forEach(i ->{
            pr.save(Posts.builder()
                    .title(i+title)
                    .content(i+content)
                    .autor(i+author)
                    .build());
        });
    }


//        System.out.println("테스트 PostsRepositoryTest @After 진입");
}