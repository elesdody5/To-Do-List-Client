/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.menu_bar;

import authontication.LoginController;
import home.Notifications;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import todolistclient.ToDoListClient;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import server_request.Server;

/**
 * FXML Controller class
 *
 * @author Elesdody
 */
public class MenuBarController  implements Initializable  {

    private ToDoListClient app;

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
    private MenuBar menu;
    //lists 
    @FXML
    private Tab tabListsNotification ;
     @FXML
    private Tab tabTasksNotification ;
    @FXML 
    private ListView<Notifications> listsNotification ;
    @FXML 
    private ListView<Notifications> tasksNotification ;
    /* start Aml Variables*/
     @FXML
    private Label label;
    

    @FXML
    private TabPane tabPane;
    @FXML
    private ListView<Friend> friendsLV;
    @FXML
    private Button searchButton;
    @FXML
    private Label resultLabel;
    /* end Aml*/

    boolean serverout;
    List<String> lists ;  
    List<String> tasks ; 

    class ProcessService extends Service<Void> {

        @Override
        protected Task<Void> createTask() {
            return new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    // Computations takes 3 seconds
                    // Calling Thread.sleep instead of random computation
                    Thread.sleep(3000);
                    return null;
                }
            };
        }
    }
    Service service = new ProcessService();

    @FXML
    private void handleChangeNameAction(ActionEvent event) {
        ConnectWithController_MenuBar.getInastance().setNewName(newName.getText());
        if (ConnectWithController_MenuBar.getInastance().sendDataToView().equals("true")) {
            userName.setText(newName.getText());
            userImage.setText(("" + newName.getText().charAt(0)).toUpperCase());
            userNameIns.setText(newName.getText());
            userImageIns.setText(("" + newName.getText().charAt(0)).toUpperCase());
            newName.setText("");
        } else if (ConnectWithController_MenuBar.getInastance().sendDataToView().equals("nameFound")) {
            if (!service.isRunning()) {
                service.start();
            }
            newName.setText("");
            status.setVisible(true);
            status.setText("This UserName is already in use , please Choose another one! ");
            service.setOnSucceeded(e -> {
                status.setVisible(false);
                //reset service
                service.reset();
            });
        } else {
            if (!service.isRunning()) {
                service.start();
            }
            newName.setText("");
            status.setVisible(true);
            status.setText("your UserName cannot be changed");
            service.setOnSucceeded(e -> {
                status.setVisible(false);
                //reset service
                service.reset();
            });
        }

    }

    @FXML
    private void handleChangePasswordAction(ActionEvent event) {
        if (newPassword.getText().equals(verfiyNewPassword.getText())) {
            ConnectWithController_MenuBar.getInastance().setNewPassword(newPassword.getText());
            if (ConnectWithController_MenuBar.getInastance().sendDataToView().equals("true")) {
                 if (!service.isRunning()) {
                service.start();
                 }
                status.setVisible(true);
                status.setText("your password is changed");
                newPassword.setText("");
                verfiyNewPassword.setText("");
                service.setOnSucceeded(e -> {
                    status.setVisible(false);
                    //reset service
                    service.reset();
                });
            } else {
                 if (!service.isRunning()) {
                service.start();
            }
                newPassword.setText("");
                verfiyNewPassword.setText("");
                status.setVisible(true);
                status.setText("your Password cannot be changed");
                      service.setOnSucceeded(e -> {
                    status.setVisible(false);
                    //reset service
                    service.reset();
                });
            }
        } else {
             if (!service.isRunning()) {
                service.start();
            }
            status.setVisible(true);
            status.setText("your Password verfication is not identical");
                  service.setOnSucceeded(e -> {
                    status.setVisible(false);
                    //reset service
                    service.reset();
                });
        }
    }

    @FXML
    private void handleProfileMenuAction(ActionEvent event) {
        status.setVisible(false);
    }

    @FXML
    private void handleLogoutAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/authontication/login.fxml"));
            Stage s = (Stage) menu.getScene().getWindow();
            s.hide();
            Parent root = (Parent) loader.load();
            //send stage to login controller
            LoginController controller = loader.getController();
            Scene scene = new Scene(root);
            controller.setStage(s);
            s.setScene(scene);
            s.show();

        } catch (Exception ex) {
            Logger.getLogger(MenuBarController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
//        class RealTimeThread extends Thread {
//        public void run() {
//            while (serverout == false) {
//                try {
//                    String replyMsg = dis.readUTF();
//                    ta.appendText(replyMsg);
//                } catch (EOFException e) {
//                    try {
//                        mySocket.close();
//                        dis.close();
//                        ps.close();
//                        serverout=true;
//                        tf.setEditable(false);  
//                    } catch (IOException ex) {
//                        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
//                    } 
//            }catch (IOException ex) {
//                    ex.printStackTrace();
//                }
//            }
//        }
//    }   
    /*start Aml Functions */
    
    /*end Aml*/
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // get data of the instance created by login 
        //get name
        ConnectWithLoginView_MenuBar getInstance = ConnectWithLoginView_MenuBar.getInastance();
        String name = getInstance.sendDataToView();
        userName.setText(name);
        userImage.setText(("" + name.charAt(0)).toUpperCase());
        userNameIns.setText(name);
        userImageIns.setText(("" + name.charAt(0)).toUpperCase());
        //get lists notifications
        ObservableList<Notifications> notLists = FXCollections.observableArrayList();
        //List <Notifications> lists = getInstance.lists;
        List <Notifications> lists = new ArrayList<>();
        Notifications n =  new Notifications();
        n.setFromUserId("J");
        n.setType("1");
        n.setData("project c");
        n.setStatus(null);
        lists.add( n );
          Notifications n2 =  new Notifications();
          n2.setFromUserId("k");
        n2.setType("1");
        n2.setData("project c");
        n2.setStatus("1");
        lists.add( n2 );
        for(Notifications li : lists){
           notLists.add(li);
        }
        listsNotification.setItems(notLists);
        listsNotification.setCellFactory((list) -> new ListRequestCell());
//        ObservableList<Notifications> notTasks = FXCollections.observableArrayList();
//        List <Notifications> tasks = getInstance.tasks; 
//        for(Notifications li : tasks){
//           notTasks.add(li);
//        }
//        tasksNotification.setItems(notTasks);
//        tasksNotification.setCellFactory((task) -> new TaskRequestCell());
        /*Aml Start*/
        
         ObservableList<Friend> items = FXCollections.observableArrayList();
        
   
          items.addAll(
                new Friend("John Doe", true),
                new Friend("Jane Doe", false),
                new Friend("Donte Dunigan", false),
                new Friend("Gavin Genna", true),
                new Friend("Darin Dear", true),
                new Friend("Pura Petty", false),
                new Friend("Herma Hines", false)
        );

          friendsLV.setItems(items);
          friendsLV.setCellFactory((listView) -> new FriendListViewCell());
        /*Aml End */
        
        
    }


}
