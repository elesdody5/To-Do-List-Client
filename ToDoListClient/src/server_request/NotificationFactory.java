/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_request;

import Enum.REQUEST;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import home.Notifications;
import home.to_do_list.TaskInfo;
import home.to_do_list.ToDoList;
import org.json.JSONObject;

/**
 *
 * @author Elesdody
 */
public class NotificationFactory {

    public static Object getNotificationObject(String type, JSONObject json) {
        switch (type) {
            case REQUEST.NOTIFICATION:
                return createNotification(json);
            case REQUEST.TASK:
                return createTask(json);
            case REQUEST.TODO:
                return createToDo(json);

        }
        
        return null;
    }

    private static Notifications createNotification(JSONObject json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json.toString(), Notifications.class);

    }

    private static TaskInfo createTask(JSONObject json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json.toString(), TaskInfo.class);
    }

    private static ToDoList createToDo(JSONObject json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json.toString(), ToDoList.class);
    }

}
