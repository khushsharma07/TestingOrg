package com.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class AdminController implements Controller {

    public void displayMenu(BufferedReader stdIn, PrintWriter out, BufferedReader in) throws IOException {
        while(true){
            System.out.println("Admin Menu:");
            System.out.println("1. View Food Menu");
            System.out.println("2. Add Item in Food Menu");
            System.out.println("3. Update Item in Food Menu");
            System.out.println("4. Delete Item in Food Menu");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            String choice = stdIn.readLine();

            switch (choice) {
                case "1":
                    out.println("VIEW_MENU_REQUEST");
                    String viewResponse = in.readLine();
                    System.out.println(viewResponse);
                    break;
                case "2":
                    MenuManager.addMenu(stdIn, out, in);
                    break;
                case "3":
                    MenuManager.updateMenu(stdIn, out, in);
                    break;
                case "4":
                    MenuManager.deleteMenu(stdIn, out, in);
                    break;
                case "5":
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }
}