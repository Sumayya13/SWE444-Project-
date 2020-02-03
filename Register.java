package com.example.easylearn;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Register extends AppCompatActivity implements View.OnClickListener {

    Button bRegster ;
    EditText etName , etUsername , etAge , etPassword ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


     etName =(EditText) findViewById(R.id.etName);
     etUsername =(EditText) findViewById(R.id.etUsername);
     etAge =(EditText) findViewById(R.id.etAge);
    etPassword =(EditText) findViewById(R.id.etPassword);
    Button bRegister = (Button) findViewById(R.id.bRegister) ;

        bRegister.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bRegister :

                String name= etName.getText().toString();
                String username= etUsername.getText().toString();
                String password= etPassword.getText().toString();
                int age = Integer.parseInt(etAge.getText().toString());


                User registeredData =new User(name,age,username,password) ;

                break;
        }

    }
}
