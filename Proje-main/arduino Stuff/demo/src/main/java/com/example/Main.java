package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.fazecast.jSerialComm.*;

import java.io.BufferedReader;
import java.io.IOException;

import java.io.InputStreamReader;
import java.io.PrintWriter;


public class Main {
    public static void main(String[] args) throws SQLException, InterruptedException {

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root", "123456");
        
        int deviceID = 1;
        int[] settings = new int[7];
        String[] log = new String[8];
        fetchDatabaseData(connection, deviceID, settings, log);

        SerialPort comPort = SerialPort.getCommPorts()[2];
        comPort.setBaudRate(9600);
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 5000, 0);

        if (!comPort.openPort()) {
            System.err.println("Failed to open the port!");
            return;
        }
        

        SensorData sensorData = new SensorData();
        new Thread(() -> {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(comPort.getInputStream()))) {
                while (true) {
                    LocalDateTime now = LocalDateTime.now();
                    try {
                         light = Double.parseDouble(br.readLine());
                         humidity = Double.parseDouble(br.readLine());
                         temperature = Double.parseDouble(br.readLine());

                        sensorData.setValues(light, humidity, temperature);

                        if (settings[4] == 1 && now.isAfter(nextTempReport)) {
                            reOrder(log);
                            log[0] = "Report Temperature is " + sensorData.getTempValue() + " celcius";
                            nextTempReport = now.plusMinutes(settings[1]);
                        }
                        if (settings[5] == 1 && now.isAfter(nextLightReport)) {
                            reOrder(log);
                            log[0] = "Report Light level " + sensorData.getLightValue() + "%";
                            nextLightReport = now.plusMinutes(settings[2]);
                        }
                        if (settings[6] == 1 && now.isAfter(nextHumidityReport)) {
                            reOrder(log);
                            log[0] = "Report Humidity level " + sensorData.getHumpValue() + "%";
                            nextHumidityReport = now.plusMinutes(settings[3]);
                        }

                        // For debugging
                        System.out.println("Light: " + light + ", Humidity: " + humidity + ", Temperature: " + temperature);
                    } catch (NumberFormatException | IOException e) {
                        System.err.println("Error: " + e.getMessage());
                        Thread.sleep(1000);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (comPort.isOpen()) {
                    comPort.closePort();
                }
            }
        }).start();

        // This are should not work at same time otherwise its gonna broke 
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(3);

        // Schedule database fetch task
        Runnable databaseFetchTask = () -> {
            try {
                fetchDatabaseData(connection, deviceID, settings, log);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        };
        executor.scheduleAtFixedRate(databaseFetchTask, 0, 5, TimeUnit.MINUTES);

        // Schedule database update task
        Runnable databaseUpdateTask = () -> {
            try {
                updateDeviceLog(connection, deviceID, log);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        };
        executor.scheduleAtFixedRate(databaseUpdateTask, 0, 10, TimeUnit.MINUTES);

        // Schedule irrigation task
        Runnable irrigationTask = () -> {
            try {
                LocalDateTime nextIrrigationTime = LocalDateTime.now().plusMinutes(settings[0]);
                while (true) {
                    if (LocalDateTime.now().isAfter(nextIrrigationTime)) {
                        sendCommandToArduino(comPort, "IRRIGATE"); 
                        nextIrrigationTime = LocalDateTime.now().plusMinutes(settings[0]);
                    }
                    Thread.sleep(1000); // To avoid overchecking
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Irrigation task interrupted: " + e.getMessage());
            }
        };
        executor.execute(irrigationTask);
    }

    private static LocalDateTime nextTempReport = LocalDateTime.now();
    private static LocalDateTime nextLightReport = LocalDateTime.now();
    private static LocalDateTime nextHumidityReport = LocalDateTime.now();

    static double light;
    static double humidity;
    static double temperature;

    
    private static void fetchDatabaseData(Connection connection, int deviceID, int[] settings, String[] log) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT irrigation_interval, temp_interval, light_interval, hum_interval, temp_b, light_b, hum_b from devicesettings where device_id=?;");
        statement.setInt(1, deviceID);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            for (int i = 0; i < 7; i++) {
                settings[i] = resultSet.getInt(i + 1);
            }
        }
        // Settings for debugging
        for (int setting : settings) {
            System.out.println(setting);
        }
        
        PreparedStatement statement2 = connection.prepareStatement("SELECT log1, log2, log3, log4, log5, log6, log7, log8 from devicelog where device_id=?;");
        statement2.setInt(1, deviceID);
        ResultSet resultSet2 = statement2.executeQuery();
        if (resultSet2.next()) {
            for (int i = 0; i < 8; i++) {
                log[i] = resultSet2.getString(i + 1);
            }
        }
        // Log the data for debugging
        for (String logEntry : log) {
            System.out.println(logEntry);
        }

        if (settings[4] == 1) {
            nextTempReport = LocalDateTime.now().plusMinutes(settings[1]); 
        }
        if (settings[5] == 1) {
            nextLightReport = LocalDateTime.now().plusMinutes(settings[2]); 
        }
        if (settings[6] == 1) {
            nextHumidityReport = LocalDateTime.now().plusMinutes(settings[3]); 
        }
    }

    private static void updateDeviceLog(Connection connection, int deviceID, String[] log) throws SQLException {
        String sql = "UPDATE devicelog SET log1=?, log2=?, log3=?, log4=?, log5=?, log6=?, log7=?, log8=? WHERE device_id=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < log.length; i++) {
                statement.setString(i + 1, log[i] != null ? log[i] : "System or device failled to update!");
            }
            statement.setInt(9, deviceID);
            statement.executeUpdate();
        }
    }
    

    private static String[] reOrder(String[] log){
        for(int i = log.length; i > 0; i--){
            log[i] = log[i-1];
        }
        return log;
    }
    
    private static void sendCommandToArduino(SerialPort comPort, String command) {
        PrintWriter out = new PrintWriter(comPort.getOutputStream());
        out.print(command);
        out.flush();
    }

    
}



        
    
    
