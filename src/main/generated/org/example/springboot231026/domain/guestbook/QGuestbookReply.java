package org.example.springboot231026.domain.guestbook;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QGuestbookReply is a Querydsl query type for GuestbookReply
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QGuestbookReply extends EntityPathBase<GuestbookReply> {

    private static final long serialVersionUID = 307467523L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QGuestbookReply guestbookReply = new QGuestbookReply("guestbookReply");

    public final org.example.springboot231026.domain.posts.QBaseTimeEntity _super = new org.example.springboot231026.domain.posts.QBaseTimeEntity(this);

    public final StringPath comment = createString("comment");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> gno = createNumber("gno", Long.class);

    public final NumberPath<Long> grno = createNumber("grno", Long.class);

    public final org.example.springboot231026.domain.member.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public QGuestbookReply(String variable) {
        this(GuestbookReply.class, forVariable(variable), INITS);
    }

    public QGuestbookReply(Path<? extends GuestbookReply> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QGuestbookReply(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QGuestbookReply(PathMetadata metadata, PathInits inits) {
        this(GuestbookReply.class, metadata, inits);
    }

    public QGuestbookReply(Class<? extends GuestbookReply> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new org.example.springboot231026.domain.member.QMember(forProperty("member")) : null;
    }

}

