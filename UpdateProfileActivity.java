package com.example.Latehtar.Client;

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
import com.example.Latehtar.API;
import com.example.Latehtar.MainActivity;
import com.example.Latehtar.NavigrationDrawer;
import com.example.Latehtar.R;
import com.example.Latehtar.Session;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateProfileActivity extends NavigrationDrawer {

    Button update;
    EditText fname,lname, phone, mail;
    boolean flag = false;
    ProgressDialog progressDialog;
    RequestQueue queue;
    Session session;


    @Override
    public int getLayoutId() {
        return R.layout.activity_update_profile;
    }

    @Override
    public String getCustomTitle() {
        return "Update Profile";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new Session(this);

        progressDialog = new ProgressDialog(UpdateProfileActivity.this, R.style.AppTheme_Dark_Dialog);
        queue = Volley.newRequestQueue(UpdateProfileActivity.this);
        update = (Button) findViewById(R.id.update);
        fname = (EditText) findViewById(R.id.fname);
        lname = (EditText) findViewById(R.id.lname);
        phone = (EditText) findViewById(R.id.phone);
        mail = (EditText) findViewById(R.id.email);


        fname.setText(session.getName().split("-")[0]);
        lname.setText(session.getName().split("-")[1]);
        phone.setText(session.getPhone());
        mail.setText(session.getEmail());


        progressDialog = new ProgressDialog(UpdateProfileActivity.this, R.style.AppTheme_Dark_Dialog);
        queue = Volley.newRequestQueue(getBaseContext());

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Update();
            }
        });
    }

    private void Update() {

        flag = false;
        if (fname.getText().toString().isEmpty()) {
            fname.setError("Please Enter First Name");
            flag = true;
        }

        if (phone.getText().toString().isEmpty()) {
            phone.setError("Please Enter Mobile Number");
            flag = true;
        } else {
            if (phone.getText().toString().length() != 10) {
                phone.setError("Please Enter Valid Mobile Number");
                flag = true;
            } else if (phone.getText().toString().charAt(0) != '0' || phone.getText().toString().charAt(1) != '5') {
                phone.setError("Please Enter Valid Mobile Number");
                flag = true;
            }
        }
        if (mail.getText().toString().isEmpty()) {
            mail.setError("Please Enter Email");
            flag = true;
        } else {
            Pattern pattern;
            Matcher matcher;

            String EMAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                    "[a-zA-Z0-9_+&*-]+)*@" +
                    "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                    "A-Z]{2,7}$";
            pattern = Pattern.compile(EMAIL_PATTERN);
            matcher = pattern.matcher(mail.getText().toString());
            if (!matcher.matches()) {

                mail.setError("Please Enter Valid Email");
                flag = true;
            }

        }


        if (!flag) {
            progressDialog.setMessage(getResources().getString(R.string.wait));
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    API.domain + "UpdateProfile.php", new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);

                        boolean succes = jsonResponse.getBoolean("succes");
                        boolean succe = jsonResponse.getBoolean("succe");

                        if (   succes && succe) {
                            progressDialog.dismiss();

                            session.setName(fname.getText().toString() + "-" + lname.getText().toString());
                            session.setEmail(mail.getText().toString());
                            session.setPhone(phone.getText().toString());

                            Toast.makeText(UpdateProfileActivity.this, "User Updated", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getBaseContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                            finish();
                        }else if(!succes){
                            progressDialog.dismiss();
                            Toast.makeText(UpdateProfileActivity.this, "Mobile Number Already Used!", Toast.LENGTH_SHORT).show();

                        }else if(!succe){
                            progressDialog.dismiss();
                            Toast.makeText(UpdateProfileActivity.this, "Email Already Used!", Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.nointernet), Toast.LENGTH_SHORT).show();

                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id", session.getId());
                    params.put("fname", fname.getText().toString());
                    params.put("lname", lname.getText().toString());
                    params.put("phone", phone.getText().toString());
                    params.put("email", mail.getText().toString());



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
}
