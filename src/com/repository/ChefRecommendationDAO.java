package com.repository;

import com.database.SQLDatabaseHelper;
import com.model.ChefRecommendation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChefRecommendationDAO {

    public void increaseVoteCount(int menuId) {
        String query = "UPDATE ChefRecommendation SET VoteCount = VoteCount + 1 WHERE MenuId = ?";

        try (Connection connection = SQLDatabaseHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, menuId);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating vote count: " + e.getMessage());
        }
    }

    public List<ChefRecommendation> getChefRecommendations() {
        List<ChefRecommendation> recommendations = new ArrayList<>();
        String query = "SELECT cr.MenuId, m.Name AS MenuName, m.Score AS score, cr.VoteCount "
                + "FROM ChefRecommendation cr "
                + "JOIN Menu m ON cr.MenuId = m.MenuId "
                + "WHERE DATE(cr.date_created) = CURDATE() "
                + "ORDER BY cr.VoteCount DESC";

        try (Connection connection = SQLDatabaseHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                ChefRecommendation recommendation = new ChefRecommendation();
                recommendation.setMenuId(resultSet.getInt("MenuId"));
                recommendation.setMenuName(resultSet.getString("MenuName"));
                recommendation.setScore(resultSet.getBigDecimal("score"));
                recommendation.setVoteCount(resultSet.getInt("VoteCount"));

                recommendations.add(recommendation);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching recommendations: " + e.getMessage());
        }
        return recommendations;
    }

    public void insertChefRecommendation(int menuId) {
        String query = "INSERT INTO ChefRecommendation (MenuId, VoteCount) VALUES (?, 0)";

        try (Connection connection = SQLDatabaseHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, menuId);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error inserting chef recommendation: " + e.getMessage());
        }
    }

    public List<ChefRecommendation> getTodayChefRecommendations() {
        List<ChefRecommendation> recommendations = new ArrayList<>();
        String query = "SELECT * FROM ChefRecommendation WHERE DATE(date_created) >= CURDATE()";

        try (Connection connection = SQLDatabaseHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                ChefRecommendation recommendation = new ChefRecommendation();
                recommendation.setRecId(resultSet.getInt("RecId"));
                recommendation.setMenuId(resultSet.getInt("MenuId"));
                recommendation.setVoteCount(resultSet.getInt("VoteCount"));
                recommendation.setCreatedDate(resultSet.getTimestamp("date_created"));

                recommendations.add(recommendation);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching today's recommendations: " + e.getMessage());
        }
        return recommendations;
    }
}