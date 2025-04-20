package org.example.springboot231026.domain.member.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.example.springboot231026.domain.member.Member;
import org.example.springboot231026.domain.member.QMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class MemberSearchImpl extends QuerydslRepositorySupport implements MemberSearch{
    public MemberSearchImpl() {
        super(Member.class);

        //System.out.println("domain-member-search패키지 MemberSearchImpl 생성자진입");
    }

    @Override
    public Page<Member> searchMemberAllModifiedDate(String[] types, String keyword, Pageable pageable, LocalDate localDate) {
        //System.out.println("domain-member-search패키지 MemberSearchImpl 오버라이딩 searchMemberAllModifiedDate() 진입");
        //System.out.println("Pageable -> "+ pageable.toString());

        QMember qp = QMember.member;
        JPQLQuery<Member> jpp = from(qp);


        //LocalDate 값이 존재할때 조건에 추가했다.
        if( (types!= null && types.length >0 ) && keyword !=null&& localDate !=null){

            BooleanBuilder bb = new BooleanBuilder();
            for(String type: types){

                switch(type){
                    case"n":
                        bb.or(qp.name.contains(keyword));
                        break;
                    case"b":
                        bb.or(qp.fromSocial.contains(keyword));
                        break;
                    case"r":
                        bb.or(qp.role.stringValue().contains(keyword));
                        break;
                    case"e":
                        bb.or(qp.email.contains(keyword));
                        break;
                }
            }//for
            jpp.where(bb);
        }//if
        //날짜를 where조건에 추가
        jpp.where(qp.createdDate.between(localDate.atTime(LocalTime.MIN), localDate.atTime(LocalTime.MAX)));
        jpp.orderBy(qp.createdDate.desc());

        /*jpp.where(qp.email.isNotNull().and(qp.createdDate.between(localDate.atTime(LocalTime.MIN),
                localDate.atTime(LocalTime.MAX))));
        */
        //jpp.where(qp.email.isNotNull());

        //System.out.println("JPQLQuery<Member> -> "+ jpp);//JPQLQuery<Member>

        //페이징처리
        this.getQuerydsl().applyPagination(pageable, jpp);

        //System.out.println("JPQLQuery<Member> -> "+ jpp);//JPQLQuery<Member>

        List<Member> list= jpp.fetch();

        long count = jpp.fetchCount();

        return new PageImpl<>(list, pageable, count);

    }


    @Override
    public Page<Member> searchMemberAllCreatedDate(String[] types, String keyword, Pageable pageable, LocalDate localDate) {
        //System.out.println("domain-member-search패키지 MemberSearchImpl 오버라이딩 searchMemberAllCreatedDate() 진입");
        //System.out.println("Pageable -> "+ pageable.toString());

        QMember qp = QMember.member;
        JPQLQuery<Member> jpp = from(qp);


        //LocalDate 값이 존재할때 조건에 추가했다.
        if( (types!= null && types.length >0 ) && keyword !=null&& localDate !=null){

            BooleanBuilder bb = new BooleanBuilder();
            for(String type: types){

                switch(type){
                    case"n":
                        bb.or(qp.name.contains(keyword));
                        break;
                    case"b":
                        bb.or(qp.fromSocial.contains(keyword));
                        break;
                    case"r":
                        bb.or(qp.role.stringValue().contains(keyword));
                        break;
                    case"e":
                        bb.or(qp.email.contains(keyword));
                        break;
                }
            }//for
            jpp.where(bb);
        }//if
        //날짜를 where조건에 추가
        jpp.where(qp.createdDate.between(localDate.atTime(LocalTime.MIN), localDate.atTime(LocalTime.MAX)));
        jpp.orderBy(qp.createdDate.desc());

        /*jpp.where(qp.email.isNotNull().and(qp.createdDate.between(localDate.atTime(LocalTime.MIN),
                localDate.atTime(LocalTime.MAX))));
        */
        //jpp.where(qp.email.isNotNull());

        //System.out.println("JPQLQuery<Member> -> "+ jpp);//JPQLQuery<Member>

        //페이징처리
        this.getQuerydsl().applyPagination(pageable, jpp);

        //System.out.println("JPQLQuery<Member> -> "+ jpp);//JPQLQuery<Member>

        List<Member> list= jpp.fetch();

        long count = jpp.fetchCount();

        return new PageImpl<>(list, pageable, count);

    }



    @Override
    public Page<Member> searchMemberAll(String[] types, String keyword, Pageable pageable) {
        //System.out.println("domain-member-search패키지 MemberSearchImpl 오버라이딩 searchMemberAll() 진입");

        QMember qp = QMember.member;
        JPQLQuery<Member> jpp = from(qp);

        if( (types!= null && types.length >0 ) && keyword !=null){

            BooleanBuilder bb = new BooleanBuilder();
            for(String type: types){

                switch(type){
                    case"n":
                        bb.or(qp.name.contains(keyword));
                        break;
                    case"b":
                        bb.or(qp.fromSocial.contains(keyword));
                        break;
                    case"r":
                        bb.or(qp.role.stringValue().contains(keyword));
                        break;
                    case"e":
                        bb.or(qp.email.contains(keyword));
                        break;
                }
            }//for
            jpp.where(bb);
        }//if

        //Pageable대신 oderBy()사용
        jpp.orderBy(qp.createdDate.desc());

        //페이징처리
        this.getQuerydsl().applyPagination(pageable, jpp);

        List<Member> list= jpp.fetch();
        long count = jpp.fetchCount();

        return new PageImpl<>(list, pageable, count);

    }
}
