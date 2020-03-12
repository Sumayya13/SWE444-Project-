package com.example.Latehtar.Client;

import android.app.ProgressDialog;
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
import com.example.Latehtar.NavigrationDrawer;
import com.example.Latehtar.R;
import com.example.Latehtar.Session;

import java.util.HashMap;
import java.util.Map;

public class UpdatePasswordActivity extends NavigrationDrawer {

    EditText pssword1;
    EditText password2;
    ProgressDialog progressDialog;

    Button save;
    RequestQueue queue;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queue = Volley.newRequestQueue(UpdatePasswordActivity.this);
        progressDialog = new ProgressDialog(UpdatePasswordActivity.this, R.style.AppTheme_Dark_Dialog);
        pssword1 = findViewById(R.id.etoldPassword);
        password2 =  findViewById(R.id.etnewPassword);
        save =  findViewById(R.id.btnChange);
        session = new Session(getBaseContext());
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePassword();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_update_password;
    }

    @Override
    public String getCustomTitle() {
        return "Update Password";
    }

    private void ChangePassword() {
        if (pssword1.getText().toString().isEmpty()) {
            pssword1.setError("Please Enter Previous Password");
        } else if (password2.getText().toString().isEmpty()) {
            password2.setError("Please Enter New Passsword");
        } else {
            if (password2.getText().toString().length() < 8) {
                password2.setError("Password Must Contains At Least 8 Letters");
                return;
            }
            if (password2.getText().toString().equals(password2.getText().toString().toLowerCase())) {
                password2.setError("Password Must Contains Capital and Small Letters");
                return;
            }
            progressDialog.setMessage(getResources().getString(R.string.wait));
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    API.domain+ "changepassword.php", new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();
                    finish();
                    Toast.makeText(UpdatePasswordActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(UpdatePasswordActivity.this, getResources().getString(R.string.nointernet), Toast.LENGTH_SHORT).show();

                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id", String.valueOf(session.getId()));
                    params.put("oldpass", pssword1.getText().toString());
                    params.put("newpass", password2.getText().toString());
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