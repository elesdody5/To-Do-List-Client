/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.to_do_list;

import Entity.User;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import server_request.Server;

/**
 * FXML Controller class
 *
 * @author sara
 */
public class TeamMemberController implements Initializable {

    @FXML
    private ListView listViewOfTeamMember;
    private ToDoList todolist;
    @FXML
    private JFXButton assignTeamMemberToTask;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
                todolist = ToDoListController.getTodoList();

        // TODO
            ToDoList todolist = new ToDoList();
        ArrayList<User> teamMateInToDo = null;
        try {
            teamMateInToDo = getTeamMemberInToDo();
            if (teamMateInToDo != null) {
                for (int i = 0; i < teamMateInToDo.size(); i++) {
                    listViewOfTeamMember.getItems().add(teamMateInToDo.get(i));
                    listViewOfTeamMember.setCellFactory((param)
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
        // System.out.println(jsonArrayOftodotasks);
        return teamMemberInfoList;

    }   
    private void showAleart(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void assignTeamMembertoTask(ActionEvent event) throws JSONException {
        
        JSONObject notificationDataJsonObject= ListAdapter.getNotificationObjectAsJson();
        sendNotificationToDataBase(notificationDataJsonObject);
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
    
}
