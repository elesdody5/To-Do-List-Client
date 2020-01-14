/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.menu_bar;

import Utility.AlertDialog;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import server_request.Server;
import org.json.JSONException;
import org.json.JSONObject;
import server_connection.Connection;
import home.NotificationKeys;
import home.Notifications;

/**
 *
 * @author ghadeerelmahdy
 */
public class ConnectWithController_MenuBar implements MenuBarModelInterface {

    private boolean isName, isList, isTask, isFriend, isPassword,  isListReject;
    private int status = -1;
    private String name;
    private String password;
    static Server s;
    private static ConnectWithController_MenuBar instance;
    Notifications request = new Notifications();

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

    public void sendRequestList(Notifications req) {
        if (req.getStatus() == NotificationKeys.ACCEPET_COLLABORATOR_REQUEST) {
            //update notif id with status equal 1
            //insert into coll table
            isList = true;
            request = req;
            sendDataToController();
        }
          if (req.getStatus() == NotificationKeys.REJECT_COLLABORATOR_REQUEST) {
            //update notif id with status equal 0
            isListReject = true;
            request = req;
            sendDataToController();
        }
        

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

        if (isList) {

            String[] key = {"updateRequestList"};
            JSONObject objNot = new JSONObject();
            try {
                //update notification table with this id
                objNot.put("notId", request.getId());
                objNot.put("status", request.getStatus());
                status = s.put(key, objNot);
                //add new collaborator
                JSONObject objColl = new JSONObject();
               String[] keyColl = {"collAcceptListRequest"};
                objColl.put("userID", request.getToUserId());
                objColl.put("todoId", request.getDataId());
                s.post(key, objColl);
            } catch (JSONException ex) {
                System.out.println("file:ConnectWithController_MenuBar 108 cannot append new collaborator");
            }
        }
         if (isListReject) {

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
