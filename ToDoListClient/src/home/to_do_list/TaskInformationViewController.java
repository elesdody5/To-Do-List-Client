/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.to_do_list;

import Entity.User;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXScrollPane;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
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
public class TaskInformationViewController implements Initializable, Observer {

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
    private JFXButton checkBoxButton;
    @FXML
    private JFXDatePicker StartDatePicker;
    @FXML
    private JFXDatePicker endDatePicker;
    @FXML
    private JFXTextArea description;
    @FXML
    private JFXTextArea comment;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // TODO
        memberList.setVisible(false);
        User user = new User();
        ToDoList todolist = new ToDoList();
        ArrayList<User> teamMateInToDo = null;
        try {
            teamMateInToDo = getTeamMemberInToDo();
            for (int i = 0; i < teamMateInToDo.size(); i++) {
                ListViewOfMember.getItems().add(teamMateInToDo.get(i));
                ListViewOfMember.setCellFactory((param)
                        -> {
                    return new ListAdapter();
                });
            }
        } catch (IOException ex) {
            Logger.getLogger(TaskInformationViewController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(TaskInformationViewController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void addMember(ActionEvent event) {
        if (memberList.isVisible()) {
            memberList.setVisible(false);
            checkBoxButton.setVisible(true);
            StartDatePicker.setVisible(true);
            endDatePicker.setVisible(true);

        } else {
            memberList.setVisible(true);
            checkBoxButton.setVisible(false);
            StartDatePicker.setVisible(false);
            endDatePicker.setVisible(false);
        }

    }

    private ArrayList<User> getTeamMemberInToDo() throws IOException, JSONException {
        String[] typrOfRequest = new String[1];
        typrOfRequest[0] = "getTeamMemberInToDo";
        Server server = new Server();
        JSONObject resultOfGetTeamMember = server.get(typrOfRequest);
        JSONArray jsonArrayOfTeamMeber = resultOfGetTeamMember.getJSONArray("listOfTeamMember");
        ArrayList<User> teamMemberInfoList = new ArrayList<User>();
        for (int i = 0; i < jsonArrayOfTeamMeber.length(); i++) {
            JSONObject teammember = jsonArrayOfTeamMeber.getJSONObject(i);
            String username = teammember.getString("userName");
            //  System.out.println(username);

            User TeamMember = new User();
            TeamMember.setUserName(username);
            teamMemberInfoList.add(TeamMember);

        }
        // System.out.println(jsonArrayOftodotasks);
        return teamMemberInfoList;

    }

    @FXML
    private void saveTaskData(ActionEvent event) {
        if (!titleOfTask.getText().toString().equals("")&&StartDatePicker.getValue()!=null&&endDatePicker.getValue()!=null) {
            try {
                todolist = ToDoListController.getTodoList();
                TaskInfo taskInfo = new TaskInfo(titleOfTask.getText().toString(), todolist.getId());
                taskInfo.setDescription(description.getText().toString());
                taskInfo.setStartTime(StartDatePicker.getValue().toString());
                taskInfo.setDeadLine(endDatePicker.getValue().toString());
                taskInfo.setComment(comment.getText().toString());
                taskInfostatic = taskInfo;
                todolist.addTaskToDoList(taskInfo);
                toDoTaskJsonObject = taskInfo.writeTaskInfoObjectAsJson();
                sendTaskInfoToServer();
                titleOfTask.setText("");
                ((Stage) taskData.getScene().getWindow()).close();

            } catch (IOException ex) {
                System.out.println("server Connection loss");
            }
        }
    }

    public static TaskInfo getTaskInfo() {
        return taskInfostatic;
    }

    private void sendTaskInfoToServer() throws IOException {
        String[] TypeOfRequest = new String[1];
        TypeOfRequest[0] = "Task";

        Server server = new Server();
        JSONObject resultJsonObject = server.post(TypeOfRequest, toDoTaskJsonObject);

    }

    public void setTodoList(ToDoList toDoList) {
        this.todolist = toDoList;
        System.out.println(toDoList.getTitle());
    }

    @Override
    public void update(Observable o, Object o1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

    }

    @FXML
    private void getIntialState(MouseEvent event) {
        memberList.setVisible(false);
        checkBoxButton.setVisible(true);
        StartDatePicker.setVisible(true);
        endDatePicker.setVisible(true);
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

}
