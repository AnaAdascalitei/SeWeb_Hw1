package ro.semanticweb.recipeapp.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/recipes/add")
public class AddRecipeServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("cuisineTypes", dataService().getAllCuisineTypes());
        req.getRequestDispatcher("/WEB-INF/jsp/add-recipe.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            dataService().addRecipe(
                    req.getParameter("title"),
                    req.getParameter("cuisineA"),
                    req.getParameter("cuisineB"),
                    req.getParameter("difficultyLevel"));
            resp.sendRedirect(req.getContextPath() + "/recipes");
        } catch (IllegalArgumentException ex) {
            req.setAttribute("error", ex.getMessage());
            req.setAttribute("cuisineTypes", dataService().getAllCuisineTypes());
            req.getRequestDispatcher("/WEB-INF/jsp/add-recipe.jsp").forward(req, resp);
        }
    }
}
