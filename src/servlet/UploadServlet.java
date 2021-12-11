package servlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class UploadServlet extends HttpServlet {
    private String filePath;
    private String tempFilePath;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        filePath = config.getInitParameter("filePath");
        tempFilePath=config.getInitParameter("tempFilePath");
        filePath=getServletContext().getRealPath(filePath);
        tempFilePath=getServletContext().getRealPath(tempFilePath);
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        PrintWriter out = resp.getWriter();
        try {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            factory.setSizeThreshold(4*1024);
            factory.setRepository(new File(tempFilePath));

            ServletFileUpload upload = new ServletFileUpload(factory);
            upload.setSizeMax(4*1024*1024);

            List<FileItem> items = upload.parseRequest(req);

            for(FileItem item:items){
                if(item.isFormField()){
                    processFormFiled(item,out);
                }else{
                    processUploadFile(item,out);
                }
            }
            out.close();
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void processUploadFile(FileItem item, PrintWriter out) throws Exception {
        String filename = item.getName();
        int index =filename.lastIndexOf("\\");
        filename = filename.substring(index+1,filename.length());
        long fileSize  =item.getSize();

        if(filename.equals("") && fileSize == 0) return;

        File uploadedFile = new File(filePath+"/"+filename);
        item.write(uploadedFile);
        out.println(filename+"is saved.");
        out.println("The size of " + filename+ "is" + fileSize + "\r\n");

    }

    private void processFormFiled(FileItem item, PrintWriter out){
        String name = item.getFieldName();
        String value = item.getString();
        out.println(name+":"+value+"\r\n");
    }
}
