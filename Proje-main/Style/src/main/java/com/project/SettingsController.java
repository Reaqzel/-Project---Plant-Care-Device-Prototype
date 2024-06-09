package com.project;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;

public class SettingsController implements Initializable {

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
    private ChoiceBox <String> temp_interval;
    @FXML
    private ChoiceBox <String> temp_c;
    
    @FXML
    private ChoiceBox <String> hum_interval;
    @FXML
    private ChoiceBox <String> hum_c;

    @FXML
    private ChoiceBox <String> light_interval;
    @FXML
    private ChoiceBox <String> light_c;

    @FXML
    private ChoiceBox <String> irrigation_interval;

    private Settings settings = new Settings();

    private String[] time = {"10 minute","20 minute","30 minute","40 minute","50 minute","60 minute"};
    private String[] setbool = {"Report","Ignore"};

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        temp_interval.getItems().addAll(time);
        hum_interval.getItems().addAll(time);
        irrigation_interval.getItems().addAll(time);
        light_interval.getItems().addAll(time);


        temp_c.getItems().addAll(setbool);
        hum_c.getItems().addAll(setbool);
        light_c.getItems().addAll(setbool);
    }

    private boolean choiceSelect(String a){
        if(a.matches("Report"))
            return true;
        else            
            return false;
    }

    public int extractNumber(String timeString) {
        String[] parts = timeString.split(" ");
        return Integer.parseInt(parts[0]);
    }
    
    // Until apply button has a lock ability we are going with this 

    public void applyToSettings() throws SQLException{
        if(temp_c.getValue() != null)
            settings.setTempB(choiceSelect(temp_c.getValue()));
        if(hum_c.getValue() != null)
            settings.setHumB(choiceSelect(hum_c.getValue()));
        if(light_c.getValue() != null)
            settings.setLightB(choiceSelect(light_c.getValue()));

        if(irrigation_interval.getValue() != null)
            settings.setirrigationinterval(extractNumber(irrigation_interval.getValue()));
        if(temp_interval.getValue() != null)
            settings.setTempInterval(extractNumber(temp_interval.getValue()));
        if(hum_interval.getValue() !=null)
            settings.setHumInterval(extractNumber(hum_interval.getValue()));
        if(light_interval.getValue() !=null)
            settings.setLightInterval(extractNumber(light_interval.getValue()));

        System.out.println(settings.toString());
        saveSettings(settings,String.valueOf(SharedData.getDeviceID()));
    }

    public void saveSettings(Settings settings, String deviceId) throws SQLException {
        // SQL query that updates settings for a specific device ID
        String sql = "UPDATE deviceSettings SET temp_interval = ?, hum_interval = ?, light_interval = ?, irrigation_interval = ?, temp_b = ?, hum_b = ?, light_b = ? WHERE device_id = ?";
    
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root", "123456");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Set parameters for the prepared statement based on the settings object
            pstmt.setInt(1, settings.getTempInterval());
            pstmt.setInt(2, settings.getHumInterval());
            pstmt.setInt(3, settings.getLightInterval());
            pstmt.setInt(4, settings.getirrigationinterval());
            pstmt.setBoolean(5, settings.isTempB());
            pstmt.setBoolean(6, settings.isHumB());
            pstmt.setBoolean(7, settings.isLightB());
    
            // Set the device ID as the last parameter
            pstmt.setString(8, deviceId);
    
            int affectedRows = pstmt.executeUpdate();
            if(affectedRows > 0) {
                System.out.println("Settings updated successfully for device ID: " + deviceId);
            } else {
                System.out.println("No rows affected, check if device ID is correct: " + deviceId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error updating settings for device ID " + deviceId + ": " + e.getMessage());
        }
    }
    
    
}
