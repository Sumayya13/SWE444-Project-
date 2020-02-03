package com.example.easylearn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.security.PublicKey;

public class Login extends AppCompatActivity implements View.OnClickListener {

    Button bLogin;
    EditText etUsername , etPassword ;
    TextView tvRegisterLink ;
    UserLocalStore userLocalStore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvRegisterLink = (TextView) findViewById(R.id.tvRegisterLink);
        bLogin = (Button) findViewById(R.id.bLogin);

    bLogin.setOnClickListener(this);
 tvRegisterLink.setOnClickListener(this);

        userLocalStore =new UserLocalStore(this);

    }
    @Override
    public void onClick (View v){
switch (v.getId()){

    case R.id.bLogin:

        User user= new User(null,null);
        userLocalStore.storeUserData (user);

    userLocalStore.setUserLoggedIn(true);

        break;

    case R.id.tvRegisterLink:

        startActivity(new Intent (this,Register.class));
        break;
}
    }

}
