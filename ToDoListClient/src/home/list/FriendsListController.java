/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.list;

import Entity.User;
import authontication.LoginController;
import home.NotificationKeys;
import home.to_do_list.ToDoList;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javax.management.Notification;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import server_request.Server;

/**
 * FXML Controller class
 *
 * @author Elesdody
 */
public class FriendsListController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private ListView<CheckBox> friendsListView;
    private int listId;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void shareList(MouseEvent e) {
        JSONObject request = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try{
        for (CheckBox box : friendsListView.getItems()) {
            if (box.isSelected()) {
                jsonArray.put(createJson(Integer.parseInt(box.getId())));
            }
        }
        request.put("notification_List", jsonArray);
        if (jsonArray.length()>1) {
            try {
                Server server = new Server();

                server.post(new String[]{"notification"}, request);
                ((Stage)friendsListView.getScene().getWindow()).close();
            } catch (IOException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Connection lost ");
                alert.showAndWait();
            }

        }
        } catch(JSONException ex )
        {
            System.out.println(ex.getMessage());
        }
            

    }

    public void setFriendsList(ArrayList<User> list) {
        for (User user : list) {
            CheckBox checkBox = new CheckBox(user.getUserName());
            checkBox.setId(user.getId() + "");
            friendsListView.getItems().add(checkBox);
        }
    }

    void setToDoid(int listId) {
        this.listId = listId;
    }

    private JSONObject createJson(int friendId) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("fromUserId", LoginController.UserId);
        json.put("toUserId", friendId);
        json.put("type", NotificationKeys.ADD_COLLABORATOR);
        json.put("listId", listId);
        return json;
    }
}
