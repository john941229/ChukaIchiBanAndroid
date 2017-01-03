package com.chuka.chuka;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.security.keystore.UserNotAuthenticatedException;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginPage extends AppCompatActivity {

    private OkHttpClient client = new OkHttpClient();
    Data app;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        initUI();
    }

    private void initUI() {
        Log.e("lr", "ui");
        // 登录按钮
        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = ((EditText) findViewById(R.id.loginUsername)).getText().toString();
                String password = ((EditText) findViewById(R.id.loginPassword)).getText().toString();

                Toast toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
                if (username.equals("")) {
                    toast.setText("请输入用户名");
                    toast.show();
                } else if (password.equals("")) {
                    toast.setText("请输入密码");
                    toast.show();
                } else {
                    login(username, password);
                }
            }
        });

        Button registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginPage.this, RegisterPage.class);
                startActivity(intent);
            }
        });
        app=(Data) getApplication();
    }

    private void login(final String username, final String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestBody formBody = new FormBody.Builder()
                        .add("username", username)
                        .add("password", password)
                        .build();
                Request request = new Request.Builder().url("http://www.bewils.cn/users/login")
                        .post(formBody)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String body =  response.body().string();
                        solveData(body);
                    } else {
                        throw new IOException("Unexpected code" + response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void solveData(String res){
        if(res.length()==0){
            LoginPage.this.setResult(RESULT_CANCELED);
            LoginPage.this.finish();
        }else{
            String[] array = new String[3];
            array = res.split(",");
            app.setUserId(array[0]);
            app.setUserName(array[1]);

            Bundle bundle = new Bundle();
            bundle.putString("userName",app.getUserName());

            Intent intent = new Intent(LoginPage.this,MainPage.class);
            intent.putExtras(bundle);
            LoginPage.this.setResult(RESULT_OK,intent);
            LoginPage.this.finish();
        }
    }


}
