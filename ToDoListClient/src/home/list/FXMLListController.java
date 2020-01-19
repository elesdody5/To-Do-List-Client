/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.list;

import home.list.Adapter.ShareListAdapter;
import home.list.Adapter.MyListAdapter;
import Entity.User;
import authontication.LoginController;
import home.to_do_list.ToDoList;
import home.to_do_list.ToDoListController;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
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
public class FXMLListController implements Initializable {

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
    // refrence to todo view to send current todo
    private ToDoListController toDoListController;
    private ArrayList<ToDoList> myTodos;
    private ArrayList<ToDoList> sharedTodos;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        friendsList = new ArrayList<>();
        myTodos = new ArrayList<>();
        currentToDo= new ToDoList();
        sharedTodos = new ArrayList<>();
        myListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        saredListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        searchField.textProperty().addListener((observable, oldVal, newVal) -> {
            search();
        });
        myListView.setCellFactory((param)
                -> {
            return new MyListAdapter(param, friendsList, myTodos);
        });
        saredListView.setCellFactory((param)
                -> {
            return new ShareListAdapter(param, friendsList, sharedTodos);
        });
        myListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (!myListView.getItems().isEmpty()) {
                int index = myListView.getSelectionModel().getSelectedIndex();
                upadteCurrentTdo(myTodos.get(index));
                Platform.runLater(() -> {
                    saredListView.getItems().clear();
                    saredListView.getItems().addAll(sharedTodos);
                });
            }

        });

        saredListView.getSelectionModel().selectedItemProperty().addListener((observable) -> {
            if (!saredListView.getItems().isEmpty()) {
                int index = saredListView.getSelectionModel().getSelectedIndex();
                upadteCurrentTdo(sharedTodos.get(index));
                Platform.runLater(() -> {
                    myListView.getItems().clear();
                    myListView.getItems().addAll(myTodos);
                });

            }
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
                if (toDoForm.getToDo().getTitle() != null) {
                    try {
                        boolean result = addToServer(createJson(toDoForm.getToDo()));
                        if (result) {
                            myTodos.add(toDoForm.getToDo());
                            myListView.getItems().add(toDoForm.getToDo());
                            setCurrentToDo(toDoForm.getToDo());
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

    private void search() {
        String text = searchField.getText();

        if (text != null && !text.isEmpty()) {

            myListView.getItems().clear();
            saredListView.getItems().clear();
            for (ToDoList todo : myTodos) {
                if (todo.getTitle().contains(text)) {
                    myListView.getItems().add(todo);
                }
            }
            for (ToDoList todo : sharedTodos) {
                if (todo.getTitle().contains(text)) {
                    saredListView.getItems().add(todo);
                }
            }

        } else {
            System.out.println(myTodos.size());
            myListView.getItems().clear();
            myListView.getItems().addAll(myTodos);
            saredListView.getItems().clear();
            saredListView.getItems().addAll(sharedTodos);
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

    public void setToDoList(ArrayList<ToDoList> todo) {
        myTodos.addAll(todo);
        myListView.getItems().addAll(todo);

    }

    public void setShareList(ArrayList<ToDoList> todo) {
        sharedTodos.addAll(todo);
        saredListView.getItems().addAll(todo);
    }

    public void setCurrentToDo(ToDoList currentToDo) {

        this.currentToDo = currentToDo;

    }

    // add share todo to list at real time
    public void addSharedList(ToDoList todo) {
        sharedTodos.add(todo);
        saredListView.getItems().add(todo);
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

    public void setToDoController(ToDoListController controller) {
        this.toDoListController = controller;
    }

    private void upadteCurrentTdo(ToDoList selectedItem) {
        toDoListController.updateCurrentTodo(selectedItem);
    }
}
