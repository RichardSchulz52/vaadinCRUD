package de.bambussoft.vaadinCRUD.backend;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EntityPathBase;

import java.util.Map;

public class FilterQueryGenerator<E> {

    private final PredicateDeterminator<E> predicateDeterminator;

    public FilterQueryGenerator(EntityPathBase<E> entityBase) {
        this.predicateDeterminator = new PredicateDeterminator<>(entityBase);
    }

    public BooleanExpression generate(EntityFilter filter) throws NoSuchFieldException, IllegalAccessException {
        BooleanExpression wholeExpression = null;
        for (Map.Entry<String, Object> e : filter.fieldNameToCompareValue.entrySet()) {
            FieldType fieldType = filter.fieldNameToType.get(e.getKey());
            BooleanExpression expression = predicateDeterminator.determine(e.getKey(), e.getValue(), fieldType);
            if (expression != null) {
                if (wholeExpression == null) {
                    wholeExpression = expression;
                } else {
                    wholeExpression = wholeExpression.and(expression);
                }
            }
        }
        return wholeExpression;
    }

    public BooleanExpression getTrueStatement() throws NoSuchFieldException, IllegalAccessException {
        return predicateDeterminator.isSet("id");
    }
}
