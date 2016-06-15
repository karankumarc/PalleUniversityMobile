package com.techpalle.poc;

import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class HomeActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListenerHome, ContactUsFragment.OnFragmentInteractionListener, MyOrdersFragment.OnFragmentInteractionListenerOders, NavigationView.OnNavigationItemSelectedListener {
	private ActionBar ab;
    //private ActionBarDrawerToggle mDrawerToggle;
    //private DrawerLayout mDrawerLayout;
    //private ListView mDrawerList;
    private String[] drawerlistitems = {"Home", "My Courses", "Orders", "Refer & Earn", "Contact Us", "Settings"};
    //private MyDrawerAdapter myDrawerAdapter;
    private CharSequence mTitle;
    private ImageView iv;
    private int prevItemSelected = -1;
    private ActionBarDrawerToggle toggle;

    private String encodeData(String input) throws UnsupportedEncodingException {
        byte[] encoded = org.apache.commons.codec.binary.Base64.encodeBase64(input.getBytes());
        return new String(encoded);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentInteractionContactUs(Uri uri) {

    }

    @Override
    public void onFragmentInteractionOrders(Uri uri) {

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int position = -1;
        switch(item.getTitle().toString()){
            case "Home":
                position = 0;
                break;
            case "My Courses":
                position = 1;
                break;
            case "My Orders":
                position = 2;
                break;
            case "Refer and Earn":
                position = 3;
                break;
            case "Quick Enquiry":
                position = 4;
                break;
        }

        if(prevItemSelected == position) {
            //mDrawerLayout.closeDrawer(mDrawerList);
            return true;
        }

        Fragment frag = null;



        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
            frag = HomeFragment.newInstance(null,"Home");
        } else if (id == R.id.nav_my_courses) {
            frag = HomeFragment.newInstance("My Courses","My Courses");
        } else if (id == R.id.nav_my_oders) {
            frag = MyOrdersFragment.newInstance(null, "My Orders");
        } else if (id == R.id.nav_ref_and_earn) {
            frag = ReferAndEarnFragment.newInstance(null, null);
        } else if (id == R.id.nav_send) {
            frag = ContactUsFragment.newInstance(null, "Contact Us");
        }

        // Insert the fragment by replacing any existing fragment
        if(frag != null) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, frag);
            if(prevItemSelected == 0) {
                //ft.addToBackStack(null);
            }
            ft.commit();
            // Highlight the selected item, update the title, and close the drawer
            //mDrawerList.setItemChecked(position, true);
            ab.setDisplayShowTitleEnabled(true);
            setTitle(drawerlistitems[position]);
            iv.setVisibility(View.GONE);
            //mDrawerLayout.closeDrawer(mDrawerList);
            prevItemSelected = position;
        }

        if(drawerlistitems[position].equals("Home")){
            iv.setVisibility(View.VISIBLE);
            ab.setDisplayShowTitleEnabled(false);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class MyDrawerAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return drawerlistitems.length;
        }

        @Override
        public Object getItem(int position) {
            return drawerlistitems[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = getLayoutInflater().inflate(R.layout.drawer_list_item, null);
            TextView tv = (TextView) v.findViewById(R.id.textView);
            tv.setText(drawerlistitems[position]);
            return v;
        }
    }


    /** Swaps fragments in the main content view */
	private void selectItem(int position) {
		// Create a new fragment and specify the planet to show based on position

        if(prevItemSelected == position) {
            //mDrawerLayout.closeDrawer(mDrawerList);
            return;
        }

        Fragment frag = null;
        switch(drawerlistitems[position]){
            case "Home":
                frag = HomeFragment.newInstance(null,"Home");
                break;
            case "My Courses":
                frag = HomeFragment.newInstance("My Courses","My Courses");
                break;
            case "Orders":
                frag = MyOrdersFragment.newInstance(null, "My Orders");
                break;
            case "Refer & Earn":
                break;
            case "Contact Us":
                frag = ContactUsFragment.newInstance(null, "Contact Us");
                break;
            case "Settings":
                break;
            default:
                return;
        }

		// Insert the fragment by replacing any existing fragment
        if(frag != null) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, frag);
            if(prevItemSelected == 0) {
                //ft.addToBackStack(null);
            }
            ft.commit();
            // Highlight the selected item, update the title, and close the drawer
            //mDrawerList.setItemChecked(position, true);
            ab.setDisplayShowTitleEnabled(true);
            setTitle(drawerlistitems[position]);
            iv.setVisibility(View.GONE);
            //mDrawerLayout.closeDrawer(mDrawerList);
            prevItemSelected = position;
        }
        if(drawerlistitems[position].equals("Home")){
            iv.setVisibility(View.VISIBLE);
            ab.setDisplayShowTitleEnabled(false);
        }

	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		//getSupportActionBar().setTitle(mTitle);
        ab.setTitle(mTitle);
	}

    public void homeFragResumed(){
        //reset the action bar title
        iv.setVisibility(View.VISIBLE);
        ab.setDisplayShowTitleEnabled(false);
    }


    /** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.home);


        prevItemSelected = 0;

	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //mDrawerList = (ListView) findViewById(R.id.left_drawer);
        //myDrawerAdapter = new MyDrawerAdapter();
        //mDrawerList.setAdapter(myDrawerAdapter);

		/*mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				selectItem(position);
			}
		});*/

		ab = getSupportActionBar();
	    ab.setDisplayShowTitleEnabled(false);
	    //ab.setDisplayHomeAsUpEnabled(true);
	    Drawable d = getResources().getDrawable(R.drawable.actionbar_bg_shape);
	    ab.setBackgroundDrawable(d);
	    /*ab.setLogo(R.drawable.palle_university_logo);*/
	    //ab.setDisplayUseLogoEnabled(true);
	    iv = new ImageView(this);
	    iv.setImageResource(R.drawable.palle_university_logo);
	    ab.setCustomView(iv);
	    
	    ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME |
                ActionBar.DISPLAY_SHOW_CUSTOM);
/*
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_name) {

            *//** Called when a drawer has settled in a completely closed state. *//*
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            *//** Called when a drawer has settled in a completely open state. *//*
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);*/
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);

        HomeFragment homeFragment = HomeFragment.newInstance(null, null);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.container, homeFragment);
        //ft.addToBackStack("home");
        ft.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.app_name, R.string.app_name);

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }

		int id = item.getItemId();

        switch(id){
			case android.R.id.home:
				break;
			/*case R.id.search:
				break;*/
		}
		return super.onOptionsItemSelected(item);
	}



    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.homemenu, menu);
		return true;
	}
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        toggle.syncState();
    }

}
