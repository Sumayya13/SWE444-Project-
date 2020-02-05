package com.example.easylearn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    EditText mTextUsername;
    EditText mTextPassword;
    EditText mTextCnfPassword ;
    Button mButtonRegister;
    TextView mTextViewLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mTextUsername = (EditText) findViewById(R.id.edittext_username);
        mTextPassword = (EditText) findViewById(R.id.edittext_password);
        mTextCnfPassword = (EditText) findViewById(R.id.edittext_cnf_password);
        mButtonRegister = (Button) findViewById(R.id.button_login);
        mTextViewLogin = (TextView) findViewById(R.id.textview_register);
        mTextViewLogin.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                Intent LoginIntent = new Intent (RegisterActivity.this,LoginActivity.class);
startActivity(LoginIntent);
            }
        } );


    }
}
