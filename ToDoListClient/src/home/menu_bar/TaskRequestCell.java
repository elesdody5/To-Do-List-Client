/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.menu_bar;

import Entity.User;
import home.Notifications;
import java.io.File;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

/**
 *
 * @author Aml Sakr
 */
public class TaskRequestCell extends ListCell<Notifications> {

    @Override
    protected void updateItem(Notifications not, boolean empty) {
        super.updateItem(not, empty);
        Button accept = new Button();
        Button reject = new Button();
        accept.setVisible(false);
        reject.setVisible(false);
        accept.setTextFill(Paint.valueOf("white"));
        accept.setBackground(new Background(new BackgroundFill(Paint.valueOf("#6F4CBB"), new CornerRadii(5), Insets.EMPTY)));
        reject.setTextFill(Paint.valueOf("white"));
        reject.setBackground(new Background(new BackgroundFill(Paint.valueOf("grey"), new CornerRadii(5), Insets.EMPTY)));
        Label txt = new Label();
        if (not != null) {
            if (not.getStatus() == null) {
                setBackground(new Background(new BackgroundFill(Paint.valueOf("#6B4D22"), new CornerRadii(5), Insets.EMPTY)));
                txt.setText(not.getFromUserId() + " Assigns you to Task : " + not.getData());
                accept.setText("Accept");
                reject.setText("Reject");
                accept.setVisible(true);
                reject.setVisible(true);
            } else {
                setBackground(new Background(new BackgroundFill(Paint.valueOf("#6B4D22"), new CornerRadii(5), Insets.EMPTY)));
                accept.setVisible(false);
                reject.setVisible(false);
                if (not.getStatus() == "1") {
                    txt.setText("you accepted to work with " + not.getFromUserId() + " in Task : " + not.getData());
                }
                if (not.getStatus() == "0") {
                    txt.setText("you rejected to work with " + not.getFromUserId() + " in Task : " + not.getData());
                }
            }
        }
        HBox buttonBox = new HBox(3,accept,reject);
        VBox box = new VBox(txt,buttonBox);
        setGraphic(box);
    }
}
