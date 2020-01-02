/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utility;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Window;

/**
 *
 * @author Ehab mohamed
 */
public class AlertDialog {
    
    
    public static void showInfoDialog(String message, String Header,String content){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setHeaderText(Header);
        alert.setContentText(content);
        alert.show();
    }
}
