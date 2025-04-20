package org.example.springboot231026.domain.posts;

import org.example.springboot231026.domain.posts.search.PostsSearch;
import org.example.springboot231026.dto.post.PostsListResponseDto;
import org.example.springboot231026.dto.post.PostsResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PostsRepository extends JpaRepository<Posts, Long> , PostsSearch {

    @Query(value="select now()", nativeQuery = true)
    String getTime();

    @Query("select p from Posts p where p.author=:author")
    Optional<List<Posts>> findByAuthor(@Param("author") String author);


    @Query("select p.id from Posts p where p.author=:author")
    List<Long> getIdByAuthor(@Param("author") String author);

    //양방향 일대다 사용하지 않음
    //@Query("select p.id, count(p.prList.size) from Posts p group by p")
    //List<Object []> getPostReply();

    //댓글 안되네..
    //@Query(value = "select p.id, count(r) from Posts p left join PostReply r on r.posts=p group by p")
    @Query(value = "select p.id, count(r) from Posts p left join PostReply r on r.pno=p.id group by p.id")
    List<Object []> getReplyCount();


    //관리자페이지 게시글수
    //@Query("select count(p) from Posts p where r.modifiedDate like localDate%")
    //Long getCountLocalDate(@Param("localDate")LocalDate localDate);

    @Query("select count(g) from Posts g where g.createdDate >= :before and g.createdDate < :after")
    Long getCountLocalDate(@Param("before") LocalDateTime before, @Param("after") LocalDateTime after);


    default PostsListResponseDto entityToDto(Posts entity){
        //System.out.println("domain-posts패키지 PostsRepository인터페이스 entityToDto() 진입");

        PostsListResponseDto dto = new PostsListResponseDto(entity.getId(), entity.getTitle(),
                entity.getAuthor(),entity.getContent(),entity.getCreatedDate(),entity.getModifiedDate());

        return dto;
    }

    default Posts dtoToEntity(PostsListResponseDto dto){
        //System.out.println("domain-posts패키지 PostsRepository인터페이스 entityToDto() 진입");

        Posts entity = Posts.builder()
                .autor(dto.getAuthor())
                .title(dto.getTitle())
                .content(dto.getContent())
                .build();
        return entity;
    }
}
