package de.bambussoft.vaadinCRUD.backend;

import java.util.HashMap;
import java.util.Map;

public class EntityFilter {
    Map<String, Object> fieldNameToCompareValue;
    Map<String, FieldType> fieldNameToType;

    public EntityFilter() {
        fieldNameToCompareValue = new HashMap<>();
    }

    public void set(EntityFilter entityFilter) {
        this.fieldNameToCompareValue = entityFilter.fieldNameToCompareValue;
        this.fieldNameToType = entityFilter.fieldNameToType;
    }

    public Object get(String name) {
        return fieldNameToCompareValue.get(name);
    }

    public void put(String key, Object value) {
        fieldNameToCompareValue.put(key, value);
    }

    public void setFieldTypes(Map<String, FieldType> fieldNameToType) {
        this.fieldNameToType = fieldNameToType;
    }
}
