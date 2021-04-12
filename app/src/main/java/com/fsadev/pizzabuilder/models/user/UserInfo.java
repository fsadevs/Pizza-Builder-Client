package com.fsadev.pizzabuilder.models.user;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;

public class UserInfo {
    private static String Name;
    private static String Email;
    private static String PhoneNumber;
    private static String ProfilePicURL;
    private static String userID;
    private static String Area;
    private static String Address;
    private static GeoPoint Loc;


    //Constructor con objeto DocumentSnapshot
    public static void setUserObject(DocumentSnapshot snapshot) {
        userID = snapshot.getId();
        Name = snapshot.getString("nombre");
        Email = snapshot.getString("email");
        PhoneNumber = snapshot.getString("tel");
        ProfilePicURL = snapshot.getString("imgURL");
        Area = snapshot.getString("area");
        Address = snapshot.getString("direccion");
        Loc = snapshot.getGeoPoint("loc");
    }


    //Getters
    public static String getName() {
        return Name;
    }

    public static String getEmail() {
        return Email;
    }

    public static String getPhoneNumber() {
        return PhoneNumber;
    }

    public static String getProfilePicURL() {
        return ProfilePicURL;
    }

    public static void setName(String name) {
        Name = name;
    }

    public static void setEmail(String email) {
        Email = email;
    }

    public static void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public static void setProfilePicURL(String profilePicURL) {
        ProfilePicURL = profilePicURL;
    }

    public static String getUserID() {
        return userID;
    }

    public static void setUserID(String userID) {
        UserInfo.userID = userID;
    }

    public static String getArea() {
        return Area;
    }

    public static void setArea(String area) {
        Area = area;
    }

    public static String getAddress() {
        return Address;
    }

    public static void setAddress(String address) {
        Address = address;
    }

    public static GeoPoint getLoc() {
        return Loc;
    }

    public static void setLoc(GeoPoint loc) {
        Loc = loc;
    }
}