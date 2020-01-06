/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.menu_bar;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
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
    private int status =-1;
    private String name;
    private String password;
    static Server s ;
    private static ConnectWithController_MenuBar instance;

    private ConnectWithController_MenuBar() {
     
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
            try {
            s= new Server();
        } catch (IOException ex) {
         Alert a = new Alert(Alert.AlertType.NONE);  
         a.setAlertType(Alert.AlertType.ERROR);
         a.setContentText("Server Connection is out");
         a.show();

        }
        String id = ConnectWithLoginView_MenuBar.getInastance().sendIdToView();
      
        if (isName) {
            isName = false;
            String[] key = {"setNewName"};
            JSONObject obj = new JSONObject();
            try {      
                obj.put("id",id );
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

    }
    @Override
    public String sendNameToView() {
        if (status == 1) {
            return "true";
        }
        else if (status == 2) {
            return "nameFound";
        }
        return "false";
    }
     @Override
    public String sendIdToView() {           
       return   ConnectWithLoginView_MenuBar.getInastance().sendIdToView();
    }
    
}
