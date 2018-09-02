package ru.javawebinar.basejava.web;

import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.Storage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ResumeServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
//        response.setHeader("Content-Type", "text/html; charset=UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        String uuid = request.getParameter("uuid");
        String htmlText = "<html>\n" +
                "<head>\n" +
                "<title>Resumes</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<table border=\"1\">";
        Storage storage = Config.get().getStorage();
        List<Resume> result = new ArrayList<>();
        if (uuid == null) {
            result = storage.getAllSorted();
        } else {
            Resume resume = storage.get(uuid);
            if (request != null) {
                result.add(resume);
            }
        }
        for (Resume r:result) {
            htmlText += "<tr>\n" +
                    "<td>"+r.getUuid()+"</td>\n" +
                    "<td>"+r.getFullName()+"</td>\n" +
                    "</tr>";
        }
        htmlText += "</table>\n" +
                "</body>\n" +
                "</html> ";
        response.getWriter().write(htmlText);
    }
}
