package com.techpalle.poc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ContactUsActivity extends AppCompatActivity implements ContactUsFragment.OnFragmentInteractionListener {
    private ActionBar ab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ab = getSupportActionBar();
        //ab.setDisplayShowTitleEnabled(false);
        Drawable d = getResources().getDrawable(R.drawable.actionbar_bg_shape);
        ab.setBackgroundDrawable(d);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Contact Us");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteractionContactUs(Uri uri) {

    }
}
