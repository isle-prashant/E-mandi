package com.twosquares.e_mandi;

import java.util.ArrayList;

/**
 * Created by PRAKARAN on 13-05-2017.
 */

public class User {
    public static String name, email, password, phoneNo, userId;
    public static int age;
    public static ArrayList<String> stars = new ArrayList<String>();
    public static ArrayList<String> productStars = new ArrayList<String>();

  public User(String name, int age, String email, String password, String phoneNo, String userId){
      User.name = name;
      User.age = age;
      User.email = email;
      User.password = password;
      User.phoneNo = phoneNo;
      User.userId = userId;


    }

    public User(String name, int age, String email, String password, String phoneNo, String userId, ArrayList<String> listStar){
        User.name = name;
        User.age = age;
        User.email = email;
        User.password = password;
        User.phoneNo = phoneNo;
        User.userId = userId;
        stars = listStar;
    }

    public User(String[] user) {
        name = user[0];
        age = Integer.parseInt(user[1]);
        email = user[2];
        password = user[3];
        phoneNo = user[4];
        userId = user[5];
    }

    public void removeUser (){
        name = "";
        age = -1;
        email = "";
        password = "";
        phoneNo = "";
        userId = "";
        stars = new ArrayList<String>();
    }
}
