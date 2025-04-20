package org.example.springboot231026.domain.dog.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.example.springboot231026.domain.dog.DogSell;
import org.example.springboot231026.domain.dog.QDogSell;
import org.example.springboot231026.domain.inquiry.Inquiry;
import org.example.springboot231026.domain.inquiry.QInquiry;
import org.hibernate.validator.internal.engine.groups.ValidationOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class DogSellSearchImpl extends QuerydslRepositorySupport implements DogSellSearch {

    public DogSellSearchImpl() {
        super(DogSell.class);
        //System.out.println("dogsell-search패키지 DogSellSearchImpl클래스 생성자진입");
    }

    //관리자페이지에서 날짜조건에 맞는 문의글 페이징처리와 동적검색 모두처리
    @Override
    public Page<DogSell> searchDogSellAllModifiedDate(String[] types, String keyword, Pageable pageable, LocalDate localDate) {
        //System.out.println("dogsell-search패키지 DogSellSearchImpl클래스 searchInquiryAllModifiedDate() 진입");

        QDogSell ds = QDogSell.dogSell;
        JPQLQuery<DogSell> jpp = from(ds);

        //LocalDate 값이 존재할때 조건에 추가했다.
            if( (types!= null && types.length >0 ) && keyword !=null&& localDate !=null){

                BooleanBuilder bb = new BooleanBuilder();
                for(String type: types){

                    switch(type){
                        case"c":
                            bb.or(ds.complete.contains(keyword));
                            break;
                        case"w":
                            bb.or(ds.writer.contains(keyword));
                            break;
                        case"t":
                            bb.or(ds.type.contains(keyword));
                            break;
                    }
                }//for
                jpp.where(bb);
            }//if
            //날짜를 where조건에 추가
            jpp.where(ds.dno.gt(0L).and(ds.modifiedDate.between(localDate.atTime(LocalTime.MIN), localDate.atTime(LocalTime.MAX))));

            //페이징처리
            this.getQuerydsl().applyPagination(pageable, jpp);

            //System.out.println("Querydsl<DogSell> -> "+jpp);
            List<DogSell> list= jpp.fetch();
            //System.out.println(list.toString());
            long count = jpp.fetchCount();

            return new PageImpl<>(list, pageable, count);
        }

    @Override
    public Page<DogSell> searchDogSellAllCreatedDate(String[] types, String keyword, Pageable pageable, LocalDate localDate) {
        //System.out.println("dogsell-search패키지 DogSellSearchImpl클래스 searchInquiryAllCreatedDate() 진입");

        QDogSell ds = QDogSell.dogSell;
        JPQLQuery<DogSell> jpp = from(ds);


        //LocalDate 값이 존재할때 조건에 추가했다.
        if( (types!= null && types.length >0 ) && keyword !=null&& localDate !=null){

            BooleanBuilder bb = new BooleanBuilder();
            for(String type: types){

                switch(type){
                    case"c":
                        bb.or(ds.complete.contains(keyword));
                        break;
                    case"w":
                        bb.or(ds.writer.contains(keyword));
                        break;
                    case"t":
                        bb.or(ds.type.contains(keyword));
                        break;
                }
            }//for
            jpp.where(bb);
        }//if
        //날짜를 where조건에 추가
        jpp.where(ds.dno.gt(0L).and(ds.createdDate.between(localDate.atTime(LocalTime.MIN), localDate.atTime(LocalTime.MAX))));

        //페이징처리
        this.getQuerydsl().applyPagination(pageable, jpp);

        //System.out.println("Querydsl<DogSell> -> "+jpp);
        List<DogSell> list= jpp.fetch();
        //System.out.println(list.toString());
        long count = jpp.fetchCount();

        return new PageImpl<>(list, pageable, count);
    }




    //관리자페이지에서 분양글 페이징처리와 동적검색 모두처리
    @Override
    public Page<DogSell> searchDogSellAll(String[] types, String keyword, Pageable pageable) {
        //System.out.println("dogsell-search패키지 DogSellSearchImpl클래스 searchInquiryAll() 진입");

        QDogSell ds = QDogSell.dogSell;
        JPQLQuery<DogSell> jpp = from(ds);

        if( (types!= null && types.length >0 ) && keyword !=null){

            BooleanBuilder bb = new BooleanBuilder();
            for(String type: types){

                switch(type){
                    case"c":
                        bb.or(ds.complete.contains(keyword));
                        break;
                    case"w":
                        bb.or(ds.writer.contains(keyword));
                        break;
                    case"t":
                        bb.or(ds.type.contains(keyword));
                        break;
                }
            }//for
            jpp.where(bb);
        }//if
        jpp.where(ds.dno.gt(0L));

        //페이징처리
        this.getQuerydsl().applyPagination(pageable, jpp);

        List<DogSell> list= jpp.fetch();
        long count = jpp.fetchCount();

        return new PageImpl<>(list, pageable, count);
    }
}
