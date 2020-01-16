/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.to_do_list;

import Entity.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import home.NotificationKeys;
import home.Notifications;
import home.list.*;
import home.to_do_list.ToDoList;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import server_request.Server;

/**
 *
 * @author sara
 */
public class ListAdapter extends ListCell<User> {
 static JSONObject notificationDataJsonObject;
 static List<Notifications>notificationList=new ArrayList<Notifications>();
    @Override
    protected void updateItem(User user, boolean empty) {
        super.updateItem(user, empty);
       // notificationList=new ArrayList<Notifications>();
       TaskInfo currntTask= ListAdapterOfTasksList.getCurrntTask();
        if (user != null) {

            ImageView image = new ImageView(new Image(getClass().getResourceAsStream("personIcon.png")));
            image.setFitHeight(30);
            image.setFitWidth(30);
            setGraphic(image);
            setText(user.getUserName());
            //System.out.print(user.getUserName()+"ss");
             setOnMousePressed((MouseEvent event) -> {
                   ImageView imagereq = new ImageView(new Image(getClass().getResourceAsStream("request.png")));
            imagereq.setFitHeight(30);
            imagereq.setFitWidth(30);
            setGraphic(imagereq);
            Notifications notificationData =new Notifications();
          ToDoList currntTodo=  ToDoListController.getTodoList();
          notificationData.setFromUserId(currntTodo.getOwnerId());
          notificationData.setToUserId(user.getId());
          notificationData.setType(NotificationKeys.ASSIGIN_TASK_MEMBER);
          notificationData.setStatus(NotificationKeys.NoResponse_COLLABORATOR_REQUEST);
          notificationData.setDataId(currntTask.getId());
          notificationList.add(notificationData);
                                System.out.print(notificationList.size());
    
      
         // sendNotificationToDataBase(notificationDataJsonObject);
          

            });
            /*  try {
               notificationDataJsonObject= writeTaskInfoObjectAsJson(notificationList);
                } catch (JSONException ex) {
                    Logger.getLogger(ListAdapter.class.getName()).log(Level.SEVERE, null, ex);
                }*/
        }
        
         
          //   System.out.println(notificationDataJsonObject.toString());
        

    }
    public static JSONObject getNotificationObjectAsJson() throws JSONException
    {
       
               notificationDataJsonObject= writeTaskInfoObjectAsJson(notificationList);
               
       return  notificationDataJsonObject;
    }
   public static JSONObject writeTaskInfoObjectAsJson(List<Notifications> notificationData) throws JSONException {
                 Gson gson = new GsonBuilder().create();
                String notificationArray = gson.toJson(notificationData);
                 JSONArray NotificationjsonArray = new JSONArray(notificationArray);
                 JSONObject jsonObjectOfNotifications = new JSONObject();
                jsonObjectOfNotifications.put("listOfNotifications", NotificationjsonArray);
                return jsonObjectOfNotifications;

/*
        JSONObject notificationDataJsonObject = new JSONObject();
        try {
            notificationDataJsonObject.put("fromUserId", notificationData.getFromUserId());
            notificationDataJsonObject.put("toUserId", notificationData.getToUserId());
            notificationDataJsonObject.put("type",notificationData.getType());
            notificationDataJsonObject.put("status", notificationData.getStatus());
            notificationDataJsonObject.put("dataId", notificationData.getDataId());
            


            
        } catch (JSONException ex) {
            Logger.getLogger(TaskInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return notificationDataJsonObject;
*/
    }
  /* public void sendNotificationToDataBase(JSONObject notificationDataJsonObject) 
   {
       Server server;
        try {
            server = new Server();
             server.post(new String[]{"Assignnotification"}, notificationDataJsonObject);
            
        } catch (IOException ex) {
 showAleart(Alert.AlertType.ERROR, "Connection lost", "Error update  List");
               }
         
   }*/
     private void showAleart(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
