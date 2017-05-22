package com.twosquares.e_mandi;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.twosquares.e_mandi.MainActivity.ip;

public class UserRegister extends AppCompatActivity implements View.OnClickListener {
    Button bRegister;
    EditText etName, etAge, etEmail, etPassword, etPhone;
    UserLocalStore userLocalStore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        etName= (EditText) findViewById(R.id.etName);
        etAge= (EditText) findViewById(R.id.etAge);
        etEmail= (EditText) findViewById(R.id.etEmail);
        etPassword= (EditText) findViewById(R.id.etPassword);
        etPhone= (EditText) findViewById(R.id.etPhone);
        bRegister= (Button) findViewById(R.id.bRegister);
        userLocalStore= new UserLocalStore(this);
        bRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bRegister:

                String name= etName.getText().toString();
                String email= etEmail.getText().toString();
                String password= etPassword.getText().toString();
                int age= Integer.parseInt(etAge.getText().toString());
                String phoneNo = etPhone.getText().toString();
                new AsyncClass().execute("http://"+ip+"/reg_user.php",name,email,password,String.valueOf(age),phoneNo);
                break;
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
                if (!response.isSuccessful()){
                    resCode = 201;
                    throw new IOException("Unexpected code " + response);
                }
                String res = response.body().string();
                System.out.println(res);
                JSONArray arr = new JSONArray(res);
                for (int i = 0; i < arr.length(); i++) {
                    jobj = arr.getJSONObject(i);
                    ServerResponse = jobj.getString("response");
                    if (jobj.getString("response").equals("successful")){
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
            Toast.makeText(UserRegister.this, ServerResponse,Toast.LENGTH_SHORT).show();
            if (resCode == 200) {
                UserRegister.this.finish();
            }
        }
    }
}
