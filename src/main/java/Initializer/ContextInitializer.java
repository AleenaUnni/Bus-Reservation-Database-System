package Initializer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.SQLException;
import java.util.HashMap;

@WebListener
public class ContextInitializer implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            ConnectionHandler.createDatabase();
            ConnectionHandler.createTables();
            sce.getServletContext().setAttribute("sessionMap",new HashMap<Long,String>());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            ConnectionHandler.closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
