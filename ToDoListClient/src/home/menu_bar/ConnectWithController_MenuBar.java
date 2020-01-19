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
    private int status = -1, friendStatus = -1, collStatus=-1 , taskStatus=-1;
    private String name;
    private String password;
    static Server s;
    private static ConnectWithController_MenuBar instance;
    Notifications request ;
    String friendRequestName, resultFriendRequest;
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
            if (req.getType() == NotificationKeys.ADD_COLLABORATOR) {
                 Thread th = new Thread( new Runnable() {
                    @Override
                    public void run() {
                        int status = RequestAcceptedServer(req, "collaborator");
                               if(status == 1) {
                                   collStatus = status;
                               }else{
                                 AlertDialog.showInfoDialog("Cannot update notification table", "Error Updating status", "");
                               }
                    }
                });
                th.start();
                try {
                    th.join();
                } catch (InterruptedException ex) {
                    System.err.println(ex); 
                  
                }
            } else if (req.getType() == NotificationKeys.ASSIGIN_TASK_MEMBER) {
                  
                 Thread th = new Thread( new Runnable() {
                    @Override
                    public void run() {
                              int status = RequestAcceptedServer(req, "taskMember");
                               if(status == 1) {
                                   taskStatus = status;
                               }else{
                                 AlertDialog.showInfoDialog("Cannot update notification table", "Error Updating status", "");
                               }
                    }
                });
                th.start();
                try {
                    th.join();
                } catch (InterruptedException ex) {
                    System.err.println(ex); 
                  
                }
            } else if (req.getType() == NotificationKeys.REQUEST_FRIEND) {
                 Thread th = new Thread( new Runnable() {
                    @Override
                    public void run() {
                         int status = RequestAcceptedServer(req, "friend");
                               if(status == 1) {
                                   friendStatus = status;
                               }else{
                                 AlertDialog.showInfoDialog("Cannot update notification table", "Error Updating status", "");
                               }
                    }
                });
                th.start();
                try {
                    th.join();
                } catch (InterruptedException ex) {
                    System.err.println(ex); 
                  
                }       
            }
            
        } else if (req.getStatus() == NotificationKeys.REJECT_NOTIFICATION_REQUEST) {
            //update notif id with status equal 0
            Thread th = new Thread( new Runnable() {
                    @Override
                    public void run() {
                        friendRequestCell obj = new friendRequestCell();
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                               int status = RequestRejectedServer(request);
                               if(status == 1) {
                                   friendStatus = status;
                               }else{
                                 AlertDialog.showInfoDialog("Cannot update notification table", "Error Updating status", "");
                               }
                            }
                        });
                    }
                });
                th.start();
                try {
                    th.join();
                } catch (InterruptedException ex) {
                    System.err.println(ex); 
                  
                }
        }
    }
    
    public int getFriendStatus (){
      return friendStatus;
    }
      public int getCollStatus (){
      return collStatus;
    }
         public int getTaskStatus (){
      return taskStatus;
    }
    //set new requests

    public void setNotificationRequest(Notifications obj) {
        
        if (obj.getType() == NotificationKeys.ADD_COLLABORATOR) {
            showListRequest(obj);
        }else if (obj.getType() == NotificationKeys.ASSIGIN_TASK_MEMBER) {
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

    private int RequestAcceptedServer(Notifications request,String keyRequestAcc) {
        String keyRequestAccept = keyRequestAcc ;
        int status = -1;
        try {
            //update notification table with this id
            String[] key = {"updateRequestStatus"};
            JSONObject objNot = new JSONObject();
            objNot.put("notId", request.getId());
            objNot.put("status", request.getStatus());
            status = s.put(key, objNot);
            //add new collaborator
            if (status > 0) {
                if (keyRequestAccept .equals("collaborator") ) {
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
                   System.out.println("new not list status not: "+ res.getInt("status"));
                    //add in collaborator table
                    String[] keyRequest = {"addNewColl"};
                    JSONObject objColl = new JSONObject();
                    objColl.put("userId", request.getToUserId());
                    objColl.put("todoId", request.getDataId());
                    res = s.post(keyRequest, objColl);
                    status = res.getInt("status");
                    System.out.println("added to table coll ");
                } else if (keyRequestAccept .equals("taskMember")) {
                  
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
                    //status = res.getInt("status");
                    //add in task member table
                    String[] keyRequest = {"addNewTaskMember"};
                    JSONObject objTask = new JSONObject();
                    objTask.put("userId", request.getToUserId());
                    objTask.put("ItemId", request.getDataId());
                    res = s.post(keyRequest, objTask);
                    status = res.getInt("status");
                } else if (keyRequestAccept .equals("friend")) {
                   
                    //send notification to sender 
                    String[] keySender = {"sender:friend:accept"};
                    JSONObject objSenderNotification = new JSONObject();
                    objSenderNotification.put("fromUserId", request.getToUserId());
                    objSenderNotification.put("toUserId", request.getFromUserId());
                    objSenderNotification.put("dataId", request.getDataId());
                    objSenderNotification.put("fromUserName", request.getFromUserName());
                    objSenderNotification.put("toUserName", currentName);
                    JSONObject res = s.post(keySender, objSenderNotification);
                   if( res.getInt("status") == 1){
                    //add in friend table
                    String[] keyRequest = {"addNewFriend"};
                    JSONObject objFriend = new JSONObject();
                    objFriend.put("userId", request.getFromUserId());
                    objFriend.put("friendId", request.getToUserId());
                    res = s.post(keyRequest, objFriend);
                     status = res.getInt("status");
                   }
                }
            }
        } catch (JSONException ex) {
            System.out.println("file:ConnectWithController_MenuBar  cannot upate data");
        }
        return status;

    }

    private int RequestRejectedServer(Notifications request) {
        String[] key = {"updateRequestList"};
        int status = -1;
        JSONObject objNot = new JSONObject();
        try {
            //update notification table with this id
            objNot.put("notId", request.getId());
            objNot.put("status", request.getStatus());
            status = s.put(key, objNot);
        } catch (JSONException ex) {
            System.out.println("file:ConnectWithController_MenuBar  cannot append new collaborator");
        }
        return status;
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
        if (isFriendRequest) {
            friendRequestServer();
            return resultFriendRequest;
        }
        return "false";
    }

    @Override
    public String sendDataToView() {
      return  ConnectWithLoginView_MenuBar.getInastance().sendDataToView();
    }

    @Override
    public String sendIdToView() {
        return ConnectWithLoginView_MenuBar.getInastance().sendIdToView();
    }

}
