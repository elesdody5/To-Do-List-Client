/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.menu_bar;

import Utility.AlertDialog;
import java.io.IOException;
import server_request.Server;
import org.json.JSONException;
import org.json.JSONObject;
import server_connection.Connection;
import home.NotificationKeys;
import home.Notifications;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ghadeerelmahdy
 */
public class ConnectWithController_MenuBar implements MenuBarModelInterface {

    private boolean isName, isRequestAccepted, isTask, isFriendRequest, isPassword, isRequestRejected;
    private int status = -1;
    private String name;
    private String password;
    static Server s;
    private static ConnectWithController_MenuBar instance;
    Notifications request = new Notifications();
    String friendRequestName, resultFriendRequest, keyRequestAccept;

    private ConnectWithController_MenuBar() {

    }

    //singelton
    public static ConnectWithController_MenuBar getInastance() {
        if (instance == null) {
            instance = new ConnectWithController_MenuBar();
        }

        return instance;
    }

    //setter to controller
    public void setNewName(String name) {
        this.name = name;
        isName = true;
        sendDataToController();
    }

    public void setNewPassword(String password) {
        this.password = password;
        isPassword = true;
        sendDataToController();
    }

    public void sendNotificationResponse(Notifications req) {
        if (req.getStatus() == NotificationKeys.ACCEPET_NOTIFICATION_REQUEST) {
            //update notif id with status equal 1
            //insert into coll table
            isRequestAccepted = true;
            request = req;
            if (req.getType() == NotificationKeys.ADD_COLLABORATOR) {
                keyRequestAccept = "collaborator";
            } else if (req.getType() == NotificationKeys.ASSIGIN_TASK_MEMBER) {
                keyRequestAccept = "taskMember";
            } else if (req.getType() == NotificationKeys.REQUEST_FRIEND) {
                keyRequestAccept = "friend";
            }
            sendDataToController();
        } else if (req.getStatus() == NotificationKeys.REJECT_NOTIFICATION_REQUEST) {
            //update notif id with status equal 0
            isRequestRejected = true;
            request = req;
            sendDataToController();
        }

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
        MenuBarController instance = MenuBarController.getInastance();
        instance.setListRequest(obj);
    }

    private void showTaskRequest(Notifications obj) {
        MenuBarController instance = MenuBarController.getInastance();
        instance.setTaskRequest(obj);

    }

    private void showFriendRequest(Notifications obj) {
        MenuBarController instance = MenuBarController.getInastance();
        instance.setFriendRequest(obj);

    }

    public String sendFriendRequest(String friendRequestName) {

        this.friendRequestName = friendRequestName;
        isFriendRequest = true;
        sendDataToController();
        return resultFriendRequest;
    }

