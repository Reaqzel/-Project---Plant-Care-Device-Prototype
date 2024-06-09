package com.project;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

public class SystemController{


    @FXML
    private void switchToLog() throws IOException {
        App.setRoot("Log");
    }
    @FXML
    private void switchToProfile() throws IOException {
        App.setRoot("Profile");
    }
    @FXML
    private void switchToSettings() throws IOException {
        App.setRoot("Settings");
    }
    @FXML
    private void switchToSystem() throws IOException {
        App.setRoot("System");
    }


    @FXML
    private VBox vbox_user_image;
    @FXML
    private Pane rightPane;
    @FXML
    private Label userID_label,Irr_label,temp_label,light_label,hum_label,device_ıd;
    @FXML
    private ImageView welcome_image,user_image,DB_image,Device_image,waterflow_image;
    
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
    public void initialize() throws SQLException {
        String userID = SharedData.getUserID();
        int deviceID = SharedData.getDeviceID();
        if (userID != null && userID_label != null && device_ıd != null) {
            userID_label.setText(userID);
            device_ıd.setText("Device ID = " + String.valueOf(deviceID));
            DB_image.setImage(new Image("/com/project/icons/pozitive.png"));
            Device_image.setImage(new Image("/com/project/icons/pozitive.png"));
        }
        else{
            DB_image.setImage(new Image("/com/project/icons/negative.png"));
            Device_image.setImage(new Image("/com/project/icons/negative.png"));
        }

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root", "123456");
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM deviceSettings WHERE device_id = ?");
        statement.setInt(1, deviceID);

        ResultSet resultSet = statement.executeQuery();
        if(resultSet.next()){

            Irr_label.setText(String.valueOf("Irrigation interval is " +resultSet.getInt("irrigation_interval") + " minute"));

            if(resultSet.getBoolean("temp_b")){
                temp_label.setText("Temputure -> Raport interval is " + resultSet.getInt("temp_interval") + " minute");
            }
            else{
                temp_label.setText("Temputure -> Ignore");
            }

            if(resultSet.getBoolean("light_b")){
                light_label.setText("Light -> Raport interval is " + resultSet.getInt("light_interval") + " minute");
            }
            else{
                light_label.setText("Light -> Ignore");
            }

            if(resultSet.getBoolean("hum_b")){
                hum_label.setText("Humidity -> Raport interval is " + resultSet.getInt("hum_interval") + " minute");
            }
            else{
                hum_label.setText("Humidity -> Ignore");
            }
        }
        
        Rectangle clip = new Rectangle();

        clip.widthProperty().bind(rightPane.widthProperty());
        clip.heightProperty().bind(rightPane.heightProperty());

        rightPane.setClip(clip);
        slideIn(848.0, 1, vbox_user_image);
    
    }

        private TranslateTransition slideIn(double position, int duration, Node node) {
        TranslateTransition transition = new TranslateTransition(Duration.seconds(duration), node);
        transition.setToY(position);
        transition.setInterpolator(Interpolator.EASE_BOTH);
        transition.play();
        return transition;
    }


    
}