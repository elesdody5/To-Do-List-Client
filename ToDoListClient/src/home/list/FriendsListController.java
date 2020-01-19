/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.list;

import Entity.User;
import authontication.LoginController;
import home.NotificationKeys;
import home.Notifications;
import home.View;
import home.menu_bar.ConnectWithController_MenuBar;
import home.menu_bar.ConnectWithLoginView_MenuBar;
import home.to_do_list.ToDoList;
import home.to_do_list.ToDoListController;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
    private ToDoListController toDoListController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        FXMLLoader loader = View.getTodoLoader();
        toDoListController = loader.getController();

    }

    @FXML
    private void shareList(MouseEvent e) {
        JSONObject request = new JSONObject();
        JSONArray notificationJsonArray = new JSONArray();
        JSONArray removedFriendsJsonArray = new JSONArray();
        ArrayList<User> remoedFriends = new ArrayList<>();
        try {
            // to get friends that assign to todo 
            for (int i = 1; i < friendsListView.getItems().size(); i++) {
                CheckBox box = friendsListView.getItems().get(i);
                if (box.isSelected()) {
                    // if todo not have collaborator on it
                    if (todo.getCollaborator().isEmpty()) {
                        notificationJsonArray.put(createJson(Integer.parseInt(box.getId())));
                    }

                    for (User user : todo.getCollaborator()) {
                        // to check if friend not in collab already
                        if (user.getId() != Integer.parseInt(box.getId())) {
                            notificationJsonArray.put(createJson(Integer.parseInt(box.getId())));
                        }
                    }
                }
            }

            request.put("notification_List", notificationJsonArray);
            // to remove collab from todo
            for (CheckBox box : friendsListView.getItems()) {
                if (!box.isSelected()) {
                    remoedFriends.add(new User(Integer.parseInt(box.getId()), box.getText()));
                }
            }
            for (User user : remoedFriends) {
                removedFriendsJsonArray.put(user.getUserAsJson());
            }
            request.put("removed_friends", removedFriendsJsonArray);
            request.put("todoId", todo.getId());
            if (notificationJsonArray.length() > 0 || removedFriendsJsonArray.length() > 0) {
                try {
                    ((Stage) friendsListView.getScene().getWindow()).close();
                  
                    Server server = new Server();

                    JSONObject result = server.post(new String[]{"notification"}, request);
                    if (!result.has("Error")) {
                        ArrayList<User> removedCollab = new ArrayList<>();
                        
                        remoedFriends.forEach((user) -> {
                            todo.getCollaborator().stream().filter((collab) -> (user.getId()==collab.getId())).forEachOrdered((collab) -> {
                                removedCollab.add(collab);
                            });
                        });
                        todo.getCollaborator().remove(removedCollab);

                        toDoListController.updateCurrentTodo(todo);

                    }
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
        CheckBox userCheckBox = new CheckBox(" (Owner)");
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

    private void setCollab(ArrayList<User> list) {
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
        setCollab(toDo.getCollaborator());
    }

    private JSONObject createJson(int friendId) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("fromUserId", LoginController.UserId);
        json.put("fromUserName", ConnectWithLoginView_MenuBar.getInastance().sendDataToView());
        json.put("toUserId", friendId);
        json.put("status", NotificationKeys.NORESPONSE_NOTIFICATION_REQUEST);
        json.put("type", NotificationKeys.ADD_COLLABORATOR);
        json.put("listId", todo.getId());
        
        return json;
    }
}
