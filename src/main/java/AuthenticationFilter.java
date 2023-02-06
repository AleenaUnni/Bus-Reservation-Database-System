import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class AuthenticationFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        HashMap<Long,String> sessionMap = (HashMap<Long, String>) req.getServletContext().getAttribute("sessionMap");
        if(sessionMap.values().contains(req.getSession().getId().toString()) || req.getRequestURI().endsWith("login") || req.getRequestURI().endsWith("users") || req.getRequestURI().endsWith("registeruser") || req.getRequestURI().endsWith("createsession")){
            chain.doFilter(req,res);
        }else{
            req.getRequestDispatcher("users.jsp").forward(req,res);
        }
    }
}
