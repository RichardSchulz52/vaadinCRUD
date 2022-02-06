package de.bambussoft.vaadinCRUD.utils;

import de.bambussoft.vaadinCRUD.backend.FieldType;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

public class GenericConverter implements Converter<Object, Object> {


    private final FieldType modelType;

    public GenericConverter(FieldType modelType) {
        this.modelType = modelType;
    }

    @Override
    public Result<Object> convertToModel(Object value, ValueContext context) {
        Object modelObject = GenericParser.parse(value, modelType);
        return Result.of(() -> modelObject, e -> "utils.GenericConverter to model failed with: " + e);
    }

    @Override
    public Object convertToPresentation(Object value, ValueContext context) {
        if (value == null) {
            return null;
        }
        Component component = context.getComponent().orElseThrow();
        if (component instanceof TextField) {
            return value.toString();
        } else if (component instanceof NumberField) {
            return Double.parseDouble(value.toString());
        }
        throw new RuntimeException("No generic converter for component: " + component.getClass().getSimpleName());
    }
}
