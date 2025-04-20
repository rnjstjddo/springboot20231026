package org.example.springboot231026.domain.dog;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDogSellReply is a Querydsl query type for DogSellReply
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QDogSellReply extends EntityPathBase<DogSellReply> {

    private static final long serialVersionUID = -635543845L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDogSellReply dogSellReply = new QDogSellReply("dogSellReply");

    public final org.example.springboot231026.domain.posts.QBaseTimeEntity _super = new org.example.springboot231026.domain.posts.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final QDogSell dogsell;

    public final NumberPath<Long> drno = createNumber("drno", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final StringPath replyer = createString("replyer");

    public final StringPath text = createString("text");

    public QDogSellReply(String variable) {
        this(DogSellReply.class, forVariable(variable), INITS);
    }

    public QDogSellReply(Path<? extends DogSellReply> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDogSellReply(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDogSellReply(PathMetadata metadata, PathInits inits) {
        this(DogSellReply.class, metadata, inits);
    }

    public QDogSellReply(Class<? extends DogSellReply> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.dogsell = inits.isInitialized("dogsell") ? new QDogSell(forProperty("dogsell")) : null;
    }

}

