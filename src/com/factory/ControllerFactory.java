package com.factory;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import com.controller.Controller;

public class ControllerFactory {

    private static final Map<String, String> controllerRegistry = new HashMap<>();

    static {
        controllerRegistry.put("Admin", "com.controller.AdminController");
        controllerRegistry.put("Employee", "com.controller.EmployeeController");
        controllerRegistry.put("Chef", "com.controller.ChefController");
    }

    public static Controller getInstance(String role) throws ClassNotFoundException, IOException, SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        Controller controller;
        String className = controllerRegistry.get(role);
        if(className == null || className.isEmpty()) {
            System.out.println("No controller found");
        }
        Class<?> dipatcherClass = Class.forName(className);
        Constructor<?> constructor = dipatcherClass.getDeclaredConstructor();
        constructor.setAccessible(true);
        controller = (Controller) constructor.newInstance();
        return controller;
    }
}
