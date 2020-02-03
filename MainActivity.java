package com.example.easylearn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button bLogout ;
    EditText etName , etUsername , etAge ;
    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        etName =(EditText) findViewById(R.id.etName);
        etUsername =(EditText) findViewById(R.id.etUsername);
        etAge =(EditText) findViewById(R.id.etAge);

        Button bLogout = (Button) findViewById(R.id.bLogout);

        bLogout.setOnClickListener(this);

        userLocalStore =new UserLocalStore(this);
    }


    protected void onStart(){
super.onStart();

if (authenticate()==true)
    displayUserDetails();
    }


private boolean authenticate(){
return userLocalStore.getUserLoggedIn();
}


private void displayUserDetails(){
        User user = userLocalStore.getLoggedInUser();
etUsername.setText(user.username);
    etName.setText(user.name);
    etAge.setText((user.age +""));



}



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bLogout:
                userLocalStore.clearUserData();
                userLocalStore.setUserLoggedIn(false);

                startActivity(new Intent(this,Login.class));


                break;
        }
    }
}
