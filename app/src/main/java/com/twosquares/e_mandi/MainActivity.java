package com.twosquares.e_mandi;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    ListView lv;
    public static String ip;
    public RowItem item;
    public static RecyclerView mRecyclerView;
    public static List <RowItem> rowItems;
    private RecyclerView.LayoutManager mLayoutManager;
    public static SwipeRefreshLayout swipeRefreshLayout;
    UserLocalStore userLocalStore;
    public static User user;
    public static boolean openSplash = true;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        Fragment fragment;
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_dashboard:
                    Intent i = new Intent(MainActivity.this,DashboardActivity.class);
                    finish();
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_right);
                    return true;
                case R.id.navigation_notifications:
                    Intent intent = new Intent(MainActivity.this,SellingActivity.class);
                    finish();
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_right);
                    return true;
            }
            return false;
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (openSplash) {
            openSplash = false;
            startActivity(new Intent(this, SplashScreen.class));
        }
        userLocalStore= new UserLocalStore(this);
        if (authenticate()==true){
            user = userLocalStore.getLoggedinUser();
        }
        else {
            startActivity(new Intent(this, UserLogin.class));
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ip = getString(R.string.ip);
        rowItems =  new ArrayList<RowItem>();
        rowItems.clear();
        isStoragePermissionGranted();
//        lv = (ListView) findViewById(R.id.item_list);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.item_list);
/*        SlideInUpAnimator animator = new SlideInUpAnimator(new OvershootInterpolator(1f));
        mRecyclerView.setItemAnimator(animator);*/
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        /*mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.HORIZONTAL));*/
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setScrollBarSize(0);


        AsyncClass asyncClass = new AsyncClass(this, "ViewLoader");
        asyncClass.execute("http://"+ip+"/index.json");

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    @Override
    protected void onStart() {
        super.onStart();


    }
    private boolean authenticate(){
        return userLocalStore.getUserLoggedIn();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            user = null;
            rowItems.clear();
            User.stars.clear();
            userLocalStore.clearUserData();
            userLocalStore.setUserLoggedIn(false);
            startActivity(new Intent(this, UserLogin.class));
        }

        return super.onOptionsItemSelected(item);
    }



    //permissions

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("Permissions", "Permission is granted");
                return true;
            } else {

                Log.v("Permissions", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("Permissions", "Permission is granted");
            return true;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
//        rowItems.clear();
        Log.e("resumed","true");
/*        AsyncClass asyncClass = new AsyncClass(this, "ViewLoader");
        asyncClass.execute("http://"+ip+"/index.json");*/
    }


    @Override
    public void onRefresh() {
        AsyncClass asyncClass = new AsyncClass(MainActivity.this, "ViewLoader");
        asyncClass.execute("http://"+ip+"/index.json");
    }
}
/*
https://192.168.0.150/emandi.php*/
