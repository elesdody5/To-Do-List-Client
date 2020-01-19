/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.menu_bar;

import home.NotificationKeys;
import home.Notifications;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;

/**
 *
 * @author ghadeerelmahdy
 */
public class friendRequestCell extends ListCell<Notifications> {

    Button accept = new Button();
    Button reject = new Button();
    Notifications not;

    public friendRequestCell() {
        super();
        setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                //do something                  
            }
        });
        accept = new Button();
        reject = new Button();
        accept.setTextFill(Paint.valueOf("white"));
        accept.setBackground(new Background(new BackgroundFill(Paint.valueOf("#6F4CBB"), new CornerRadii(5), Insets.EMPTY)));
        reject.setTextFill(Paint.valueOf("black"));
        reject.setBackground(new Background(new BackgroundFill(Paint.valueOf("#E3D9F8"), new CornerRadii(5), Insets.EMPTY)));
        accept.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                not.setStatus(1);
                ConnectWithController_MenuBar instance = ConnectWithController_MenuBar.getInastance();
                instance.sendNotificationResponse(not);
            }
        });
        reject.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                not.setStatus(0);
                ConnectWithController_MenuBar instance = ConnectWithController_MenuBar.getInastance();
                instance.sendNotificationResponse(not);
            }
        });
    }

    @Override
    protected void updateItem(Notifications not, boolean empty) {
        super.updateItem(not, empty);
        this.not = not;
        setWrapText(true);
        accept.setVisible(false);
        reject.setVisible(false);
        accept.setTextFill(Paint.valueOf("white"));
        accept.setBackground(new Background(new BackgroundFill(Paint.valueOf("#6F4CBB"), new CornerRadii(5), Insets.EMPTY)));
        reject.setTextFill(Paint.valueOf("black"));
        reject.setBackground(new Background(new BackgroundFill(Paint.valueOf("#E3D9F8"), new CornerRadii(5), Insets.EMPTY)));
        Label txt = new Label();
        txt.setMaxWidth(180);
        txt.setWrapText(true);
        txt.setAlignment(Pos.CENTER);
        txt.setTextAlignment(TextAlignment.JUSTIFY);
        if (not != null) {
            if (not.getStatus() == NotificationKeys.NORESPONSE_NOTIFICATION_REQUEST) {
                setBackground(new Background(new BackgroundFill(Paint.valueOf("C2B5DE"), new CornerRadii(5), Insets.EMPTY)));
                txt.setText(not.getFromUserName() + " sends you a friend request");
                accept.setText("Accept");
                reject.setText("Reject");
                accept.setVisible(true);
                reject.setVisible(true);
            } else if (not.getStatus() == NotificationKeys.SEND_RESPONSE_BACK_TO_SENDER_ACCEPT) {
                setBackground(new Background(new BackgroundFill(Paint.valueOf("C2B5DE"), new CornerRadii(5), Insets.EMPTY)));
                accept.setVisible(false);
                reject.setVisible(false);
                txt.setText(not.getFromUserName() + " accepted your friend request ");
                setOnMouseClicked(new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent event) {
                        txt.setTextFill(Paint.valueOf("black"));
                        setBackground(new Background(new BackgroundFill(Paint.valueOf("white"), new CornerRadii(5), Insets.EMPTY)));
                        not.setStatus(NotificationKeys.RESPONSE_BACK_IS_READ);
                        ConnectWithController_MenuBar instance = ConnectWithController_MenuBar.getInastance();
                        instance.sendNotificationResponse(not);
                    }
                });
            } else if (not.getStatus() == NotificationKeys.RESPONSE_BACK_IS_READ) {
                txt.setTextFill(Paint.valueOf("black"));
                setBackground(new Background(new BackgroundFill(Paint.valueOf("white"), new CornerRadii(5), Insets.EMPTY)));
                txt.setText(not.getFromUserName() + " accepted your friend request ");
                accept.setVisible(false);
                reject.setVisible(false);
            } else {
                setBackground(new Background(new BackgroundFill(Paint.valueOf("white"), new CornerRadii(5), Insets.EMPTY)));
                accept.setVisible(false);
                reject.setVisible(false);
                if (not.getStatus() == NotificationKeys.ACCEPET_NOTIFICATION_REQUEST) {
                    txt.setText("you are now friend with " + not.getFromUserName());
                }
                if (not.getStatus() == NotificationKeys.REJECT_NOTIFICATION_REQUEST) {
                    txt.setText("you rejected friend request from " + not.getFromUserName());
                }
            }
        }
        HBox buttonBox = new HBox(3, accept, reject);
        VBox box = new VBox(txt, buttonBox);
        setGraphic(box);
    }
}
