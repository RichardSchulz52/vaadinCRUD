package de.bambussoft.vaadinCRUD.backend;

import de.bambussoft.vaadinCRUD.backend.superclasses.AbstractEntity;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import de.bambussoft.vaadinCRUD.utils.ServiceResponse;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public abstract class CrudService<E extends AbstractEntity> implements CallbackDataProvider.FetchCallback<E, EntityFilter>, CallbackDataProvider.CountCallback<E, EntityFilter> {

    QuerydslPredicateExecutor<E> repository;

    protected abstract E write(E bean);

    protected abstract void delete(E bean);

    public abstract EntityPathBase<E> getEntityBasePath();

    public CrudService(QuerydslPredicateExecutor<E> repository) {
        this.repository = repository;
    }

    public ServiceResponse saveEntity(E bean) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Object>> violations = validator.validate(bean);

        if (violations.isEmpty()) {
            E saved = write(bean);
            return ServiceResponse.saved(saved, saved.getDisplayString());
        } else {
            return ServiceResponse.error(violations);
        }
    }

    public ServiceResponse deleteEntity(E bean) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Object>> violations = validator.validate(bean);

        if (violations.isEmpty()) {
            delete(bean);
            return ServiceResponse.deleted(bean);
        } else {
            return ServiceResponse.error(violations);
        }
    }

    @Override
    public int count(Query<E, EntityFilter> query) {
        Optional<EntityFilter> oFilter = query.getFilter();
        BooleanExpression predicate = getBooleanExpression(oFilter);
        return (int) repository.count(predicate);
    }

    @Override
    public Stream<E> fetch(Query<E, EntityFilter> query) {
        Optional<EntityFilter> oFilter = query.getFilter();
        BooleanExpression predicate = getBooleanExpression(oFilter);
        Pageable pageRequest = new ChunkRequest(query.getOffset(), query.getLimit(), query.getSortOrders());
        return StreamSupport.stream(repository.findAll(predicate, pageRequest).spliterator(), false);

    }

    private BooleanExpression getBooleanExpression(Optional<EntityFilter> oFilter) {
        BooleanExpression predicate = null;
        FilterQueryGenerator<E> fqg = new FilterQueryGenerator<>(getEntityBasePath());
        if (oFilter.isPresent()) {
            EntityFilter filter = oFilter.get();
            try {
                predicate = fqg.generate(filter);
                if (predicate == null) {
                    predicate = fqg.getTrueStatement();
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return predicate;
    }

    public abstract Stream<E> findForComboBox(String filter, int skip, int limit);

    public abstract Integer countForComboBox(String filter);
}
