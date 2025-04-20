package org.example.springboot231026.domain.guestbook;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QGuestbook is a Querydsl query type for Guestbook
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QGuestbook extends EntityPathBase<Guestbook> {

    private static final long serialVersionUID = -1358082521L;

    public static final QGuestbook guestbook = new QGuestbook("guestbook");

    public final org.example.springboot231026.domain.posts.QBaseTimeEntity _super = new org.example.springboot231026.domain.posts.QBaseTimeEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> gno = createNumber("gno", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final StringPath title = createString("title");

    public final StringPath writer = createString("writer");

    public QGuestbook(String variable) {
        super(Guestbook.class, forVariable(variable));
    }

    public QGuestbook(Path<? extends Guestbook> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGuestbook(PathMetadata metadata) {
        super(Guestbook.class, metadata);
    }

}

