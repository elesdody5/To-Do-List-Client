/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.list.Adapter;

import Entity.User;
import authontication.LoginController;
import home.View;
import home.list.FXMLListController;
import home.list.FriendsListController;
import home.list.ToDoForm;
import home.to_do_list.ToDoList;
import home.to_do_list.ToDoListController;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.json.JSONException;
import org.json.JSONObject;
import server_request.Server;

/**
 *
 * @author Elesdody
 */
public abstract class Adapter extends ListCell<ToDoList> {

    protected ListView<ToDoList> itemListView;
    private ArrayList<User> friends;
    protected ArrayList<ToDoList> todoList;
    private ToDoListController toDoListController;

    public Adapter(ListView<ToDoList> itemListView, ArrayList<User> friends, ArrayList<ToDoList> todoList) {
        this.itemListView = itemListView;
        this.friends = friends;
        this.todoList = todoList;
        FXMLLoader loader = View.getTodoLoader();
        this.toDoListController = loader.getController();
    }

    @Override
    protected void updateItem(ToDoList item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null && !empty) {
            ImageView image = new ImageView(new Image(getClass().getResourceAsStream("lists.png")));
            image.setFitHeight(30);
            image.setFitWidth(30);
            setGraphic(image);
            setText(item.getTitle());
            setContextMenu(createContextMenu(item));

        } else {

            setGraphic(null);
            setText(null);
        }

    }

    protected ContextMenu createContextMenu(ToDoList item) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem edit = new MenuItem("Edit");
        MenuItem share = new MenuItem("Share");
        MenuItem delete = new MenuItem("Delete");
        contextMenu.getItems().addAll(edit, share, delete);
        edit.setOnAction((ActionEvent event) -> {
            try {
                openForm(item);
            } catch (IOException ex) {
                Logger.getLogger(MyListAdapter.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        delete.setOnAction((ActionEvent event) -> {
            delete(item);
        });
        share.setOnAction((ActionEvent event) -> {
            try {
                openShareList(item);
            } catch (IOException ex) {
                Logger.getLogger(MyListAdapter.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        return contextMenu;
    }

    protected void openForm(ToDoList todo) throws IOException {

        // to open form to add new todo to list 
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/home/list/ListForm.fxml"));
        Parent form = loader.load();
        ToDoForm toDoForm = loader.getController();
        Scene scene = new Scene(form);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.UTILITY);
        stage.initOwner(getScene().getWindow());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.show();
        if (todo != null) {
            // to update form ui
            toDoForm.setToDo(todo);
        }

        stage.setOnHidden((WindowEvent event) -> {
            try {
                if (toDoForm.isEdited()) {
                    int response = updateInServer(createJson(todo));
                    if (response != -1) {
                        updateItem(todo, false);
                        showAleart(Alert.AlertType.INFORMATION, "Done ", "updated Succefully");
                        setCurrentTodo(todo);
                    } else {
                        showAleart(Alert.AlertType.ERROR, "Error ", "can't update todo");
                    }

                }
            } catch (JSONException ex) {
                Logger.getLogger(FXMLListController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    protected int updateInServer(JSONObject json) {

        try {
            Server server = new Server();
            int response = server.put(new String[]{"list"}, json);
            return response;
        } catch (IOException ex) {
            showAleart(Alert.AlertType.ERROR, "Connection lost", "Error update  List");
            return -1;
        }

    }

    protected JSONObject createJson(ToDoList todo) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("id", todo.getId());
        json.put("ownerId", LoginController.UserId);
        json.put("title", todo.getTitle());
        json.put("startDate", todo.getStartTime());
        json.put("deadLine", todo.getDeadLine());
        json.put("color", todo.getColor());
        json.put("description", todo.getDescription());
        return json;
    }

    protected void showAleart(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    protected void delete(ToDoList todo) {
        try {
            Server server = new Server();
            int response = server.delete(new String[]{"list", todo.getId() + ""});
            if (response != -1) {
                itemListView.getItems().remove(todo);
                todoList.remove(todo);
                if (!todoList.isEmpty()) //set current todo first one
                {
                    setCurrentTodo(todoList.get(0));
                } else {
                    setCurrentTodo(null);
                }
                showAleart(Alert.AlertType.INFORMATION, "Done ", "deleted Succefully");
            } else {
                showAleart(Alert.AlertType.ERROR, "Error ", "cann't delete todo");
            }
        } catch (IOException ex) {
            showAleart(Alert.AlertType.ERROR, "Connection lost", "Error update  List");

        }
    }

    protected void openShareList(ToDoList todo) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/home/list/FriendsList.fxml"));
        Parent form = loader.load();
        FriendsListController friendsList = loader.getController();
        friendsList.setFriendsList(friends);
        friendsList.setToDo(todo);
        Scene scene = new Scene(form);
        Stage stage = new Stage();
        stage.setScene(scene);
//        stage.getIcons().add(new Image(getClass().getResourceAsStream("friendship.png")));
        stage.setTitle("Frineds");
        stage.initStyle(StageStyle.UTILITY);
        stage.initOwner(getScene().getWindow());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.show();
    }

    protected void setCurrentTodo(ToDoList todo) {
        this.toDoListController.updateCurrentTodo(todo);
    }
}
