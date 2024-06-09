package com.project;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LogController implements Initializable{

    // sql making me tilted 
    public String[] getLogFromDatabase(int deviceID) throws SQLException {
        String[] logs = new String[8];
        String sql = "SELECT log1, log2, log3, log4, log5, log6, log7, log8 FROM devicelog WHERE device_id = ?";
    
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root", "123456");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setInt(1, deviceID);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    for (int i = 0; i < logs.length; i++) {
                        logs[i] = rs.getString("log" + (i + 1));
                    }
                }
            }
        }
        return logs;
    }

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
    private Label log1_action, log2_action ,log3_action,log4_action,log5_action,log6_action,log7_action,log8_action;
    @FXML
    private Label log1_actionRD, log2_actionRD ,log3_actionRD,log4_actionRD,log5_actionRD,log6_actionRD,log7_actionRD,log8_actionRD;
    @FXML
    private ImageView log1_image, log2_image, log3_image,log4_image,log5_image,log6_image,log7_image,log8_image;



    public void setLog(int deviceID) throws SQLException{
        Label[] actionLabels = {log1_action, log2_action,log3_action,log4_action,log5_action,log6_action,log7_action,log8_action}; 
        Label[] actionRDLabels = {log1_actionRD, log2_actionRD,log3_actionRD,log4_actionRD,log5_actionRD,log6_actionRD,log7_actionRD,log8_actionRD};
        ImageView[] images = {log1_image, log2_image, log3_image,log4_image,log5_image,log6_image,log7_image,log8_image}; // Array of all image views
        
        String[] logArray = getLogFromDatabase(deviceID);

        for (int i = 0; i < logArray.length; i++) {
            if (logArray[i].isEmpty()) {
                actionLabels[i].setText("No action");
                actionRDLabels[i].setText("---------");
                images[i].setImage(new Image("/com/project/icons/empty.png"));
            } else {
                String[] parts = logArray[i].split(" ", 3); // Split into 3 parts: action, name, rest
                String action = parts[0];
                String name = parts[1];
                String details = parts.length > 2 ? parts[2] : "";

                actionLabels[i].setText(action);
                actionRDLabels[i].setText(name + " " + details);

                // Set image based on action and name
                if (action.equals("Report")) {
                    if (name.equals("Temperature")) {
                        images[i].setImage(new Image("/com/project/icons/temp.png"));
                    } else if (name.equals("Light")) {
                        images[i].setImage(new Image("/com/project/icons/Light.png"));
                    }else if (name.equals("Humidity")){
                        images[i].setImage(new Image("/com/project/icons/hum.png"));
                    }
                    // Add more conditions as needed
                } else if (action.equals("Irrigation")) {
                    images[i].setImage(new Image("/com/project/icons/report.png"));
                
                // Add more conditions as needed
                }
            }
        }
    }
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        try {
            setLog(SharedData.getDeviceID());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}


