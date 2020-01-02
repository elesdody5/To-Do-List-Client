/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.to_do_list;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import org.json.JSONObject;
import server_request.Server;

/**
 * FXML Controller class
 *
 * @author sara
 */
public class ToDoListController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private JFXButton addTask;
    @FXML
    private JFXTextArea titleOfTask;
    @FXML
    private JFXListView listOfTasks;
    JSONObject toDoTaskJsonObject;
    ToDoList todolist;

    @FXML
    public void addTaskToToDoList() throws IOException {
        GridPane gridPane = new GridPane();
        //  gridPane.setStyle("-fx-padding: 5;-fx-border-color:#babac9;-fx-border-radius: 5;-fx-background-radius: 20;-fx-background-color: white;");
        Text title = new Text(titleOfTask.getText());
        gridPane.add(title, 0, 0);
        if (title.getText() != "") {
            listOfTasks.getItems().add(gridPane);
            TaskInfo taskInfo = new TaskInfo(title.getText().toString());
            todolist.addTaskToDoList(taskInfo);
            toDoTaskJsonObject = taskInfo.writeTaskInfoObjectAsJson();
            sendTaskInfoToServer();
            titleOfTask.setText("");
            //print list of tasks 
            for (int i = 0; i < todolist.getTasksInTODOList().size(); i++) {
                System.out.println(todolist.getTasksInTODOList().get(i).getTitle());
            }
        }
    }

    public void sendTaskInfoToServer() throws IOException {
        String[] TypeOfRequest = new String[1];
        TypeOfRequest[0] = "Task";
        Server server = new Server();
        JSONObject resultJsonObject = server.post(TypeOfRequest, toDoTaskJsonObject);

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        //  tasks=new ArrayList<TaskInfo>();
        todolist = new ToDoList();
    }

}
