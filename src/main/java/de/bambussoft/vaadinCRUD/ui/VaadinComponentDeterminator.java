package de.bambussoft.vaadinCRUD.ui;

import de.bambussoft.vaadinCRUD.backend.CrudClassFinder;
import de.bambussoft.vaadinCRUD.backend.CrudService;
import de.bambussoft.vaadinCRUD.backend.FieldType;
import de.bambussoft.vaadinCRUD.backend.FilterBool;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;

import java.lang.reflect.InvocationTargetException;

public class VaadinComponentDeterminator {

    public static AbstractField getValueField(FieldType f, boolean filter) throws NoSuchFieldException, IllegalAccessException {
        String name = f.getField().getName();
        switch (f) {
            case INT:
            case LONG:
                return new NumberField(name);
            case BOOL:
                if (filter) {
                    ComboBox<FilterBool> filterBoolComboBox = new ComboBox<>(name, FilterBool.values());
                    filterBoolComboBox.setValue(FilterBool.ANY);
                    filterBoolComboBox.setAllowCustomValue(false);
                    return filterBoolComboBox;
                } else {
                    return new Checkbox(name);
                }
            case STRING:
                return new TextField(name);
            case ENUM:
                return new ComboBox(name, f.getEnumValues());
            case ENTITY:
                ComboBox ref = new ComboBox(name);
                CrudClassFinder finder = CrudClassFinder.getInstance();
                Class<?> returnClass = f.getField().getType();
                CrudService service = finder.getService(returnClass);
                ref.setItemLabelGenerator(e -> {
                    try {
                        return (String) returnClass.getMethod("getDisplayString").invoke(e);
                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                        ex.printStackTrace();
                        return "error";
                    }
                });
                ref.setDataProvider(service::findForComboBox, s -> service.countForComboBox((String) s));
                return ref;
        }
        return null;
    }
}
