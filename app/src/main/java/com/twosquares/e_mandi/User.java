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
