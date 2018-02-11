package com.twosquares.e_mandi.views;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.twosquares.e_mandi.R;
import com.twosquares.e_mandi.utils.UserLocalStore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.twosquares.e_mandi.views.MainActivity.ip;

public class UserRegister extends AppCompatActivity implements View.OnClickListener {
    Button bRegister;
    EditText etName, etAge, etEmail, etPassword, etPhone;
    UserLocalStore userLocalStore;
    String name, email, password, phoneNo;
    int age;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        etName = (EditText) findViewById(R.id.etName);
        etAge = (EditText) findViewById(R.id.etAge);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etPhone = (EditText) findViewById(R.id.etPhone);
        bRegister = (Button) findViewById(R.id.bRegister);
        userLocalStore = new UserLocalStore(this);
        bRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bRegister:

                intialize();
                if (validate()) {
                    new AsyncClass().execute("http://" + ip + "/reg_user.php", name, email, password, String.valueOf(age), phoneNo);
                }
                break;
        }
    }

    public boolean validate() {
        boolean valid = true;
        if (name.isEmpty() || name.length() > 20) {
            etName.setError("please enter a valid Name");
            valid = false;
        }
        if (age <= 0 || age > 90) {
            etAge.setError("please enter valid age");
            valid = false;
        }
        String y = email;
        int atpos = y.indexOf("@");
        int dotpos = y.lastIndexOf(".");
        if ((atpos < 1) || (dotpos < atpos + 2) || (dotpos + 2 >= y.length())) {
            etEmail.setError("please enter a valid email");
            valid = false;
        }
        if (password.isEmpty() || password.length() < 4) {
            etPassword.setError("please enter a valid password");
            valid = false;
        }
        if (phoneNo.length() != 10) {
            etPhone.setError("please anter a valid phone no.");
            valid = false;
        }
        return valid;
    }

    public void intialize() {
        name = etName.getText().toString().trim();
        email = etEmail.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        phoneNo = etPhone.getText().toString().trim();
        if (etAge.getText().toString().length() != 0) {
            age = Integer.parseInt(etAge.getText().toString().trim());
        } else {
            age = 0;
        }

    }

    private class AsyncClass extends AsyncTask<String, Void, Void> {
        private final OkHttpClient client = new OkHttpClient();
        int resCode;
        JSONObject jobj;
        String userId;
        String ServerResponse;

        @Override
        protected Void doInBackground(String... strings) {
            Response response = null;
            RequestBody requestBody = new FormBody.Builder()
                    .add("name", strings[1])
                    .add("email", strings[2])
                    .add("password", strings[3])
                    .add("age", strings[4])
                    .add("phone_no", strings[5])
                    .build();

            Request request = new Request.Builder()
                    .url(strings[0])
                    .post(requestBody)
                    .build();
            JSONObject jobj;
            try {
                response = client.newCall(request).execute();
                Log.e("Address", strings[0]);
                if (!response.isSuccessful()) {
                    resCode = 201;
                    throw new IOException("Unexpected code " + response);
                }
                String res = response.body().string();
                System.out.println(res);
                JSONArray arr = new JSONArray(res);
                for (int i = 0; i < arr.length(); i++) {
                    jobj = arr.getJSONObject(i);
                    ServerResponse = jobj.getString("response");
                    if (jobj.getString("response").equals("successful")) {
                        resCode = 200;
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
            Toast.makeText(UserRegister.this, ServerResponse, Toast.LENGTH_SHORT).show();
            if (resCode == 200) {
                UserRegister.this.finish();
            }
        }
    }
}
