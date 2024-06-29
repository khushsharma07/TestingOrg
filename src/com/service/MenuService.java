package com.service;

import java.sql.SQLException;
import java.util.List;
import com.google.gson.Gson;
import com.model.MenuDTO;
import com.repository.MenuDAO;

public class MenuService {
    private final MenuDAO menuDAO;

    public MenuService() throws SQLException {
        this.menuDAO = new MenuDAO();
    }

    public boolean addMenu(MenuDTO menu) throws SQLException {
        return menuDAO.addMenu(menu);
    }

    public boolean updateMenu(MenuDTO menu) {
        return menuDAO.updateMenu(menu);
    }

    public boolean deleteMenu(String menuId) {
        return menuDAO.deleteMenu(menuId);
    }

    public String viewMenu() {
        List<MenuDTO> menus = menuDAO.getAllMenus();
        Gson gson = new Gson();
        return gson.toJson(menus);
    }

    public String viewTopRecommendations() {
        List<MenuDTO> topRecommendations = menuDAO.getTopRecommendations();
        Gson gson = new Gson();
        return gson.toJson(topRecommendations);
    }
}