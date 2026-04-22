package ro.semanticweb.recipeapp.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/users")
public class UsersServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("users", dataService().getAllUsers());
        req.setAttribute("cuisineTypes", dataService().getAllCuisineTypes());
        req.getRequestDispatcher("/WEB-INF/jsp/users.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            dataService().addUser(
                    req.getParameter("name"),
                    req.getParameter("surname"),
                    req.getParameter("cookingSkillLevel"),
                    req.getParameter("preferredCuisineType"));
            resp.sendRedirect(req.getContextPath() + "/users");
        } catch (IllegalArgumentException ex) {
            req.setAttribute("error", ex.getMessage());
            req.setAttribute("users", dataService().getAllUsers());
            req.setAttribute("cuisineTypes", dataService().getAllCuisineTypes());
            req.getRequestDispatcher("/WEB-INF/jsp/users.jsp").forward(req, resp);
        }
    }
}
