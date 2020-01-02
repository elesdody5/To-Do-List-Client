/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home;

import home.list.FXMLListController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Elesdody
 */
public class HomeController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    BorderPane borderPane;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       start();
    }
   public void start()
    {
    try {
            // TODO
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/home/list/FXMLList.fxml"));
            FXMLListController list = loader.getController();
            VBox root = loader.load();
            borderPane.setLeft(root);
            
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
