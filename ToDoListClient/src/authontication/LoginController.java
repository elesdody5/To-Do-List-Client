/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package authontication;

import Entity.User;
import Enum.MESSAGES;
import Enum.REQUEST;
import Enum.RESPOND_CODE;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import Utility.*;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.json.JSONException;
import org.json.JSONObject;
import server_request.Server;

/**
 * FXML Controller class
 *
 * @author Ashraf Mohamed
 */
public class LoginController implements Initializable {

    private Stage stage;
    private boolean isFull;
    public static int UserId;
    @FXML
    private Circle close_id;
    @FXML
    private Circle restor_down_id;
    @FXML
    private Circle min_id;
    @FXML
    private TextField email_id;
    @FXML
    private Label forget_password_id;
    @FXML
    private ImageView back_id;
    @FXML
    private StackPane stack_pane_id;
    @FXML
    private Pane sign_in_pane_id;
    @FXML
    private PasswordField password_id;
    @FXML
    private Button sign_in_btn_id;
    @FXML
    private Button sign_up_btn_id;
    @FXML
    private Pane sign_up_pane_id;
    @FXML
    private TextField sign_up_email_id;
    @FXML
    private TextField sign_up_password_id;
    @FXML
    private TextField sign_up_confirm_id;
    @FXML
    private Button create_account_btn_id;
    @FXML
    private BorderPane borderPane_id;
    @FXML
    private Label password_message_id;
    private Server server;
    private String[] params = {REQUEST.LOGIN};

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void handleMouseClicked(MouseEvent event) {
        if (event.getSource().equals(close_id)) {
            closeLoginWindow();
        }
        if (event.getSource().equals(back_id)) {
            new animatefx.animation.FadeIn(sign_in_pane_id).setSpeed(2).play();
            sign_in_pane_id.toFront();
        }

        if (event.getSource().equals(restor_down_id)) {
            System.out.println("restor down ");
            maxmizeLoginWindow();
        }
        if (event.getSource().equals(min_id)) {
            minimzeLoginWindow();

        }
    }

    private void closeLoginWindow() {
        System.exit(0);
    }

    private void minimzeLoginWindow() {
        stage.setIconified(true);
    }

    private void maxmizeLoginWindow() {
        isFull = stage.isFullScreen();
        if (isFull) {
            stage.setFullScreen(false);
        } else {
            stage.setFullScreen(true);
        }

    }

    @FXML
    private void handleButtonClicked(ActionEvent event) throws JSONException {
        if (event.getSource().equals(create_account_btn_id)) {
            new animatefx.animation.FadeIn(sign_up_pane_id).setSpeed(2).play();
            sign_up_pane_id.toFront();
        }
        if (event.getSource().equals(sign_up_btn_id)) {
            signUp();
        }
        if (event.getSource().equals(sign_in_btn_id)) {
            signIn();
        }
        if (event.getSource().equals(forget_password_id)) {
            forgetPassword();
        }

    }

    //sign up method
    private void signUp() {
        System.out.println("signUp");
        String email = sign_up_email_id.getText().trim();
        String password = sign_up_password_id.getText().trim();
        String confirm = sign_up_confirm_id.getText().trim();
        boolean isConfirmed = Validator.checkPasswordEquality(password, confirm);
        if (isConfirmed && !password.isEmpty()) {
            goToRegistrationScreen();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(stage);
            alert.setContentText("Password Must Be Identical");
            alert.setHeaderText("Password Issue");
            alert.show();
        }

    }

    //sign in method
    private void signIn() throws JSONException {
        sign_in_btn_id.setDisable(true);
        password_message_id.setText("");
        //get userName and password from input field
        String userName = email_id.getText().trim();
        String password = password_id.getText().trim();

        //check and validate input, if empty password or user name
        boolean isInputNotEmpty = Validator.checkPasswordAndUserName(userName, password);

        //if valid input, then make server request
        if (isInputNotEmpty) {
            User user = new User(userName, password);
            JSONObject userJson = user.getUserAsJson();

            //TODO:server request in the background, to not freez UI thread
            /* Issue , when making more than one signIn, UI freez 
                even if user input (userName , password) are correct
             */
            try {
                server = new Server();

                JSONObject response = server.post(params, userJson);
                int code = 0;
                //get respond code (SUCCESS , FAILD)after server request
                if (response != null) {
                    code = response.getInt("Code");
                }
                switch (code) {
                    case RESPOND_CODE.SUCCESS:
                        UserId = response.getInt("ID");
                        goToHomeScreen();
                        break;
                    case RESPOND_CODE.FAILD:
                        showFaildAccessMessage();
                        break;

                    case RESPOND_CODE.IS_LOGIN:
                        showIsLoginMessage();
                        break;
                }
            } catch (IOException ex) {
                //AlertDialog.showInfoDialog("Connection Down", "Connection Issue", "Please try again");
                Alert alert = new Alert(AlertType.ERROR);
                alert.setContentText("Connection Down");
                alert.showAndWait();
                sign_in_btn_id.setDisable(false);

            }
        } else {
            //show alert if userName or password is empty
            //AlertDialog.showInfoDialog("Empty User Name or Password", "Invalid Input", "Please try again");
            Alert alert = new Alert(AlertType.ERROR);
                alert.setContentText("Connection Down");
                alert.showAndWait();
                sign_in_btn_id.setDisable(false);
        }

        //TODO: send data to server for authontication
    }

    //forget password method
    private void forgetPassword() {
        System.out.println("forget Password");
        //TODO: restor password process
    }

    //set stage
    public void setStage(Stage s) {
        // get stage reference for minimizing and maxmizing scene operation
        stage = s;
    }

    private void goToHomeScreen() {
        try {
            // go to home scene after success login
            password_message_id.setText("");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/home/Home.fxml"));
            Parent root = (Parent) loader.load();
            Scene scene = new Scene(root);
            //
            Stage stage = new Stage();
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/authontication/Icons/logo.png")));
            stage.setOnCloseRequest((WindowEvent event) -> {
                server.logOut();
                System.exit(0);
            });
            stage.setScene(scene);
            //stage.initStyle(StageStyle.UTILITY);
            stage.show();
            this.stage.close();

        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void showFaildAccessMessage() {
        sign_in_btn_id.setDisable(false);
        // show wrong access message when wrong password or username got entered from user
        password_message_id.setText(MESSAGES.WRONG_ACCESS);
    }

    private void goToRegistrationScreen() {
        String userName = sign_up_email_id.getText().trim();
        String password = sign_up_password_id.getText().trim();
        String confirm = sign_up_confirm_id.getText().trim();
        User user = new User(userName, password);

        Registeration registration = new Registeration(user, confirm);
        int result = registration.registerUser();
        if (result == 1) {
            sign_in_pane_id.toFront();
        }
    }

    private void showIsLoginMessage() {
        sign_in_btn_id.setDisable(false);
        password_message_id.setText("This User Is Already Loggin");
    }
}
