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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
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
    @FXML
    private JFXButton addTask;
    @FXML
    private JFXTextArea titleOfTask;
    @FXML
    private JFXListView listOfTasks;

    JSONObject toDoTaskJsonObject;
    private ToDoList todolist;

    @FXML
    private void addTaskToToDoList() throws IOException {
     /*   GridPane gridPane = new GridPane();
        //  gridPane.setStyle("-fx-padding: 5;-fx-border-color:#babac9;-fx-border-radius: 5;-fx-background-radius: 20;-fx-background-color: white;");
        Text title = new Text(titleOfTask.getText());
        gridPane.add(title, 0, 0);

        if (title.getText() != "") {
            try {

                listOfTasks.getItems().add(gridPane);
                TaskInfo taskInfo = new TaskInfo(title.getText().toString(), todolist.getId());
                todolist.addTaskToDoList(taskInfo);
                toDoTaskJsonObject = taskInfo.writeTaskInfoObjectAsJson();
                sendTaskInfoToServer();

                titleOfTask.setText("");
            } catch (IOException ex) {
                System.out.println("server Connection loss");

                // Logger.getLogger(ToDoListController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        //print list of tasks 
        for (int i = 0; i < todolist.getTasksInTODOList().size(); i++) {
            System.out.println(todolist.getTasksInTODOList().get(i).getTitle());
        }*/
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
                            listOfTasks.getItems().add(addefTask.getTitle());
                }
                   
            });
                      

    }
                                
                                
    
    private void sendTaskInfoToServer() throws IOException {
        String[] TypeOfRequest = new String[1];
        TypeOfRequest[0] = "Task";

        Server server = new Server();
        JSONObject resultJsonObject = server.post(TypeOfRequest, toDoTaskJsonObject);

    }

    private ArrayList<TaskInfo> getFristTaskInfo() throws IOException, JSONException {
        String[] typrOfRequest = new String[1];
        typrOfRequest[0] = "getTasksOflist";
        Server server = new Server();
        JSONObject resultOfGetFisrtList = server.get(typrOfRequest);
        JSONArray jsonArrayOftodotasks = resultOfGetFisrtList.getJSONArray("listOfTasks");
        ArrayList<TaskInfo> tasksOfToDoList = new ArrayList<TaskInfo>();
        for (int i = 0; i < jsonArrayOftodotasks.length(); i++) {
            JSONObject task = jsonArrayOftodotasks.getJSONObject(i);
            String title = task.getString("title");
          //  System.out.println(title);
            int todoid = task.getInt("listId");
            TaskInfo taskinfo = new TaskInfo(title, todoid);
            tasksOfToDoList.add(taskinfo);

        }
       // System.out.println(jsonArrayOftodotasks);
        return tasksOfToDoList;

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        //  tasks=new ArrayList<TaskInfo>();
        todolist = new ToDoList();
        ArrayList <TaskInfo> firstTask = null;
       try {
            firstTask = getFristTaskInfo();
        } catch (IOException ex) {
            Logger.getLogger(ToDoListController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(ToDoListController.class.getName()).log(Level.SEVERE, null, ex);
        }
       for(int i =0;i<firstTask.size();i++)
       {  GridPane gridpane = new GridPane();
        Text titleOfFirstTask = new Text(firstTask.get(i).getTitle());
        gridpane.add(titleOfFirstTask, 0, 0);
        listOfTasks.getItems().add(gridpane); }
       
       ////////////////////////////
        listOfTasks.setOnMouseClicked(new ListViewHandler() {
            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                // System.out.print(listview.getSelectionModel().getSelectedIndex());
             

            }
        });
    }
   static ToDoList to;
    public void setTodoList(ToDoList toDoList) {
        this.todolist = toDoList;
        to=todolist;
        System.out.println(todolist.getTitle());
    }
     public static ToDoList getTodoList() {
                 System.out.println(to.getId());
       return to;
    }
    

    @Override
    public void update(Observable o, Object o1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
