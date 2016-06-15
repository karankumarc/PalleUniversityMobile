package com.techpalle.poc;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

class RowClass{
	public String videoTitle;
	public String videoDuration;
	public String videoCode;
}
enum PurchseMode{
        invalid,
        sdcard,
        pendrive,
        webaccess,
        both
        }

public class CourseDetailsActivity extends AppCompatActivity {

	private ActionBar ab;
    private TextView courseTitle;
	private String course, course_id;
    private String course_fee_inr, course_fee_inr_after_discount,course_fee_usd,course_fee_usd_after_discount;


    private TextView tv;
	private ViewPager viewPager;
	private Button enroll;
	private TextView tv6, tv7, tv16, tv17;
    private String priceINR, priceUSD;
	private ImageView sdCardImage, webImage; //added
    private int count = -1;

    private CheckBox payWithSd, payWithPenDrive, payWithWebAccess; //added
    private LinearLayout courseDetails,courseFeeDetails;
    private TextView border;
    private int lastSelected; //1-sdcard, 2-pendrive

    private PurchseMode purchseMode = PurchseMode.invalid;

	private class SampleFragmentPagerAdapter extends FragmentStatePagerAdapter{
		final int PAGE_COUNT = 2;//4;
		private String[] titles = {"Demo Videos","Course Details"};//,"About Trainer","Reviews"};
		
		public SampleFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int pos) {
			switch(pos){
			case 0: //demo videos
				return DemoVideosFragment.newInstance(pos+1, course);
			case 1: //course details
				return PurchasedCourseContentFragment.newInstance(-1, course);
			default:
				return DemoVideosFragment.newInstance(pos+1, course);
			}
			
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
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.course_details);
	   //	sdCardImage = (ImageView) findViewById(R.id.sdCardImage);
        Intent in = getIntent();
        Bundle bnd = in.getExtras();

        if(bnd != null){
            course = bnd.getString("course");
            course_id = bnd.getString("course_id");
            course_fee_inr = bnd.getString("course_fee_inr");
            course_fee_inr_after_discount = bnd.getString("course_fee_inr_after_discount");;
            course_fee_usd = bnd.getString("course_fee_usd");
            course_fee_usd_after_discount = bnd.getString("course_fee_usd_after_discount");

        }
        courseTitle = (TextView) findViewById(R.id.courseTitle);
        courseTitle.setText(course);

        payWithSd = (CheckBox) findViewById(R.id.payWithSd);
        payWithPenDrive = (CheckBox) findViewById(R.id.payWithPenDrive);
        payWithWebAccess = (CheckBox) findViewById(R.id.payWithWebAccess); //added
        courseDetails = (LinearLayout) findViewById(R.id.courseDetails);
        courseFeeDetails = (LinearLayout) findViewById(R.id.courseFeeDetails);
        border = (TextView) findViewById(R.id.border);

        CompoundButton.OnCheckedChangeListener paywithclicklistner2 = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                courseDetails.setVisibility(View.VISIBLE);
                courseFeeDetails.setVisibility(View.VISIBLE);
                border.setVisibility(View.VISIBLE);

