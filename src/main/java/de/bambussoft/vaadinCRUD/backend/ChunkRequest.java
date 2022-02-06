package de.bambussoft.vaadinCRUD.backend;

import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ChunkRequest implements Pageable {

    private final int limit;
    private final int offset;
    private final Sort sort;

    public ChunkRequest(int skip, int limit) {
        this(skip, limit, Collections.emptyList());
    }

    public ChunkRequest(int offset, int limit, List<QuerySortOrder> sortOrders) {
        if (limit < 0)
            throw new IllegalArgumentException("Limit must not be less than zero!");

        if (offset < 0)
            throw new IllegalArgumentException("Offset must not be less than zero!");

        this.limit = limit;
        this.offset = offset;
        this.sort = buildSpringSort(sortOrders);
    }

    @Override
    public int getPageNumber() {
        return 0;
    }

    @Override
    public int getPageSize() {
        return limit;
    }

    @Override
    public long getOffset() {
        return offset;
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public Pageable next() {
        return null;
    }

    @Override
    public Pageable previousOrFirst() {
        return this;
    }

    @Override
    public Pageable first() {
        return this;
    }

    @Override
    public boolean hasPrevious() {
        return false;
    }

    private Sort buildSpringSort(List<QuerySortOrder> sortOrders) {
        if (sortOrders.isEmpty()) {
            return Sort.unsorted();
        } else {
            List<Sort.Order> collect = sortOrders.stream().map(o -> {
                if (o.getDirection() == SortDirection.ASCENDING) {
                    return Sort.Order.asc(o.getSorted());
                } else {
                    return Sort.Order.desc(o.getSorted());
                }
            }).collect(Collectors.toList());
            return Sort.by(collect);
        }
    }
}