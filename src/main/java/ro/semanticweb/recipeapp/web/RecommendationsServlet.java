package ro.semanticweb.recipeapp.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ro.semanticweb.recipeapp.model.UserProfile;
import java.io.IOException;
import java.util.List;

@WebServlet("/recommendations")
public class RecommendationsServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        List<UserProfile> users = dataService().getAllUsers();
        String userId = req.getParameter("userId");

        UserProfile selectedUser;
        if (userId != null && !userId.isBlank()) {
            selectedUser = dataService().getUserById(userId);
        } else {
            selectedUser = dataService().getFirstUser();
        }

        if (selectedUser != null) {
            userId = selectedUser.getId();
        }

        req.setAttribute("users", users);
        req.setAttribute("selectedUser", selectedUser);
        req.setAttribute("selectedUserId", userId);
        req.setAttribute("skillRecommendations", selectedUser != null ? dataService().getRecipesBySkillForUser(selectedUser) : List.of());
        req.setAttribute("skillCuisineRecommendations", selectedUser != null ? dataService().getRecipesBySkillAndCuisineForUser(selectedUser) : List.of());

        req.getRequestDispatcher("/WEB-INF/jsp/recommendations.jsp").forward(req, resp);
    }
}