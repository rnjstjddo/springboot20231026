package org.example.springboot231026.domain.inquiry;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QInquiryReply is a Querydsl query type for InquiryReply
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QInquiryReply extends EntityPathBase<InquiryReply> {

    private static final long serialVersionUID = 605756983L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QInquiryReply inquiryReply = new QInquiryReply("inquiryReply");

    public final org.example.springboot231026.domain.posts.QBaseTimeEntity _super = new org.example.springboot231026.domain.posts.QBaseTimeEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final QInquiry inquiry;

    public final NumberPath<Long> inrenum = createNumber("inrenum", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public QInquiryReply(String variable) {
        this(InquiryReply.class, forVariable(variable), INITS);
    }

    public QInquiryReply(Path<? extends InquiryReply> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QInquiryReply(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QInquiryReply(PathMetadata metadata, PathInits inits) {
        this(InquiryReply.class, metadata, inits);
    }

    public QInquiryReply(Class<? extends InquiryReply> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.inquiry = inits.isInitialized("inquiry") ? new QInquiry(forProperty("inquiry")) : null;
    }

}

