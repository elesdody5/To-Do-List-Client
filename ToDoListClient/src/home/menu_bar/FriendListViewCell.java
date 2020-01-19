/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.menu_bar;

import Entity.User;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

/**
 *
 * @author Aml Sakr
 */
public class FriendListViewCell extends ListCell<User> {

    HBox hbox = new HBox();
    Label label = new Label();
    Pane pane = new Pane();
    Button button = new Button();
    String lastItem;
    User friend ;
    RemoveItemInterface removeItemInterface;

    public FriendListViewCell( RemoveItemInterface removeItemInterface) {
        super();
        this.removeItemInterface = removeItemInterface;
        hbox.getChildren().addAll(label, pane, button);
        HBox.setHgrow(pane, Priority.ALWAYS);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println(lastItem + " : " + event);
                removeItemInterface.removeItem(friend);
            }
        });
    }

    @Override
    protected void updateItem(User friendItem, boolean empty) {
        super.updateItem(friendItem, empty);
        
        if (friendItem != null) {
            setGraphic(hbox);
            friend = friendItem;
            label.setText(friendItem.getUserName());
            String imageURL = "remove.png";
            Image image = new Image(getClass().getResourceAsStream(imageURL));
            ImageView imageview = new ImageView(image);
            button.setGraphic(imageview);
        }
    }
}
