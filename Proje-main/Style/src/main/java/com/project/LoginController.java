package com.project;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


public class LoginController {

    Connection connection;
    PreparedStatement pst;
    ResultSet rs ;

    @FXML
    private TextField User_ID;
    @FXML
    private PasswordField password;
    @FXML
    private ImageView user_image , password_image , logo_image , User_login_image , minimaze_image , close_image;
    @FXML
    private Button login;
    //New Close and min button 
    @FXML
    private void minimizeWindow(ActionEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }
    @FXML
    private void tryLogin(ActionEvent Event)throws IOException, ClassNotFoundException, SQLException{
        
        String userID = User_ID.getText();
        String userPassword = password.getText();

        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root", "123456");
        pst = connection.prepareStatement("select * from users WHERE user_id=? and user_password=?");

        pst.setString(1,userID);
        pst.setString(2,userPassword);

        rs = pst.executeQuery();

        if(rs.next())
        {
            int deviceID = rs.getInt("device_id");

            //To use this in other controllers
            SharedData.setUserID(userID); 
            SharedData.setDeviceID(deviceID);

            App.setRoot("System");
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input didnt match");
            alert.setHeaderText(null);
            alert.setContentText("Something wrong with inputs?");
            alert.showAndWait();
        }
    }
    @FXML
    public void handleMouseEntered(MouseEvent event) {
        login.setCursor(Cursor.HAND);
    }
    @FXML
    public void handleMouseExited(MouseEvent event) {
        login.setCursor(Cursor.DEFAULT);


    }

}
