package de.bambussoft.vaadinCRUD.ui;

import de.bambussoft.vaadinCRUD.backend.EntityFilter;
import de.bambussoft.vaadinCRUD.backend.FieldType;
import de.bambussoft.vaadinCRUD.backend.FilterBool;
import de.bambussoft.vaadinCRUD.utils.Reflects;
import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class GenericFilter<E> extends HorizontalLayout {

    private final Map<String, AbstractField> valueFields;
    private final Map<String, FieldType> fieldNameToType;

    private final Button search = new Button("Suche");

    public GenericFilter(Class<E> tClass) throws NoSuchFieldException, IllegalAccessException {
        valueFields = new HashMap<>();
        fieldNameToType = new HashMap<>();
        for (Field f : Reflects.getNonStaticFields(tClass)) {
            FieldType type = FieldType.determine(tClass, f);
            String fieldName = type.getField().getName();
            fieldNameToType.put(fieldName, type);
            AbstractField valueField = VaadinComponentDeterminator.getValueField(type, true);
            valueFields.put(fieldName, valueField);
            add(valueField);
        }
        add(search);
    }

    void setSearchCallback(Runnable callback) {
        search.addClickListener(e -> callback.run());
    }

    EntityFilter getEntityFilter() {
        EntityFilter entityFilter = new EntityFilter();
        valueFields.forEach((key, value) -> {
            if (isSet(value.getValue())) {
                if (fieldNameToType.get(key) == FieldType.BOOL) {
                    Boolean compare = ((FilterBool) value.getValue()).get();
                    if (compare != null) {
                        entityFilter.put(key, compare);
                    }
                } else {
                    entityFilter.put(key, value.getValue());
                }
            }
        });
        entityFilter.setFieldTypes(fieldNameToType);
        return entityFilter;
    }

    private boolean isSet(Object value) {
        return !(value == null || value instanceof String && ((String) value).isBlank());
    }


}
