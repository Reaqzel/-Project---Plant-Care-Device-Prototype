package com.project;

public class SharedData {
    private static String userID;
    private static int deviceID;

    public static void setUserID(String userID) {
        SharedData.userID = userID;
    }

    public static String getUserID() {
        return userID;
    }

    public static void setDeviceID(int DeviceID){
        SharedData.deviceID = DeviceID;
    }

    public static int getDeviceID(){
        return deviceID;
    }
}
