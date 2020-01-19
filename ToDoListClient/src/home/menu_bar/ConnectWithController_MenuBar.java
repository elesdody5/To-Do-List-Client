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
import home.View;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

/**
 *
 * @author ghadeerelmahdy
 */
public class ConnectWithController_MenuBar implements MenuBarModelInterface {

    private boolean isName = false, isRequestAccepted = false, isTask = false, isFriendRequest = false, isPassword = false, isRequestRejected = false;
    private int status = -1;
    private String name;
    private String password;
    static Server s;
    private static ConnectWithController_MenuBar instance;
    Notifications request = new Notifications();
    String friendRequestName, resultFriendRequest, keyRequestAccept;
    String id = ConnectWithLoginView_MenuBar.getInastance().sendIdToView();
    String currentName = ConnectWithLoginView_MenuBar.getInastance().sendDataToView();

    private ConnectWithController_MenuBar() {

    }

    //singelton
    public static ConnectWithController_MenuBar getInastance() {
        if (instance == null) {
            instance = new ConnectWithController_MenuBar();
            try {
                s = new Server();
            } catch (IOException ex) {
                AlertDialog.showInfoDialog("server", "Error", "Server Connection is out!");

            }
        }

        return instance;
    }

    //setter to controller
    public void setNewName(String name) {
        this.name = name;
        isName = true;
        ServerThread th = new ServerThread();
        th.start();
    }

