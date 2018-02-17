package com.twosquares.e_mandi.views;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pushbots.push.Pushbots;
import com.twosquares.e_mandi.R;
import com.twosquares.e_mandi.utils.AsyncClass;
import com.twosquares.e_mandi.utils.UserLocalStore;
import com.twosquares.e_mandi.datamodels.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.twosquares.e_mandi.views.MainActivity.ip;
import static com.twosquares.e_mandi.datamodels.User.stars;

public class UserLogin extends AppCompatActivity implements View.OnClickListener {

    Button bLogin;
    EditText etEmail, etPassword;
    TextView tvRegisterLink;
    UserLocalStore userLocalStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        etEmail= findViewById(R.id.etEmail);
        etPassword= findViewById(R.id.etPassword);
        bLogin= findViewById(R.id.bLogin);
        tvRegisterLink= findViewById(R.id.tvRegisterLink);
        bLogin.setOnClickListener(this);
        tvRegisterLink.setOnClickListener(this);
        userLocalStore= new UserLocalStore(this);
        stars.clear();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bLogin:
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                new AsyncClass().execute("http://"+ip+"/login_user.php", email, password);


                break;
            case R.id.tvRegisterLink:
                startActivity(new Intent(this, UserRegister.class));


                break;

        }
    }


    private class AsyncClass extends AsyncTask<String, Void, Void> {
        private final OkHttpClient client = new OkHttpClient();
        int resCode;
        String name,email,password,phoneNo,userId;
        int age;
        String ServerResponse;
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(UserLogin.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            dialog.setMessage("Logging you in. \nHang in There");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            Response response = null;
            RequestBody requestBody = new FormBody.Builder()
                    .add("email", strings[1])
                    .add("password", strings[2])
                    .build();

            Request request = new Request.Builder()
                    .url(strings[0])
                    .post(requestBody)
                    .build();
            JSONObject jobj;
            try {
                response = client.newCall(request).execute();
                Log.e("Address", strings[0]);
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                String res = response.body().string();
                System.out.println(res);
                JSONArray arr = new JSONArray(res);
                for (int i = 0; i < arr.length(); i++) {
                    jobj = arr.getJSONObject(i);
                    ServerResponse = jobj.getString("response");
                    if (jobj.getString("response").equals("Successful")){
                        resCode = 200;
                         name= jobj.getString("name");
                         email= jobj.getString("email");
                         password= jobj.getString("password");
                         age= Integer.parseInt(jobj.getString("age"));
                         phoneNo = jobj.getString("phoneNo");
                         userId= jobj.getString("userId");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(UserLogin.this, ServerResponse,Toast.LENGTH_SHORT).show();
            if (resCode == 200) {
                User registerData = new User(name, age, email, password, phoneNo, userId,new ArrayList<String>());
                userLocalStore.storeUserData(registerData);
                userLocalStore.setUserLoggedIn(true);
                Pushbots.setAlias(User.userId);
                com.twosquares.e_mandi.utils.AsyncClass asyncClass = new com.twosquares.e_mandi.utils.AsyncClass(UserLogin.this, "ViewLoader");
                asyncClass.execute("http://"+ip+"/index.json");
                finish();
            }

            if (dialog.isShowing())
                dialog.dismiss();
        }
    }
}
