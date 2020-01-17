/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home;

import home.list.FXMLListController;
import java.sql.DriverManager;
import javafx.fxml.FXMLLoader;

/**
 *
 * @author Elesdody
 */
public class View {

    private View() {
        todoLoader = new FXMLLoader(getClass().getResource("/home/to_do_list/ToDoList.fxml"));
        listloader = new FXMLLoader(getClass().getResource("/home/list/FXMLList.fxml"));
        onlineFriendsLoader = new FXMLLoader(getClass().getResource("/onlineFriends/OnlineFriends.fxml"));
        

    }
    private static FXMLLoader todoLoader;
    private static FXMLLoader listloader;
    private static FXMLLoader onlineFriendsLoader;

    public static FXMLLoader getTodoLoader() {
        if (todoLoader == null) {
            synchronized (View.class) {
                new View();
            }
        }
        return todoLoader;
    }

    public static FXMLLoader getListLoader() {
        if (todoLoader == null) {
            synchronized (View.class) {
                new View();
            }
        }
        return listloader;
    }
     public static FXMLLoader getOnlineListLoader() {
        if (onlineFriendsLoader == null) {
            synchronized (View.class) {
                new View();
            }
        }
        return onlineFriendsLoader;
    }
}
