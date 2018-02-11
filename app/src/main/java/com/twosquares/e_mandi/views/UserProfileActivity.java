package com.twosquares.e_mandi.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.twosquares.e_mandi.R;
import com.twosquares.e_mandi.datamodels.User;

import static com.twosquares.e_mandi.views.MainActivity.rowItems;
import static com.twosquares.e_mandi.views.MainActivity.user;
import static com.twosquares.e_mandi.MyApplication.userLocalStore;

/**
 * Created by PRASHANT on 28-05-2017.
 */

public class UserProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvAge = (TextView) findViewById(R.id.tvAge);
        TextView tvPhone = (TextView) findViewById(R.id.tvPhone);
        TextView tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvName.setText(User.name);
        tvAge.setText(String.valueOf(User.age));
        tvEmail.setText(User.email);
        tvPhone.setText(User.phoneNo);
        Button btnLogout = (Button) findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user = null;
                rowItems.clear();
                User.stars.clear();
                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);
                startActivity(new Intent(UserProfileActivity.this, UserLogin.class));
                finish();

            }
        });
    }
}
