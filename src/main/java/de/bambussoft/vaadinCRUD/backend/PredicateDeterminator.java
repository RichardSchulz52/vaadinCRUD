package de.bambussoft.vaadinCRUD.backend;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.StringExpression;

public class PredicateDeterminator<E> {

    private final EntityPathBase<E> entityPathBase;

    public PredicateDeterminator(EntityPathBase<E> entityPathBase) {
        this.entityPathBase = entityPathBase;
    }

    public BooleanExpression determine(String name, Object val, FieldType fieldType) throws IllegalAccessException, NoSuchFieldException {
        SimpleExpression expr = getExpression(name);
        switch (fieldType) {
            case STRING:
                StringExpression sexpr = (StringExpression) expr;
                return sexpr.startsWith((String) val);
        }
        return expr.eq(val);
    }

    public BooleanExpression isSet(String fieldName) throws NoSuchFieldException, IllegalAccessException {
        return getExpression(fieldName).isNotNull();
    }

    private SimpleExpression<?> getExpression(String fieldName) throws NoSuchFieldException, IllegalAccessException {
        return (SimpleExpression<?>) entityPathBase.getClass().getField(fieldName).get(entityPathBase);
    }
}
