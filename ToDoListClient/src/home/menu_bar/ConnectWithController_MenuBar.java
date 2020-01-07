/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.menu_bar;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import server_request.Server;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author ghadeerelmahdy
 */
public class ConnectWithController_MenuBar implements MenuBarModelInterface  {
    private boolean isPassword;
    private boolean isName;
    private int status;
    private String name;
    private String password;
    Server s ;
    private static ConnectWithController_MenuBar instance;

    private ConnectWithController_MenuBar() {
        try {
            s= new Server();
        } catch (IOException ex) {
            System.out.println("file:ConnectWithController_MenuBar 31 cannot create new server object");
        }
    }

    //singelton
    public static ConnectWithController_MenuBar getInastance(){
        if (instance == null) {
            instance = new ConnectWithController_MenuBar();
        }
        return instance;
    }   
    public  void setNewName(String name) {
        this.name=name; 
        isName = true;
        sendDataToController();
    }
    public void setNewPassword(String password) {
        this.password = password;
        isPassword = true;
         sendDataToController();
    }
    private void sendDataToController()  {
        String id = ConnectWithLoginView_MenuBar.getInastance().sendIdToView();
      
        if (isName) {
            isName = false;
            String[] key = {"setNewName"};
            JSONObject obj = new JSONObject();
            try {
                obj.append("id", id);
                obj.append("username", name);
             

            } catch (JSONException ex) {
               System.out.println("file:ConnectWithController_MenuBar 65 cannot append new username");
            }
               status = s.put(key, obj);
        }
        if (isPassword) {
            isPassword = false;
            String[] key = {"setPassword"};
            JSONObject obj = new JSONObject();

            try {
                obj.append("id", id);
                obj.append("password", password);
            } catch (JSONException ex) {
               System.out.println("file:ConnectWithController_MenuBar 78 cannot append new password");
            }

            status = s.put(key, obj);

        }

    }
    @Override
    public String sendNameToView() {
        System.out.println(status);
        if (status == 1) {
            return "true";
        }
        if (status == 0) {
            return "false";
        }
        if (status == 2) {
            return "nameFound";
        }
        return "error";
    }
     @Override
    public String sendIdToView() {           
       return   ConnectWithLoginView_MenuBar.getInastance().sendIdToView();
    }
    
}
