package com.project;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.animation.Interpolator;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class ProfileController {

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
    private VBox vbox_changeFields , vBox_changeFields2;
    @FXML
    private TextField old_field , new_field , newR_field , old_field2 , new_field2 , newR_field2;
    @FXML
    private Label User_id;

    @FXML
    private Pane rightPane; // For animations stay on their pane used in below clip 
    @FXML
    private Button password_change,ID_change;
    


    public void initialize() {

        User_id.setText(SharedData.getUserID());
        
        Rectangle clip = new Rectangle();

        clip.widthProperty().bind(rightPane.widthProperty());
        clip.heightProperty().bind(rightPane.heightProperty());

        rightPane.setClip(clip);

    }


    
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
    private void giveVboxID(){

        password_change.setDisable(true);
        ID_change.setDisable(true);
        slideOut(0, 1, vBox_changeFields2);
        slideIn(658, 2, vbox_changeFields);


        TranslateTransition transitionSlideOut = slideOut(-600, 3, User_id);
            transitionSlideOut.setOnFinished(event -> {
                password_change.setDisable(false);
                ID_change.setDisable(false);
            });
    }

    @FXML
    private void giveVboxPassword(){

        password_change.setDisable(true);
        ID_change.setDisable(true);

        slideOut(0, 1, vbox_changeFields);
        slideIn(0, 2, User_id);
        //Same with above
        TranslateTransition transitionSlideIn = slideIn(658, 3, vBox_changeFields2);
            transitionSlideIn.setOnFinished(event->{
                password_change.setDisable(false);
                ID_change.setDisable(false);
            });
        
    }

    private TranslateTransition slideIn(double position, int duration, Node node) {
        TranslateTransition transition = new TranslateTransition(Duration.seconds(duration), node);
        transition.setToX(position);
        transition.setInterpolator(Interpolator.EASE_BOTH);
        transition.play();
        return transition;
    }
    
    private TranslateTransition slideOut(double position, int duration, Node node) {
        TranslateTransition transition = new TranslateTransition(Duration.seconds(duration), node);
        transition.setToX(position);
        transition.setInterpolator(Interpolator.EASE_BOTH);
        transition.play();
        return transition;
    }

    @FXML
    public void applyToDatabaseID() throws SQLException{
        if(old_field.getText().equals(SharedData.getUserID())){
            if(new_field.getText().equals(newR_field.getText())){
                String sql = "UPDATE users SET user_id = ? WHERE user_id = ?";
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root", "123456");
                    PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        pstmt.setString(1, new_field.getText());
                        pstmt.setString(2, SharedData.getUserID());

                        int affectedRows = pstmt.executeUpdate();
                        if(affectedRows > 0) {
                            System.out.println("Something changed");
                        } else {
                            System.out.println("not changed");
                        }

                        SharedData.setUserID(new_field.getText());
                        User_id.setText(SharedData.getUserID());

                        slideIn(0, 5, User_id);
                        slideOut(0, 5, vbox_changeFields);
                        showPopup("Success");
                    }
            }
            else{
                showPopup("New ID's should match!");
            }
        }
        else{
            showPopup("UserID is wrong!");
        }
    }

    @FXML 
    public void applyToDatabasePassword() throws SQLException{
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root", "123456");
        PreparedStatement statement = connection.prepareStatement("SELECT user_password FROM users WHERE user_id=?");
        statement.setString(1, SharedData.getUserID());
        ResultSet resultSet = statement.executeQuery();
        String password = "";
        if (resultSet.next()){
            password = resultSet.getString("user_password");
        }
        if(password.equals(old_field2.getText())){
            if(new_field2.getText().equals(newR_field2.getText())){
                String sql = "UPDATE users SET user_password = ? WHERE user_id = ?";
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root", "123456");
                    PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        pstmt.setString(1, new_field2.getText());
                        pstmt.setString(2, SharedData.getUserID());

                        int affectedRows = pstmt.executeUpdate();
                        if(affectedRows > 0) {
                            System.out.println("Something changed");
                        } else {
                            System.out.println("not changed");
                        }
                        slideOut(0, 5, vBox_changeFields2);
                        showPopup("Success");
                    }
            }
            else{
                showPopup("New passwords should match!");
            }
        }
        else{
            showPopup("Current password is wrong!");
        }
    }


    //popup if you want to use just copy and paste the method somewhere, it has a style class in css if you want to change something 
    public void showPopup(String message) {
        
        Stage popupStage = new Stage(StageStyle.UNDECORATED);
        popupStage.initModality(Modality.APPLICATION_MODAL);
    
       
        Label messageLabel = new Label(message);
        messageLabel.getStyleClass().add("popup-message");
    
        
        VBox layout = new VBox(messageLabel);
        layout.setAlignment(Pos.CENTER);
        layout.getStyleClass().add("popup-layout");
    
        
        Scene popupScene = new Scene(layout, 300, 100);
        popupScene.getStylesheets().add("styles.css");
        popupStage.setScene(popupScene);
    
        
        popupStage.show();
    
        
        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(event -> popupStage.close());
        delay.play();
    }
    
}
