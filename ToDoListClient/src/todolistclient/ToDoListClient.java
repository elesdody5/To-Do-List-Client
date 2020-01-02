/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package todolistclient;

import authontication.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


/**
 *
 * @author Elesdody
 */
public class ToDoListClient extends Application {

    private double xOffset, yOffset;

    @Override
    public void start(Stage stage) throws Exception {
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/authontication/login.fxml"));
        Parent root = (Parent) loader.load();
        //send stage to login controller
        LoginController loginController = loader.getController();
        loginController.setStage(stage);

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);

        root.setOnMousePressed((MouseEvent event) -> {
            xOffset = stage.getX() - event.getScreenX();
            yOffset = stage.getY() - event.getScreenY();
        });

        root.setOnMouseDragged((MouseEvent event) -> {
            if (!stage.isFullScreen()) {
                stage.setX(event.getScreenX() + xOffset);
                stage.setY(event.getScreenY() + yOffset);
            }

        });
        stage.setScene(scene);
        
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
