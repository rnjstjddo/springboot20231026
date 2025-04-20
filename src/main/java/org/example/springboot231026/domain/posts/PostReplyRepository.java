package org.example.springboot231026.domain.posts;

import org.example.springboot231026.domain.member.Member;
import org.example.springboot231026.domain.posts.search.PostsSearch;
import org.example.springboot231026.dto.post.PostReplyDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PostReplyRepository extends JpaRepository<PostReply, Long>, PostsSearch {

    @Query("select r from PostReply r where r.pno =:pno")
    List<PostReply> list(@Param("pno") Long pno);

    @Modifying
    @Query("delete from PostReply r where r.pno =:pno")
    void deleteByPno(@Param("pno") Long pno);

    //댓글수 가져오기 다대일관계
    //@Query("select r.prno, count(r) from PostReply r group by r.posts")
    //List<Object []> getReplyCount();

    //댓글수
    @Query("select r.pno, count(r) from PostReply r group by r.pno")
    List<Object []> getPostReplyCount();

    //관리자페이지 게시글댓글수
    //@Query("select count(p) from PostReply p where r.modifiedDate like localDate%")
    //Long getCountLocalDate(@Param("localDate") LocalDate localDate);

    @Query("select count(g) from PostReply g where g.createdDate >= :before and g.createdDate < :after")
    Long getCountLocalDate(@Param("before") LocalDateTime before, @Param("after") LocalDateTime after);



    default PostReply dtoToEntity(PostReplyDTO dto, Member member, Posts post){
        //System.out.println("domain-posts패키지 PostsReplyRepository인터페이스 dtoToEntity() 진입");
        PostReply entity = PostReply.builder()
                .member(member)
                .prno(dto.getPrno())
                .posts(post)
                .comment(dto.getComment())
                .pno(dto.getPno())
                .build();
        return entity;
    }


    default PostReplyDTO entityToDto(PostReply entity){
        //System.out.println("domain-posts패키지 PostsReplyRepository인터페이스 entityToDto() 진입");

        PostReplyDTO dto = new PostReplyDTO(entity.getPrno(), entity.getPno(),
                entity.getComment(),entity.getComment(),entity.getModifiedDate(),entity.getCreatedDate());

        return dto;
    }

}
