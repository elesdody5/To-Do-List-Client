/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.to_do_list;

import Entity.User;
import home.Notifications;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import server_request.Server;

/**
 *
 * @author sara
 */
public class ListAdapterOfTaskMemberRequest extends ListCell<User> {
    TaskInfo currntTask;
       private ListView<User> listviewOfUser;
 public ListAdapterOfTaskMemberRequest(ListView<User> listviewOfUser) {
        this.listviewOfUser = listviewOfUser;
    }
     @Override
    protected void updateItem(User user, boolean empty) {
            super.updateItem(user, empty);
          currntTask=ListAdapterOfTasksList.getCurrntTask();
            if (user != null) {

            ImageView image = new ImageView(new Image(getClass().getResourceAsStream("personIcon.png")));
            image.setFitHeight(30);
            image.setFitWidth(30);
            Button btn=new Button("remove");
            setText(user.getUserName());
            setGraphic(btn);
            btn.setOnAction(new EventHandler() {
                @Override
                public void handle(Event event) {
                   remove(user); 
                }

                           });
            }

    }
   
     private void showAleart(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
      private void remove(User user) {
             try {
            Server server = new Server();
            int response = server.delete(new String[]{"teammember",String.valueOf(user.getId())});
            if (response != -1) {
                listviewOfUser.getItems().remove(user);
                
                showAleart(Alert.AlertType.INFORMATION, "Done ", "deleted Succefully");
            } else {
                showAleart(Alert.AlertType.ERROR, "Error ", "cann't delete todo");
            }
        } catch (IOException ex) {
            showAleart(Alert.AlertType.ERROR, "Connection lost", "Error update  List");

        }
                }

     
    
}
