package com.example.Latehtar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Latehtar.Client.ClientMainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    EditText username, password;
    Button btnLogin,btnRegister;
    Session session;
    ProgressDialog progressDialog;
    RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        btnLogin = findViewById(R.id.login);

        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(),RegisterActivity.class));
            }
        });
        progressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        queue = Volley.newRequestQueue(MainActivity.this);
        session = new Session(this);
        if(session.getLoggedIn()){
            Intent i = new Intent(getBaseContext(), ClientMainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();

        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().isEmpty()) {
                    username.setError("Please Enter Username");
                } else if (password.getText().toString().isEmpty()) {
                    password.setError("Please Enter Password");
                } else {
                    progressDialog.setMessage(getString(R.string.wait));
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST,
                            API.domain + "Login.php", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                String fname,lname, phone,email, id = " ";
                                if (success) {
                                    String type = jsonResponse.getString("role");
                                    if (type.equals("client")) {
                                        JSONArray array = jsonResponse.getJSONArray("client");
                                        id = array.getJSONObject(0).getString("id");
                                        fname = array.getJSONObject(0).getString("fname");
                                        lname = array.getJSONObject(0).getString("lname");
                                        email = array.getJSONObject(0).getString("email");
                                        phone = array.getJSONObject(0).getString("phone");

                                        session.setId(id);
                                        session.setEmail(email);
                                        session.setPhone(phone);
                                        session.setName(fname + "-" + lname);
                                        session.setLogin(true);
                                        session.setType(type);

                                        progressDialog.dismiss();
                                        Toast.makeText(MainActivity.this, "User Logged In", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(getBaseContext(), ClientMainActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(i);
                                        finish();
                                    }
                                }
                                else {
                                    progressDialog.dismiss();
                                    Toast.makeText(MainActivity.this, "Username Or Password Incorrect !!", Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, getResources().getString(R.string.nointernet), Toast.LENGTH_SHORT).show();

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("username", username.getText().toString());
                            params.put("password", password.getText().toString());
                            return params;
                        }
                    };
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                            30000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    queue.add(stringRequest);
                }

            }
        });
    }


}
