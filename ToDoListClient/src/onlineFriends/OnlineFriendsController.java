/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package onlineFriends;

import Entity.User;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

/**
 * FXML Controller class
 *
 * @author Aml Sakr
 */
public class OnlineFriendsController implements Initializable {

    @FXML
    private ListView<User> onlineFriendsLV;

    List<User> onlineFriendsList ;
    ObservableList<User> onlineFriendsObseverList;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

    }    
    
    public void getOnlineFriends(List<User> onlineFriends){
        this.onlineFriendsList = onlineFriends ;
         onlineFriendsObseverList = FXCollections.observableArrayList();
   

        for (User user : onlineFriends) {
            onlineFriendsObseverList.add(user);
        }
        onlineFriendsLV.setItems(onlineFriendsObseverList);
        onlineFriendsLV.setCellFactory((listView) -> new OnlineFriendListViewCell());
    }

    
    public void notifyUserOnlineOrOffline (User user , boolean status ){
        if (status){
            onlineFriendsLV.getItems().add(user);
        }else{
            int removedItem =-8;
            for (int i = 0; i < onlineFriendsLV.getItems().size(); i++) {
                if (onlineFriendsLV.getItems().get(i).getId() == user.getId())
                    removedItem = i ;
            }
             onlineFriendsLV.getItems().remove(removedItem);

        }
    }
}
