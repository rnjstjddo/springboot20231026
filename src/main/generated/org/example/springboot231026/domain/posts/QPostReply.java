package org.example.springboot231026.domain.posts;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPostReply is a Querydsl query type for PostReply
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QPostReply extends EntityPathBase<PostReply> {

    private static final long serialVersionUID = -654949982L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPostReply postReply = new QPostReply("postReply");

    public final QBaseTimeEntity _super = new QBaseTimeEntity(this);

    public final StringPath comment = createString("comment");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final org.example.springboot231026.domain.member.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final NumberPath<Long> pno = createNumber("pno", Long.class);

    public final QPosts posts;

    public final NumberPath<Long> prno = createNumber("prno", Long.class);

    public QPostReply(String variable) {
        this(PostReply.class, forVariable(variable), INITS);
    }

    public QPostReply(Path<? extends PostReply> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPostReply(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPostReply(PathMetadata metadata, PathInits inits) {
        this(PostReply.class, metadata, inits);
    }

    public QPostReply(Class<? extends PostReply> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new org.example.springboot231026.domain.member.QMember(forProperty("member")) : null;
        this.posts = inits.isInitialized("posts") ? new QPosts(forProperty("posts")) : null;
    }

}

