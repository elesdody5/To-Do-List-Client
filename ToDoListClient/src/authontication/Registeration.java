/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package authontication;

import Entity.User;
import Enum.RESPOND_CODE;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import org.json.JSONException;
import org.json.JSONObject;
import server_request.Server;

/**
 * FXML Controller class
 *
 * @author Elesdody
 */
public class Registeration {

    /**
     * Initializes the controller class.
     *
     * @param url
     */
    private User user;
    private String confirm;

    public Registeration(User user, String confirmPassword) {
        this.user = user;
        this.confirm = confirmPassword;
    }

    public int registerUser() {
        int result = 0;
        String userName = user.getUserName();
        String password = user.getPassword();
        String confirmPassword = confirm;

        int validatePassword = validatePassword(password, confirmPassword);
        System.out.println(validatePassword);
        if (validateUserName(userName) && validatePassword == RESPOND_CODE.CORRECT_INPUT) {
            try {
                JSONObject userJsonObject = new JSONObject();
                userJsonObject.put("username", userName);
                userJsonObject.put("password", password);
                result = sendRequest(userJsonObject);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        } else {
            result = -1;
            if (!validateUserName(userName)) {
                Alert alert  = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Please enter  valid user name which length greather 6 ");
                alert.showAndWait();
            }
            if (validatePassword == RESPOND_CODE.NOT_EQUAL_PASSWORD) {
                Alert alert  = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("confirm password not equal user password");
                alert.showAndWait();
            } else if (validatePassword == RESPOND_CODE.SHORT_PASSWORD) {
                Alert alert  = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("password length => 6");
                alert.showAndWait();
            }

        }
        return result;
    }

    public int sendRequest(JSONObject jSONObject) {
        try {
            String[] paramters = new String[1];
            String userName = jSONObject.getString("username");
            String password = jSONObject.getString("password");
            paramters[0] = "register";
            Server server = new Server();
            JSONObject resultJsonObject = server.post(paramters, jSONObject);
            String res = resultJsonObject.getString("result");
            if (res.equals("1")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Sign Up successfully :D");
                alert.showAndWait();
                return 1;
                // closeregisterScreenAndOpenLoginScreen();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You already exist ");
                alert.showAndWait();
                
            }

        } catch (JSONException ex) {
            Logger.getLogger(Registeration.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Connection lost");
            alert.showAndWait();
        }
        return -1;
    }

    /*public void closeregisterScreenAndOpenLoginScreen() {
        try {
            registerButton.getScene().getWindow().hide();
            Parent root = FXMLLoader.load(getClass().getResource("/authontication/login.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) registerButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(Registeration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/
    private boolean validateUserName(String userName) {
        return userName.length() >= 6;
    }

    private int validatePassword(String password, String confirmPassword) {
        if (password.equals(confirmPassword) && password.length() > 6) {
            return RESPOND_CODE.CORRECT_INPUT;
        } else {
            if(!password.equals(confirmPassword)){
                return RESPOND_CODE.NOT_EQUAL_PASSWORD;
            }else{
                return RESPOND_CODE.SHORT_PASSWORD;
            }
        }
    }

}