    public void setNewPassword(String password) {
        this.password = password;
        isPassword = true;
        ServerThread th = new ServerThread();
        th.start();
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
            ServerThread th = new ServerThread();
            th.start();
        } else if (req.getStatus() == NotificationKeys.REJECT_NOTIFICATION_REQUEST) {
            //update notif id with status equal 0
            isRequestRejected = true;
            request = req;
            ServerThread th = new ServerThread();
            th.start();
        }

    }
    //set new requests

    public void setNotificationRequest(Notifications obj) {
        
        
            showListRequest(obj);
          if (obj.getType() == NotificationKeys.ASSIGIN_TASK_MEMBER) {
            showTaskRequest(obj);
        } else if (obj.getType() == NotificationKeys.REQUEST_FRIEND) {
            showFriendRequest(obj);
        }
    }

    private void showListRequest(Notifications obj) {
        Thread th = new Thread(new Runnable() {

            @Override
            public void run() {
                MenuBarController instance = View.getMenuLoader().getController();
                System.out.println("inside thread list: "+ obj.getFromUserName() + obj.getToUserId() + obj.getDataId() );
                instance.setListRequest(obj);
            }
        });
        th.start();

    }

    private void showTaskRequest(Notifications obj) {
     
            Thread th = new Thread(new Runnable() {

            @Override
            public void run() {
                MenuBarController instance = View.getMenuLoader().getController();
                instance.setTaskRequest(obj);
                System.out.println("inside thread task : "+ obj.getFromUserName() + obj.getToUserId() + obj.getDataId() );
          
            }
        });
        th.start();

    }

    private void showFriendRequest(Notifications obj) {
        MenuBarController instance = View.getMenuLoader().getController();
        instance.setFriendRequest(obj);

    }

    public void sendFriendRequest(String friendRequestName) {

        this.friendRequestName = friendRequestName;
        isFriendRequest = true;
        ServerThread th = new ServerThread();
        th.start();

    }

    class ServerThread extends Thread {

        public void run() {
            if(isName){
                String result = sendDataToController();
                MenuBarController instance = View.getMenuLoader().getController();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        instance.setResultChangeName(result);
                    }
                });
            }
            if(isPassword){
                String result = sendDataToController();
                MenuBarController instance = View.getMenuLoader().getController();
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                         instance.setResultChangePassword(result);
                    }
                });
            
            }
            if (isFriendRequest) {
                String result = sendDataToController();
                MenuBarController instance = View.getMenuLoader().getController();
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        instance.setResultLabelFriendRequest(result);
                    }
                });

            }

            if (isRequestAccepted) {
                String result = sendDataToController();
                if (result.equals("true")) {
                    if (keyRequestAccept == "collaborator") {
                        ListRequestCell obj = new ListRequestCell();
                        obj.updateItem(request, true);
                        //obj.accept.setDisable(false);
                        System.out.println("true add coll : "+ request.getId() + request.getStatus());
//                        Platform.runLater(new Runnable() {
//                            @Override
//                            public void run() {
//                               
//                               
//                                
//                            }
//                        });
                    } else if (keyRequestAccept == "taskMember") {
                        TaskRequestCell obj = new TaskRequestCell();
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                 obj.accept.setDisable(false);
                                obj.updateItem(request, true);
                            }
                        });
                    } else if (keyRequestAccept == "friend") {
                        friendRequestCell obj = new friendRequestCell();
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                obj.updateItem(request, true);
                            }
                        });
                    }

                } else {
                    Platform.runLater(new Runnable() {

                        @Override
                        public void run() {
                            AlertDialog.showInfoDialog("Cannot update notification table", "Error Updating", "");
                        }
                    });

                }
            }
            if (isRequestRejected) {
                String result = sendDataToController();
                if (result.equals("true")) {
                    friendRequestCell obj = new friendRequestCell();
                    Platform.runLater(new Runnable() {

                        @Override
                        public void run() {
                            obj.updateItem(request, true);
                        }
                    });
                } else {
                    Platform.runLater(new Runnable() {

                        @Override
                        public void run() {
                            AlertDialog.showInfoDialog("Cannot update notification table", "Error Updating", "");
                        }
                    });

                }
            }
        }
    }

    private void setNameServer() {
        isName = false;
        String[] key = {"setNewName"};
        JSONObject obj = new JSONObject();
        try {
            obj.put("id", id);
            obj.put("username", name);
            status = s.put(key, obj);
        } catch (JSONException ex) {
            System.out.println("file:ConnectWithController_MenuBar cannot append new username");
        }
    }

    private void setPasswordServer() {
        isPassword = false;
        String[] key = {"setPassword"};
        JSONObject obj = new JSONObject();

        try {
            obj.put("id", id);
            obj.put("password", password);
            status = s.put(key, obj);
        } catch (JSONException ex) {
            System.out.println("file:ConnectWithController_MenuBar cannot append new password");
        }
    }

    private void RequestAcceptedServer() {
        try {
            isRequestAccepted = false;
            //update notification table with this id
            String[] key = {"updateRequestStatus"};
            JSONObject objNot = new JSONObject();
            objNot.put("notId", request.getId());
            objNot.put("status", request.getStatus());
            status = s.put(key, objNot);
            //add new collaborator
            if (status > 0) {
                if (keyRequestAccept == "collaborator") {
                    //send notification to sender 
                    String[] keySender = {"sender:list:accept"};
                    JSONObject objSenderNotification = new JSONObject();
                    objSenderNotification.put("fromUserId", request.getToUserId());
                    objSenderNotification.put("toUserId", request.getFromUserId());
                    objSenderNotification.put("dataId", request.getDataId());
                    objSenderNotification.put("fromUserName", request.getFromUserName());
                    objSenderNotification.put("toUserName", currentName);
                    objSenderNotification.put("data", request.getData());
                    JSONObject res = s.post(keySender, objSenderNotification);
                    System.out.println("added to table not sender:list:accept");
                    //res.getInt("status");
                   System.out.println("list status not: "+ res.getInt("status"));
                    //add in collaborator table
                    String[] keyRequest = {"addNewColl"};
                    JSONObject objColl = new JSONObject();
                    objColl.put("userId", request.getToUserId());
                    objColl.put("todoId", request.getDataId());
                    s.post(keyRequest, objColl);
                    System.out.println("added to table coll ");
                } else if (keyRequestAccept == "taskMember") {
                    //send notification to sender 
                    String[] keySender = {"sender:task:accept"};
                    JSONObject objSenderNotification = new JSONObject();
                    objSenderNotification.put("fromUserId", request.getToUserId());
                    objSenderNotification.put("toUserId", request.getFromUserId());
                    objSenderNotification.put("dataId", request.getDataId());
                    objSenderNotification.put("fromUserName", request.getFromUserName());
                    objSenderNotification.put("toUserName", currentName);
                    objSenderNotification.put("data", request.getData());
                    JSONObject res = s.post(keySender, objSenderNotification);
                    status = res.getInt("status");
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
                    objSenderNotification.put("fromUserName", request.getFromUserName());
                    objSenderNotification.put("toUserName", currentName);
                    JSONObject res = s.post(keySender, objSenderNotification);
                    status = res.getInt("status");
                    //add in friend table
                    String[] keyRequest = {"addNewFriend"};
                    JSONObject objFriend = new JSONObject();
                    objFriend.put("userId", request.getFromUserId());
                    objFriend.put("friendId", request.getToUserId());
                    s.post(keyRequest, objFriend);
                }
            }
        } catch (JSONException ex) {
            System.out.println("file:ConnectWithController_MenuBar  cannot upate data");
        }

    }

    private void RequestRejectedServer() {
        isRequestRejected = false;
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

    private void friendRequestServer() {
        isFriendRequest = false;
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
            System.out.println("file:ConnectWithController_MenuBar 253");
        }
    }

    private String sendDataToController() {

        if (isName) {
            setNameServer();
              if (status == 1) {
                return "true";
            } else if (status == 2) {
            return "nameFound";
              }
        }
        if (isPassword) {
            setPasswordServer();
        }
        if (isRequestAccepted) {
            RequestAcceptedServer();
            if (status == 1) {
                return "true";
            }
        }
        if (isRequestRejected) {
            RequestRejectedServer();
            if (status == 1) {
                return "true";
            }
        }
        if (isFriendRequest) {
            friendRequestServer();
            return resultFriendRequest;
        }
        return "false";
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
