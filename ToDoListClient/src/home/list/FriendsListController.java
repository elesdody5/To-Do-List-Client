/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.list;

import Entity.User;
import authontication.LoginController;
import home.NotificationKeys;
import home.menu_bar.ConnectWithController_MenuBar;
import home.menu_bar.ConnectWithLoginView_MenuBar;
import home.to_do_list.ToDoList;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
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
    private ToDoList todo;
    private ArrayList<User> collab;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void shareList(MouseEvent e) {
        JSONObject request = new JSONObject();
                    JSONArray notificationJsonArray = new JSONArray();
                    JSONArray removedFriends = new JSONArray();
                    try {
                        // to get friends that assign to todo 
                        for (int i = 1; i < friendsListView.getItems().size(); i++) {
                            CheckBox box = friendsListView.getItems().get(i);
                            if (box.isSelected()) {
                                // if todo not have collaborator on it
                                if (collab.isEmpty()) {
                                    notificationJsonArray.put(createJson(Integer.parseInt(box.getId())));
                                }

                                for (User user : collab) {
                                    // to check if friend not in collab already
                                    if (user.getId() != Integer.parseInt(box.getId())) {
                                        System.out.println("asdf");
                                        notificationJsonArray.put(createJson(Integer.parseInt(box.getId())));
                                    }
                                }
                            }
                        }

                        request.put("notification_List", notificationJsonArray);
                        // to remove collab from todo
                        for (CheckBox box : friendsListView.getItems()) {
                            if (!box.isSelected()) {
                                removedFriends.put(new User(Integer.parseInt(box.getId()), box.getText()).getUserAsJson());
                            }
                        }
                        request.put("removed_friends", removedFriends);
                        if (notificationJsonArray.length() > 0 || removedFriends.length() > 0) {
                            try {
                                Server server = new Server();

                                server.post(new String[]{"notification"}, request);
                                ((Stage) friendsListView.getScene().getWindow()).close();
                            } catch (IOException ex) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setContentText("Connection lost ");
                                alert.showAndWait();
                            }

                        }
                    } catch (JSONException ex) {
                        System.out.println(ex.getMessage());
                    }

                }

    

    public void setFriendsList(ArrayList<User> list) {
        // add todo owner in first of list
        String ownerName = ConnectWithLoginView_MenuBar.getInastance().sendDataToView();
        CheckBox userCheckBox = new CheckBox(ownerName + " (Owner)");
        userCheckBox.setId(LoginController.UserId + "");
        userCheckBox.setSelected(true);
        userCheckBox.setDisable(true);
        // add friend to listView
        friendsListView.getItems().add(userCheckBox);
        for (User user : list) {
            CheckBox checkBox = new CheckBox(user.getUserName());
            checkBox.setId(user.getId() + "");
            friendsListView.getItems().add(checkBox);
        }
    }

    public void setCollab(ArrayList<User> list) {
        collab = list;
        if (list != null && list.size() != 0) {
            friendsListView.getItems().forEach((friendbox) -> {
                list.stream().filter((user) -> (user.getId() == Integer.parseInt(friendbox.getId()))).forEachOrdered((_item) -> {
                    friendbox.setSelected(true);
                });
            });
        }
    }

    public void setToDo(ToDoList toDo) {
        this.todo = toDo;
    }

    private JSONObject createJson(int friendId) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("fromUserId", LoginController.UserId);
        json.put("fromUserName", ConnectWithController_MenuBar.getInastance().sendDataToView());
        json.put("toUserId", friendId);
        json.put("status", NotificationKeys.NORESPONSE_NOTIFICATION_REQUEST);
        json.put("type", NotificationKeys.ADD_COLLABORATOR);
        json.put("listId", todo.getId());
        return json;
    }
}
