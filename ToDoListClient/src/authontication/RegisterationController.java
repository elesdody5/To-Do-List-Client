/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package authontication;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.http.client.methods.HttpPost;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import server_request.Server;

/**
 * FXML Controller class
 *
 * @author Elesdody
 */
public class RegisterationController implements Initializable {

    /**
     * Initializes the controller class.
     *
     * @param url
     */
    @FXML
    Button registerButton;
    @FXML
    TextField userNameTextField;
    @FXML
    TextField passwordTextField;
    @FXML
    TextField confirmPasswordTextField;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

    }

    @FXML
    public void registerUser(ActionEvent actionEvent) {
        String userName = userNameTextField.getText();
        String password = passwordTextField.getText();
        String confirmPassword = confirmPasswordTextField.getText();
        validateUserName(userName);
        int validatePassword =validatePassword(password,confirmPassword);
            if (validateUserName(userName)&& validatePassword == 1 ){
             try {


                JSONObject userJsonObject = new JSONObject();
                userJsonObject.put("username", userName);
                userJsonObject.put("password", password);
                sendRequest(userJsonObject);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        } else {
                if (!validateUserName(userName))
                    System.out.println("Please enter  valid user name which length greather 6 ");
                if (validatePassword == 3)
                    System.out.println("confirm password not equal user password");
                else if (validatePassword == 2)
                    System.out.println("password length => 4");
              
            }
    }

    public void sendRequest(JSONObject jSONObject) {
        try {
            String[] paramters = new String[1];
            String userName = jSONObject.getString("username");
            String password = jSONObject.getString("password");
            paramters[0] = "register";
            Server server = new Server();
            JSONObject resultJsonObject = server.post(paramters, jSONObject);
            String res = resultJsonObject.getString("result");
            if (res.equals("1")) {
                closeregisterScreenAndOpenLoginScreen();
            } else {
                System.out.println("You already exist in DB");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException ex) {
            Logger.getLogger(RegisterationController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void closeregisterScreenAndOpenLoginScreen() {
        try {
            registerButton.getScene().getWindow().hide();
            Parent root = FXMLLoader.load(getClass().getResource("/authontication/login.fxml"));

            Scene scene = new Scene(root);
            Stage stage = (Stage) registerButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(RegisterationController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean validateUserName(String userName) {
        return userName.length()>=6 ;

    }

    private int validatePassword(String password ,String confirmPassword) {
        if (password.equals(confirmPassword))
        {
            if (password.length()>4)
                return 1 ;
            else 
                return 2;
        } else 
            return 3;

    }

}
