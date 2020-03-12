package com.example.Latehtar.Client;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Latehtar.API;
import com.example.Latehtar.Client.Poll.Poll;
import com.example.Latehtar.Client.Poll.PollAdapter;
import com.example.Latehtar.NavigrationDrawer;
import com.example.Latehtar.R;
import com.example.Latehtar.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ClientMainActivity extends NavigrationDrawer {

    @Override
    public int getLayoutId() {
        return R.layout.activity_client_main;
    }

    @Override
    public String getCustomTitle() {
        return "Client Main";
    }


    RecyclerView recyclerView;
    PollAdapter pollAdapter;
    RequestQueue queue;
    ProgressDialog progressDialog;
    public static ArrayList<Poll> polls;
    TextView hide;
    Session session;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new Session(this);

        progressDialog = new ProgressDialog(ClientMainActivity.this, R.style.AppTheme_Dark_Dialog);
        queue = Volley.newRequestQueue(ClientMainActivity.this);
        polls = new ArrayList<>();
        hide = findViewById(R.id.hide);

        recyclerView = findViewById(R.id.recycler);
        pollAdapter = new PollAdapter(ClientMainActivity.this, polls);
        recyclerView.setLayoutManager(new GridLayoutManager(getBaseContext(), 1));
        recyclerView.setAdapter(pollAdapter);
        recyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPoll();
    }

    private void getPoll(){
        polls.clear();
        progressDialog.setMessage(getResources().getString(R.string.wait));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                API.domain+"getPoll.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray result = new JSONArray(response);
                    if (result.length() == 0) {
                        hide.setVisibility(View.VISIBLE);
                        Toast.makeText(getBaseContext(), "No Poll", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }else {
                        hide.setVisibility(View.GONE);
                        for (int i = 0; i < result.length(); i++) {
                            JSONObject object = result.getJSONObject(i);
                            Poll data = new Poll();

                            data.setId(object.getString("id"));
                            data.setDatetime(object.getString("datetime"));
                            data.setCaprion(object.getString("caption"));
                            data.setClientname(object.getString("cname"));
                            data.setEnddate(object.getString("enddate"));
                            data.setEndtime(object.getString("endtime"));
                            data.setStatus(object.getString("status"));
                            data.setCategory(object.getString("category"));
                            data.setImage1(object.getString("image1"));
                            data.setImage2(object.getString("image2"));
                            if(!session.getId().equals(object.getString("clientid"))) {

                                Date userDob = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(object.getString("enddate") + " " + object.getString("endtime"));
                                Date today = new Date();
                                long diff =    userDob.getTime() - today.getTime();
                                long diffInMin = TimeUnit.MILLISECONDS.toMinutes(diff);


                                if(diffInMin > 0 ) {

                                    polls.add(data);
                                }
                            }
                            pollAdapter.notifyDataSetChanged();
                        }
                        pollAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                } catch (ParseException e) {
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
