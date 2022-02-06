package de.bambussoft.vaadinCRUD.utils;


import com.vaadin.flow.component.notification.Notification;

public class ResponseHandler {

    public static final int DURATION = 5000;

    public static void showAsNotification(ServiceResponse serviceResponse) {
        if (serviceResponse.getAction() == ServiceResponse.Action.ERROR) {
            Notification.show("Error: " + serviceResponse.getMessage(), DURATION, Notification.Position.BOTTOM_STRETCH);
        } else {
            String message = serviceResponse.getMessage() != null ? " " + serviceResponse.getMessage() : "";
            Notification.show(serviceResponse.getAction() + message, DURATION, Notification.Position.BOTTOM_STRETCH);
        }
    }
}