                //added - Modified login to accommodate additional checkbox
                if((payWithSd.isChecked() && payWithPenDrive.isChecked()) || (payWithSd.isChecked() && payWithWebAccess.isChecked())
                        || (payWithWebAccess.isChecked() && payWithPenDrive.isChecked())){
                    if(lastSelected == 1){
                        if(payWithPenDrive.isChecked()){
                            lastSelected = 2;
                            purchseMode = PurchseMode.pendrive;
                            payWithSd.setChecked(false);
                        } else {
                            lastSelected = 3;
                            purchseMode = PurchseMode.webaccess;
                            payWithSd.setChecked(false);
                        }
                    }else if(lastSelected == 2){
                        if(payWithSd.isChecked()){
                            lastSelected = 1;
                            purchseMode = PurchseMode.sdcard;
                            payWithPenDrive.setChecked(false);
                        } else {
                            lastSelected = 3;
                            purchseMode = PurchseMode.webaccess;
                            payWithPenDrive.setChecked(false);
                        }
                    } else {
                        if(payWithSd.isChecked()){
                            lastSelected = 1;
                            purchseMode = PurchseMode.sdcard;
                            payWithWebAccess.setChecked(false);
                        } else {
                            lastSelected = 2;
                            purchseMode = PurchseMode.pendrive;
                            payWithWebAccess.setChecked(false);
                        }
                    }
                    /*purchseMode = PurchseMode.both;
                    tv6.setText("\u20B9 "+((Integer.parseInt(course_fee_inr.trim())*2)));
                    tv16.setText("\u20B9 "+((Integer.parseInt(course_fee_inr_after_discount.trim())*2)));
                    tv7.setText("$ "+((Integer.parseInt(course_fee_usd.trim())*2)));
                    tv17.setText("$ "+((Integer.parseInt(course_fee_usd_after_discount.trim())*2)));*/
                }else if(payWithSd.isChecked()){
                    lastSelected = 1;
                    purchseMode = PurchseMode.sdcard;
                    tv6.setText("\u20B9 "+((Integer.parseInt(course_fee_inr.trim()))));
                    tv16.setText("\u20B9 "+((Integer.parseInt(course_fee_inr_after_discount.trim()))));
                    tv7.setText("$ "+((Integer.parseInt(course_fee_usd.trim()))));
                    tv17.setText("$ "+((Integer.parseInt(course_fee_usd_after_discount.trim()))));
                }else if(payWithPenDrive.isChecked()){
                    lastSelected = 2;
                    purchseMode = PurchseMode.pendrive;
                    tv6.setText("\u20B9 "+((Integer.parseInt(course_fee_inr.trim()))));
                    tv16.setText("\u20B9 "+((Integer.parseInt(course_fee_inr_after_discount.trim()))));
                    tv7.setText("$ "+((Integer.parseInt(course_fee_usd.trim()))));
                    tv17.setText("$ "+((Integer.parseInt(course_fee_usd_after_discount.trim()))));
                } else if(payWithWebAccess.isChecked()){ //added else if
                    lastSelected = 3;
                    purchseMode = PurchseMode.webaccess;
                    tv6.setText("\u20B9 "+((Integer.parseInt(course_fee_inr.trim()))));
                    tv16.setText("\u20B9 "+((Integer.parseInt(course_fee_inr_after_discount.trim()))));
                    tv7.setText("$ "+((Integer.parseInt(course_fee_usd.trim()))));
                    tv17.setText("$ "+((Integer.parseInt(course_fee_usd_after_discount.trim()))));
                } else{
                    purchseMode = PurchseMode.invalid;
                    tv6.setText("\u20B9 "+((Integer.parseInt(course_fee_inr.trim()))));
                    tv16.setText("\u20B9 "+((Integer.parseInt(course_fee_inr_after_discount.trim()))));
                    tv7.setText("$ "+((Integer.parseInt(course_fee_usd.trim()))));
                    tv17.setText("$ "+((Integer.parseInt(course_fee_usd_after_discount.trim()))));
                }
                //tv6.setText("\u20B9 "+course_fee_inr);


            }
        };

        OnClickListener paywithclicklistener = new OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        };


        payWithSd.setOnCheckedChangeListener(paywithclicklistner2);
        payWithPenDrive.setOnCheckedChangeListener(paywithclicklistner2);
        payWithWebAccess.setOnCheckedChangeListener(paywithclicklistner2); //added
        //payWithSd.setOnClickListener(paywithclicklistener);
        //payWithPenDrive.setOnClickListener(paywithclicklistener);


        enroll = (Button) findViewById(R.id.enroll);
	    tv = (TextView) findViewById(R.id.textView1);

        tv6 = (TextView) findViewById(R.id.textView6);
		tv7 = (TextView) findViewById(R.id.textView7);

        tv6.setText("\u20B9 "+course_fee_inr);
		tv6.setPaintFlags(tv6.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
		tv7.setText("$ " + course_fee_usd);
		tv7.setPaintFlags(tv7.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        tv16 = (TextView) findViewById(R.id.textView16);
        tv16.setText("₹ "+course_fee_inr_after_discount);
        priceINR = tv16.getText().toString();
        tv17 = (TextView) findViewById(R.id.textView17);
        tv17.setText("$ "+course_fee_usd_after_discount);

        priceUSD = tv17.getText().toString();


		viewPager = (ViewPager) findViewById(R.id.mypager);
	    viewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager()));
	    
	    
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
	    		WindowManager.LayoutParams.FLAG_FULLSCREEN);


	    ab = getSupportActionBar();
	    
	    Drawable d = getResources().getDrawable(R.drawable.actionbar_bg_shape);
	    ab.setBackgroundDrawable(d);
	    ab.setDisplayShowTitleEnabled(true);
	    ab.setDisplayHomeAsUpEnabled(true);
	    //if(course != null)
	    ab.setTitle(course);
	    
	    //tv.setText(course);
	    
	    /*sdCardImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

	    /*ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | 
				ActionBar.DISPLAY_SHOW_CUSTOM);
		*/
	    enroll.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                if(purchseMode == PurchseMode.invalid){
                    Toast.makeText(CourseDetailsActivity.this, "Please select one check box", Toast.LENGTH_LONG).show();
                    return;
                }
                String price_inr_after_discount = null;
                String price_usd_after_discount = null;

                MyDialog m=new MyDialog();
                Bundle bnd = new Bundle();
                bnd.putString("course",course);
				bnd.putString("course_id", course_id);
                if(purchseMode == PurchseMode.both){
                    priceINR = ""+(Integer.parseInt(course_fee_inr)*2);
                    priceUSD = ""+(Integer.parseInt(course_fee_usd)*2);
                    price_inr_after_discount = ""+(Integer.parseInt(course_fee_inr_after_discount.trim())*2);
                    price_usd_after_discount = ""+(Integer.parseInt(course_fee_usd_after_discount.trim())*2);
                }else if(purchseMode == PurchseMode.sdcard || purchseMode == PurchseMode.pendrive
                        || purchseMode == PurchseMode.webaccess){
                    priceINR = ""+(Integer.parseInt(course_fee_inr));
                    priceUSD = ""+(Integer.parseInt(course_fee_usd));
                    price_inr_after_discount = ""+(Integer.parseInt(course_fee_inr_after_discount.trim()));
                    price_usd_after_discount = ""+(Integer.parseInt(course_fee_usd_after_discount.trim()));
                }

                bnd.putString("priceINR",priceINR);
                bnd.putString("priceUSD",priceUSD);
                bnd.putString("price_inr_after_discount",price_inr_after_discount);
                bnd.putString("price_usd_after_discount", price_usd_after_discount);

                if(purchseMode == PurchseMode.webaccess) {
                    bnd.putInt("purchaseWith", 2);
                }else if(purchseMode == PurchseMode.pendrive){
                    bnd.putInt("purchaseWith", 1);
                }else if(purchseMode == PurchseMode.sdcard){
                    bnd.putInt("purchaseWith", 0);
                }

                m.setArguments(bnd);
                m.show(getSupportFragmentManager(), "mydialog");


				/*Intent in = new Intent(CourseDetailsActivity.this, CoursePurchaseActivity.class);
				in.putExtra("course",course);
				in.putExtra("priceINR",priceINR);
				in.putExtra("priceUSD",priceUSD);
				startActivity(in);
                */

				if(course.equals("C# For Freshers")){
					//LAUNCH CC AVENUE - only testing purpose
					//MyDialog m=new MyDialog();

					//m.show(getSupportFragmentManager(), "mydialog");

					/*Intent in = new Intent(CourseDetailsActivity.this,
							InitialScreenActivity.class);
					startActivity(in);*/
				}
				if(course.equals("C# For Experts")){
					//LAUNCH PAYPAL - only testing purpose
					//MyDialog m=new MyDialog();
					//m.show(getSupportFragmentManager(), "mydialog");

					/*Intent in = new Intent(CourseDetailsActivity.this,
							PayPalActivity.class);
					startActivity(in);*/
				}

			}
		});
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id){
            case android.R.id.home:
                finish();
                break;
            case R.id.contact:
				Intent in = new Intent(this, ContactUsActivity.class);
				startActivity(in);
                break;
            case R.id.mail:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.coursedetailsmenu, menu);
		menu.getItem(1).setVisible(false);
		return true;
	}

}
