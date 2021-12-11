package servlet;

import org.apache.commons.io.file.Counters;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CounterServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext context = getServletContext();
        Counters.Counter counter =(Counters.Counter)context.getAttribute("counter");

        if(counter ==null){
            counter = new Counters.Counter(1);
        }
    }
}
