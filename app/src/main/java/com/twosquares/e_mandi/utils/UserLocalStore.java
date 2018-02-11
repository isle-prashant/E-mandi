package com.twosquares.e_mandi.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.twosquares.e_mandi.datamodels.User;
import com.twosquares.e_mandi.utils.AsyncClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.twosquares.e_mandi.views.MainActivity.ip;
import static com.twosquares.e_mandi.datamodels.User.stars;

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
        spEditor.putString("name", User.name);
        spEditor.putInt("age", User.age);
        spEditor.putString("email", User.email);
        spEditor.putString("password", User.password);
        spEditor.putString("phoneNo", User.phoneNo);
        spEditor.putString("userId", User.userId);
        //set of stars
        Set<String> set = new HashSet<String>();
        spEditor.putStringSet("stars",set);
        //set of user who stared the product
        Set<String> set1 = new HashSet<String>();
        spEditor.putStringSet("productStars", set1);
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
        Log.e("Set", ""+ set);

        Set<String> set1 = userLocalDatabase.getStringSet("productStars", new HashSet<String>());
        Log.e("Uset", "" + set1);

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

    public boolean getUserLoggedIn() {

        return userLocalDatabase.getBoolean("LoggedIn", false) == true;

    }

    public void setUserLoggedIn(boolean LoggedIn){
        SharedPreferences.Editor spEditor= userLocalDatabase.edit();
        spEditor.putBoolean("LoggedIn", LoggedIn);
        spEditor.commit();
    }

    public void setproductStars(String str) {
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        Set<String> set1 = userLocalDatabase.getStringSet("productStars", new HashSet<String>());
        set1.add(str);
        Log.e("set", "" + set1);
        spEditor.putStringSet("productStars", set1);
        spEditor.apply();
        spEditor.commit();
    }

    public void setStar(String id, String imageId, String ownerId){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        Set<String> set = new HashSet<String>();

        Log.e("star", ""+stars);
        if (!stars.contains(id)) {
            stars.add(id);


            AsyncClass asyncClass = new AsyncClass(context, "Star");
            asyncClass.execute("http://" + ip + "/PushBots/push.php", User.userId, imageId, ownerId, User.name);
        }
        else {
            stars.remove(id);
        }
        set.addAll(stars);
        spEditor.putStringSet("stars",set);
        spEditor.commit();
    }

    public Map<String, ArrayList<ArrayList<String>>> getStarProducts() {
        Map<String, ArrayList<ArrayList<String>>> hm = new HashMap<String, ArrayList<ArrayList<String>>>();

        Set<String> set1 = userLocalDatabase.getStringSet("productStars", new HashSet<String>());
        ArrayList<String> userStar = new ArrayList<String>();
        userStar.addAll(set1);
        Log.e("userStar size ", "" + set1);

        for (int i = 0; i < userStar.size(); i++) {
            ArrayList<String> list = new ArrayList<>();
            String s = userStar.get(i);
            for (String retval : s.split("[$]")) {
                System.out.println(retval);
                list.add(retval);
            }
            ArrayList<ArrayList<String>> list1 = hm.get(list.get(0));
            if (list1 == null) {
                list1 = new ArrayList<>();
            }
            list1.add(list);
            hm.put(list.get(0), list1);

        }
        return hm;
    }


    public void clearUserData(){
        SharedPreferences.Editor spEditor= userLocalDatabase.edit();
        spEditor.clear();
        spEditor.apply();
        stars.clear();
        spEditor.commit();
    }

}
