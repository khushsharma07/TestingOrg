package com.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;

import com.google.gson.Gson;
import com.model.ChefRecommendation;
import com.model.FeedbackDTO;
import com.model.MenuDTO;
import com.model.User;
import com.repository.ChefRecommendationDAO;
import com.service.AuthService;
import com.service.ChefRecommendationService;
import com.service.FeedbackService;
import com.service.MenuService;

public class ClientThread implements Runnable {
    private final Socket socket;
    private final AuthService authService;
    private final MenuService menuService;
    private final FeedbackService feedbackService;

    private final ChefRecommendationDAO chefRecommendationDAO;

    private final ChefRecommendationService chefRecommendationService;

    public ClientThread(Socket socket) throws SQLException {
        this.socket = socket;
        this.authService = new AuthService();
        this.menuService = new MenuService();
        this.feedbackService = new FeedbackService();
        this.chefRecommendationDAO = new ChefRecommendationDAO();
        this.chefRecommendationService = new ChefRecommendationService();
    }

    public void run() {

        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String request;
            while ((request = in.readLine()) != null) {
                System.out.println(request);
                String[] parts = request.split(";", 2);
                String requestType = parts[0];
                String requestData = parts.length > 1 ? parts[1] : "";

                switch (requestType) {
                    case "LOGIN_REQUEST":
                        processLoginRequest(requestData, out);
                        break;
                    case "ADD_MENU_REQUEST":
                        processAddMenuRequest(requestData, out);
                        break;
                    case "UPDATE_MENU_REQUEST":
                        processUpdateMenuRequest(requestData, out);
                        break;
                    case "DELETE_MENU_REQUEST":
                        processDeleteMenuRequest(requestData, out);
                        break;
                    case "VIEW_MENU_REQUEST":
                        processViewMenuRequest(out);
                        break;
                    case "VIEW_TOP_RECOMMENDATIONS":
                        processViewTopRecommendationsRequest(out);
                        break;
                    case "VIEW_CHEF_RECOMMENDATIONS":
                        System.out.println("Hi");
                        processViewChefRecommendationsRequest(out);
                        break;
                    case "VOTE_RECOMMENDATION_REQUEST":
                        processVoteRecommendationRequest(parts, out);
                        break;
                    case "VIEW_VOTED_REPORT":
                        processViewVotedReportRequest(out);
                        break;
                    case "ROLLOUT_NEXT_DAY_MENU_REQUEST":
                        processRollOutNextDayMenuRequest(parts, out);
                        break;
                    case "GIVE_FEEDBACK_REQUEST":
                        processGiveFeedbackRequest(parts[1], out);
                        break;
                    default:
                        out.println("UNKNOWN_REQUEST");
                }
            }
        } catch (IOException | SQLException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            System.out.println("Executed Finally");
        }
    }

    private void processLoginRequest(String requestData, PrintWriter out) {
        String[] parts = requestData.split(";");
        if (parts.length == 2) {
            String employeeId = parts[0];
            String password = parts[1];
            User user = authService.authenticate(employeeId, password);

            if (user != null) {
                out.println("LOGIN_RESPONSE;SUCCESS;Login successful, Role: " + user.getRoleName() + ";" + user.getRoleName());
            } else {
                out.println("LOGIN_RESPONSE;FAILURE;Incorrect EmployeeId/Password");
            }
        } else {
            out.println("LOGIN_RESPONSE;FAILURE;Invalid login request format");
        }
    }

    private void processAddMenuRequest(String requestData, PrintWriter out) throws SQLException {
        Gson gson = new Gson();
        MenuDTO menu = gson.fromJson(requestData, MenuDTO.class);
        menu.setAvailabilityStatus("Yes");
        menu.setScore(new BigDecimal(0));
        boolean success = menuService.addMenu(menu);

        if (success) {
            out.println("ADD_MENU_RESPONSE;SUCCESS;Menu item added successfully");
        } else {
            out.println("ADD_MENU_RESPONSE;FAILURE;Failed to add menu item");
        }
    }

    private void processUpdateMenuRequest(String requestData, PrintWriter out) {
        Gson gson = new Gson();
        MenuDTO menu = gson.fromJson(requestData, MenuDTO.class);
        boolean success = menuService.updateMenu(menu);

        if (success) {
            out.println("UPDATE_MENU_RESPONSE;SUCCESS;Menu item updated successfully");
        } else {
            out.println("UPDATE_MENU_RESPONSE;FAILURE;Failed to update menu item");
        }
    }

    private void processDeleteMenuRequest(String requestData, PrintWriter out) {
        boolean success = menuService.deleteMenu(requestData);

        if (success) {
            out.println("DELETE_MENU_RESPONSE;SUCCESS;Menu item deleted successfully");
        } else {
            out.println("DELETE_MENU_RESPONSE;FAILURE;Failed to delete menu item");
        }
    }

    private void processViewMenuRequest(PrintWriter out) {
        try {
            String menuList = menuService.viewMenu();
            out.println("VIEW_MENU_RESPONSE;" + menuList);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void processViewTopRecommendationsRequest(PrintWriter out) throws SQLException {
        feedbackService.updateMenuScoresAccToFeedback();
        String recommendations = menuService.viewTopRecommendations();
        out.println(recommendations);
    }

    private void processViewChefRecommendationsRequest(PrintWriter out) {
        try {
            List<ChefRecommendation> recommendations = chefRecommendationService.getChefRecommendations();
            StringBuilder response = new StringBuilder("CHEF_RECOMMENDATIONS");
            for (ChefRecommendation recommendation : recommendations) {
                response.append(";")
                        .append("MenuId: ").append(recommendation.getMenuId())
                        .append(", MenuName: ").append(recommendation.getMenuName())
                        .append(", VoteCount: ").append(recommendation.getVoteCount());
            }
            out.println(response);
        }catch(Exception error){
            System.err.println(error.getMessage());
        }
    }

    private void processVoteRecommendationRequest(String[] parts, PrintWriter out) {
        String[] menuIds = parts[1].split(",");
        boolean success = chefRecommendationService.voteForRecommendations(menuIds);
        if (success) {
            out.println("VOTE_RECOMMENDATION_RESPONSE;SUCCESS");
        } else {
            out.println("VOTE_RECOMMENDATION_RESPONSE;FAILURE");
        }
    }

    private void processViewVotedReportRequest(PrintWriter out) {
        List<ChefRecommendation> recommendations = chefRecommendationService.getTodayChefRecommendations();
        StringBuilder response = new StringBuilder("VOTED_REPORT");
        for (ChefRecommendation recommendation : recommendations) {
            response.append(";")
                    .append("RecId: ").append(recommendation.getRecId())
                    .append(", MenuId: ").append(recommendation.getMenuId())
                    .append(", VoteCount: ").append(recommendation.getVoteCount())
                    .append(", CreatedDate: ").append(recommendation.getCreatedDate());
        }
        out.println(response);
    }

    private void processRollOutNextDayMenuRequest(String[] parts, PrintWriter out) {
        String[] menuIds = parts[1].split(",");
        boolean success = chefRecommendationService.rollOutNextDayMenu(menuIds);
        if (success) {
            out.println("ROLLOUT_NEXT_DAY_MENU_RESPONSE;SUCCESS");
        } else {
            out.println("ROLLOUT_NEXT_DAY_MENU_RESPONSE;FAILURE");
        }
    }

    private void processGiveFeedbackRequest(String jsonFeedback, PrintWriter out) {
        try {
            Gson gson = new Gson();
            FeedbackDTO feedbackDTO = gson.fromJson(jsonFeedback, FeedbackDTO.class);
            String employeeId = feedbackDTO.getEmployeeId();
            Integer menuId = feedbackDTO.getMenuId();
            String comment = feedbackDTO.getComment();
            int rating = feedbackDTO.getRating();

            feedbackService.submitFeedback(employeeId, menuId, comment, rating);
            out.println("GIVE_FEEDBACK_RESPONSE;SUCCESS");
        } catch (SQLException e) {
            System.err.println();
            out.println("GIVE_FEEDBACK_RESPONSE;FAILURE");
        } catch (Exception e) {
            System.err.println();
            out.println("GIVE_FEEDBACK_RESPONSE;FAILURE");
        }
    }
}