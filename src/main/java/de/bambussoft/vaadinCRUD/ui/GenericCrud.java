package de.bambussoft.vaadinCRUD.ui;

import de.bambussoft.vaadinCRUD.backend.EntityFilter;
import de.bambussoft.vaadinCRUD.backend.CrudService;
import de.bambussoft.vaadinCRUD.backend.superclasses.AbstractEntity;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.CallbackDataProvider;

import java.lang.reflect.InvocationTargetException;

public class GenericCrud<E extends AbstractEntity> extends VerticalLayout {

    protected final EntityFilter filter = new EntityFilter();
    protected final Grid<E> grid;
    protected GenericDetailsForm<E> details;

    public GenericCrud(Class<E> tClass, CrudService<E> crudService) {
        grid = new Grid<>(tClass);
        try {

            DetailsGenerator<E> formGenerator = new DetailsGenerator<>(tClass, crudService);
            details = formGenerator.generate();

            grid.getDataCommunicator().setDataProvider(getDataProvider(crudService), filter);
            grid.asSingleSelect().addValueChangeListener(event -> details.setEntity(event.getValue()));
            grid.removeColumnByKey("persisted"); // TODO make list configurable trough builder
            grid.removeColumnByKey("deleted");
            grid.removeColumnByKey("displayString");
            details.gridRefresh(() -> grid.getDataProvider().refreshAll());

            GenericFilter<E> genericFilter = new GenericFilter<>(tClass);
            genericFilter.setSearchCallback(() -> {
                filter.set(genericFilter.getEntityFilter());
                grid.getDataProvider().refreshAll();
            });

            add(details, genericFilter, grid);
        } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException | InstantiationException e) {
            System.out.println(e);
            add(new TextField("Error"));
        }
    }

    private CallbackDataProvider<E, EntityFilter> getDataProvider(CrudService<E> crudService) {
        return new CallbackDataProvider<>(crudService, crudService);
    }
}
