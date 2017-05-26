package com.twosquares.e_mandi;

import java.util.ArrayList;

/**
 * Created by PRAKARAN on 13-05-2017.
 */

public class User {
    public static String name, email, password, phoneNo, userId;
    public static int age;
    public static ArrayList<String> stars = new ArrayList<String>();

  public User(String name, int age, String email, String password, String phoneNo, String userId){
      this.name = name;
      this.age= age;
      this.email = email;
      this.password = password;
      this.phoneNo = phoneNo;
      this.userId = userId;


    }

    public User(String name, int age, String email, String password, String phoneNo, String userId, ArrayList<String> listStar){
        this.name = name;
        this.age= age;
        this.email = email;
        this.password = password;
        this.phoneNo = phoneNo;
        this.userId = userId;
        this.stars = listStar;
    }

    public User(String[] user) {
        this.name = user[0];
        this.age= Integer.parseInt(user[1]);
        this.email = user[2];
        this.password = user[3];
        this.phoneNo = user[4];
        this.userId = user[5];
    }

    public void removeUser (){
        this.name = "";
        this.age= -1;
        this.email = "";
        this.password = "";
        this.phoneNo = "";
        this.userId = "";
        this.stars = new ArrayList<String>();
    }
}
