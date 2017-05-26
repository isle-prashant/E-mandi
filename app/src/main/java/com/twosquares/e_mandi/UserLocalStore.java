package com.twosquares.e_mandi;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.twosquares.e_mandi.MainActivity.ip;
import static com.twosquares.e_mandi.MainActivity.user;
import static com.twosquares.e_mandi.User.stars;

/**
 * Created by PRAKARAN on 13-05-2017.
 */

public class UserLocalStore {
    public static final String SP_NAME = "userDetails";
    SharedPreferences userLocalDatabase;
    Context context;

    public UserLocalStore(Context context){
        userLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
        this.context = context;
    }

    public void storeUserData(User user){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("name", user.name);
        spEditor.putInt("age", user.age);
        spEditor.putString("email", user.email);
        spEditor.putString("password", user.password);
        spEditor.putString("phoneNo", user.phoneNo);
        spEditor.putString("userId",user.userId);
        //set of stars
        Set<String> set = new HashSet<String>();
        spEditor.putStringSet("stars",set);
        //set of user who stared the product
        Set<String> set1 = new HashSet<String>();
        spEditor.putStringSet("userStars",set1);
        System.out.println(user);
        spEditor.commit();
    }
    public User getLoggedinUser(){
        String name= userLocalDatabase.getString("name", "");
        int age= userLocalDatabase.getInt("age",-1);
        String email = userLocalDatabase.getString("email","");
        String password = userLocalDatabase.getString("password","");
        String phoneNo = userLocalDatabase.getString("phoneNo","");
        String userId = userLocalDatabase.getString("userId","");
        //list of stared products
        Set<String> set = userLocalDatabase.getStringSet("stars", new HashSet<String>());
        ArrayList<String> listStar = new ArrayList<String>();
        listStar.addAll(set);
        //list of products stared
        Set<String> set1 = userLocalDatabase.getStringSet("userStars", new HashSet<String>());
        ArrayList<String> userStar = new ArrayList<String>();
        userStar.addAll(set1);
        Log.e("Set", ""+ set);
        User storeduser = new User(name, age, email, password, phoneNo,userId,listStar);
        return  storeduser;
        
    }
    public void login(String email, String password){
        String storeEmail = userLocalDatabase.getString("email","").toString();
        String storePassword = userLocalDatabase.getString("password","");
        if (email.equals(storeEmail) && password.equals(storePassword)){
            setUserLoggedIn(true);
            Toast.makeText(context, "Login Success", Toast.LENGTH_SHORT).show();
        }
        else{
            setUserLoggedIn(false);
            Toast.makeText(context, "Login Unsuccessfull", Toast.LENGTH_SHORT).show();

        }
    }
    public void setUserLoggedIn(boolean LoggedIn){
        SharedPreferences.Editor spEditor= userLocalDatabase.edit();
        spEditor.putBoolean("LoggedIn", LoggedIn);
        spEditor.commit();
    }

    public boolean getUserLoggedIn() {

        if (userLocalDatabase.getBoolean("LoggedIn", false) == true)
        {
            return true;

        }else{
            return false;
        }

    }

    public void setStar(String id, String imageId, String ownerId){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        Set<String> set = new HashSet<String>();

        Log.e("star", ""+stars);
        if (!stars.contains(id)) {
            stars.add(id);


            AsyncClass asyncClass = new AsyncClass(context, "Star");
            asyncClass.execute("http://" + ip + "/PushBots/push.php",user.userId,imageId, ownerId, user.name);
        }
        else {
            stars.remove(id);
        }
        set.addAll(stars);
        spEditor.putStringSet("stars",set);
        spEditor.commit();
    }

    public void clearUserData(){
        SharedPreferences.Editor spEditor= userLocalDatabase.edit();
        spEditor.clear();
        spEditor.apply();
        stars.clear();
        spEditor.commit();
    }

}
