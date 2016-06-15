package com.techpalle.poc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

//import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.EncoderException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.provider.Settings.Secure;
//import android.util.Base64;
import android.util.Base64;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import junit.framework.Test;

public class SDCardActivity extends AppCompatActivity {
	private File file;
	//private ArrayList<String> myVideosList;
	//private ArrayList<Topics> myTopics;
	private Map<String, ArrayList<VideoNames>> topicAndVideos;
    private ArrayList<Tests> testpapers;

	private ArrayAdapter<String> aa;
	private ListView lv;
	private TextView tv;
	private SharedPreferences sp;
	private SharedPreferences.Editor et;
	private CredDataBase cdb;
	private String po_order_no;
	private MyTask myTask;
	
	private String imei_meid_esn;
	private String simSerialNumber;
	private String phoneNumber;
	private String ANDROID_ID;
	private String BUILD_SERIAL_NO;
	
	private ConnectivityManager connMgr;
	private MyProgressDialog mpd;
	private MySdCardUserSettingFileGeneratorDialog mysdcardDialog;
	private MySdCardFileGeneratorTask sdcardtask;
	
	//new
	private ActionBar ab;
	private String course;
	public static String sharedCourse;
	//private TextView tv;
	private ViewPager viewPager;
	//new end
	/*
	 * Below are our predefined never changing folder paths
	 * in our sd card which will be sold to user.
	 */
	private String datatypespath = "/palle_university/Full .NET/csharp/encrypted/";
	
	/*
	 * Below folder is used to store decrypted temporary files. Once user exits app, these files will be deleted.
	 * temporary folder is hidden
	 */
	private String temppath = "/palle_university/.temp/";
	
