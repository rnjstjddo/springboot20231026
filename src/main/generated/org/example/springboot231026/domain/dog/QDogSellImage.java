package org.example.springboot231026.domain.dog;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDogSellImage is a Querydsl query type for DogSellImage
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QDogSellImage extends EntityPathBase<DogSellImage> {

    private static final long serialVersionUID = -643631796L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDogSellImage dogSellImage = new QDogSellImage("dogSellImage");

    public final QDogSell dogsell;

    public final StringPath imgName = createString("imgName");

    public final NumberPath<Long> inum = createNumber("inum", Long.class);

    public final StringPath path = createString("path");

    public final StringPath uuid = createString("uuid");

    public QDogSellImage(String variable) {
        this(DogSellImage.class, forVariable(variable), INITS);
    }

    public QDogSellImage(Path<? extends DogSellImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDogSellImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDogSellImage(PathMetadata metadata, PathInits inits) {
        this(DogSellImage.class, metadata, inits);
    }

    public QDogSellImage(Class<? extends DogSellImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.dogsell = inits.isInitialized("dogsell") ? new QDogSell(forProperty("dogsell")) : null;
    }

}

