package de.bambussoft.vaadinCRUD.ui;

import de.bambussoft.vaadinCRUD.backend.CrudService;
import de.bambussoft.vaadinCRUD.backend.superclasses.AbstractEntity;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import de.bambussoft.vaadinCRUD.utils.ResponseHandler;
import de.bambussoft.vaadinCRUD.utils.ServiceResponse;

import java.lang.reflect.InvocationTargetException;

public class GenericDetailsForm<C extends AbstractEntity> extends FormLayout {

    public static final String SAVE_TEXT = "Speichern";
    public static final String SAVE_NEW_TEXT = "Speichern (neu)";

    private AbstractEntity entity;
    private Binder<AbstractEntity> binder;
    private CrudService<AbstractEntity> service;
    private Class<C> tClass;

    private Button save;
    private Button delete;
    private Runnable refresh;

    void addButtons() {
        save = new Button(SAVE_NEW_TEXT);
        delete = new Button("Löschen");
        Button newEntity = new Button("Neu");

        save.addClickListener(c -> validateAndSave());
        delete.addClickListener(c -> delete());
        newEntity.addClickListener(c -> setEmptyEntity());

        add(newEntity, save, delete);
    }

    public void setEntity(AbstractEntity entity) {
        this.entity = entity;
        String saveText = entity.isPersisted() ? SAVE_TEXT : SAVE_NEW_TEXT;
        save.setText(saveText);
        delete.setEnabled(entity.isPersisted());
        binder.setBean(entity);
    }

    public AbstractEntity getEntity() {
        return entity;
    }

    private void delete() {
        if (entity.isPersisted()) {
            ServiceResponse response = service.deleteEntity(entity);
            ResponseHandler.showAsNotification(response);
            setEmptyEntity();
            refresh.run();
        }
    }

    private void validateAndSave() {
        try {
            binder.writeBean(entity);
            ServiceResponse response = service.saveEntity(entity);
            ResponseHandler.showAsNotification(response);
            setEntity(response.getEntity());
            refresh.run();
        } catch (ValidationException ignored) {
        }
    }

    void setBinder(Binder binder) {
        this.binder = binder;
    }

    void setClass(Class<C> tClass) {
        this.tClass = tClass;
    }

    public void setCrudService(CrudService crudService) {
        this.service = crudService;
    }

    private void setEmptyEntity() {
        try {
            setEntity(tClass.getConstructor().newInstance());
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void gridRefresh(Runnable refresh) {
        this.refresh = refresh;
    }
}
