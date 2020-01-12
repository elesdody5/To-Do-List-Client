/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.to_do_list;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import home.list.FXMLListController;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import server_connection.Connection;
import server_request.Server;

/**
 * FXML Controller class
 *
 * @author sara
 */
public class ToDoListController implements Initializable, Observer {

    /**
     * Initializes the controller class.
     */
    TaskInformationViewController taskview;
    @FXML
    private JFXButton addTask;
    @FXML
    private JFXListView listOfTasks;
    JSONObject toDoTaskJsonObject;
    ToDoList selectedTodo;
    ToDoList selectedTodo2;
    @FXML
    private GridPane tasksListView;
    @FXML
    private AnchorPane initialStatePane;
    @FXML
    private Label titleOfTodo;
    static ToDoList todo;

    @FXML
    private void addTaskToToDoList() throws IOException {

        Stage appStage;
        Parent root;
        appStage = new Stage();
        root = FXMLLoader.load(getClass().getResource("taskInformationView.fxml"));
        Scene scene = new Scene(root);
        appStage.setScene(scene);
        appStage.initOwner(listOfTasks.getScene().getWindow());
        appStage.initModality(Modality.WINDOW_MODAL);
        appStage.show();
        appStage.setOnHidden((WindowEvent event) -> {
            if (TaskInformationViewController.getTaskInfo() != null) {

                TaskInfo addefTask = TaskInformationViewController.getTaskInfo();
                 listOfTasks.getItems().add(addefTask);
            }

        });
        

    }

   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      //  Object ol = null;
      // update(selectedTodo2, ol);
        listOfTasks.setCellFactory((param)
                -> {
            return new ListAdapterOfTasksList((ListView<TaskInfo>) param);
        });
      taskview=new TaskInformationViewController();
     
    }


    public void setTodoList(ToDoList toDoList) {
          selectedTodo=toDoList;
          todo=selectedTodo;
          titleOfTodo.setText(selectedTodo.getTitle());
          titleOfTodo.setTextFill(Color.web(selectedTodo.getColor()));
          System.out.println(toDoList.getTasksInTODOList().size());
          tasksListView.setVisible(true);
          List<TaskInfo> tasks = selectedTodo.getTasksInTODOList();

            for (int i = 0; i < tasks.size(); i++) {
                  listOfTasks.getItems().add(tasks.get(i));
              
    }
}
    public static ToDoList getTodoList() {
        return todo;
    }

    @Override
    public void update(Observable o, Object o1) {

        selectedTodo = (ToDoList) o;
        todo=selectedTodo;
        if (selectedTodo == null) {
            tasksListView.setVisible(false);
            initialStatePane.setVisible(true);
        } else {
            tasksListView.setVisible(true);
            initialStatePane.setVisible(false);
            List<TaskInfo> tasks = selectedTodo.getTasksInTODOList();
            for (int i = 0; i < tasks.size(); i++) {
                 listOfTasks.getItems().add(tasks.get(i));
               
            }
        }

    }

}
