package org.example.springboot231026.domain.inquiry;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QInquiry is a Querydsl query type for Inquiry
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QInquiry extends EntityPathBase<Inquiry> {

    private static final long serialVersionUID = 1742328947L;

    public static final QInquiry inquiry = new QInquiry("inquiry");

    public final org.example.springboot231026.domain.posts.QBaseTimeEntity _super = new org.example.springboot231026.domain.posts.QBaseTimeEntity(this);

    public final StringPath complete = createString("complete");

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath email = createString("email");

    public final NumberPath<Long> innum = createNumber("innum", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final StringPath phone = createString("phone");

    public final StringPath writer = createString("writer");

    public QInquiry(String variable) {
        super(Inquiry.class, forVariable(variable));
    }

    public QInquiry(Path<? extends Inquiry> path) {
        super(path.getType(), path.getMetadata());
    }

    public QInquiry(PathMetadata metadata) {
        super(Inquiry.class, metadata);
    }

}

