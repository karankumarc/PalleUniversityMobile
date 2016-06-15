package com.techpalle.poc;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

public class LoginActivity extends AppCompatActivity {

    ActionBar ab;
    String course;
    String priceINR, priceUSD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent in = getIntent();
        if(in != null){
            Bundle bnd = in.getExtras();
            if(bnd != null){
                course = bnd.getString("course");
                priceINR = bnd.getString("priceINR");
                priceUSD = bnd.getString("priceUSD");
            }
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ab = getSupportActionBar();

        Drawable d = getResources().getDrawable(R.drawable.actionbar_bg_shape);
        ab.setBackgroundDrawable(d);
        ab.setDisplayShowTitleEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        //if(course != null)
        ab.setTitle(course);


        Frag3 m1=new Frag3();
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.add(R.id.container, m1);
        ft.commit();
    }

    public void pressed()
    {

        Terms t=new Terms();
        FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container,t).addToBackStack("tag");
        ft.commit();

    }
    public void pressedprivacy()
    {

        Privacy p=new Privacy();
        FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, p);
        ft.commit();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        /*if (id == R.id.action_settings) {
            return true;
        }*/
        if(id==android.R.id.home)
        {
            Frag1 mmm=new Frag1();
            FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container,mmm);
            ft.commit();
        }
        return super.onOptionsItemSelected(item);
    }
}
