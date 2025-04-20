package org.example.springboot231026.domain.posts.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.example.springboot231026.domain.inquiry.Inquiry;
import org.example.springboot231026.domain.posts.PostReply;
import org.example.springboot231026.domain.posts.Posts;
import org.example.springboot231026.domain.posts.QPostReply;
import org.example.springboot231026.domain.posts.QPosts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class PostsSearchImpl extends QuerydslRepositorySupport implements PostsSearch {

    public PostsSearchImpl(){
        super(Posts.class);
        //System.out.println("domain-posts-search패키지 PostsSearchImpl클래스 생성자진입");

    }


    @Override
    public Page<Posts> searchAll(String[] types, String keyword, Pageable pageable) {
        //System.out.println("domain-posts-search패키지 PostsSearchImpl클래스 오버라이딩 searchAll() 진입");

        QPosts qp = QPosts.posts;
        JPQLQuery<Posts> jpp = from(qp);

        if( (types!= null && types.length >0 ) && keyword !=null){

            BooleanBuilder bb = new BooleanBuilder();
            for(String type: types){

                switch(type){
                    case"t":
                        bb.or(qp.title.contains(keyword));
                        break;
                    case"c":
                        bb.or(qp.content.contains(keyword));
                        break;
                    case"w":
                        bb.or(qp.author.contains(keyword));
                        break;
                }
            }//for
            jpp.where(bb);
        }//if

        jpp.where(qp.id.gt(0L));
        
        //페이징처리
        this.getQuerydsl().applyPagination(pageable, jpp);

        List<Posts> list= jpp.fetch();
        long count = jpp.fetchCount();

        return new PageImpl<>(list, pageable, count);

    }


    //관리자페이지에서 날짜조건에 맞는 게시글 페이징처리와 동적검색 모두처리
    @Override
    public Page<Posts> searchAllModifiedDate(String[] types, String keyword, Pageable pageable, LocalDate localDate) {
        //System.out.println("domain-posts-search패키지 PostsSearchImpl클래스 오버라이딩 searchAllModifiedDate() 진입");

        QPosts qp = QPosts.posts;
        JPQLQuery<Posts> jpp = from(qp);

        //LocalDate 값이 존재할때 조건에 추가했다.
        if( (types!= null && types.length >0 ) && keyword !=null&& localDate !=null){

            BooleanBuilder bb = new BooleanBuilder();
            for(String type: types){

                switch(type){
                    case"t":
                        bb.or(qp.title.contains(keyword));
                        break;
                    case"c":
                        bb.or(qp.content.contains(keyword));
                        break;
                    case"w":
                        bb.or(qp.author.contains(keyword));
                        break;
                }
            }//for
            jpp.where(bb);
        }//if
        //날짜를 where조건에 추가
        jpp.where(qp.id.gt(0L).and(qp.modifiedDate.between(localDate.atTime(LocalTime.MIN), localDate.atTime(LocalTime.MAX))));

        //페이징처리
        this.getQuerydsl().applyPagination(pageable, jpp);

        List<Posts> list= jpp.fetch();
        long count = jpp.fetchCount();

        return new PageImpl<>(list, pageable, count);
        
    }

    @Override
    public Page<Posts> searchAllCreatedDate(String[] types, String keyword, Pageable pageable, LocalDate localDate) {
        //System.out.println("domain-posts-search패키지 PostsSearchImpl클래스 오버라이딩 searchAllCreatedDate() 진입 " + localDate);

        QPosts qp = QPosts.posts;
        JPQLQuery<Posts> jpp = from(qp);

        //LocalDate 값이 존재할때 조건에 추가했다.
        if( (types!= null && types.length >0 ) && keyword !=null&& localDate !=null){

            BooleanBuilder bb = new BooleanBuilder();
            for(String type: types){

                switch(type){
                    case"t":
                        bb.or(qp.title.contains(keyword));
                        break;
                    case"c":
                        bb.or(qp.content.contains(keyword));
                        break;
                    case"w":
                        bb.or(qp.author.contains(keyword));
                        break;
                }
            }//for
            jpp.where(bb);
        }//if
        //날짜를 where조건에 추가
        jpp.where(qp.id.gt(0L).and(qp.createdDate.between(localDate.atTime(LocalTime.MIN), localDate.atTime(LocalTime.MAX))));

        //페이징처리
        this.getQuerydsl().applyPagination(pageable, jpp);

        List<Posts> list= jpp.fetch();
        long count = jpp.fetchCount();

        return new PageImpl<>(list, pageable, count);

    }

    //관리자페이지에서 날짜조건에 맞는 게시글 페이징처리와 동적검색 모두처리
    @Override
    public Page<PostReply> searchReplyAllModifiedDate(String[] types, String keyword, Pageable pageable, LocalDate localDate) {
        //System.out.println("domain-posts-search패키지 PostsSearchImpl클래스 오버라이딩 searchReplyAllModifiedDate() 진입");

        QPostReply qp = QPostReply.postReply;
        JPQLQuery<PostReply> jpp = from(qp);

        //LocalDate 값이 존재할때 조건에 추가했다.
        if( (types!= null && types.length >0 ) && keyword !=null&& localDate !=null){

            BooleanBuilder bb = new BooleanBuilder();
            for(String type: types){

                switch(type){
                    case"n":
                        bb.or(qp.pno.stringValue().contains(keyword));
                        break;
                    case"c":
                        bb.or(qp.comment.contains(keyword));
                        break;
                    case"w":
                        bb.or(qp.member.name.contains(keyword));
                        break;
                }
            }//for
            jpp.where(bb);
        }//if
        //날짜를 where조건에 추가
        jpp.where(qp.prno.gt(0L)
                .and(qp.modifiedDate.between(localDate.atTime(LocalTime.MIN), localDate.atTime(LocalTime.MAX)))
                );

        //페이징처리
        this.getQuerydsl().applyPagination(pageable, jpp);

        //System.out.println("JPQLQuery<PostReply> -> "+ jpp);
        List<PostReply> list= jpp.fetch();
        long count = jpp.fetchCount();

        return new PageImpl<>(list, pageable, count);

    }


    @Override
    public Page<PostReply> searchReplyAllCreatedDate(String[] types, String keyword, Pageable pageable, LocalDate localDate) {
        //System.out.println("domain-posts-search패키지 PostsSearchImpl클래스 오버라이딩 searchReplyAllCreatedDate() 진입");

        QPostReply qp = QPostReply.postReply;
        JPQLQuery<PostReply> jpp = from(qp);

        //LocalDate 값이 존재할때 조건에 추가했다.
        if( (types!= null && types.length >0 ) && keyword !=null&& localDate !=null){

            BooleanBuilder bb = new BooleanBuilder();
            for(String type: types){

                switch(type){
                    case"n":
                        bb.or(qp.pno.stringValue().contains(keyword));
                        break;
                    case"c":
                        bb.or(qp.comment.contains(keyword));
                        break;
                    case"w":
                        bb.or(qp.member.name.contains(keyword));
                        break;
                }
            }//for
            jpp.where(bb);
        }//if
        //날짜를 where조건에 추가
        jpp.where(qp.prno.gt(0L)
                .and(qp.createdDate.between(localDate.atTime(LocalTime.MIN), localDate.atTime(LocalTime.MAX)))
        );

        //페이징처리
        this.getQuerydsl().applyPagination(pageable, jpp);

        //System.out.println("JPQLQuery<PostReply> -> "+ jpp);
        List<PostReply> list= jpp.fetch();
        long count = jpp.fetchCount();

        return new PageImpl<>(list, pageable, count);

    }


    @Override
    public Page<PostReply> searchReplyAll(String[] types, String keyword, Pageable pageable) {

        //System.out.println("domain-posts-search패키지 PostsSearchImpl클래스 오버라이딩 searchReplyAll() 진입");

        QPostReply qp = QPostReply.postReply;
        JPQLQuery<PostReply> jpp = from(qp);

        if( (types!= null && types.length >0 ) && keyword !=null){

            BooleanBuilder bb = new BooleanBuilder();
            for(String type: types){

                switch(type){
                    case"n":
                        bb.or(qp.pno.stringValue().contains(keyword));
                        break;
                    case"c":
                        bb.or(qp.comment.contains(keyword));
                        break;
                    case"w":
                        bb.or(qp.member.name.contains(keyword));
                        break;
                }
            }//for
            jpp.where(bb);
        }//if

        jpp.where(qp.prno.gt(0L));

        //페이징처리
        this.getQuerydsl().applyPagination(pageable, jpp);

        List<PostReply> list= jpp.fetch();
        long count = jpp.fetchCount();

        return new PageImpl<>(list, pageable, count);
    }


}
