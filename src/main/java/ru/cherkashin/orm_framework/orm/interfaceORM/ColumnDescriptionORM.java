package ru.cherkashin.orm_framework.orm.interfaceORM;

import ru.cherkashin.orm_framework.orm.enumORM.ORMGenerateId;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.ANNOTATION_TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnDescriptionORM {
    String name();
    ORMGenerateId generatedId() default ORMGenerateId.NOT_AUTO;
}
