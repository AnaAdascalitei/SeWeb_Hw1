package ro.semanticweb.recipeapp.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import ro.semanticweb.recipeapp.model.Recipe;

@WebServlet(urlPatterns = {"/", "/recipes"})
public class HomeServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String cuisine = req.getParameter("cuisine");
        String userId = req.getParameter("userId");
        List<Recipe> recipes = dataService().getRecipesByCuisine(cuisine);
        req.setAttribute("recipes", recipes);
        req.setAttribute("cuisineTypes", dataService().getAllCuisineTypes());
        req.setAttribute("selectedCuisine", cuisine);
        req.setAttribute("users", dataService().getAllUsers());
        req.setAttribute("selectedUserId", userId);
        req.setAttribute("xslRenderedHtml", dataService().transformRecipesWithXsl(userId));
        req.getRequestDispatcher("/WEB-INF/jsp/home.jsp").forward(req, resp);
    }
}
