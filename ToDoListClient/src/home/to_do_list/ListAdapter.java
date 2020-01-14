/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.to_do_list;

import Entity.User;
import home.list.*;
import home.to_do_list.ToDoList;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author sara
 */
public class ListAdapter extends ListCell<User> {

    @Override
    protected void updateItem(User user, boolean empty) {
        super.updateItem(user, empty);
        if (user != null) {

            ImageView image = new ImageView(new Image(getClass().getResourceAsStream("personIcon.png")));
            image.setFitHeight(30);
            image.setFitWidth(30);
            setGraphic(image);
            setText(user.getUserName());
            //System.out.print(user.getUserName()+"ss");
        }
        

    }
}