	/*
	 * user settings folder - contains purchase order no in - po.txt file in this folder path in sd card.
	 */
	private String usersettingpath = "/palle_university/user_settings/";
	/*
	 * usersettings is the the shared preference file name
	 * where we will store if user is first time user or not
	 */
	private static String usersettings = "UserSettings";
	private boolean isFirstTimeUser = true;
	private String root_sd;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.purchased_course_details);
		
		//new
	    tv = (TextView) findViewById(R.id.textView1);
	    
	    viewPager = (ViewPager) findViewById(R.id.mypager);
	    viewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager()));
	    
	    
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
	    		WindowManager.LayoutParams.FLAG_FULLSCREEN);

	    Intent in = getIntent();
	    Bundle bnd = in.getExtras();
	    if(bnd != null){
	    	course = bnd.getString("course");
			sharedCourse = course;
			root_sd = bnd.getString("sdcard_path");
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

		//new end
		//lv = (ListView) findViewById(R.id.listView1);
		//tv = (TextView) findViewById(R.id.textView1);
		
		cdb = new CredDataBase(this);
		cdb.open();
		
		connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		//below gets device id
		/*
		 * returns IMEI - FOR - GSM 
		 * OR
		 * returns MEID OR ESN - FOR - CDMA
		 */
		imei_meid_esn = telephonyManager.getDeviceId();
		//below gets sim serial number
		simSerialNumber = telephonyManager.getSimSerialNumber();  
	    //below gets mobile number phone number
		phoneNumber = telephonyManager.getLine1Number();


		//below is unique id when user first time boots up
		ANDROID_ID = Secure.getString(getContentResolver(), Secure.ANDROID_ID);//Settings.Secure.ANDROID_ID;
		//below is hardware serial no if available
		BUILD_SERIAL_NO = android.os.Build.SERIAL;
		
		/*tv.setText(tv.getText().toString()+"\n"+
					"ANDROID_ID:"+ANDROID_ID+"\n"+
				"HW_SERIAL_NO:"+BUILD_SERIAL_NO+"\n"+
					"IMEI_MEID_ESN:"+imei_meid_esn+"\n"+
				"SIM_SERL_NO:"+simSerialNumber+"\n"+
					"PH_NO:"+phoneNumber);
		*/
		
		//setContentView(lv);
		//myVideosList = new ArrayList<String>();
		
		/*
		 * First check if sd card is available or not for
		 * read and write, else return from here.
		 */
		if(isSdCardAvailable() == false){
			Toast.makeText(this, "CHECK SD CARD ONCE - NOT PROPER", Toast.LENGTH_LONG).show();
			//SHOW OFFLINE FREE VIDEOS / YOUTUBE FREE VIDEOS IF NET AVAILABLE.
			return;
		}
		
		
		/*
		 * you are here - that means sd card is available for
		 * read and write
		 */
		Toast.makeText(this, "SD CARD - PERFECT	", 1).show();
		//root_sd = Environment.getExternalStorageDirectory().toString();
		/*file = new File(root_sd+datatypespath);
		File[] list = file.listFiles();
		for(int i=0; i<list.length; i++){
			//myVideosList.add(list[i].getName());
		}*/

        //get all question papers from papers.xml of sd card
        testpapers = new ArrayList<Tests>();

        File topics_file0 = new File(root_sd+"/palle_university/"+course+"/papers.xml");
        if(topics_file0.exists()){
            File inputWorkbook = new File(root_sd+"/palle_university/"+course+"/papers.xml");
            if(inputWorkbook.exists()){
                try{
                    InputStream is = new FileInputStream(inputWorkbook);
                    XmlPullParser xp = Xml.newPullParser();
                    xp.setInput(is, null);
                    int evt = xp.next();

                    Tests t = null;
                    ArrayList<Questions> quests = null;
                    Questions quest = null;

                    while(evt != XmlPullParser.END_DOCUMENT){
                        if(evt == XmlPullParser.START_TAG){
                            String tagname = xp.getName();
                            //read question
                            if(tagname.equals("paper")){
                                t = new Tests();
                                quests = new ArrayList<Questions>();

                                String pid = xp.getAttributeValue(0);
                                String technology = xp.getAttributeValue(1);
                                String testname = xp.getAttributeValue(2);
                                t.setPid(pid);
                                t.setTechnology(technology);
                                t.setTestname(testname);
                                t.setQuestions(quests);

                                testpapers.add(t);

                            }else if(tagname.equals("question")){
                                String qid = xp.getAttributeValue(0);
                                String question = xp.getAttributeValue(1);

                                quest = new Questions();
                                quests.add(quest);

                                quest.setQid(qid);
                                quest.setQuestion(question);
                            }else if(tagname.equals("option")){
                                String id = xp.getAttributeValue(0);
                                String val = xp.getAttributeValue(1);
                                switch(id){
                                    case "a":
                                        quest.setOp1(val);
                                        break;
                                    case "b":
                                        quest.setOp2(val);
                                        break;
                                    case "c":
                                        quest.setOp3(val);
                                        break;
                                    case "d":
                                        quest.setOp4(val);
                                        break;
                                }
                            }else if(tagname.equals("Answer")){
                                String ans = xp.getAttributeValue(1);
                                quest.setAns(ans);
                            }
                        }
                        evt = xp.next();
                    }
                }catch(XmlPullParserException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            else
            {
                //papers XML FILE CORRUPT
            }

        }else{
            //papers.xml file is missing from sd card
        }

        //end of question papers

        //get all topic names from topics.xml of sd card
        topicAndVideos = new LinkedHashMap<String, ArrayList<VideoNames>>();

        File topics_file = new File(root_sd+"/palle_university/"+course+"/topics.xml");
        if(topics_file.exists()){
            File inputWorkbook = new File(root_sd+"/palle_university/"+course+"/topics.xml");
            if(inputWorkbook.exists()){
                try{
                    InputStream is = new FileInputStream(inputWorkbook);
                    XmlPullParser xp = Xml.newPullParser();
                    xp.setInput(is, null);
                    int evt = xp.next();
                    int cnt = -1;

                    String quest = null;
                    ArrayList<VideoNames> videos = null;

                    while(evt != XmlPullParser.END_DOCUMENT){
                        if(evt == XmlPullParser.START_TAG){
                            String tagname = xp.getName();
                            //read question
                            if(tagname.equals("topics")){
                                if(quest != null){
                                    topicAndVideos.put(quest, videos);
                                }
                                videos = new ArrayList<VideoNames>();
                                quest = xp.getAttributeValue(0);

                            }else if(tagname.equals("videos")){
                                String vid_disp_name = xp.getAttributeValue(0);
                                String paper_id = xp.getAttributeValue(1);
                                xp.next();
                                String enc_file_name = xp.getText();
                                VideoNames v = new VideoNames();
                                videos.add(v);

                                v.setVideo_disp_name(vid_disp_name);
                                v.setPaper_id(paper_id);
                                v.setEnc_file_name(enc_file_name);
                            }
                        }

                        evt = xp.next();
                    }
                    //check
                    if(quest != null){
                        topicAndVideos.put(quest, videos);
                    }
                }catch(XmlPullParserException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
            else
            {
                //TOPICS XML FILE CORRUPT
            }

        }else{
            //EDtopics.xml file is missing from sd card
        }

		/*aa = new ArrayAdapter<>(this, 
				android.R.layout.simple_list_item_1, 
				myVideosList);
		lv.setAdapter(aa);
		*/
		
		/*lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, 
					View view,
					int position, long id) {
				String path = root_sd+datatypespath+myVideosList.get(position);
				Intent in = new Intent(SDCardActivity.this,
						VideoPlayActivity.class);
				in.putExtra("path", path);
				in.putExtra("temppath",root_sd+temppath);
				startActivity(in);
			}
		});*/
		
		//aa.notifyDataSetChanged();

		/*
		 * create temp folder automatically if not exsiting.
		 */
		File tempFolder = new File(root_sd+temppath);
		//if(tempFolder.exists() == false){
		tempFolder.mkdir();
		//}

		
		//TESTING CREATE A HIDDEN FOLDER IN SD CARD. DUMMY CODE TO TEST HOW TO CREATE HIDDEN FOLDER AND FILES.
		//REMOVE IT LATER
		/*
		File hiddenDirectory = new File(root_sd+"/palle_university/lol/");
		hiddenDirectory.mkdir();
		File hiddenFile = new File(root_sd+"/palle_university/lol/", ".pallesecured.txt");
		File normalFile = new File(root_sd+"/palle_university/lol/", "pallenormal.txt");
		FileWriter fw1, fw2;
		try {
			fw1 = new FileWriter(hiddenFile);
			fw1.write("palle tech secured");
			fw1.close();
			fw2 = new FileWriter(normalFile);
			fw2.write("palle tech normal");
			fw2.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/		
		//TESTING END
		
		/*
		 * Fake user check - start
		 */
		sp = getSharedPreferences(usersettings, MODE_PRIVATE);
		et = sp.edit();
		
		isFirstTimeUser = sp.getBoolean("isfirsttime", true);
		po_order_no = sp.getString("po_order_no", null); //get purchase order no also
		
		if(isFirstTimeUser){
			//he is first time user			
			et.putBoolean("isfirsttime", false);
			et.commit();
			//connect to server - with firsttimeregistration function
			first_time_registration();
		}else{
			//he is second time user
			//validate user credentials for fake detection
			boolean genuine = false;
	
			Cursor c = cdb.getCredentials();
			if(c != null && c.getCount() > 0){
				c.moveToFirst();
				String aid = c.getString(1);
				String bsn = c.getString(2);
				String ime = c.getString(3);
				String ssn = c.getString(4);
				String pn = c.getString(5);
				
				
				if(aid != null && !(aid.equals("")) && aid.equals(ANDROID_ID)){
					genuine = true;
				}
				if(bsn != null && !(bsn.equals("")) && bsn.equals(BUILD_SERIAL_NO)){
					genuine = true;
				}
				if(ime != null && !(ime.equals("")) && ime.equals(imei_meid_esn)){
					genuine = true;
				}
				if(ssn != null && !(ssn.equals("")) && ssn.equals(simSerialNumber)){
					genuine = true;
				}
				if(pn != null && !(pn.equals("")) && pn.equals(phoneNumber)){
					genuine = true;
				}			
			}
	
			if(genuine){
				//GENUINE USER
				Toast.makeText(SDCardActivity.this, "second time USER TEST PASS", 1).show();
			}else{
				//FAKE USER
				if(c != null && c.getCount() <= 0){
					/*
					There is a possiblity that first time registration failed
					becuase of network problem, and database values are not inserted
					properly. In that case call first_time_registration method one more
					time
					 */
					first_time_registration();
				}else {
					Toast.makeText(SDCardActivity.this, "second time USER TEST FAIL", 1).show();
					finish(); //comment for time being
				}
			}
	
		}
		//fake user check end
	}
	
	/*
	 * this function connects to server, checks if this sdcard po-order no is already registered or not
	 * if already registered server returns -ve ack 
	 * with previously stored android_id, build_serial_no, imei_meid_esn, simserialnumber, phonenumber
	 * else returns +ve ack only.
	 */
	public void first_time_registration(){
		//check internet status first, if there is no internet display a dialog to user
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if(networkInfo != null && networkInfo.isConnected()){
			myTask = new MyTask();
			myTask.execute();
		}else{
			Toast.makeText(this, "FIRST TIME REGISTRATION - REQUIRES INTERNET CONNECTION, "
					+ "but there is no network, please try again", 1).show();
			finish();
		}

	}

	private String encodeData(String input) throws UnsupportedEncodingException {
		// encrypt data on your side using BASE64

		//encoding  byte array into base 64

		//android.util.Base64.e

		byte[] encoded = org.apache.commons.codec.binary.Base64.encodeBase64(input.getBytes());


		//decoding byte array into base64
		//byte[] decoded = Base64.decodeBase64(encoded);
		return new String(encoded);
		//return null;
	}

	private class MyTask extends AsyncTask<Void, Void, String>{
		@Override
		protected void onPreExecute() {
			//SHOW A DIALOG PROGRESS BOX - VALIDATING USER AUTHENTICATION
			mpd = new MyProgressDialog();
			mpd.show(getSupportFragmentManager(), "progress");
			
			Toast.makeText(SDCardActivity.this, "VALIDATION IN PROGRESS WITH SERVER...PLZ WAIT", 1).show();
			super.onPreExecute();
		}
		
		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			get_sdcard_po_order_no(); //get purchase order no to send to server
			
			HttpURLConnection con = null;
			try {
				URL url  = new URL("http://techpalle.com/PalleUniversity_App.svc/Mobile/ServerDetails/app");
				con = (HttpURLConnection) url.openConnection();
				//con.connect();
				////con.setReadTimeout(10000); //READ TIME OUT IN MILLI SECONDS
				////con.setConnectTimeout(15000); //CONNECTION TIME OUT IN MILLI SECONDS
				//con.setRequestMethod("POST"); //we are posting data to server
				//con.setDoInput(true); //permit us to receive data
				//con.setDoOutput(true); //permit us to send data
				//android.util.Base64.encodeToString();

/*              String luser = Base64.encodeToString("Palle_Tech_Palle_University".getBytes(), 0);//encodeData("Palle_Tech_Palle_University");//Base64.encodeToString("Palle_Tech_Palle_University".getBytes(), 0);
				String lpass = Base64.encodeToString("University_Palle_Tech_Palle".getBytes(), 0);//encodeData("University_Palle_Tech_Palle");//Base64.encodeToString("University_Palle_Tech_Palle".getBytes(), 0);

				String lpo_no = Base64.encodeToString(po_order_no.getBytes(), 0);//encodeData(po_order_no);//Base64.encodeToString(po_order_no.getBytes(), 0);
				String limei =  Base64.encodeToString(imei_meid_esn.getBytes(), 0);//encodeData(imei_meid_esn);//Base64.encodeToString(imei_meid_esn.getBytes(), 0);
				String lsim =  Base64.encodeToString(simSerialNumber.getBytes(), 0);//encodeData(simSerialNumber);//Base64.encodeToString(simSerialNumber.getBytes(), 0);
				String lphone = Base64.encodeToString(phoneNumber.getBytes(), 0);//encodeData(phoneNumber);//Base64.encodeToString(phoneNumber.getBytes(), 0);
				String land =  Base64.encodeToString(ANDROID_ID.getBytes(), 0);//encodeData(ANDROID_ID);//Base64.encodeToString(ANDROID_ID.getBytes(), 0);
				String lbuild =  Base64.encodeToString(BUILD_SERIAL_NO.getBytes(), 0);//encodeData(BUILD_SERIAL_NO);//Base64.encodeToString(BUILD_SERIAL_NO.getBytes(), 0);

				con.setRequestProperty("uname", luser);
				con.setRequestProperty("pwd", lpass);
				con.setRequestProperty("payment_order_no", lpo_no);//po_order_no);
				con.setRequestProperty("imei_meid_esn", limei);//imei_meid_esn);
				con.setRequestProperty("sim_serial_num", lsim);//simSerialNumber);
				con.setRequestProperty("phone_num", lphone);//phoneNumber);
				con.setRequestProperty("android_id", land);//ANDROID_ID);
				con.setRequestProperty("hw_build_Serial_no", lbuild);//BUILD_SERIAL_NO);
*/

/*
				String luser = Base64.encodeToString("uname:Palle_Tech_Palle_University".getBytes("UTF-8"),Base64.NO_WRAP);
                con.addRequestProperty("Authorization", "Basic "+luser);
				String lpass = Base64.encodeToString("pwd:University_Palle_Tech_Palle".getBytes("UTF-8"), Base64.NO_WRAP);
				con.addRequestProperty("Authorization", "Basic " + lpass);

                String lpo_no = Base64.encodeToString(("payment_order_no:"+po_order_no).getBytes("UTF-8"),Base64.NO_WRAP);
                con.addRequestProperty("Authorization", "Basic " + lpo_no);
                String limei =  Base64.encodeToString(("imei_meid_esn:"+imei_meid_esn).getBytes("UTF-8"),Base64.NO_WRAP);
                con.addRequestProperty("Authorization", "Basic " + limei);
                String lsim =  Base64.encodeToString(("sim_serial_num:"+simSerialNumber).getBytes("UTF-8"),Base64.NO_WRAP);
                con.addRequestProperty("Authorization", "Basic " + lsim);
                String lphone = Base64.encodeToString(("phone_num:"+phoneNumber).getBytes("UTF-8"),Base64.NO_WRAP);
                con.addRequestProperty("Authorization", "Basic " + lphone);
                String land =  Base64.encodeToString(("android_id:"+ANDROID_ID).getBytes("UTF-8"),Base64.NO_WRAP);
                con.addRequestProperty("Authorization", "Basic " + land);
                String lbuild =  Base64.encodeToString(("hw_build_Serial_no:"+BUILD_SERIAL_NO).getBytes("UTF-8"),Base64.NO_WRAP);
                con.addRequestProperty("Authorization", "Basic " + lbuild);
*/

/*
                String luser = Base64.encodeToString("Palle_Tech_Palle_University".getBytes("UTF-8"), Base64.NO_WRAP);//encodeData("Palle_Tech_Palle_University");//Base64.encodeToString("Palle_Tech_Palle_University".getBytes(), 0);
				String lpass = Base64.encodeToString("University_Palle_Tech_Palle".getBytes("UTF-8"), Base64.NO_WRAP);//encodeData("University_Palle_Tech_Palle");//Base64.encodeToString("University_Palle_Tech_Palle".getBytes(), 0);

				String lpo_no = Base64.encodeToString(po_order_no.getBytes("UTF-8"), Base64.NO_WRAP);//encodeData(po_order_no);//Base64.encodeToString(po_order_no.getBytes(), 0);
				String limei =  Base64.encodeToString(imei_meid_esn.getBytes("UTF-8"), Base64.NO_WRAP);//encodeData(imei_meid_esn);//Base64.encodeToString(imei_meid_esn.getBytes(), 0);
				String lsim =  Base64.encodeToString(simSerialNumber.getBytes("UTF-8"), Base64.NO_WRAP);//encodeData(simSerialNumber);//Base64.encodeToString(simSerialNumber.getBytes(), 0);
				String lphone = Base64.encodeToString(phoneNumber.getBytes("UTF-8"), Base64.NO_WRAP);//encodeData(phoneNumber);//Base64.encodeToString(phoneNumber.getBytes(), 0);
				String land =  Base64.encodeToString(ANDROID_ID.getBytes("UTF-8"), Base64.NO_WRAP);//encodeData(ANDROID_ID);//Base64.encodeToString(ANDROID_ID.getBytes(), 0);
				String lbuild =  Base64.encodeToString(BUILD_SERIAL_NO.getBytes("UTF-8"), Base64.NO_WRAP);//encodeData(BUILD_SERIAL_NO);//Base64.encodeToString(BUILD_SERIAL_NO.getBytes(), 0);

				con.setRequestProperty("uname", luser);
				con.setRequestProperty("pwd", lpass);
				con.setRequestProperty("payment_order_no", lpo_no);//po_order_no);
				con.setRequestProperty("imei_meid_esn", limei);//imei_meid_esn);
				con.setRequestProperty("sim_serial_num", lsim);//simSerialNumber);
				con.setRequestProperty("phone_num", lphone);//phoneNumber);
				con.setRequestProperty("android_id", land);//ANDROID_ID);
				con.setRequestProperty("hw_build_Serial_no", lbuild);//BUILD_SERIAL_NO);
*/

/*
                String luser = encodeData("Palle_Tech_Palle_University");//Base64.encodeToString("Palle_Tech_Palle_University".getBytes(), 0);
                String lpass = encodeData("University_Palle_Tech_Palle");//Base64.encodeToString("University_Palle_Tech_Palle".getBytes(), 0);

                String lpo_no = encodeData(po_order_no);//Base64.encodeToString(po_order_no.getBytes(), 0);
                String limei =  encodeData(imei_meid_esn);//Base64.encodeToString(imei_meid_esn.getBytes(), 0);
                String lsim =  encodeData(simSerialNumber);//Base64.encodeToString(simSerialNumber.getBytes(), 0);
                String lphone = encodeData(phoneNumber);//Base64.encodeToString(phoneNumber.getBytes(), 0);
                String land =  encodeData(ANDROID_ID);//Base64.encodeToString(ANDROID_ID.getBytes(), 0);
                String lbuild =  encodeData(BUILD_SERIAL_NO);//Base64.encodeToString(BUILD_SERIAL_NO.getBytes(), 0);

                con.setRequestProperty("payment_order_no", lpo_no);//po_order_no);
                con.setRequestProperty("imei_meid_esn", limei);//imei_meid_esn);
                con.setRequestProperty("sim_serial_num", lsim);//simSerialNumber);
                con.setRequestProperty("phone_num", lphone);//phoneNumber);
                con.setRequestProperty("android_id", land);//ANDROID_ID);
                con.setRequestProperty("hw_build_Serial_no", lbuild);//BUILD_SERIAL_NO);
                con.setRequestProperty("uname", luser);
                con.setRequestProperty("pwd", lpass);
*/
				String luser = encodeData("Palle_Tech_Palle_University");//Base64.encodeToString("Palle_Tech_Palle_University".getBytes(), 0);
				String lpass = encodeData("University_Palle_Tech_Palle");//Base64.encodeToString("University_Palle_Tech_Palle".getBytes(), 0);

				String lpo_no = encodeData(po_order_no);//Base64.encodeToString(po_order_no.getBytes(), 0);
				String limei =  encodeData(imei_meid_esn);//Base64.encodeToString(imei_meid_esn.getBytes(), 0);
				String lsim =  encodeData(simSerialNumber);//Base64.encodeToString(simSerialNumber.getBytes(), 0);
				String lphone = encodeData(phoneNumber);//Base64.encodeToString(phoneNumber.getBytes(), 0);
				String land =  encodeData(ANDROID_ID);//Base64.encodeToString(ANDROID_ID.getBytes(), 0);
				String lbuild =  encodeData(BUILD_SERIAL_NO);//Base64.encodeToString(BUILD_SERIAL_NO.getBytes(), 0);

				con.setRequestProperty("payment_order_no", lpo_no);//po_order_no);
				con.setRequestProperty("imei_meid_esn", limei);//imei_meid_esn);
				con.setRequestProperty("sim_serial_num", lsim);//simSerialNumber);
				con.setRequestProperty("phone_num", lphone);//phoneNumber);
				con.setRequestProperty("android_id", land);//ANDROID_ID);
				con.setRequestProperty("hw_build_Serial_num", lbuild);//BUILD_SERIAL_NO);
				con.setRequestProperty("uname", luser);
				con.setRequestProperty("pwd", lpass);

				con.connect();

                InputStream i = con.getInputStream();
				InputStreamReader ir = new InputStreamReader(i);
				BufferedReader br = new BufferedReader(ir);
				
				StringBuilder s = new StringBuilder();
				String str = br.readLine();
				while(str != null){
					s.append(str);
					str = br.readLine();
				}
				return s.toString();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            return null;

		}
		
		@Override
		protected void onPostExecute(String result) {
			Toast.makeText(SDCardActivity.this, "VALIDATION OVER", 1).show();
			mpd.dismiss();

			try {
				//RESULT CAME FROM SERVER
                JSONObject j = new JSONObject(result);
				JSONObject jo = j.getJSONObject("GetMobileDetailsResult");
				String ack = jo.getString("ack");
				String android_id = jo.getString("android_id");
                String email_id = jo.getString("email_id");
				String build_serial_no = jo.getString("hw_build_Serial_num");
				String imei_meid_esn = jo.getString("imei_meid_esn");
                String phonenumber = jo.getString("phone_num");
                String sd_card_expiry_date = jo.getString("sd_card_expiry_date");
				String simserialnumber = jo.getString("sim_serial_num");

				
				boolean genuineuser = false;
				
				if(ack.equals("+ve")){
					//GENUINE USER'S FIRST TIME REGISTRATION.
					//PROCEED FURTHER WITH OUT ANY CHECKS.
					//INSERT DATA INTO CREDENTIALS TABLE.
					Toast.makeText(SDCardActivity.this, "USER TEST PASS", 1).show();
					cdb.insert(android_id, build_serial_no, imei_meid_esn, simserialnumber, phonenumber);
				}else if(ack.equals("-ve")){
					//USER ALREADY REGISTERED WITH THIS PURCHASE ORDER NO.
					//CHECK IF THIS IS THE PHONE WHICH HAS REGISTERED.
					if(android_id != null && !(android_id.equals("")) && android_id.equals(SDCardActivity.this.ANDROID_ID)){
						genuineuser = true;
					}
					if(build_serial_no != null && !(build_serial_no.equals("")) && build_serial_no.equals(SDCardActivity.this.BUILD_SERIAL_NO)){
						genuineuser = true;
					}
					if(imei_meid_esn != null && !(imei_meid_esn.equals("")) && imei_meid_esn.equals(SDCardActivity.this.imei_meid_esn)){
						genuineuser = true;
					}
					if(simserialnumber != null && !(simserialnumber.equals("")) && simserialnumber.equals(SDCardActivity.this.simSerialNumber)){
						genuineuser = true;
					}
					if(phonenumber != null && !(phonenumber.equals("")) && phonenumber.equals(SDCardActivity.this.phoneNumber)){
						genuineuser = true;
					}			

					//DELETE DATA FROM USER CREDENTIALS TABLE. MAY NOT BE REQUIRED, NEEDS TO TEST
					
					//AND INSERT SERVER GIVEN DETAILS TO DATABASE TABLE.
					cdb.insert(android_id, build_serial_no, imei_meid_esn, simserialnumber, phonenumber);
					
					if(genuineuser == false){
						Toast.makeText(SDCardActivity.this, "USER TEST FAIL..NOT MATCHING WITH SERVER DETAILS", 1).show();
						finish();						
					}
				}
				
			} catch (JSONException e) {
				Toast.makeText(SDCardActivity.this, "USER TEST fail..try again later", 1).show();
				finish();
				e.printStackTrace();
			}
			
			super.onPostExecute(result);
		}
		
	}
	
	/*
	 * Below function reads sdcard unique purchase order no, which comes in build with each sd card
	 * purchase order no should not conflict with other sd card's po number.
	 * if this file is corrupted, it should be recreated 
	 */
	public String get_sdcard_po_order_no(){
		
		if(po_order_no != null){
			//already purchase order no is in preference file, no need to read from sd card
			return po_order_no;
		}else{
			//read purchase order no from sd card file, and store it in preference file also.
			File po_file = new File(root_sd+usersettingpath+"po.txt");
			StringBuilder sb = new StringBuilder();
			
			try {
				FileReader fr = new FileReader(po_file);
				BufferedReader br = new BufferedReader(fr);
				String s = br.readLine();
				do{
					sb.append(s);
					s = br.readLine();
				}while(s != null);
				
				br.close();
				fr.close();
				
			} catch (FileNotFoundException e) {
				//purchase order file missing or might have corrupted.
				/* CODE YET TO WRITE
				 * either user can check his mail, and manually insert purchase order no
				 * to recreate this file in sd card.
				 * or if he can't find in his mail inbox, he can request server to send one more mail
				 * with his purchase order no.
				 */
				//close previous progress dialog
				mpd.dismiss();
				//close async task for server validation
				myTask.cancel(true);
				
				mysdcardDialog = new MySdCardUserSettingFileGeneratorDialog();
				mysdcardDialog.show(getSupportFragmentManager(), "sddialog");
				
				e.printStackTrace();
			} catch (IOException e) {
				//purchase order file missing or might have corrupted.
				/* CODE YET TO WRITE
				 * either user can check his mail, and manually insert purchase order no
				 * to recreate this file in sd card.
				 * or if he can't find in his mail inbox, he can request server to send one more mail
				 * with his purchase order no.
				 */
				
				//close previous progress dialog
				mpd.dismiss();
				//close async task for server validation
				myTask.cancel(true);
				
				mysdcardDialog = new MySdCardUserSettingFileGeneratorDialog();
				mysdcardDialog.show(getSupportFragmentManager(), "sddialog");
				
				e.printStackTrace();
			}
			
			po_order_no = sb.toString();
			
			et.putString("po_order_no", po_order_no);
			et.commit();
		}
		return po_order_no;
	}
	
	
	public boolean isSdCardAvailable(){
		String state = Environment.getExternalStorageState();
		if(Environment.MEDIA_MOUNTED.equals(state)){
			//external storage is available for read and write
			return true;
		}else{
			/*
			 * some thing wrong with sd card.
			 * probably mounted to computer, or not available.
			 */
			if(Environment.MEDIA_MOUNTED.equals(state)||
					Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)){
				Toast.makeText(this, "SD CARD - READ ONLY", 1).show();
				Toast.makeText(this, "other errors "+state, 1).show();
			}
			
			return false;
		}
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
		switch(id){
			case android.R.id.home:
				finish();
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	protected void onDestroy() {
		cdb.close();
		super.onDestroy();
	}
	
	/*
	 * while we check first time user registration, show a progress dialog to user.
	 * In background we connect to server and validate user.
	 */
	private class MyProgressDialog extends DialogFragment{
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			Dialog d = null;
			ProgressDialog pd = new ProgressDialog(getActivity());
			pd.setTitle("Valdiation in progress");
			pd.setMessage("Please wait while we validate with server..");
			d = pd;
			return d;
		}
	}
	
	private class MySdCardUserSettingFileGeneratorDialog extends DialogFragment{
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			Dialog d = null;
			AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
			View v = getActivity().getLayoutInflater().inflate(R.layout.po_order_email, null);
			Button b = (Button) v.findViewById(R.id.button1);
			final EditText et = (EditText) v.findViewById(R.id.editText1);
			
			b.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String email = et.getText().toString();
					
					sdcardtask = new MySdCardFileGeneratorTask();
					sdcardtask.execute(email);
					
					mysdcardDialog.dismiss();
				}
			});
			
			ab.setView(v);
			d = ab.create();
			return d;
		}
	}
	
	/*
	 * User will enter his/her email address.
	 * We will send email address to server, and server will return purchase order number in json format.
	 */
	
	private class MySdCardFileGeneratorTask extends AsyncTask<String, Void, String>{
		
		@Override
		protected void onPreExecute() {
			Toast.makeText(SDCardActivity.this, "Fetching SD CARD files from server..please wait", 1).show();
			super.onPreExecute();
		}
		@Override
		protected String doInBackground(String... params) {
			String email = params[0];

			HttpURLConnection con = null;
			try {
				URL url  = new URL("http://api.androidhive.info/contacts/");
				con = (HttpURLConnection) url.openConnection();
				
				con.setReadTimeout(10000); //READ TIME OUT IN MILLI SECONDS
				con.setConnectTimeout(15000); //CONNECTION TIME OUT IN MILLI SECONDS
				//con.setRequestMethod("POST"); //we are posting data to server
				//con.setDoInput(true); //permit us to receive data
				//con.setDoOutput(true); //permit us to send data
				con.addRequestProperty("email", email);
				
				InputStream i = con.getInputStream();
				InputStreamReader ir = new InputStreamReader(i);
				BufferedReader br = new BufferedReader(ir);
				
				StringBuilder s = new StringBuilder();
				String str = br.readLine();
				while(str != null){
					s.append(str);
					str = br.readLine();
				}
				return s.toString();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			Toast.makeText(SDCardActivity.this, "SD CARD FILE GENERATING", 1).show();
			try {
				JSONObject jo = new JSONObject(result);
				String po_order_no = jo.getString("po_order_no");
				
				//UPDATE PO ORDER NO IN PREFERENCE FILE
				et.putString("po_order_no", po_order_no);
				et.commit();
				
				//GENERATE SD CARD PO.TXT FILE AND WRITE PO_ORDER_NO TO IT.
				File myfile = new File(root_sd+usersettingpath+"po.txt");
				FileWriter fw = new FileWriter(myfile);
				fw.write(po_order_no);
				fw.close();
				
				//CALL FIRST TIME REGISTRATION ONE MORE TIME.
				first_time_registration();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Toast.makeText(SDCardActivity.this, "UNABLE TO FETCH..TRY LATER", 1).show();
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Toast.makeText(SDCardActivity.this, "UNABLE TO FETCH..TRY LATER", 1).show();
				e.printStackTrace();
			}
			super.onPostExecute(result);
		}
	}
	
	
	private class SampleFragmentPagerAdapter extends FragmentStatePagerAdapter{
		final int PAGE_COUNT = 3;//4;
		private String[] titles = {"Course Details","Lectures","Take Test"};//,"Downloads"};
		
		public SampleFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int pos) {
			String videos_path = "/palle_university/"+course+"/encrypted/";
			switch(pos){
			case 0: //demo videos
				return PurchasedCourseContentFragment.newInstance(pos+1,null);
			case 1:
				return PurchasedVideosFragment.newInstance(pos+1, topicAndVideos, root_sd+videos_path, root_sd+temppath);
			case 2:
				return PurchasedTestsFragment.newInstance(pos+1, testpapers, root_sd+"/palle_university/"+course+"/",
                                                                                    null);
			default:
				return PurchasedVideosFragment.newInstance(pos+1, topicAndVideos, root_sd+videos_path, root_sd+temppath);
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


}
