package com.controller;

import com.google.gson.Gson;
import com.model.Feedback;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class EmployeeController implements Controller {

    public void displayMenu(BufferedReader stdIn, PrintWriter out, BufferedReader in) throws IOException {
        while(true){
            System.out.println("Employee Menu:");
            System.out.println("1. Vote for Next Day Recommendation");
            System.out.println("2. Give com.model.Feedback to Chef");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            String choice = stdIn.readLine();

            switch (choice) {
                case "1":
                    voteForRecommendation(stdIn, out, in);
                    break;
                case "2":
                    giveFeedbackToChef(stdIn, out, in);
                    break;
                case "3":
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    private static void voteForRecommendation(BufferedReader stdIn, PrintWriter out, BufferedReader in) throws IOException {
        out.println("VIEW_CHEF_RECOMMENDATIONS");
        String response = in.readLine();

        if (response != null && response.startsWith("VIEW_RECOMMENDATIONS_RESPONSE")) {
            String[] parts = response.split(";");
            System.out.println("Chef Recommendations:");
            for (int i = 1; i < parts.length; i += 3) {
                System.out.println("MenuId: " + parts[i] + ", Name: " + parts[i + 1] + ", Score: " + parts[i + 2]);
            }

            System.out.print("Enter the MenuIds to vote for (comma separated): ");
            String menuIds = stdIn.readLine();
            out.println("VOTE_RECOMMENDATION_REQUEST;" + menuIds);

            String voteResponse = in.readLine();
            if (voteResponse != null && voteResponse.startsWith("VOTE_RECOMMENDATION_RESPONSE")) {
                String[] voteParts = voteResponse.split(";");
                if ("SUCCESS".equals(voteParts[1])) {
                    System.out.println("Votes registered successfully.");
                } else {
                    System.out.println("Failed to register votes.");
                }
            }
        }
    }

    private static void giveFeedbackToChef(BufferedReader stdIn, PrintWriter out, BufferedReader in) throws IOException {
        System.out.print("Enter your EmployeeId: ");
        String employeeId = stdIn.readLine();

        System.out.print("Enter the MenuId to give feedback");
        int menuId = Integer.parseInt(stdIn.readLine());

        System.out.print("Enter your comment: ");
        String comment = stdIn.readLine();

        System.out.print("Enter your rating: ");
        int rating = Integer.parseInt(stdIn.readLine());

        Feedback feedbackDTO = new Feedback();
        feedbackDTO.setEmployeeId(employeeId);
        feedbackDTO.setMenuId(menuId);
        feedbackDTO.setComment(comment);
        feedbackDTO.setRating(rating);

        Gson gson = new Gson();
        String jsonFeedback = gson.toJson(feedbackDTO);
        out.println("GIVE_FEEDBACK_REQUEST;" + jsonFeedback);

        String feedbackResponse = in.readLine();
        if (feedbackResponse != null && feedbackResponse.startsWith("GIVE_FEEDBACK_RESPONSE")) {
            String[] feedbackParts = feedbackResponse.split(";");
            if ("SUCCESS".equals(feedbackParts[1])) {
                System.out.println("com.model.Feedback submitted successfully.");
            } else {
                System.out.println("Failed to submit feedback.");
            }
        }
    }
}