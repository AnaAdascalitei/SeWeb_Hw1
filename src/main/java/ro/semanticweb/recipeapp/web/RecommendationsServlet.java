package ro.semanticweb.recipeapp.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/recommendations")
public class RecommendationsServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("firstUser", dataService().getFirstUser());
        req.setAttribute("skillRecommendations", dataService().getRecipesBySkillForFirstUser());
        req.setAttribute("skillCuisineRecommendations", dataService().getRecipesBySkillAndCuisineForFirstUser());
        req.getRequestDispatcher("/WEB-INF/jsp/recommendations.jsp").forward(req, resp);
    }
}
