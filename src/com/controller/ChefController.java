package com.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ChefController implements Controller {

    public void displayMenu(BufferedReader stdIn, PrintWriter out, BufferedReader in) throws IOException {
        while (true) {
            System.out.println("Chef Menu:");
            System.out.println("1. View Food Menu");
            System.out.println("2. View Top Recommendations");
            System.out.println("3. Roll Out Next Day Menu");
            System.out.println("4. View Voted Report");
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
                    out.println("VIEW_TOP_RECOMMENDATIONS");
                    String recommendationsResponse = in.readLine();
                    System.out.println(recommendationsResponse);
                    break;
                case "3":
                    rollOutNextDayMenu(stdIn, out, in);
                    break;
                case "4":
                    out.println("VIEW_VOTED_REPORT");
                    String votedReportResponse = in.readLine();
                    System.out.println(votedReportResponse);
                    break;
                case "5":
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    private static void rollOutNextDayMenu(BufferedReader stdIn, PrintWriter out, BufferedReader in) throws IOException {
        System.out.print("Enter the MenuIds for the next day (comma separated): ");
        String menuIds = stdIn.readLine();
        out.println("ROLLOUT_NEXT_DAY_MENU_REQUEST;" + menuIds);

        String response = in.readLine();
        if (response != null && response.startsWith("ROLLOUT_NEXT_DAY_MENU_RESPONSE")) {
            String[] parts = response.split(";");
            if ("SUCCESS".equals(parts[1])) {
                System.out.println("Next day's menu rolled out successfully.");
            } else {
                System.out.println("Failed to roll out next day's menu.");
            }
        }
    }
}