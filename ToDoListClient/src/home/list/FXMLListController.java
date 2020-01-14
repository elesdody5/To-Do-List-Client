/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.list;

import Entity.User;
import authontication.LoginController;
import home.to_do_list.ToDoList;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.json.JSONException;
import org.json.JSONObject;
import server_request.Server;

/**
 * FXML Controller class
 *
 * @author Elesdody
 */
public class FXMLListController implements Initializable  {

    /**
     * Initializes the controller class.
     */
    @FXML
    ListView<ToDoList> myListView;
    @FXML
    ListView<ToDoList> saredListView;
    @FXML
    TextField searchField;
    @FXML
    VBox vBox;
    private ToDoList currentToDo;
    private ArrayList<Entity.User> friendsList;
    public interface ShareToDo
    {
        void addShareTodo(ToDoList todo);
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        friendsList = new ArrayList<>();
        myListView.setCellFactory((param)
                -> {
            return new ListAdapter(param, friendsList);
        });
        saredListView.setCellFactory((param)
                -> {
            return new ListAdapter(param, friendsList);
        });
        myListView.getSelectionModel().selectedItemProperty().addListener((observable) -> {
          upadteCurrentTdo(myListView.getSelectionModel().getSelectedItem());
        });
       
    }

    @FXML

    private void AddToDo(MouseEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/home/list/ListForm.fxml"));
            Parent form = loader.load();
            ToDoForm toDoForm = loader.getController();
            Scene scene = new Scene(form);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.initOwner(myListView.getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();

            stage.setOnHidden((WindowEvent event) -> {
                if (toDoForm.getToDo() != null) {
                    try {
                        currentToDo = toDoForm.getToDo();
                        boolean result = addToServer(createJson(currentToDo));
                        if (result) {
                            myListView.getItems().add(currentToDo);
                        }
                    } catch (JSONException ex) {
                        Logger.getLogger(FXMLListController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        } catch (IOException ex) {
            Logger.getLogger(FXMLListController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean addToServer(JSONObject json) throws JSONException {
        try {
            Server server = new Server();
            JSONObject jsonResponse = server.post(new String[]{"list"}, json);
            if (jsonResponse.has("id")) {
                currentToDo.setId(jsonResponse.getInt("id"));
                return true;
            } else {
                return false;
            }
        } catch (IOException ex) {
            showAleart(AlertType.ERROR, "Connection lost", "Error adding new List");
            return false;
        }
    }

    private JSONObject createJson(ToDoList todo) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("ownerId", LoginController.UserId);
        json.put("title", todo.getTitle());
        json.put("startDate", todo.getStartTime());
        json.put("deadLine", todo.getDeadLine());
        json.put("color", todo.getColor());
        json.put("description", todo.getDescription());
        return json;
    }

    public ListView<ToDoList> getMyListView() {
        return myListView;
    }
    public ListView<ToDoList> getSharedListView()
    {
        return saredListView;
    }
    public void setCurrentToDo(ToDoList currentToDo) {
        this.currentToDo = currentToDo;
    }

    private void showAleart(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public ArrayList<User> getFriendsList() {
        return friendsList;
    }

    private void upadteCurrentTdo(ToDoList selectedItem) {
        currentToDo.setId(selectedItem.getId());
        currentToDo.setOwnerId(selectedItem.getOwnerId());
        currentToDo.setTitle(selectedItem.getTitle());
        currentToDo.setColor(selectedItem.getColor());
        currentToDo.setDescription(selectedItem.getDescription());
        currentToDo.setStartTime(selectedItem.getStartTime());
        currentToDo.setTasksInTODOList(selectedItem.getTasksInTODOList());
        currentToDo.notifyList();
        }
}
