package com.chuka.chuka;

import android.app.Activity;
import android.os.Bundle;
import android.security.keystore.UserNotAuthenticatedException;
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

public class LoginPage extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        initUI();
    }

    public void initUI() {
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

                }
            }
        });

        EditText usernameTextField = (EditText) findViewById(R.id.loginUsername);
        final EditText passwordTextField = (EditText) findViewById(R.id.loginPassword);
        View.OnKeyListener keyboardListener = new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                Log.e("lr", "listen");
                if(keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    Log.e("lr", "enter");
//                    usernameTextField.clearFocus();
                    passwordTextField.clearFocus();
                }
                return false;
            }
        };

//        usernameTextField.setOnKeyListener(keyboardListener);
//        passwordTextField.setOnKeyListener(keyboardListener);
        usernameTextField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.e("lr", "error");
                if (actionId == EditorInfo.IME_ACTION_SEND) {//EditorInfo.IME_ACTION_SEARCH、EditorInfo.IME_ACTION_SEND等分别对应EditText的imeOptions属性
                    //TODO回车键按下时要执行的操作
                    Log.e("lr", "error");
                }
                return false;
            }
        });
    }
}
