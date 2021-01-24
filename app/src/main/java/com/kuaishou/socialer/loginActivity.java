package com.kuaishou.socialer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.toast.XToast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class loginActivity extends AppCompatActivity {
    EditText editTextAccount;
    EditText editTextPassword;
    CheckBox checkBoxRememberAccountInformation;
    Button loginButton;
    static SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        loginButton = (Button)findViewById(R.id.LoginButton);
        editTextAccount = (EditText)findViewById(R.id.accountInput);
        editTextPassword = (EditText)findViewById(R.id.passwordInput);
        checkBoxRememberAccountInformation = (CheckBox)findViewById(R.id.rememberAccountInfo);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(loginActivity.this);
        if(sharedPreferences.getBoolean("isRemember",false) == true)
        {
            editTextAccount.setText(sharedPreferences.getString("account",""));
            editTextPassword.setText(sharedPreferences.getString("password",""));
            checkBoxRememberAccountInformation.setChecked(true);
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(editTextAccount.getText().toString().equals("admin") && editTextPassword.getText().toString().equals("123456"))
            {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if(checkBoxRememberAccountInformation.isChecked())
                {
                    editor.putString("account",editTextAccount.getText().toString());
                    editor.putString("password",editTextPassword.getText().toString());
                    editor.putBoolean("isRemember",true);
                    editor.apply();
                    XToast.success(loginActivity.this,"加载主页",XToast.LENGTH_LONG).show();
                }
                else
                    editor.clear();
                Intent intent = new Intent(loginActivity.this,MainActivity.class);
                startActivity(intent);
            }
            else {
                XToast.error(loginActivity.this,"账户信息错误",XToast.LENGTH_LONG).show();
            }
            }
        });

    }
}