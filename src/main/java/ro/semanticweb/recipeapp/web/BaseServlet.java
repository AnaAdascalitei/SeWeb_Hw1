package ro.semanticweb.recipeapp.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import ro.semanticweb.recipeapp.service.XmlDataService;

public abstract class BaseServlet extends HttpServlet {
    protected XmlDataService dataService() throws ServletException {
        Object service = getServletContext().getAttribute(AppContextListener.DATA_SERVICE_KEY);
        if (service instanceof XmlDataService xmlDataService) {
            return xmlDataService;
        }
        throw new ServletException("XML data service not initialized.");
    }
}
