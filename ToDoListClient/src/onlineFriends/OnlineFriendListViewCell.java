/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package onlineFriends;

import home.menu_bar.*;
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
public class OnlineFriendListViewCell extends ListCell<User> {

    @Override
    protected void updateItem(User friendItem, boolean empty) {
        super.updateItem(friendItem, empty);
        if (friendItem != null) {
            setText(friendItem.getUserName());
            String imageURL = "online.png";
            Image image = new Image(getClass().getResourceAsStream(imageURL));
            ImageView imageview = new ImageView(image);
            setGraphic(imageview);

        }
    }
}
