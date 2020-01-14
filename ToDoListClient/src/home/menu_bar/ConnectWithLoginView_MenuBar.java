/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.menu_bar;

import Entity.User;
import home.Notifications;
import java.util.List;
import home.NotificationKeys;
import java.util.ArrayList;

/**
 *
 * @author ghadeerelmahdy
 */
public class ConnectWithLoginView_MenuBar implements MenuBarModelInterface {

    private String name ;
    private String id ;
    private boolean isName;
    private List<Notifications> notifications;
    private List<User> friends;
    private List<Notifications> lists ;
     private List<Notifications> tasks;
     private List<Notifications> friendRequests;
    int[] listRequest = new int[2];
    int[] taskRequest = new int[2];
    private static ConnectWithLoginView_MenuBar instance;

    private ConnectWithLoginView_MenuBar() {
    }

    //singelton
    public static ConnectWithLoginView_MenuBar getInastance() {
        if (instance == null) {
            instance = new ConnectWithLoginView_MenuBar();
        }
        return instance;
    }

    // setters from home of name and id
    public void setUserName(String name) {
        isName = true;
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }
//set new requests

    public void setNotificationRequest(Notifications obj) {
        if (obj.getType() == NotificationKeys.ADD_COLLABORATOR) {
            showListRequest(obj);
        } else if (obj.getType() == NotificationKeys.ASSIGIN_TASK_MEMBER) {
            showTaskRequest(obj);
        } else if (obj.getType() == NotificationKeys.REQUEST_FRIEND) {
            showFriendRequest(obj);
        }
    }

    private void showListRequest(Notifications obj) {  
        MenuBarController con = new MenuBarController();
        con.setListRequest(obj);
    }

    private void showTaskRequest(Notifications obj) {
        MenuBarController con = new MenuBarController();
        con.setTaskRequest(obj);

    }

    private void showFriendRequest(Notifications obj) {
        MenuBarController con = new MenuBarController();
        con.setFriendRequest(obj);

    }

    //load old lists

    public void loadAllLists(List<User> friends, List<Notifications> notification) {
        this.friends = friends;
        notifications = notification;
    }

    //show all old lists to view

    List<User> sendFriendListToView() {
        return friends;
    }

    List<Notifications> sendListsToView() {
        lists = new ArrayList<>();
        for (int i = 0; i < notifications.size(); i++) {
            if (notifications.get(i).getType() == NotificationKeys.ADD_COLLABORATOR) {
                lists.add(notifications.get(i));
            }

        }
        return lists;
    }

    List<Notifications> sendTasksToView() {
         tasks = new ArrayList<>();
        for (int i = 0; i < notifications.size(); i++) {
            if (notifications.get(i).getType() == NotificationKeys.ASSIGIN_TASK_MEMBER) {
                tasks.add(notifications.get(i));
            }
        }
        return tasks;
    }

    List<Notifications> sendFriendRequestToView() {
         friendRequests = new ArrayList<>();
        for (int i = 0; i < notifications.size(); i++) {
            if (notifications.get(i).getType() == NotificationKeys.REQUEST_FRIEND) {
                friendRequests.add(notifications.get(i));
            }
        }
        return friendRequests;
    }

    @Override
    public String sendDataToView() {
        return name;

    }

    @Override
    public String sendIdToView() {
        return id;
    }

}
