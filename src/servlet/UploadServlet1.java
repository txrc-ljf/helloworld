package servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

@WebServlet(name="UploadServlet1",urlPatterns = "/upload1")
@MultipartConfig

public class UploadServlet1 extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");

        String savePath = req.getServletContext().getRealPath("/store");
        PrintWriter out = resp.getWriter();

        Collection<Part> parts = req.getParts();

        for(Part part:parts){
            String header = part.getHeader("content-disposition");

            System.out.println("-----Part-----");
            System.out.println("type:"+part.getContentType());
            System.out.println("size:"+part.getSize());
            System.out.println("name:"+part.getName());
            System.out.println("header:"+ header);

            if(part.getContentType() == null){
                String name = part.getName();
                String value = req.getParameter(name);
                out.println(name+": "+ value+"\r\n");
            }else if(part.getName().indexOf("file") != -1){
                String fileName = getFileName(header);
                part.write(savePath+ File.separator+fileName);
                out.println(fileName+"is saved.");
                out.println("The size of " +fileName+"is" + part.getSize()+"byte\r\n");
            }
        }
        out.close();
    }

    private String getFileName(String header) {
        String fileName = header.substring(
                header.lastIndexOf("=")+2,
                header.length()-1
        );

        return fileName;
    }
}
