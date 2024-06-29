package com.controller;

import com.google.gson.Gson;
import com.model.MenuDTO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.InputMismatchException;

public class MenuManager {

    public static void addMenu(BufferedReader stdIn, PrintWriter out, BufferedReader in) throws IOException {
        try{
            MenuDTO menu = new MenuDTO();
            menu.setName(promptString(stdIn, "Enter Name: "));
            menu.setPrice(promptBigDecimal(stdIn, "Enter Price: "));
            menu.setAvailabilityStatus("Yes");
            menu.setMealType(promptString(stdIn, "Enter Meal Type: "));
            menu.setScore(new BigDecimal(0));

            Gson gson = new Gson();
            String json = gson.toJson(menu);
            out.println("ADD_MENU_REQUEST;" + json);
            System.out.println(in.readLine());
        }catch(InputMismatchException error){
            System.err.println("Input is invalid. Please Try again");
        }
    }

    public static void updateMenu(BufferedReader stdIn, PrintWriter out, BufferedReader in) throws IOException {
        try {
            MenuDTO menu = new MenuDTO();
            menu.setMenuId(promptBigDecimal(stdIn, "Enter Menu Id: "));
            menu.setName(promptString(stdIn, "Enter Name: "));
            menu.setPrice(promptBigDecimal(stdIn, "Enter Price: "));
            menu.setAvailabilityStatus(promptString(stdIn, "Enter Availability Status (Yes/No): "));
            menu.setMealType(promptString(stdIn, "Enter Meal Type: "));

            Gson gson = new Gson();
            String json = gson.toJson(menu);
            out.println("UPDATE_MENU_REQUEST;" + json);
            System.out.println(in.readLine());
        }catch(InputMismatchException error){
            System.err.println("Input is invalid. Please Try again");
        }
    }


    public static void deleteMenu(BufferedReader stdIn, PrintWriter out, BufferedReader in) throws IOException {
        try {
            String menuId = promptString(stdIn, "Enter Menu ID: ");
            out.println("DELETE_MENU_REQUEST;" + menuId);
            System.out.println(in.readLine());
        }catch(InputMismatchException error){
            System.err.println("Input is invalid. Please Try again");
        }
    }

    private static String promptString(BufferedReader stdIn, String prompt) throws IOException {
        System.out.print(prompt);
        return stdIn.readLine();
    }

    private static BigDecimal promptBigDecimal(BufferedReader stdIn, String prompt) throws IOException {
        System.out.print(prompt);
        return new BigDecimal(stdIn.readLine());
    }
}