package com.example.earth.workshop;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.appdatasearch.GetRecentContextCall;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText etdis;
    private EditText etuser;
    private EditText etpass;
    private EditText etpasscon;
    private Button btreg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etdis = (EditText) findViewById(R.id.etdis);
        etuser = (EditText) findViewById(R.id.etuser);
        etpass = (EditText) findViewById(R.id.etpass);
        etpasscon = (EditText) findViewById(R.id.etpasscon);
        btreg = (Button) findViewById(R.id.btreg);

        btreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (set()) {
                    new Reg(etuser.getText().toString(),
                            etpass.getText().toString(),
                            etpasscon.getText().toString(),
                            etdis.getText().toString()).execute();
                } else {
                    Toast.makeText(RegisterActivity.this, "กรุณากรอกข้อมูลให้ครบถ้วน", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean set() {

        String username = etuser.getText().toString();
        String password = etpass.getText().toString();
        String passwordConfirm = etpasscon.getText().toString();
        String displayName = etdis.getText().toString();

        if (username.isEmpty()) return false;

        if (password.isEmpty()) return false;

        if (passwordConfirm.isEmpty()) return false;

        if(!password.equals(passwordConfirm)) return false;

        if (displayName.isEmpty()) return false;
        else
        return true;
    }

    private class Reg extends AsyncTask<Void, Void, String> {

        private String username;
        private String password;
        private String passwordCon;
        private String displayName;

        public Reg(String username, String password, String passwordCon, String displayName) {
            this.username = username;
            this.password = password;
            this.passwordCon = passwordCon;
            this.displayName = displayName;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();
            Request request;
            Response response;

            RequestBody requestBody = new FormBody.Builder()
                    .add("username", username)
                    .add("password", password)
                    .add("password_con", passwordCon)
                    .add("display_name", displayName)
                    .build();
            request = new  Request.Builder()
                    .url("http://kimhun55.com/pollservices/signup.php")
                    .post(requestBody)
                    .build();
            try{
                response = client.newCall(request).execute();

                if(response.isSuccessful()){
                    return response.body().string();
                }
            }catch (IOException ex){
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        try{
            JSONObject rootObj = new JSONObject(s);
            if (rootObj.has("result")){
                JSONObject resultObj = rootObj.getJSONObject("result");
                if (resultObj.getInt("result") == 1){
                    Toast.makeText(RegisterActivity.this, resultObj.getString("result_desc"), Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(RegisterActivity.this, resultObj.getString("result_desc"), Toast.LENGTH_SHORT).show();
                }
            }
        }catch (JSONException ex){

        }

        }
    }
}

