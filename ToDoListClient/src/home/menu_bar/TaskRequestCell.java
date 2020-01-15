/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.menu_bar;

import Entity.User;
import home.NotificationKeys;
import home.Notifications;
import java.io.File;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
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
public class TaskRequestCell extends ListCell<Notifications> {

    Button accept = new Button();
    Button reject = new Button();
    Notifications not;

    public TaskRequestCell() {
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
                instance.sendRequestResponse(not);
                // make sure that Notification table is updated
                if (instance.sendDataToView() == "true") {
                    updateItem(not, true);
                }
            }
        });
        reject.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                not.setStatus(0);
                ConnectWithController_MenuBar instance = ConnectWithController_MenuBar.getInastance();
                instance.sendRequestResponse(not);
                // make sure that Notification table is updated
                if (instance.sendDataToView() == "true") {
                    updateItem(not, true);
                }
            }
        });
    }

    @Override
    protected void updateItem(Notifications not, boolean empty) {
        super.updateItem(not, empty);
        setWrapText(true);
        this.not = not;
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
                txt.setText(not.getFromUserName() + " Assigns you to Task : " + not.getData());
                accept.setText("Accept");
                reject.setText("Reject");
                accept.setVisible(true);
                reject.setVisible(true);
             } else if (not.getStatus() == NotificationKeys.SEND_RESPONSE_BACK_TO_SENDER_ACCEPT) {
                setBackground(new Background(new BackgroundFill(Paint.valueOf("C2B5DE"), new CornerRadii(5), Insets.EMPTY)));
                accept.setVisible(false);
                reject.setVisible(false);
                //TODO : mention name of this list
                txt.setText(not.getFromUserName() + " accepted to be assigned in Task : " + not.getData());
                setOnMouseClicked(new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent event) {
                        txt.setTextFill(Paint.valueOf("black"));
                        setBackground(new Background(new BackgroundFill(Paint.valueOf("white"), new CornerRadii(5), Insets.EMPTY)));
                    }
                }); 
             }else {
                setBackground(new Background(new BackgroundFill(Paint.valueOf("white"), new CornerRadii(5), Insets.EMPTY)));
                accept.setVisible(false);
                reject.setVisible(false);
                if (not.getStatus() == NotificationKeys.ACCEPET_NOTIFICATION_REQUEST) {
                    txt.setText("you accepted to work with " + not.getFromUserName() + " in Task : " + not.getData());
                }
                if (not.getStatus() == NotificationKeys.REJECT_NOTIFICATION_REQUEST) {
                    txt.setText("you rejected to work with " + not.getFromUserName() + " in Task : " + not.getData());
                }
            }
        }
        HBox buttonBox = new HBox(3, accept, reject);
        VBox box = new VBox(txt, buttonBox);
        setGraphic(box);
    }
}
