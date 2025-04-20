package org.example.springboot231026.domain.inquiry.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.example.springboot231026.domain.inquiry.Inquiry;
import org.example.springboot231026.domain.inquiry.QInquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class InquirySearchImpl extends QuerydslRepositorySupport implements InquirySearch {

    public InquirySearchImpl() {
        super(Inquiry.class);
        //System.out.println("inquiry-search패키지 InquirySearchImpl클래스 생성자진입");
    }

    //관리자페이지에서 날짜조건에 맞는 문의글 페이징처리와 동적검색 모두처리
    @Override
    public Page<Inquiry> searchInquiryAllModifiedDate(String[] types, String keyword, Pageable pageable, LocalDate localDate) {
        //System.out.println("inquiry-search패키지 InquirySearchImpl클래스 searchInquiryAllModifiedDate() 진입");

            QInquiry qp = QInquiry.inquiry;
            JPQLQuery<Inquiry> jpp = from(qp);

            //LocalDate 값이 존재할때 조건에 추가했다.
            if( (types!= null && types.length >0 ) && keyword !=null&& localDate !=null){

                BooleanBuilder bb = new BooleanBuilder();
                for(String type: types){

                    switch(type){
                        case"a":
                            bb.or(qp.complete.contains(keyword));
                            break;
                        case"c":
                            bb.or(qp.content.contains(keyword));
                            break;
                        case"w":
                            bb.or(qp.writer.contains(keyword));
                            break;
                    }
                }//for
                jpp.where(bb);
            }//if
            //날짜를 where조건에 추가
            jpp.where(qp.innum.gt(0L).and(qp.modifiedDate.between(localDate.atTime(LocalTime.MIN), localDate.atTime(LocalTime.MAX))));

            //페이징처리
            this.getQuerydsl().applyPagination(pageable, jpp);

            //System.out.println("Querydsl<Inquiry> -> "+jpp);
            List<Inquiry> list= jpp.fetch();
            //System.out.println(list.toString());
            long count = jpp.fetchCount();

            return new PageImpl<>(list, pageable, count);
        }

    @Override
    public Page<Inquiry> searchInquiryAllCreatedDate(String[] types, String keyword, Pageable pageable, LocalDate localDate) {
        //System.out.println("inquiry-search패키지 InquirySearchImpl클래스 searchInquiryAllCreatedDate() 진입");

        QInquiry qp = QInquiry.inquiry;
        JPQLQuery<Inquiry> jpp = from(qp);

        //LocalDate 값이 존재할때 조건에 추가했다.
        if( (types!= null && types.length >0 ) && keyword !=null&& localDate !=null){

            BooleanBuilder bb = new BooleanBuilder();
            for(String type: types){

                switch(type){
                    case"a":
                        bb.or(qp.complete.contains(keyword));
                        break;
                    case"c":
                        bb.or(qp.content.contains(keyword));
                        break;
                    case"w":
                        bb.or(qp.writer.contains(keyword));
                        break;
                }
            }//for
            jpp.where(bb);
        }//if
        //날짜를 where조건에 추가
        jpp.where(qp.innum.gt(0L).and(qp.createdDate.between(localDate.atTime(LocalTime.MIN), localDate.atTime(LocalTime.MAX))));

        //페이징처리
        this.getQuerydsl().applyPagination(pageable, jpp);

        //System.out.println("Querydsl<Inquiry> -> "+jpp);
        List<Inquiry> list= jpp.fetch();
        //System.out.println(list.toString());
        long count = jpp.fetchCount();

        return new PageImpl<>(list, pageable, count);
    }




    //관리자페이지에서 문의글 페이징처리와 동적검색 모두처리
    @Override
    public Page<Inquiry> searchInquiryAll(String[] types, String keyword, Pageable pageable) {
        //System.out.println("inquiry-search패키지 InquirySearchImpl클래스 searchInquiryAll() 진입");

        QInquiry qp = QInquiry.inquiry;
        JPQLQuery<Inquiry> jpp = from(qp);

        if( (types!= null && types.length >0 ) && keyword !=null){

            BooleanBuilder bb = new BooleanBuilder();
            for(String type: types){

                switch(type){
                    case"a":
                        bb.or(qp.complete.contains(keyword));
                        break;
                    case"c":
                        bb.or(qp.content.contains(keyword));
                        break;
                    case"w":
                        bb.or(qp.writer.contains(keyword));
                        break;
                }
            }//for
            jpp.where(bb);
        }//if
        jpp.where(qp.innum.gt(0L));

        //페이징처리
        this.getQuerydsl().applyPagination(pageable, jpp);

        List<Inquiry> list= jpp.fetch();
        long count = jpp.fetchCount();

        return new PageImpl<>(list, pageable, count);
    }
}
