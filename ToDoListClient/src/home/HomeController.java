/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home;

import Entity.User;
import authontication.LoginController;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import home.list.FXMLListController;
import home.menu_bar.ConnectWithController_MenuBar;
import home.menu_bar.ConnectWithLoginView_MenuBar;
import home.to_do_list.ToDoList;
import home.to_do_list.ToDoListController;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.json.JSONException;
import org.json.JSONObject;
import server_request.Server;
import java.lang.reflect.Type;
import javafx.application.Platform;
import javafx.scene.Parent;
import server_connection.Connection;

/**
 * FXML Controller class
 *
 * @author Elesdody
 */
public class HomeController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    BorderPane borderPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

       // Platform.runLater(() -> {
            try {
                Server server = new Server();
                JSONObject json = server.get(new String[]{"todo", LoginController.UserId + ""});
                User user = new User(json.getInt("ID"), json.getString("user name"), json.getString("password"));
                Gson gson = new GsonBuilder().create();
                // convert jsonArray to frindsList
                Type frindsListType = new TypeToken<ArrayList<User>>() {
                }.getType();
                ArrayList<User> friendsList = gson.fromJson(json.getJSONArray("friends").toString(), frindsListType);

                // convert jsonArray to todoList
                Type ListType = new TypeToken<ArrayList<ToDoList>>() {
                }.getType();
                ArrayList<ToDoList> todoList = gson.fromJson(json.getJSONArray("todo_list").toString(), ListType);
                // convert shared todo
                
                ArrayList<ToDoList> sharedList = gson.fromJson(json.getJSONArray("shared_list").toString(), ListType);
               // convert notification
                Type notificationListType = new TypeToken<ArrayList<Notifications>>() {
                }.getType();
                ArrayList<Notifications> notifications = gson.fromJson(json.getJSONArray("notification").toString(), notificationListType);
                // start home screen
                System.out.println(json);
                start(user, friendsList, todoList,sharedList,notifications);
            } catch (IOException ex) {
                Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JSONException ex) {
                System.out.println(ex);
            }
       // });

    }

    public void start(User user, ArrayList<User> friendsList, ArrayList<ToDoList> todoList,ArrayList<ToDoList> sharedList,ArrayList<Notifications> notifications) {
        try {
            // TODO
            // create user menu bar 
            ConnectWithLoginView_MenuBar connect = ConnectWithLoginView_MenuBar.getInastance();
            connect.setUserName(user.getUserName());
            connect.setId(user.getId() + "");
            //System.out.println(notifications.get(0).getfromUserName());
            FXMLLoader menuloader = new FXMLLoader(getClass().getResource("/home/menu_bar/MenuBar.fxml"));
            Parent menuBar = menuloader.load();

            // create left list 
            FXMLLoader listloader = new FXMLLoader(getClass().getResource("/home/list/FXMLList.fxml"));
            VBox list = listloader.load();
            FXMLListController listController = listloader.getController();
            listController.getMyListView().getItems().addAll(todoList);
            listController.getFriendsList().addAll(friendsList);
            listController.getSharedListView().getItems().addAll(sharedList);
            // create todo loader and controller
            FXMLLoader todoLoader = new FXMLLoader(getClass().getResource("/home/to_do_list/ToDoList.fxml"));
            Parent todo = todoLoader.load();
            ToDoListController todoController = todoLoader.getController();
            if (todoList.size() >= 1) {
                ToDoList currentTodo = todoList.get(0);
                currentTodo.addObserver(todoController);
                todoController.setTodoList(currentTodo);
                listController.setCurrentToDo(currentTodo);
            }

            // add component to main pane
            borderPane.setLeft(list);
            borderPane.setCenter(todo);
            borderPane.setTop(menuBar);

        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
