package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import launch.model.Task;


import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.servlet.ServletException;

import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
        name = "MyServlet",
        urlPatterns = {"/todo"}
)
public class HelloServlet extends HttpServlet {
    private Integer id = 0;
    private ArrayList<Task> taskList = new ArrayList<>();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.getFactory().configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET,false);
        ServletOutputStream out = resp.getOutputStream();
        objectMapper.writer().writeValue(out, taskList);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String response = req.getReader().lines().collect(Collectors.joining("\n,\r"));
        Task task = objectMapper.readValue(response, Task.class);

        if (!Objects.isNull(task.getId())) {
            for (Task inCollectionTask : taskList) {
                if (inCollectionTask.getId().equals(task.getId())) {
                    inCollectionTask.setText(task.getText());
                }
            }
        } else {
            task.setId(++id);
            taskList.add(task);
        }
//        String jsonString = objectMapper.writeValueAsString(task);
//        System.out.println(jsonString);
        ServletOutputStream out = resp.getOutputStream();
        objectMapper.writer().writeValue(out, task);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (Objects.isNull(req.getQueryString())) {
            taskList.clear();
        } else {
            String[] response = req.getQueryString().split("[=]");
            String id = response[response.length - 1];
            taskList.removeIf(task -> task.getId() == Integer.parseInt(id));
        }
    }

}
