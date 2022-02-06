package de.bambussoft.vaadinCRUD.backend;

import javax.persistence.Entity;
import java.lang.reflect.Field;

public enum FieldType {
    INT,
    BOOL,
    LONG,
    STRING,
    ENUM,
    ENTITY;

    private Class<?> tClass;
    private Field field;

    public <E extends Enum> E[] getEnumValues()
            throws NoSuchFieldException, IllegalAccessException {
        Field f = field.getType().getDeclaredField("$VALUES");
        f.setAccessible(true);
        Object o = f.get(null);
        return (E[]) o;
    }

    public static FieldType determine(Class<?> tClass, Field f) {
        Class<?> fieldClass = f.getType();
        FieldType type = null;
        if (fieldClass.isPrimitive()) {
            type = getPrimitiveElement(f);
        } else if (f.getGenericType().getTypeName().startsWith("java.lang")) {
            type = getJavaLangElement(f);
        } else if (fieldClass.isEnum()) {
            type = ENUM;
        } else if (tClass.getAnnotation(Entity.class) != null) {
            type = ENTITY;
        }

        if (type != null) {
            type.tClass = tClass;
            type.field = f;
        }
        return type;

    }

    private static FieldType getJavaLangElement(Field field) {
        Class<?> fieldType = field.getType();
        if (fieldType == String.class) {
            return STRING;
        } else if (fieldType == Long.class) {
            return LONG;
        }
        return null;
    }

    private static FieldType getPrimitiveElement(Field f) {
        if (f.getGenericType().getTypeName().equals("int")) {
            return INT;
        } else if (f.getGenericType().getTypeName().equals("boolean")) {
            return BOOL;
        }
        return null;
    }

    public Field getField() {
        return field;
    }

    public Class<?> gettClass() {
        return tClass;
    }
}
