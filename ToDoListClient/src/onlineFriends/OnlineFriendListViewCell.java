/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package onlineFriends;

import Entity.User;
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
            String imageURL = "/home/menu_bar/icons/online.png";
            Image image = new Image(getClass().getResourceAsStream(imageURL));
            ImageView imageview = new ImageView(image);
            setGraphic(imageview);

        } else {
            setText("");
            setGraphic(null);
        }
    }
}
