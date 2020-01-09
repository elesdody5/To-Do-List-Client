/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.menu_bar;

/**
 *
 * @author ghadeerelmahdy
 */
public class AlertUser {
    public static void show (String text){
         javafx.scene.control.Alert a = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.NONE);  
         a.setAlertType(javafx.scene.control.Alert.AlertType.ERROR);
         a.setContentText(text);
         a.show();
    }
    
}
