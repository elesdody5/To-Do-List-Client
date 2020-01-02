/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.list;

import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author Elesdody
 */
public class ListAdapter extends ListCell<ToDoList> {

    @Override
    protected void updateItem(ToDoList item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {

            ImageView image = new ImageView(new Image(getClass().getResourceAsStream("note.jpg")));
            image.setFitHeight(30);
            image.setFitWidth(30);
            setGraphic(image);
            setText(item.getTitle());
           
        }
        

    }
}
