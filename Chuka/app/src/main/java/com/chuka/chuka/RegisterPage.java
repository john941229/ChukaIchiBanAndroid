package com.chuka.chuka;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.Delayed;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.R.attr.delay;
import static android.R.attr.startDelay;

public class RegisterPage extends AppCompatActivity {
    private OkHttpClient client = new OkHttpClient();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.register_page);

        initUI();
    }

    private void initUI() {
        Button registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = ((EditText) findViewById(R.id.registerUsername)).getText().toString();
                String password = ((EditText) findViewById(R.id.registerPassword)).getText().toString();
                String repeatPassword = ((EditText) findViewById(R.id.registerRepeatPassword)).getText().toString();

                Toast toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
                if (username.equals("")) {
                    toast.setText("请输入用户名");
                    toast.show();
                } else if (password.equals("")) {
                    toast.setText("请输入密码");
                    toast.show();
                } else if (repeatPassword.equals("")) {
                    toast.setText("请确认密码");
                    toast.show();
                } else if (!password.equals(repeatPassword)) {
                    toast.setText("请确认两次输入的密码是否一致");
                    toast.show();
                } else {
                    register(username, password);
                }
            }
        });
    }

    private void register(final String username, final String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestBody formBody = new FormBody.Builder()
                        .add("username", username)
                        .add("password", password)
                        .build();
                Request request = new Request.Builder().url("http://www.bewils.cn/users/signup")
                        .post(formBody)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String body =  response.body().string();
                        if (body.equals("true")) {
                            final TextView registerInfo = (TextView) findViewById(R.id.registerInfo);
                            registerInfo.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "注册成功，请登录开始体验", (Toast.LENGTH_SHORT)).show();
                                    RegisterPage.this.finish();
                                }
                            });
                        }
                    } else {
                        throw new IOException("Unexpected code" + response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
