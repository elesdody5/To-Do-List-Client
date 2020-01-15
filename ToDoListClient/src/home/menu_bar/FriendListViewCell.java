/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.menu_bar;

import Entity.User;
import java.io.File;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author Aml Sakr
 */
public class FriendListViewCell extends ListCell<User> {



    @Override
    protected void updateItem(User friendItem, boolean empty) {
                   super.updateItem(friendItem, empty);

        if (friendItem != null){
       
            String imageURL = "";
            setText(friendItem.getUserName());
            
//            if (friendItem.isOnline())
//                imageURL = "online.png";
//            else 
//               imageURL = "offline.png";
            
            
            Image image = new Image(getClass().getResourceAsStream(imageURL));
            ImageView imageview = new ImageView(image);
            setGraphic(imageview);
        

    
    }
}
}
