package de.bambussoft.vaadinCRUD.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class Reflects {

    public static List<Field> getNonStaticFields(Class<?> c) {
        List<Field> fields = new ArrayList<>();
        for (Field f : c.getDeclaredFields()) {
            f.setAccessible(true);
            if (Modifier.isStatic(f.getModifiers())) {
                continue;
            }
            fields.add(f);
        }
        return fields;
    }

}