    private void sendDataToController() {
        try {
            s = new Server();
        } catch (IOException ex) {
            AlertDialog.showInfoDialog("server", "Error", "Server Connection is out!");

        }
        String id = ConnectWithLoginView_MenuBar.getInastance().sendIdToView();

        if (isName) {
            isName = false;
            String[] key = {"setNewName"};
            JSONObject obj = new JSONObject();
            try {
                obj.put("id", id);
                obj.put("username", name);
                status = s.put(key, obj);
            } catch (JSONException ex) {
                System.out.println("file:ConnectWithController_MenuBar 65 cannot append new username");
            }

        }
        if (isPassword) {
            isPassword = false;
            String[] key = {"setPassword"};
            JSONObject obj = new JSONObject();

            try {
                obj.put("id", id);
                obj.put("password", password);
                status = s.put(key, obj);
            } catch (JSONException ex) {
                System.out.println("file:ConnectWithController_MenuBar 78 cannot append new password");
            }

        }

        if (isRequestAccepted) {

            try {
                //update notification table with this id
                String[] key = {"updateRequestStatus"};
                JSONObject objNot = new JSONObject();
                objNot.put("notId", request.getId());
                objNot.put("status", request.getStatus());
                status = s.put(key, objNot);
                //add new collaborator
                if (keyRequestAccept == "collaborator") {
                    //send notification to sender 
                    String[] keySender = {"sender:list:accept"};
                    JSONObject objSenderNotification = new JSONObject();
                    objSenderNotification.put("fromUserId", request.getToUserId());
                    objSenderNotification.put("toUserId", request.getFromUserId());
                    objSenderNotification.put("dataId", request.getDataId());
                    s.post(keySender, objSenderNotification);
                    //add in collaborator table
                    String[] keyRequest = {"addNewColl"};
                    JSONObject objColl = new JSONObject();
                    objColl.put("userId", request.getToUserId());
                    objColl.put("todoId", request.getDataId());
                    s.post(keyRequest, objColl);

                } else if (keyRequestAccept == "taskMember") {
                    //send notification to sender 
                    String[] keySender = {"sender:task:accept"};
                    JSONObject objSenderNotification = new JSONObject();
                    objSenderNotification.put("fromUserId", request.getToUserId());
                    objSenderNotification.put("toUserId", request.getFromUserId());
                    objSenderNotification.put("dataId", request.getDataId());
                    s.post(keySender, objSenderNotification);
                    //add in task member table
                    String[] keyRequest = {"addNewTaskMember"};
                    JSONObject objTask = new JSONObject();
                    objTask.put("userId", request.getToUserId());
                    objTask.put("ItemId", request.getDataId());
                    s.post(keyRequest, objTask);
                } else if (keyRequestAccept == "friend") {
                    //send notification to sender 
                    String[] keySender = {"sender:friend:accept"};
                    JSONObject objSenderNotification = new JSONObject();
                    objSenderNotification.put("fromUserId", request.getToUserId());
                    objSenderNotification.put("toUserId", request.getFromUserId());
                    objSenderNotification.put("dataId", request.getDataId());
                    s.post(keySender, objSenderNotification);
                    //add in friend table
                    String[] keyRequest = {"addNewFriend"};
                    JSONObject objFriend = new JSONObject();
                    objFriend.put("userId", request.getFromUserId());
                    objFriend.put("friendId", request.getToUserId());
                    s.post(keyRequest, objFriend);
                }

            } catch (JSONException ex) {
                System.out.println("file:ConnectWithController_MenuBar 108 cannot append new collaborator");
            }
        }
        if (isRequestRejected) {

            String[] key = {"updateRequestList"};
            JSONObject objNot = new JSONObject();
            try {
                //update notification table with this id
                objNot.put("notId", request.getId());
                objNot.put("status", request.getStatus());
                status = s.put(key, objNot);
            } catch (JSONException ex) {
                System.out.println("file:ConnectWithController_MenuBar 108 cannot append new collaborator");
            }
        }
        if (isFriendRequest) {
            ConnectWithLoginView_MenuBar getInstance = ConnectWithLoginView_MenuBar.getInastance();
            String[] requestType = {"sendFriendRequest"};
            String name = getInstance.sendDataToView();
            JSONObject friendJsonObject = new JSONObject();
            try {
                friendJsonObject.put("currentUserID", sendIdToView());
                friendJsonObject.put("currentUserName", name);
                friendJsonObject.put("friendName", friendRequestName);
                JSONObject resultJSONObject = s.post(requestType, friendJsonObject);
                resultFriendRequest = resultJSONObject.getString("result");
            } catch (JSONException ex) {
                System.out.println("file:ConnectWithController_MenuBar 164 cannot send friend request");
            }

        }

    }

    @Override
    public String sendDataToView() {
        if (status == 1) {
            return "true";
        } else if (status == 2) {
            return "nameFound";
        }
        return "false";
    }

    @Override
    public String sendIdToView() {
        return ConnectWithLoginView_MenuBar.getInastance().sendIdToView();
    }

}
