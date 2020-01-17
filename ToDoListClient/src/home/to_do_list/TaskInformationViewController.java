/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.to_do_list;

import Entity.User;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import server_request.Server;

/**
 * FXML Controller class
 *
 * @author sara
 */
public class TaskInformationViewController implements Initializable {

    @FXML
    private AnchorPane memberList;
    @FXML
    private ListView ListViewOfMember;
    @FXML
    private JFXTextField titleOfTask;
    private ToDoList todolist;
    JSONObject toDoTaskJsonObject;
    static TaskInfo taskInfostatic;
    @FXML
    private AnchorPane taskData;
    @FXML
    private JFXDatePicker StartDatePicker;
    @FXML
    private JFXDatePicker endDatePicker;
    @FXML
    private JFXTextArea description;
    @FXML
    private JFXTextArea comment;
    private boolean edit = false;
    private TaskInfo CurrentTask;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        todolist = ToDoListController.getTodoList();
        memberList.setVisible(false);
        User user = new User();
        ToDoList todolist = new ToDoList();
        ArrayList<User> teamMateInToDo = null;
        try {
            teamMateInToDo = getTeamMemberInToDo();
            if (teamMateInToDo != null) {
                for (int i = 0; i < teamMateInToDo.size(); i++) {
                    ListViewOfMember.getItems().add(teamMateInToDo.get(i));
                    ListViewOfMember.setCellFactory((param)
                            -> {
                        return new ListAdapter();
                    });
                }
            }
        } catch (JSONException ex) {
            Logger.getLogger(TaskInformationViewController.class.getName()).log(Level.SEVERE, null, ex);

        }

    }

    private ArrayList<User> getTeamMemberInToDo() throws JSONException {
        String[] typrOfRequest = new String[2];
        typrOfRequest[0] = "getTeamMemberInToDo";
        typrOfRequest[1] = String.valueOf(todolist.getId());
        Server server = null;
        try {
            server = new Server();
        } catch (IOException ex) {
            showAleart(Alert.AlertType.ERROR, "Connection lost", "Error update  List");
        }
        JSONObject resultOfGetTeamMember = server.get(typrOfRequest);
        ArrayList<User> teamMemberInfoList = null;
        if (resultOfGetTeamMember != null) {
            JSONArray jsonArrayOfTeamMeber = resultOfGetTeamMember.getJSONArray("listOfTeamMember");
            teamMemberInfoList = new ArrayList<User>();
            for (int i = 0; i < jsonArrayOfTeamMeber.length(); i++) {
                JSONObject teammember = jsonArrayOfTeamMeber.getJSONObject(i);
                String username = teammember.getString("userName");
                int userId = (int) teammember.get("id");

                User TeamMember = new User();
                TeamMember.setUserName(username);
                TeamMember.setId(userId);
                teamMemberInfoList.add(TeamMember);

            }
        }
        return teamMemberInfoList;

    }

    static boolean isClicked = false;

    @FXML
    private void saveTaskData(ActionEvent event) {
        isClicked = true;
        TaskInfo addedTask = null;
        if (!titleOfTask.getText().toString().equals("") && StartDatePicker.getValue() != null && endDatePicker.getValue() != null) {
            addedTask = new TaskInfo();
            addedTask.setTitle(titleOfTask.getText().toString());
            addedTask.setListId(todolist.getId());
            addedTask.setDescription(description.getText().toString());
            addedTask.setStartTime(StartDatePicker.getValue().toString());
            addedTask.setDeadLine(endDatePicker.getValue().toString());
            addedTask.setComment(comment.getText().toString());
            toDoTaskJsonObject = addedTask.writeTaskInfoObjectAsJson();
        }

        if (!ListAdapterOfTasksList.isEdited()) {

            try {
                if (addedTask != null) {
                    taskInfostatic = addedTask;
                    todolist.addTaskToDoList(addedTask);
                    edit = true;
                    sendTaskInfoToServer();
                    titleOfTask.setText("");
                 //  JSONObject notificationDataJsonObject= ListAdapter.getNotificationObjectAsJson();
                  // sendNotificationToDataBase(notificationDataJsonObject);
                    ((Stage) taskData.getScene().getWindow()).close();
                }

            } catch (IOException ex) {
                showAleart(Alert.AlertType.ERROR, "Connection lost", "Error update  List");
            }

        } else {

            CurrentTask.setTitle(titleOfTask.getText().toString());
            CurrentTask.setListId(todolist.getId());
            CurrentTask.setDescription(description.getText().toString());
            CurrentTask.setStartTime(StartDatePicker.getValue().toString());
            CurrentTask.setDeadLine(endDatePicker.getValue().toString());
            CurrentTask.setComment(comment.getText().toString());
            CurrentTask.setId(CurrentTask.getId());
            toDoTaskJsonObject = CurrentTask.writeTaskInfoObjectAsJson();
            updateInServer(toDoTaskJsonObject);
            edit = false;

            ((Stage) taskData.getScene().getWindow()).close();

        }
    }

    public static TaskInfo getTaskInfo() {
        return taskInfostatic;
    }
 public void sendNotificationToDataBase(JSONObject notificationDataJsonObject) 
   {
       Server server;
        try {
            server = new Server();
             server.post(new String[]{"Assignnotification"}, notificationDataJsonObject);
            
        } catch (IOException ex) {
 showAleart(Alert.AlertType.ERROR, "Connection lost", "Error update  List");
               }
         
   }
    private void sendTaskInfoToServer() throws IOException {
        String[] TypeOfRequest = new String[1];
        TypeOfRequest[0] = "Task";

        Server server = new Server();
        JSONObject resultJsonObject = server.post(TypeOfRequest, toDoTaskJsonObject);

    }

    public void setTodoList(ToDoList toDoList) {
        this.todolist = toDoList;
    }

    public void setTask(TaskInfo task) {
        CurrentTask = task;
    }

    

    private void showAleart(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

   

    @FXML
    private void setStartDate(Event event) {
        if (StartDatePicker.getValue() != null) {
            if (endDatePicker.getValue() != null) {
                if (endDatePicker.getValue().compareTo(StartDatePicker.getValue()) < 0) {

                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Sorry the End Date must be after the start Date");
                    alert.showAndWait();
                    return;
                }
            }

        }
    }

    @FXML
    private void setEndate(Event event) {
        if (StartDatePicker.getValue() != null) {
            if (endDatePicker.getValue() != null) {
                if (endDatePicker.getValue().compareTo(StartDatePicker.getValue()) < 0) {
                    endDatePicker.setValue(null);
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Sorry the End Date must be after the start Date");
                    alert.showAndWait();
                }

            }
        } else {
            endDatePicker.setValue(null);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please, enter StartDate first");
            alert.showAndWait();
        }
    }

    public void setTaskView(TaskInfo task) {

        titleOfTask.setText(task.getTitle());
        final DateTimeFormatter datetimeformater = DateTimeFormatter.ofPattern("yyyy-M-d");

        StartDatePicker.setValue(LocalDate.parse(task.getStartTime(), datetimeformater));
        endDatePicker.setValue(LocalDate.parse(task.getDeadLine(), datetimeformater));

        description.setText(task.getDescription());
        comment.setText(task.getComment());
    }

    boolean isEdited() {
        return edit;
    }

    private int updateInServer(JSONObject json) {

        try {
            Server server = new Server();
            int response = server.put(new String[]{"task"}, json);
            return response;

        } catch (IOException ex) {
            showAleart(Alert.AlertType.ERROR, "Connection lost", "Error update  List");
            return -1;
        }

    }

}
