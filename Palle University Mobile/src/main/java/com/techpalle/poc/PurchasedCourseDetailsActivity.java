package com.techpalle.poc;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.TextView;

public class PurchasedCourseDetailsActivity extends AppCompatActivity {

	private ActionBar ab;
	private String course;
	private TextView tv;
	private ViewPager viewPager;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.purchased_course_details);
	    // TODO Auto-generated method stub
	    tv = (TextView) findViewById(R.id.textView1);
	    
	    viewPager = (ViewPager) findViewById(R.id.mypager);
	    viewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager()));
	    
	    
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
	    		WindowManager.LayoutParams.FLAG_FULLSCREEN);

	    Intent in = getIntent();
	    Bundle bnd = in.getExtras();
	    if(bnd != null){
	    	course = bnd.getString("course");
	    }
	    
	    ab = getSupportActionBar();
	    
	    Drawable d = getResources().getDrawable(R.drawable.actionbar_bg_shape);
	    ab.setBackgroundDrawable(d);
	    ab.setDisplayShowTitleEnabled(true);
	    ab.setDisplayHomeAsUpEnabled(true);
	    //if(course != null)
	    ab.setTitle(course);
	    
	    //tv.setText(course);
	    
	    
	    /*ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | 
				ActionBar.DISPLAY_SHOW_CUSTOM);
		*/

	}
	
	private class SampleFragmentPagerAdapter extends FragmentStatePagerAdapter{
		final int PAGE_COUNT = 4;
		private String[] titles = {"Course Details","Lectures","Take Test","Downloads"};
		
		public SampleFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int pos) {
			/*switch(pos){
			case 0: //demo videos
				return PurchasedVideosFragment.newInstance(pos+1);
			default:
				return PurchasedVideosFragment.newInstance(pos+1);
			}*/
			return null;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return PAGE_COUNT;
		}
		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			return titles[position];
		}
	}


}
