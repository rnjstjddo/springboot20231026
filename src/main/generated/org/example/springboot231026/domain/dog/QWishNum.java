package org.example.springboot231026.domain.dog;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWishNum is a Querydsl query type for WishNum
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QWishNum extends EntityPathBase<WishNum> {

    private static final long serialVersionUID = -411213824L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWishNum wishNum = new QWishNum("wishNum");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final org.example.springboot231026.domain.member.QMember member;

    public final NumberPath<Long> wishnum = createNumber("wishnum", Long.class);

    public QWishNum(String variable) {
        this(WishNum.class, forVariable(variable), INITS);
    }

    public QWishNum(Path<? extends WishNum> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWishNum(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWishNum(PathMetadata metadata, PathInits inits) {
        this(WishNum.class, metadata, inits);
    }

    public QWishNum(Class<? extends WishNum> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new org.example.springboot231026.domain.member.QMember(forProperty("member")) : null;
    }

}

