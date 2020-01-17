/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.to_do_list;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import org.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author sara
 */
public class ToDoListController implements Initializable {

    /**
     * Initializes the controller class.
     */
    TaskInformationViewController taskview;
    @FXML
    private JFXButton addTask;
    @FXML
    private JFXListView<TaskInfo> listOfTasks;
    JSONObject toDoTaskJsonObject;
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
     TaskInfo addefTask = TaskInformationViewController.getTaskInfo();

            if (addefTask != null) {

                listOfTasks.getItems().add(addefTask);
                addefTask=null;
            }
               

        });

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

     listOfTasks.setCellFactory((param)

                -> {
            return new ListAdapterOfTasksList( param);
        });

      taskview=new TaskInformationViewController();
    }


    
 
    

    private  void setTodoList(ToDoList toDoList) {

        todo = toDoList;
        titleOfTodo.setText(todo.getTitle());
        Color color =Color.valueOf(toDoList.getColor());
        titleOfTodo.setTextFill(color);
        listOfTasks.setStyle("-fx-control-inner-background: "+color.toString().substring(2));
        tasksListView.setVisible(true);
        List<TaskInfo> tasks = toDoList.getTasksInTODOList();
        listOfTasks.getItems().clear();
        listOfTasks.getItems().addAll(tasks);
       
    }

    public static ToDoList getTodoList() {
        return todo;
    }

    public void updateCurrentTodo(ToDoList toDoList) {

        if (toDoList==null) {
            tasksListView.setVisible(false);
            initialStatePane.setVisible(true);
        } else {
            tasksListView.setVisible(true);
            initialStatePane.setVisible(false);
            setTodoList(toDoList);
        }

    }

}
