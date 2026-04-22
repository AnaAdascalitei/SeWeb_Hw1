package ro.semanticweb.recipeapp.web;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import ro.semanticweb.recipeapp.service.XmlDataService;

@WebListener
public class AppContextListener implements ServletContextListener {
    public static final String DATA_SERVICE_KEY = "dataService";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        String appPath = context.getRealPath("/");
        if (appPath == null || appPath.isBlank()) {
            appPath = System.getProperty("user.dir");
        }
        XmlDataService service = new XmlDataService(appPath);
        service.initialize();
        context.setAttribute(DATA_SERVICE_KEY, service);
    }
}
