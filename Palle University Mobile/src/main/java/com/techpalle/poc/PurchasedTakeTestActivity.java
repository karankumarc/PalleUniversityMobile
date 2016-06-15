package com.techpalle.poc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.techpalle.poc.TakeTestInProgressFragment.OnSubmitResultSelected;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class PurchasedTakeTestActivity extends AppCompatActivity
					implements TakeTestWelcomeFragment.OnStartTestSelected,
								OnSubmitResultSelected{
	private int pos = -1;

	LinearLayout parent;
	private TakeTestWelcomeFragment welcomeFrag;
	private TakeTestInProgressFragment testInProgressFrag;
	private TakeTestResultsFragment testResultFrag;
	private FragmentManager fragManager;
	private FragmentTransaction fragTransaction;
	private ActionBar ab;
	private String pid;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.purchased_take_test);

		Intent in = getIntent();
		Bundle bnd = in.getExtras();
		if(bnd != null){
			pos = bnd.getInt("pos");
			pid = bnd.getString("pid");
		}
        /*if(pid != null){
            //change orientation to portrait as user is coming form video view
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }*/
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
	    		WindowManager.LayoutParams.FLAG_FULLSCREEN);

	    ab = getSupportActionBar();
	    //ab.hide();
	    Drawable d = getResources().getDrawable(R.drawable.actionbar_bg_shape);
	    ab.setBackgroundDrawable(d);
	    ab.setDisplayShowTitleEnabled(true);
	    ab.setDisplayHomeAsUpEnabled(true);
	    //if(course != null)
	    ab.setTitle("Test In Progress");

	    
	    parent = (LinearLayout) findViewById(R.id.parent);	    

        if(savedInstanceState == null) {
            fragManager = getSupportFragmentManager();
            fragTransaction = fragManager.beginTransaction();
            welcomeFrag = new TakeTestWelcomeFragment();
            fragTransaction.add(R.id.parent, welcomeFrag, "welcome");
            fragTransaction.commit();
        }
	    
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
	public void onStartTestClicked() {
		// TODO Auto-generated method stub
		Toast.makeText(this, "OK..I GOT YOUR MESSAGE", Toast.LENGTH_LONG).show();
		fragTransaction = fragManager.beginTransaction();
		testInProgressFrag = new TakeTestInProgressFragment();
        Bundle bnd = new Bundle();
        bnd.putInt("pos",pos);
        bnd.putString("pid",pid);
        testInProgressFrag.setArguments(bnd);

		fragTransaction.replace(R.id.parent, testInProgressFrag);
	    fragTransaction.commit();
	    
	}

	@Override
	public void onSubmitResultClicked(ArrayList<QuestionsAndAnswers> list) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "OK..submit test results clicked", Toast.LENGTH_LONG).show();
		fragTransaction = fragManager.beginTransaction();
		testResultFrag = new TakeTestResultsFragment(list);
		fragTransaction.replace(R.id.parent, testResultFrag);
	    fragTransaction.commit();
	}

}


class QuestionsAndAnswers{
	private String question, op1, op2, op3, op4, op5;
	private String ans;
	private int userChoice;
	
	public int getUserChoice() {
		return userChoice;
	}
	public void setUserChoice(int userChoice) {
		this.userChoice = userChoice;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getOp1() {
		return op1;
	}
	public void setOp1(String op1) {
		this.op1 = op1;
	}
	public String getOp2() {
		return op2;
	}
	public void setOp2(String op2) {
		this.op2 = op2;
	}
	public String getOp3() {
		return op3;
	}
	public void setOp3(String op3) {
		this.op3 = op3;
	}
	public String getOp4() {
		return op4;
	}
	public void setOp4(String op4) {
		this.op4 = op4;
	}
	public String getOp5() {
		return op5;
	}
	public void setOp5(String op5) {
		this.op5 = op5;
	}
	public String getAns() {
		return ans;
	}
	public void setAns(String ans) {
		this.ans = ans;
	}	
}

