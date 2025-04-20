package org.example.springboot231026.domain.dog;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QDogSell is a Querydsl query type for DogSell
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QDogSell extends EntityPathBase<DogSell> {

    private static final long serialVersionUID = 66174287L;

    public static final QDogSell dogSell = new QDogSell("dogSell");

    public final org.example.springboot231026.domain.posts.QBaseTimeEntity _super = new org.example.springboot231026.domain.posts.QBaseTimeEntity(this);

    public final NumberPath<Double> age = createNumber("age", Double.class);

    public final StringPath complete = createString("complete");

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> dno = createNumber("dno", Long.class);

    public final StringPath gender = createString("gender");

    public final StringPath health = createString("health");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final StringPath name = createString("name");

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final StringPath title = createString("title");

    public final StringPath type = createString("type");

    public final NumberPath<Double> weight = createNumber("weight", Double.class);

    public final StringPath writer = createString("writer");

    public QDogSell(String variable) {
        super(DogSell.class, forVariable(variable));
    }

    public QDogSell(Path<? extends DogSell> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDogSell(PathMetadata metadata) {
        super(DogSell.class, metadata);
    }

}

