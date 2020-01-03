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
import javafx.scene.Parent;

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
        try {
            Server server = new Server();
            JSONObject json = server.get(new String[]{"todo", LoginController.UserId + ""});
            System.out.println(json);
            User user = new User(json.getInt("ID"), json.getString("user name"), json.getString("password"));
            Gson gson = new GsonBuilder().create();
            Type ListType = new TypeToken<ArrayList<ToDoList>>() {
            }.getType();
            ArrayList<ToDoList> todoList = gson.fromJson(json.getJSONArray("todo_list").toString(), ListType);
// start home screen
            start(user, todoList);
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            System.out.println(ex);
        }

    }

    public void start(User user, ArrayList<ToDoList> todoList) {
        try {
            // TODO
            // create user menu bar 
            FXMLLoader menuloader = new FXMLLoader(getClass().getResource("/home/menu_bar/MenuBar.fxml"));
            Parent menuBar = menuloader.load();
            ConnectWithLoginView_MenuBar connect = ConnectWithLoginView_MenuBar.getInastance();
            connect.setUserName(user.getUserName());
            connect.setId(user.getId()+"");
            
            // create left list 
            FXMLLoader listloader = new FXMLLoader(getClass().getResource("/home/list/FXMLList.fxml"));
            VBox list = listloader.load();
            FXMLListController listController = listloader.getController();
            listController.getMyListView().getItems().addAll(todoList);
            // create todo loader and controller
            FXMLLoader todoLoader = new FXMLLoader(getClass().getResource("/home/to_do_list/ToDoList.fxml"));
            Parent todo = todoLoader.load();
            ToDoListController todoController = todoLoader.getController();
            todoController.setTodoList(todoList.get(0));
            // add component to main pane
            borderPane.setLeft(list);
            borderPane.setCenter(todo);
            borderPane.setTop(menuBar);

        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
