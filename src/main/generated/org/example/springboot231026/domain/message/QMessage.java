package org.example.springboot231026.domain.message;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMessage is a Querydsl query type for Message
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QMessage extends EntityPathBase<Message> {

    private static final long serialVersionUID = -1261403917L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMessage message = new QMessage("message");

    public final org.example.springboot231026.domain.posts.QBaseTimeEntity _super = new org.example.springboot231026.domain.posts.QBaseTimeEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath email = createString("email");

    public final NumberPath<Long> messageid = createNumber("messageid", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final StringPath name = createString("name");

    public final StringPath number = createString("number");

    public final org.example.springboot231026.domain.member.QMember recipient;

    public final org.example.springboot231026.domain.member.QMember sender;

    public QMessage(String variable) {
        this(Message.class, forVariable(variable), INITS);
    }

    public QMessage(Path<? extends Message> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMessage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMessage(PathMetadata metadata, PathInits inits) {
        this(Message.class, metadata, inits);
    }

    public QMessage(Class<? extends Message> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.recipient = inits.isInitialized("recipient") ? new org.example.springboot231026.domain.member.QMember(forProperty("recipient")) : null;
        this.sender = inits.isInitialized("sender") ? new org.example.springboot231026.domain.member.QMember(forProperty("sender")) : null;
    }

}

