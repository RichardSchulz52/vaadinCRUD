package de.bambussoft.vaadinCRUD.backend;

public enum FilterBool {
    ANY,
    TRUE,
    FALSE;

    public Boolean get() {
        switch (this) {
            case ANY:
                return null;
            case TRUE:
                return true;
            case FALSE:
                return false;
        }
        return null;
    }
}
