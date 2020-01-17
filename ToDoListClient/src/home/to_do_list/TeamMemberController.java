/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.to_do_list;

import Entity.User;
import com.jfoenix.controls.JFXButton;
import home.Notifications;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
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
    @FXML
    private ListView listViewOfassignedTeamMember;
    List<User>teamMemberAssigned;
    List<Notifications> notificationsList;  
    TaskInfo currntTask;
    Server server = null;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
            try {
            server = new Server();
        } catch (IOException ex) {
            showAleart(Alert.AlertType.ERROR, "Connection lost", "Error update  List");
        }
                  currntTask=ListAdapterOfTasksList.getCurrntTask();

        teamMemberAssigned=new ArrayList<User>();
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
        try {
            //////
            /*   try {
            notificationsList=getNotification();
            for (int i = 0; i < notificationsList.size(); i++) {
            if(notificationsList.get(i).getStatus()==1)
            {
            User user;
            String[] typrOfRequest = new String[2];
            typrOfRequest[0] = "getUser";
            typrOfRequest[1] = String.valueOf(notificationsList.get(i).getToUserId());
            JSONObject resultOfGetUser= server.get(typrOfRequest);
            String userName=resultOfGetUser.getString("user name");
            int id =notificationsList.get(i).getToUserId();
            user =new User();
            user.setUserName(userName);
            user.setId(id);
            teamMemberAssigned.add(user);
            listViewOfTeamMember.getItems().remove(user);
            // System.out.println(user.getUserName());
            }
            }
            
            } catch (JSONException ex) {
            Logger.getLogger(TeamMemberController.class.getName()).log(Level.SEVERE, null, ex);
            }*/
            teamMemberAssigned= getTaskMemberInToDo();
        } catch (JSONException ex) {
            Logger.getLogger(TeamMemberController.class.getName()).log(Level.SEVERE, null, ex);
        }
            if (teamMemberAssigned != null) {
                System.out.print("fddddd");
                for (int i = 0; i < teamMemberAssigned.size(); i++) {
                    listViewOfassignedTeamMember.getItems().add(teamMemberAssigned.get(i));
                    
                    listViewOfassignedTeamMember.setCellFactory((param)
                            -> {
                        return new ListAdapterOfTaskMemberRequest((ListView<User>) param);
                    });
                }
            }
        
        //////
          listViewOfTeamMember.setOnMouseClicked(new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {
         //   User teamMember= (User) listViewOfTeamMember.getSelectionModel().getSelectedItem();
         //   teamMemberAssigned.add(teamMember);
        }
    });
    
      
    }
/**/
     private ArrayList<User> getTaskMemberInToDo() throws JSONException {
       
        String[] typrOfRequest = new String[2];
        typrOfRequest[0] = "getTaskMemberInToDo";
        typrOfRequest[1] = String.valueOf(currntTask.getId());
        
      
        JSONObject resultOfGetTeamMember = server.get(typrOfRequest);
        ArrayList<User> taskMemberInfoList = null;
        if (resultOfGetTeamMember != null) {
            JSONArray jsonArrayOfTeamMeber = resultOfGetTeamMember.getJSONArray("listOfTaskMember");
            taskMemberInfoList = new ArrayList<User>();
            for (int i = 0; i < jsonArrayOfTeamMeber.length(); i++) {
                JSONObject taskMember = jsonArrayOfTeamMeber.getJSONObject(i);
              
                String username = taskMember.getString("userName");
                int userId = (int) taskMember.get("id");

                User TeamMember = new User();
                TeamMember.setUserName(username);
                TeamMember.setId(userId);
                taskMemberInfoList.add(TeamMember);

            }
        }
        // System.out.println(jsonArrayOftodotasks);
        return taskMemberInfoList;

    } 
    /**/
   private ArrayList<User> getTeamMemberInToDo() throws JSONException {
       
        String[] typrOfRequest = new String[2];
        typrOfRequest[0] = "getTeamMemberInToDo";
        typrOfRequest[1] = String.valueOf(todolist.getId());
        
      
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
    public List<Notifications> getNotification() throws JSONException
    {
           String[] typrOfRequest = new String[2];
        typrOfRequest[0] = "getnotificationInTask";
        typrOfRequest[1] = String.valueOf(currntTask.getId());
        Server server = null;
        try {
            server = new Server();
        } catch (IOException ex) {
            showAleart(Alert.AlertType.ERROR, "Connection lost", "Error update  List");
        }
        JSONObject resultOfGetNotifications = server.get(typrOfRequest);
        ArrayList<Notifications> listOfnotification = null;
        if (resultOfGetNotifications != null) {
            JSONArray jsonArrayOfNotification = resultOfGetNotifications.getJSONArray("listOfNotifications");
            listOfnotification = new ArrayList<Notifications>();
            for (int i = 0; i < jsonArrayOfNotification.length(); i++) {
                JSONObject notifications = jsonArrayOfNotification.getJSONObject(i);
              
                int id = notifications.getInt("id");
                 int fromUserId = notifications.getInt("fromUserId");
                   int toUserId = notifications.getInt("toUserId");
                    int type = notifications.getInt("type");
                     int status = notifications.getInt("status");

                Notifications notification = new Notifications();
                notification.setId(id);
                notification.setFromUserId(fromUserId);
                notification.setToUserId(toUserId);
                notification.setStatus(status);
                notification.setType(type);
                listOfnotification.add(notification);

            }
        }
        // System.out.println(jsonArrayOftodotasks);
        return listOfnotification;
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
        for(int i =0;i<teamMemberAssigned.size();i++)
        {  listViewOfTeamMember.getItems().remove(teamMemberAssigned.get(i));
         //  listViewOfassignedTeamMember.getItems().add(teamMemberAssigned.get(i));
        }
    teamMemberAssigned=new ArrayList<User>();
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
