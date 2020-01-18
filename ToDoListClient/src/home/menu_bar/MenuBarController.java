
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.menu_bar;

import Entity.User;
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
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Elesdody
 */
public class MenuBarController implements Initializable {

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
    //vbox
    @FXML
    private VBox notListBox;
    @FXML
    private VBox notTaskBox;
    @FXML
    private VBox notFriendBox;
    //menubar
    @FXML
    private MenuBar menu;
    //lists 
    @FXML
    private Tab tabListsNotification;
    @FXML
    private Tab tabTasksNotification;
    @FXML
    private ListView<Notifications> listsNotification;
    @FXML
    private ListView<Notifications> tasksNotification;
    @FXML
    private ListView<Notifications> friendRequestsNotification;
    /* start Aml Variables*/
    @FXML
    private TabPane tabPane;
    @FXML
    private ListView<User> friendsLV;
    @FXML
    private Button searchButton;
    @FXML
    private Label resultLabel;
    @FXML
    private TextField friendRequestTextField;
    List<User> friendsOfUser;
    List<User> friends;
    /* end Aml*/

    boolean serverout;
    ObservableList<Notifications> notLists;
    ObservableList<Notifications> notTasks;
    ObservableList<Notifications> notFriendRequests;
    ObservableList<User> frienditems;
    List<Notifications> lists;
    private static MenuBarController instance;

    public MenuBarController() {
        notLists = FXCollections.observableArrayList();
        notTasks = FXCollections.observableArrayList();
        notFriendRequests = FXCollections.observableArrayList();
        frienditems = FXCollections.observableArrayList();
    }

    public static MenuBarController getInastance() {
        if (instance == null) {
            synchronized (MenuBarController.class) {
                instance = new MenuBarController();
            }
        }
        return instance;
    }

    //to hide label after specific time
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

    void setListRequest(Notifications list) {
        notLists.add(0, list);
        System.out.println(list.getId());
        listsNotification.setItems(notLists);
        listsNotification.setCellFactory((li) -> new ListRequestCell());
    }

    void setTaskRequest(Notifications task) {
        notTasks.add(0, task);
        tasksNotification.setItems(notTasks);
        tasksNotification.setCellFactory((ta) -> new TaskRequestCell());
    }

    public void setFriendRequest(Notifications friend) {
        System.out.println("new not friend: 268 mbc " + friend.getId());
        
        // notFriendBox.setVisible(false);
        //notFriendRequests.add(friend);
//        System.out.println(" lv "+friendRequestsNotification.getItems().get(0).getId());
         resultLabel.setText("testtttttt");
//        friendRequestsNotification.getItems().add(friend);
//         friendRequestsNotification.refresh();
        // friendRequestsNotification.setVisible(true);
//        friendRequestsNotification.setCellFactory((fr) -> new friendRequestCell());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        MenuBarController.getInastance();
        // get data of the instance created by login 
        //get name
        ConnectWithLoginView_MenuBar getInstance = ConnectWithLoginView_MenuBar.getInastance();
        String name = getInstance.sendDataToView();
        userName.setText(name);
        userImage.setText(("" + name.charAt(0)).toUpperCase());
        userNameIns.setText(name);
        userImageIns.setText(("" + name.charAt(0)).toUpperCase());
        //get lists notifications 
        List<Notifications> lists = getInstance.sendListsToView();
        if (!lists.isEmpty()) {
            notListBox.setVisible(false);
            for (Notifications li : lists) {
                notLists.add(0, li);
            }
            listsNotification.setVisible(true);
            listsNotification.setItems(notLists);
            listsNotification.setCellFactory((list) -> new ListRequestCell());
        } else {
            notListBox.setVisible(true);
            listsNotification.setVisible(false);
        }
        //get tasks notifications
        List<Notifications> tasks = getInstance.sendTasksToView();
        if (!tasks.isEmpty()) {
            notTaskBox.setVisible(false);
            for (Notifications li : tasks) {
                notTasks.add(0, li);
            }
            tasksNotification.setVisible(true);
            tasksNotification.setItems(notTasks);
            tasksNotification.setCellFactory((task) -> new TaskRequestCell());
        } else {
            tasksNotification.setVisible(false);
            notTaskBox.setVisible(true);
        }
        //get friend requests notifications
        List<Notifications> friendRequests = getInstance.sendFriendRequestToView();
        if (!friendRequests.isEmpty()) {
            notFriendBox.setVisible(false);
            for (Notifications li : friendRequests) {
                notFriendRequests.add(0, li);
                System.out.println("friend req Not :" + li);
            }
            friendRequestsNotification.setVisible(true);
            friendRequestsNotification.setItems(notFriendRequests);
            System.out.println(friendRequestsNotification.getItems().get(0));
            friendRequestsNotification.setCellFactory((friendRequest) -> new friendRequestCell());
        } else {
            friendRequestsNotification.setVisible(false);
            notFriendBox.setVisible(true);

        }
        /*Aml Start*/
        //get friend list 

        friends = getInstance.sendFriendListToView();
        for (User user : friends) {
            frienditems.add(user);
        }
        friendsLV.setItems(frienditems);
        friendsLV.setCellFactory((listView) -> new FriendListViewCell());
        /*Aml End */

    }

    /*start Aml Functions */
    @FXML
    public void sendFriendRequest(ActionEvent event) {

        String friendRequestName = friendRequestTextField.getText().trim();
        ConnectWithLoginView_MenuBar getInstance = ConnectWithLoginView_MenuBar.getInastance();
        String name = getInstance.sendDataToView();
        String result = "";
        if (name.equals(friendRequestName)) {
            result = "You can not send request to yourself";
        } else {
            result = ConnectWithController_MenuBar.getInastance().sendFriendRequest(friendRequestName);
        }
        resultLabel.setMaxWidth(100);
        resultLabel.setWrapText(true);
        resultLabel.setText(result);
        friendRequestTextField.setText("");
    }

    public void notifyAcceptingFriend(User friendUser) {
        int size = friends.size();
        friends.add(friendUser);

    }
    /*end Aml*/
}
/*end Aml*/
