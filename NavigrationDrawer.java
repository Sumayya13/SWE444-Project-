package com.example.Latehtar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.Latehtar.Client.ClientMainActivity;

import com.example.Latehtar.Client.MyPoll.MyPollActivity;
import com.example.Latehtar.Client.UpdatePasswordActivity;
import com.example.Latehtar.Client.UpdateProfileActivity;
import com.example.Latehtar.Client.images.RandomImageListActivity;
import com.example.Latehtar.Client.images.SelectdImageActivity;
import com.google.android.material.navigation.NavigationView;


public abstract class NavigrationDrawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected NavigationView navigationView;

    Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigration_activity);
        session = new Session(this);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getCustomTitle());
        FrameLayout frameLayout = findViewById(R.id.main_content);
        getLayoutInflater().inflate(getLayoutId(), frameLayout);
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if(session.getType().equals("client")){
            navigationView.inflateMenu(R.menu.client);
        }
    }

    public abstract int getLayoutId();

    public abstract String getCustomTitle();

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if(session.getType().equals("client")) {
                int id = item.getItemId();
                if (id == R.id.home) {
                    launchActivity(ClientMainActivity.class);
                }
                else if(R.id.mypoll == id){
                    launchActivity(MyPollActivity.class);
                }
                else if(R.id.image == id){
                    launchActivity(RandomImageListActivity.class);
                }
                else if(R.id.random == id){

                    launchActivity(SelectdImageActivity.class);

                }
                else if (id == R.id.profile) {
                    launchActivity(UpdateProfileActivity.class);
                }
                else if (id == R.id.password) {
                    launchActivity(UpdatePasswordActivity.class);
                }
                else if (id == R.id.logout) {
                    session.setLogin(false);
                    session.setEmail("");
                    session.setId("");
                    session.setName("");
                    startActivity(new Intent(getBaseContext(),MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    finish();
                }
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;

        }
        return false;
    }

    private void launchActivity(Class _class) {
        if (this.getClass() != _class) {
            startActivity(makePageIntent(_class));
            overridePendingTransition(0,0);
        }
    }
    private void launchActivity(Class _class,String key) {
        if (this.getClass() != _class) {
            startActivity(makePageIntent(_class,key));
            overridePendingTransition(0,0);
        }
    }
    public Intent makePageIntent(Class _class) {
        Intent intent = new Intent(this, _class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    public Intent makePageIntent(Class _class,String key) {
        Intent intent = new Intent(this, _class);
        intent.putExtra(key,session.getId());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }
}
