package ro.semanticweb.recipeapp.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/recipes/xsl")
public class XslRecipesServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String selectedUserId = req.getParameter("userId");
        req.setAttribute("users", dataService().getAllUsers());
        req.setAttribute("xslRenderedHtml", dataService().transformRecipesWithXsl(selectedUserId));
        req.setAttribute("selectedUserId", selectedUserId);
        req.getRequestDispatcher("/WEB-INF/jsp/xsl-view.jsp").forward(req, resp);
    }
}
