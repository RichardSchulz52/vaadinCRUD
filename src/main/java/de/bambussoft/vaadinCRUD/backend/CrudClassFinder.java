package de.bambussoft.vaadinCRUD.backend;

import java.util.HashMap;
import java.util.Map;

public class CrudClassFinder {

    static CrudClassFinder instance;

    private final Map<Class<?>, CrudService<?>> entityToService;

    public static CrudClassFinder getInstance() {
        if (instance == null) {
            instance = new CrudClassFinder();
            return instance;
        }
        return instance;
    }

    private CrudClassFinder() {
        entityToService = new HashMap<>();
    }

    public void register(Class<?> c, CrudService<?> service) {
        entityToService.put(c, service);
    }

    public CrudService<?> getService(Class<?> c) {
        return entityToService.get(c);
    }

}
