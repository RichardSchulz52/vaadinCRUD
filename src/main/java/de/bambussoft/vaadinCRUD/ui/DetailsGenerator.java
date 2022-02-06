package de.bambussoft.vaadinCRUD.ui;

import de.bambussoft.vaadinCRUD.backend.FieldType;
import de.bambussoft.vaadinCRUD.utils.GenericConverter;
import de.bambussoft.vaadinCRUD.utils.Reflects;
import de.bambussoft.vaadinCRUD.backend.CrudService;
import de.bambussoft.vaadinCRUD.backend.superclasses.AbstractEntity;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class DetailsGenerator<C extends AbstractEntity> {

    private final Class<C> tClass;
    private final Binder<C> binder;
    private final CrudService<C> crudService;
    private final GenericDetailsForm<C> form;

    public DetailsGenerator(Class<C> tClass, CrudService<C> crudService) {
        this.tClass = tClass;
        this.binder = new BeanValidationBinder<>(tClass);
        this.crudService = crudService;
        this.form = new GenericDetailsForm<>();
        this.form.setClass(tClass);

    }

    public GenericDetailsForm<C> generate() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        for (Field f : Reflects.getNonStaticFields(tClass)) {
            FieldType type = FieldType.determine(tClass, f);
            AbstractField valueField = VaadinComponentDeterminator.getValueField(type, false);
            if (type == FieldType.LONG) { // TODO irgendein problem existiert mit Long cannot be cast to Double. Der Binder war es wohl nicht
                binder.forField(valueField).withConverter(new GenericConverter(type)).bind(f.getName());
            } else {
                binder.bind(valueField, f.getName());
            }
            form.add(valueField);
        }
        form.addButtons();
        form.setBinder(binder);
        form.setCrudService(crudService);
        form.setEntity(tClass.getConstructor().newInstance());
        return form;
    }
}
