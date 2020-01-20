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
import javafx.beans.property.BooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;

/**
 *
 * @author ghadeerelmahdy
 */
public class ListRequestCell extends ListCell<Notifications> {

    Button accept;
    Button reject ;
    Notifications not;

    public ListRequestCell() {
        super();
        setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                //do something                  
            }
        });
        accept = new Button();
        reject = new Button();
       accept.setVisible(false);
        reject.setVisible(false);
        accept.setTextFill(Paint.valueOf("white"));
        accept.setBackground(new Background(new BackgroundFill(Paint.valueOf("#6F4CBB"), new CornerRadii(5), Insets.EMPTY)));
        reject.setTextFill(Paint.valueOf("black"));
        reject.setBackground(new Background(new BackgroundFill(Paint.valueOf("#E3D9F8"), new CornerRadii(5), Insets.EMPTY)));
        accept.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                not.setStatus(1);
                accept.setDisable(true);
                reject.setDisable(true);
                ConnectWithController_MenuBar instance = ConnectWithController_MenuBar.getInastance();
                //accept.setDisable(true);
                instance.sendNotificationResponse(not);
                if( instance.getCollStatus() == 1){
                   updateItem(not, true);
               
               }
            }
        });
        reject.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                not.setStatus(0);
                accept.setDisable(true);
                reject.setDisable(true);
                ConnectWithController_MenuBar instance = ConnectWithController_MenuBar.getInastance();
                //reject.setDisable(true);
                instance.sendNotificationResponse(not);
                 if( instance.getCollStatus() == 1){
                   updateItem(not, true);
               
               }
            }
        });
       
    }

    @Override
    protected void updateItem(Notifications not, boolean empty) {
        super.updateItem(not, empty);
        this.not = not;  
        
        setWrapText(true);
        Label txt = new Label();
        txt.setMaxWidth(180);
        txt.setWrapText(true);
        txt.setAlignment(Pos.CENTER);
        txt.setTextAlignment(TextAlignment.JUSTIFY);
        txt.setTextFill(Paint.valueOf("black"));
        if (not != null) {
            if (not.getStatus() == NotificationKeys.ACCEPET_NOTIFICATION_REQUEST) {
                setBackground(new Background(new BackgroundFill(Paint.valueOf("white"), new CornerRadii(5), Insets.EMPTY)));
                accept.setVisible(false);
                reject.setVisible(false);
                txt.setText("you accepted a Collaboration with " + not.getFromUserName() + " in List : " + not.getData());

            } else if (not.getStatus() == NotificationKeys.REJECT_NOTIFICATION_REQUEST) {
                setBackground(new Background(new BackgroundFill(Paint.valueOf("white"), new CornerRadii(5), Insets.EMPTY)));
                accept.setVisible(false);
                reject.setVisible(false);
                txt.setText("you rejected a Collaboration with " + not.getFromUserName() + " in List : " + not.getData());
            } else if (not.getStatus() == NotificationKeys.SEND_RESPONSE_BACK_TO_SENDER_ACCEPT) {
                setBackground(new Background(new BackgroundFill(Paint.valueOf("C2B5DE"), new CornerRadii(5), Insets.EMPTY)));
                accept.setVisible(false);
                reject.setVisible(false);
                txt.setText(not.getFromUserName() + " accepted a Collaboration with you in List : " + not.getData());
                setOnMouseClicked(new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent event) {
                        txt.setTextFill(Paint.valueOf("black"));
                        setBackground(new Background(new BackgroundFill(Paint.valueOf("white"), new CornerRadii(5), Insets.EMPTY)));
                    }
                });
            } else if (not.getStatus() == NotificationKeys.NORESPONSE_NOTIFICATION_REQUEST) {
                setBackground(new Background(new BackgroundFill(Paint.valueOf("C2B5DE"), new CornerRadii(5), Insets.EMPTY)));
                txt.setText(not.getFromUserName() + " sends you a request to Collaborate in List : " + not.getData());
                accept.setText("Accept");
                reject.setText("Reject");
                accept.setVisible(true);
                reject.setVisible(true);

            }
        }

        HBox buttonBox = new HBox(10, accept, reject);
        VBox box = new VBox(5, txt, buttonBox);
        setGraphic(box);
    }
}
