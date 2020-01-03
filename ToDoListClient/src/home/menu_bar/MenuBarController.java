/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.menu_bar;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import todolistclient.ToDoListClient;

/**
 * FXML Controller class
 *
 * @author Elesdody
 */
public class MenuBarController implements Initializable {
    private ToDoListClient app ;

    //labels
    @FXML
    private Label userName;
    @FXML
    private Label userImage;
    @FXML
    private Label userNameIns;
    @FXML
    private Label userImageIns;
      @FXML
    private Label status;
    //textfields
    @FXML
    private TextField newName;
    @FXML
    private TextField newPassword;
    @FXML
    private TextField verfiyNewPassword;
    //buttons
    @FXML
    private Button changeName;
    @FXML
    private Button changePassword;
    @FXML
    private Button logout;
   
    
    @FXML
    private void handleProfileMenuAction(ActionEvent event) {
         status.setVisible(false);
    }
    @FXML
    private void handleChangeNameAction(ActionEvent event) {
          ConnectWithController_MenuBar.getInastance().setNewName(newName.getText());
         if(ConnectWithController_MenuBar.getInastance().sendNameToView().equals("true")){
             userName.setText(newName.getText());
             userImage.setText(("" + newName.getText().charAt(0)).toUpperCase());
             userNameIns.setText(newName.getText());
             userImageIns.setText(("" +newName.getText().charAt(0)).toUpperCase());
             newName.setText(""); 
         }else if(ConnectWithController_MenuBar.getInastance().sendNameToView().equals("nameFound")){ 
             newName.setText(""); 
             status.setVisible(true);
             status.setText("This UserName is already in use , please Choose another one! ");
         }else{
             newName.setText(""); 
             status.setVisible(true);
             status.setText("your UserName cannot be changed");
         }
         
    }

    @FXML
    private void handleChangePasswordAction(ActionEvent event)  {
        if (newPassword.getText().equals(verfiyNewPassword.getText())) {
            ConnectWithController_MenuBar.getInastance().setNewPassword(newPassword.getText());
            if (ConnectWithController_MenuBar.getInastance().sendNameToView().equals("true")) {
                status.setVisible(true);
                status.setText("your password is changed");
                newPassword.setText("");
                verfiyNewPassword.setText("");
            } else {
                newPassword.setText("");
                verfiyNewPassword.setText("");
                status.setVisible(true);
                status.setText("your Password cannot be changed");
            }
        } else {
            status.setVisible(true);
            status.setText("your Password verfication is not identical");
        }
    }
    @FXML
    private void handleLogoutAction(ActionEvent event) {
       // app.gotoLogin();

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // get data of the instance created by login
        ConnectWithLoginView_MenuBar getName =  ConnectWithLoginView_MenuBar.getInastance() ; 
        String name = getName.sendNameToView();
        userName.setText(name);
        userImage.setText(("" + name.charAt(0)).toUpperCase());
        userNameIns.setText(name);
        userImageIns.setText(("" + name.charAt(0)).toUpperCase());
    }
     
    
}
