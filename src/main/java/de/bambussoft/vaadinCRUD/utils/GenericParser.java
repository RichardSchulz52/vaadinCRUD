package de.bambussoft.vaadinCRUD.utils;

import de.bambussoft.vaadinCRUD.backend.FieldType;

public class GenericParser {

    static Object parse(Object value, FieldType type) {
        if (value == null) {
            return null;
        }
        switch (type) {
            case INT:
                return Integer.parseInt(value.toString());
            case BOOL:
                return Boolean.parseBoolean(value.toString());
            case LONG:
                if (value instanceof Double) {
                    return Math.round((Double) value);
                }
                return Long.parseLong(value.toString());
            case STRING:
                return value.toString();
        }
        throw new RuntimeException("not supported");
    }
}
