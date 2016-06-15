package com.techpalle.poc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
//import android.graphics.Camera;
import android.database.Cursor;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListenerHome} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment{//} implements TextureView.SurfaceTextureListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    //private CardView cv0, cv1, cv2, cv3, cv4, cv5, cv6;
//    private VideoView vw;
    //private ScrollView scv;
    private ListView lv;
    private String title;
    private boolean homeFragPaused = false;
    private boolean isFirstTimeUser = false;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextureView tv;
    private Camera mCamera;
    private MediaPlayer mMediaPlayer;
    private FragmentActivity fa;
    private OnFragmentInteractionListenerHome mListener;
    private ViewPager vp;
    private PalleDatabase pdb;
    private Cursor c;
    private ArrayList<CourseDetailsFromServer> courses;
    private ArrayList<CourseDetailsFromServer> mycourses;
    private MyAdapter m;
    private SharedPreferences sp;
    private SharedPreferences.Editor et;
    private boolean isMyCourses;
    private class MyAdapter extends BaseAdapter{
        ArrayList<CourseDetailsFromServer> courses;
        public MyAdapter(ArrayList<CourseDetailsFromServer> c){
            courses = c;
        }

        @Override
        public int getCount() {
            return courses.size();
        }
        @Override
        public Object getItem(int position) {
            return courses.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = getActivity().getLayoutInflater().inflate(R.layout.courses_row, null);
            TextView tv1 = (TextView) v.findViewById(R.id.textView1);
            TextView tv2 = (TextView) v.findViewById(R.id.textView2);
            TextView tv3 = (TextView) v.findViewById(R.id.textView3);
            TextView tv4 = (TextView) v.findViewById(R.id.textView4);
            ImageView iv = (ImageView) v.findViewById(R.id.sdCardImage);

            if(isMyCourses){
                iv.setVisibility(View.VISIBLE);
            }

            CourseDetailsFromServer c = courses.get(position);
            tv1.setText(c.getCourse_display_name());
            tv2.setText("Duration : "+c.getCourse_hours()+" hrs");
            tv3.setText("Assignments : "+c.getAssignment_hours()+" hrs");
            tv4.setText("Tests : "+c.getTotal_tests()+" hrs");

            return v;
        }
    }

    private class MyPagerAdapter extends FragmentStatePagerAdapter{
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new SalientFeaturesFragment();
                /*
                case 1:
                    return new SalientFeaturesFragment();
                */
            }
            return null;
        }
        @Override
        public int getCount() {
            return 1;
        }
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        homeFragPaused = true;
    }

    @Override
    public void onResume() {
        super.onResume();
//        if(mParam2!=null)
//            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(mParam2);
        if(homeFragPaused){
            homeFragPaused = false;
            ((HomeActivity)getActivity()).homeFragResumed();
        }
    }

    private void fillCoursesArray(){
        c = pdb.getCourseDetailsFromServer();
        courses.clear();//remove existing course details, and refill
        mycourses.clear();

        if(c != null){
            while(c.moveToNext()){
                String assignment_hours = c.getString(c.getColumnIndex("assignment_hours"));
                String avg_rating = c.getString(c.getColumnIndex("avg_rating"));
                String course_id = c.getString(c.getColumnIndex("course_id"));
                String course_fee_inr_after_discount = c.getString(c.getColumnIndex("course_fee_inr_after_discount"));
                String course_fee_usd_after_discount = c.getString(c.getColumnIndex("course_fee_usd_after_discount"));
                String course_display_name = c.getString(c.getColumnIndex("course_display_name"));
                String course_fee_inr = c.getString(c.getColumnIndex("course_fee_inr"));
                String course_fee_usd = c.getString(c.getColumnIndex("course_fee_usd"));
                String course_hours = c.getString(c.getColumnIndex("course_hours"));
                String total_tests = c.getString(c.getColumnIndex("total_tests"));
                String total_views = c.getString(c.getColumnIndex("total_views"));

                CourseDetailsFromServer cds = new CourseDetailsFromServer();
                cds.setAssignment_hours(assignment_hours);
                cds.setAvg_rating(avg_rating);
                cds.setCourse_id(course_id);
                cds.setCourse_fee_inr_after_discount(course_fee_inr_after_discount);
                cds.setCourse_fee_usd_after_discount(course_fee_usd_after_discount);
                cds.setCourse_display_name(course_display_name);
                cds.setCourse_fee_inr(course_fee_inr);
                cds.setCourse_fee_usd(course_fee_usd);
                cds.setCourse_hours(course_hours);
                cds.setTotal_tests(total_tests);
                cds.setTotal_views(total_views);

                courses.add(cds);
                mycourses.add(cds);
            }
        }
    }
    private void refreshCardViewTitles(){
        if(courses.size() > 0){
            //courses details available in database

        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        sp = getActivity().getSharedPreferences("user",0);
        int count = sp.getInt("count", 0);
        et = sp.edit();
        et.putInt("count",count+1);
        et.commit();

        fa = getActivity();
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        FrameLayout fl = (FrameLayout) v.findViewById(R.id.videoFrame);

        courses = new ArrayList<CourseDetailsFromServer>();
        mycourses = new ArrayList<CourseDetailsFromServer>();

        if(count != 0){
            fl.setVisibility(View.GONE);
        }
/*
        cv0 = (CardView) v.findViewById(R.id.cv0);//complete dotnet
        cv1 = (CardView) v.findViewById(R.id.cv1);
        cv2 = (CardView) v.findViewById(R.id.cv2);
        cv3 = (CardView) v.findViewById(R.id.cv3);
        cv4 = (CardView) v.findViewById(R.id.cv4);
        cv5 = (CardView) v.findViewById(R.id.cv5);
        cv6 = (CardView) v.findViewById(R.id.cv6);
*/

        if(mParam2!=null && mParam2.equals("My Courses")){
            m = new MyAdapter(mycourses);
            isMyCourses = true;
        }else{
            m = new MyAdapter(courses);
            isMyCourses = false;
        }

        lv = (ListView) v.findViewById(R.id.listView1);

        //vw = (VideoView) v.findViewById(R.id.videoView1);
        //tv = (TextureView) v.findViewById(R.id.textureView1);
        vp = (ViewPager) v.findViewById(R.id.viewPager1);
        vp.setAdapter(new MyPagerAdapter(getActivity().getSupportFragmentManager()));

        pdb = new PalleDatabase(getActivity());
        pdb.open();

        fillCoursesArray();
        refreshCardViewTitles();

        lv.setAdapter(m);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(isMyCourses){
                    String course_name = mycourses.get(position).getCourse_display_name();
                    if (isSdCardAvailable()) {
                        File f = new File(Environment.getExternalStorageDirectory() + "/palle_university/" + course_name + "/");
                        Toast.makeText(getActivity(), "1st.." + Environment.getExternalStorageDirectory(), Toast.LENGTH_LONG).show();
                        //getext
                        if (f.exists() && f.isDirectory()) {
                            //open purchsed sd card course content.
                            Toast.makeText(getActivity(), "found in internal emulated sd card", Toast.LENGTH_LONG).show();
                            Intent in = new Intent(getActivity(), SDCardActivity.class);
                            in.putExtra("course", course_name);
                            in.putExtra("sdcard_path", Environment.getExternalStorageDirectory().toString());
                            startActivity(in);
                            return;
                        }

                        //GET ALL SD CARD PATHS - secondary sd card - check for palle university in secondary sd cards
                        //HashSet<String> sdcards = getExternalMounts();
                        String[] sdcards = getStorageDirectories();
                        for (String path : sdcards) {
                            Toast.makeText(getActivity(), "2nd.." + path, Toast.LENGTH_LONG).show();
                            File f1 = new File(path + "/palle_university/" + course_name + "/");
                            if (f1.exists() && f1.isDirectory()) {
                                //if(Environment.getExternalStorageState(f1))
                                //String str = Environment.getExternalStorageState(f1);//Environment.getExternalStorageState(f1);
                                Toast.makeText(getActivity(), "found in secondary sd card", Toast.LENGTH_LONG).show();
                                //Toast.makeText(HomeActivity.this, "card state.."+str, Toast.LENGTH_LONG).show();
                                //open purchsed sd card course content.
                                Intent in = new Intent(getActivity(), SDCardActivity.class);
                                in.putExtra("course", course_name);
                                in.putExtra("sdcard_path", path);
                                startActivity(in);
                                return;
                            }
                        }
                    }
                }
                else {
                    String course_name = courses.get(position).getCourse_display_name();
                    String course_id = courses.get(position).getCourse_id();

                    String course_fee_inr = courses.get(position).getCourse_fee_inr();
                    String course_fee_inr_after_discount = courses.get(position).getCourse_fee_inr_after_discount();
                    String course_fee_usd = courses.get(position).getCourse_fee_usd();
                    String course_fee_usd_after_discount = courses.get(position).getCourse_fee_usd_after_discount();

                    if (isSdCardAvailable()) {
                        File f = new File(Environment.getExternalStorageDirectory() + "/palle_university/" + course_name + "/");
                        Toast.makeText(getActivity(), "1st.." + Environment.getExternalStorageDirectory(), Toast.LENGTH_LONG).show();
                        //getext
                        if (f.exists() && f.isDirectory()) {
                            //open purchsed sd card course content.
                            Toast.makeText(getActivity(), "found in internal emulated sd card", Toast.LENGTH_LONG).show();
                            Intent in = new Intent(getActivity(), SDCardActivity.class);
                            in.putExtra("course", course_name);
                            in.putExtra("sdcard_path", Environment.getExternalStorageDirectory().toString());
                            startActivity(in);
                            return;
                        }

                        //GET ALL SD CARD PATHS - secondary sd card - check for palle university in secondary sd cards
                        //HashSet<String> sdcards = getExternalMounts();
                        String[] sdcards = getStorageDirectories();
                        for (String path : sdcards) {
                            Toast.makeText(getActivity(), "2nd.." + path, Toast.LENGTH_LONG).show();
                            File f1 = new File(path + "/palle_university/" + course_name + "/");
                            if (f1.exists() && f1.isDirectory()) {
                                //if(Environment.getExternalStorageState(f1))
                                //String str = Environment.getExternalStorageState(f1);//Environment.getExternalStorageState(f1);
                                Toast.makeText(getActivity(), "found in secondary sd card", Toast.LENGTH_LONG).show();
                                //Toast.makeText(HomeActivity.this, "card state.."+str, Toast.LENGTH_LONG).show();
                                //open purchsed sd card course content.
                                Intent in = new Intent(getActivity(), SDCardActivity.class);
                                in.putExtra("course", course_name);
                                in.putExtra("sdcard_path", path);
                                startActivity(in);
                                return;
                            }
                        }
                    }
                    //we are here - means no sd card courses found
                    Intent in = new Intent(getActivity(), CourseDetailsActivity.class);
                    in.putExtra("course", course_name);
                    in.putExtra("course_id", course_id);
                    in.putExtra("course_fee_inr", course_fee_inr);
                    in.putExtra("course_fee_inr_after_discount", course_fee_inr_after_discount);
                    in.putExtra("course_fee_usd", course_fee_usd);
                    in.putExtra("course_fee_usd_after_discount", course_fee_usd_after_discount);

                    startActivity(in);
                }
            }
        });


        if(mParam2 != null && mParam2.equals("My Courses")){

        }else {
            new GetCoursesDetailsTask().execute();//read from server, latest details about course
        }

        //new CourseDetailsTask().execute();
/*
        float scale = getActivity().getResources().getDisplayMetrics().density;
        tv.setCameraDistance(-20 * scale);
*/

        /*Matrix txform = new Matrix();
        tv.getTransform(txform);
        float scale = getActivity().getResources().getDisplayMetrics().density;
        Toast.makeText(getActivity(), ""+scale, Toast.LENGTH_SHORT).show();

        txform.setScale((float) 1.3f, (float) 1.4f);
        txform.postTranslate(0, 0);
        tv.setTransform(txform);

        tv.setSurfaceTextureListener(this);
        */

        //scv = (ScrollView) v.findViewById(R.id.scrollView1);
        //vw.setZOrderOn(true);


        SharedPreferences sp = getActivity().getSharedPreferences("firsttimeuser",0);
        isFirstTimeUser = sp.getBoolean("firsttime",true);
        //if(isFirstTimeUser) {
            //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            SharedPreferences.Editor et = sp.edit();
        et.putBoolean("firsttime", false);
            et.commit();
            //vw.setVisibility(View.VISIBLE);
            MediaController mc = new MediaController(getActivity());
            //vw.setMediaController(mc);
            //mc.setAnchorView(vw);

        /*DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        android.widget.FrameLayout.LayoutParams params =  (android.widget.FrameLayout.LayoutParams) vw.getLayoutParams();
        params.width = metrics.widthPixels;
        params.height = ((metrics.heightPixels)*5)/6;
        params.leftMargin = 0;
        //vw.setLayoutParams(params);
*/
        String salientpath = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.salientfeatures;

        removeNonSdCardVideos();

        //vw.setVideoURI(Uri.parse(salientpath));
            //vw.start();
        /*vw.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // TODO Auto-generated method stub
                vw.setVisibility(View.GONE);
                scv.setVisibility(View.VISIBLE);
            }
        });*/
        //}

        //vw.setVisibility(View.GONE);
        //scv.setVisibility(View.VISIBLE);
/*
        if((mParam1 != null) && (mParam1.equals("My Courses"))){
            //User is looking for purchased and available sd card courses.
            //vw.setVisibility(View.GONE);
            cv0.setVisibility(View.GONE);
            cv1.setVisibility(View.GONE);
            cv2.setVisibility(View.GONE);
            cv3.setVisibility(View.GONE);
            cv4.setVisibility(View.GONE);
            cv5.setVisibility(View.GONE);
            cv6.setVisibility(View.GONE);

            boolean avilable = true;

            if(isSdCardAvailable()){
                File f = new File(Environment.getExternalStorageDirectory()+"/palle_university/dotnet/");
                if((f.exists() && f.isDirectory()) == true){
                    cv0.setVisibility(View.VISIBLE);
                    avilable = false;
                }
                File f1 = new File(Environment.getExternalStorageDirectory()+"/palle_university/csharpfershers/");
                if((f1.exists() && f1.isDirectory()) == true){
                    cv1.setVisibility(View.VISIBLE);
                    avilable = false;
                }
                File f2 = new File(Environment.getExternalStorageDirectory()+"/palle_university/csharpexperts/");
                if((f2.exists() && f2.isDirectory()) == true){
                    cv2.setVisibility(View.VISIBLE);
                    avilable = false;
                }
                File f3 = new File(Environment.getExternalStorageDirectory()+"/palle_university/sqlfreshers/");
                if((f3.exists() && f3.isDirectory()) == true){
                    cv3.setVisibility(View.VISIBLE);
                    avilable = false;
                }
                File f4 = new File(Environment.getExternalStorageDirectory()+"/palle_university/sqlexperts/");
                if((f4.exists() && f4.isDirectory()) == true){
                    cv4.setVisibility(View.VISIBLE);
                    avilable = false;
                }
                File f5 = new File(Environment.getExternalStorageDirectory()+"/palle_university/javafreshers/");
                if((f5.exists() && f5.isDirectory())== true){
                    cv5.setVisibility(View.VISIBLE);
                    avilable = false;
                }
                File f6 = new File(Environment.getExternalStorageDirectory()+"/palle_university/javaexperts/");
                if((f6.exists() && f6.isDirectory()) == true){
                    cv6.setVisibility(View.VISIBLE);
                    avilable = false;
                }

                String[] sdcards = getStorageDirectories();
                for(String path: sdcards){
                    //Toast.makeText(getActivity(), "2nd.."+path, Toast.LENGTH_LONG).show();

                    File f11 = new File(path+"/palle_university/dotnet/");
                    if(f11.exists() && f11.isDirectory()){
                        cv0.setVisibility(View.VISIBLE);
                        avilable = false;
                    }
                    File f21 = new File(path+"/palle_university/csharpfershers/");
                    if(f21.exists() && f21.isDirectory()){
                        cv1.setVisibility(View.VISIBLE);
                        avilable = false;
                    }
                    File f31 = new File(path+"/palle_university/csharpexperts/");
                    if(f31.exists() && f31.isDirectory()){
                        cv2.setVisibility(View.VISIBLE);
                        avilable = false;
                    }
                    File f41 = new File(path+"/palle_university/sqlfreshers/");
                    if(f41.exists() && f41.isDirectory()){
                        cv3.setVisibility(View.VISIBLE);
                        avilable = false;
                    }
                    File f51 = new File(path+"/palle_university/sqlexperts/");
                    if(f51.exists() && f51.isDirectory()){
                        cv4.setVisibility(View.VISIBLE);
                        avilable = false;
                    }
                    File f61 = new File(path+"/palle_university/sqlfreshers/");
                    if(f61.exists() && f61.isDirectory()){
                        cv5.setVisibility(View.VISIBLE);
                        avilable = false;
                    }
                    File f71 = new File(path+"/palle_university/sqlfreshers/");
                    if(f71.exists() && f71.isDirectory()){
                        cv6.setVisibility(View.VISIBLE);
                        avilable = false;
                    }
                }

                if(avilable == true){
                    Toast.makeText(getActivity(),"NO COURSES AVAILABLE", Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(getActivity(),"SD CARD NOT AVAILABLE", Toast.LENGTH_LONG).show();
            }
        }*/

        //FULL DOT NET COURSE - CLICKED

        /*cv0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if user has purchased sd card.
                if(isSdCardAvailable()){
                    File f = new File(Environment.getExternalStorageDirectory()+"/palle_university/dotnet/");
                    Toast.makeText(getActivity(), "1st.." + Environment.getExternalStorageDirectory(), Toast.LENGTH_LONG).show();
                    //getext
                    if(f.exists() && f.isDirectory()){
                        //open purchsed sd card course content.
                        Toast.makeText(getActivity(), "found in internal emulated sd card", Toast.LENGTH_LONG).show();
                        Intent in = new Intent(getActivity(), SDCardActivity.class);
                        in.putExtra("course", "Complete .Net");
                        in.putExtra("sdcard_path",Environment.getExternalStorageDirectory().toString());
                        startActivity(in);
                        return;
                    }

                    //GET ALL SD CARD PATHS - secondary sd card - check for palle university in secondary sd cards
                    //HashSet<String> sdcards = getExternalMounts();
                    String[] sdcards = getStorageDirectories();
                    for(String path: sdcards){
                        Toast.makeText(getActivity(), "2nd.."+path, Toast.LENGTH_LONG).show();
                        File f1 = new File(path+"/palle_university/dotnet/");
                        if(f1.exists() && f1.isDirectory()){
                            //if(Environment.getExternalStorageState(f1))
                            //String str = Environment.getExternalStorageState(f1);//Environment.getExternalStorageState(f1);
                            Toast.makeText(getActivity(), "found in secondary sd card", Toast.LENGTH_LONG).show();
                            //Toast.makeText(HomeActivity.this, "card state.."+str, Toast.LENGTH_LONG).show();
                            //open purchsed sd card course content.
                            Intent in = new Intent(getActivity(), SDCardActivity.class);
                            in.putExtra("course", "Complete .Net");
                            in.putExtra("sdcard_path",path);
                            startActivity(in);
                            return;
                        }
                        //Toast.makeText(HomeActivity.this, path, Toast.LENGTH_LONG).show();
                    }
                }
                //else{
                //Intent in = new Intent(HomeActivity.this, CourseDetailsActivity.class);
                //in.putExtra("course", "Complete .Net");
                //startActivity(in);
                //}
            }
        });*/


/*
        cv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //check if user has purchased sd card.
                if(isSdCardAvailable()){
                    File f = new File(Environment.getExternalStorageDirectory()+"/palle_university/csharp_freshers/");
                    if(f.exists() && f.isDirectory()){
                        //open purchsed sd card course content.
                        Intent in = new Intent(getActivity(), SDCardActivity.class);
                        in.putExtra("course", "C# For Freshers");
                        startActivity(in);
                        return;
                    }
                }
                //else{
                Intent in = new Intent(getActivity(), CourseDetailsActivity.class);
                in.putExtra("course", "C# For Freshers");
                startActivity(in);
                //}

            }
        });

        cv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //check if user has purchased sd card.
                if(isSdCardAvailable()){
                    File f = new File(Environment.getExternalStorageDirectory()+"/palle_university/csharp_experts/");
                    if(f.exists() && f.isDirectory()){
                        //open purchsed sd card course content.
                        Intent in = new Intent(getActivity(), SDCardActivity.class);
                        in.putExtra("course", "C# For Experts");
                        startActivity(in);
                        return;
                    }
                }
                //else{
                Intent in = new Intent(getActivity(), CourseDetailsActivity.class);
                in.putExtra("course", "C# For Experts");
                startActivity(in);
                //}

            }
        });

        cv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //check if user has purchased sd card.
                if(isSdCardAvailable()){
                    File f = new File(Environment.getExternalStorageDirectory()+"/palle_university/sql_server_freshers/");
                    if(f.exists() && f.isDirectory()){
                        //open purchsed sd card course content.
                        Intent in = new Intent(getActivity(), SDCardActivity.class);
                        in.putExtra("course", "SQL For Freshers");
                        startActivity(in);
                        return;
                    }
                }
                //else{
                Intent in = new Intent(getActivity(), CourseDetailsActivity.class);
                in.putExtra("course", "SQL For Freshers");
                startActivity(in);
                //}

            }
        });

        cv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //check if user has purchased sd card.
                if(isSdCardAvailable()){
                    File f = new File(Environment.getExternalStorageDirectory()+"/palle_university/sql_server_experts/");
                    if(f.exists() && f.isDirectory()){
                        //open purchsed sd card course content.
                        Intent in = new Intent(getActivity(), SDCardActivity.class);
                        in.putExtra("course", "SQL For Experts");
                        startActivity(in);
                        return;
                    }
                }
                //else{
                Intent in = new Intent(getActivity(), CourseDetailsActivity.class);
                in.putExtra("course", "SQL For Experts");
                startActivity(in);
                //}

            }
        });
*/

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListenerHome) {
            mListener = (OnFragmentInteractionListenerHome) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
/*

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {


        */
/*tv.setLayoutParams(new FrameLayout.LayoutParams(getActivity().getResources().getDisplayMetrics().widthPixels,
                getActivity().getResources().getDisplayMetrics().heightPixels/3));
        *//*

        Surface s = new Surface(surface);

        try {
            mMediaPlayer= new MediaPlayer();
            mMediaPlayer.setDataSource(getActivity(),  Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.salientfeatures));

            //vw.setVideoURI(Uri.parse(salientpath)););
            //mMediaPlayer.setDataSource("http://daily3gp.com/vids/747.3gp");

            mMediaPlayer.setSurface(s);
            mMediaPlayer.prepare();
            mMediaPlayer.setOnBufferingUpdateListener(null);
            mMediaPlayer.setOnCompletionListener(null);
            mMediaPlayer.setOnPreparedListener(null);
            mMediaPlayer.setOnVideoSizeChangedListener(null);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

*/
/*
            mMediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                @Override
                public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {

                }
            });
*//*


            //mMediaPlayer.setDisplay();
            mMediaPlayer.start();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        */
/*mCamera = Camera.open();

        try {
            mCamera.setPreviewTexture(surface);
            mCamera.startPreview();
        } catch (IOException ioe) {
            // Something bad happened
        }*//*

    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        //return false;
        */
/*mCamera.stopPreview();
        mCamera.release();
        *//*

        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

*/
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListenerHome {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private static final Pattern DIR_SEPORATOR = Pattern.compile("/");

    /**
     * Raturns all available SD-Cards in the system (include emulated)
     *
     * Warning: Hack! Based on Android source code of version 4.3 (API 18)
     * Because there is no standart way to get it.
     * TODO: Test on future Android versions 4.4+
     *
     * @return paths to all available SD-Cards in the system (include emulated)
     */
    public static String[] getStorageDirectories()
    {
        // Final set of paths
        final Set<String> rv = new HashSet<String>();
        // Primary physical SD-CARD (not emulated)
        final String rawExternalStorage = System.getenv("EXTERNAL_STORAGE");
        // All Secondary SD-CARDs (all exclude primary) separated by ":"
        final String rawSecondaryStoragesStr = System.getenv("SECONDARY_STORAGE");

        // Primary emulated SD-CARD
        final String rawEmulatedStorageTarget = System.getenv("EMULATED_STORAGE_TARGET");
        if(TextUtils.isEmpty(rawEmulatedStorageTarget))
        {
            // Device has physical external storage; use plain paths.
            if(TextUtils.isEmpty(rawExternalStorage))
            {
                // EXTERNAL_STORAGE undefined; falling back to default.
                rv.add("/storage/sdcard0");
            }
            else
            {
                rv.add(rawExternalStorage);
            }
        }
        else
        {
            // Device has emulated storage; external storage paths should have
            // userId burned into them.
            final String rawUserId;
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1)
            {
                rawUserId = "";
            }
            else
            {
                final String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                final String[] folders = DIR_SEPORATOR.split(path);
                final String lastFolder = folders[folders.length - 1];
                boolean isDigit = false;
                try
                {
                    Integer.valueOf(lastFolder);
                    isDigit = true;
                }
                catch(NumberFormatException ignored)
                {
                }
                rawUserId = isDigit ? lastFolder : "";
            }
            // /storage/emulated/0[1,2,...]
            if(TextUtils.isEmpty(rawUserId))
            {
                rv.add(rawEmulatedStorageTarget);
            }
            else
            {
                rv.add(rawEmulatedStorageTarget + File.separator + rawUserId);
            }
        }
        // Add all secondary storages
        if(!TextUtils.isEmpty(rawSecondaryStoragesStr))
        {
            // All Secondary SD-CARDs splited into array
            final String[] rawSecondaryStorages = rawSecondaryStoragesStr.split(File.pathSeparator);
            Collections.addAll(rv, rawSecondaryStorages);
        }
        return rv.toArray(new String[rv.size()]);
    }

    public static HashSet<String> getExternalMounts() {
        final HashSet<String> out = new HashSet<String>();
        String reg = "(?i).*vold.*(vfat|ntfs|exfat|fat32|ext3|ext4).*rw.*";
        String s = "";
        try {
            final Process process = new ProcessBuilder().command("mount")
                    .redirectErrorStream(true).start();
            process.waitFor();
            final InputStream is = process.getInputStream();
            final byte[] buffer = new byte[1024];
            while (is.read(buffer) != -1) {
                s = s + new String(buffer);
            }
            is.close();
        } catch (final Exception e) {
            e.printStackTrace();
        }

        // parse output
        final String[] lines = s.split("\n");
        for (String line : lines) {
            if (!line.toLowerCase(Locale.US).contains("asec")) {
                if (line.matches(reg)) {
                    String[] parts = line.split(" ");
                    for (String part : parts) {
                        if (part.startsWith("/"))
                            if (!part.toLowerCase(Locale.US).contains("vold"))
                                out.add(part);
                    }
                }
            }
        }
        return out;
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
                Toast.makeText(getActivity(), "SD CARD - READ ONLY", Toast.LENGTH_LONG).show();
                Toast.makeText(getActivity(), "other errors "+state, Toast.LENGTH_LONG).show();
            }

            return false;
        }
    }

    private String encodeData(String input) throws UnsupportedEncodingException {
        byte[] encoded = org.apache.commons.codec.binary.Base64.encodeBase64(input.getBytes());
        return new String(encoded);
    }


    private class GetCoursesDetailsTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub

            HttpURLConnection con = null;
            try {
                URL url  = new URL("http://palleuniversity.com/PalleUniversity_App.svc/Mobile/New_User_Course_Deatails_Discounts/app");
                con = (HttpURLConnection) url.openConnection();

                String luser = encodeData("Palle_Tech_Palle_University");//Base64.encodeToString("Palle_Tech_Palle_University".getBytes(), 0);
                String lpass = encodeData("University_Palle_Tech_Palle");//Base64.encodeToString("University_Palle_Tech_Palle".getBytes(), 0);

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
            if(result == null){
                //Toast.makeText(CoursePurchaseActivity.this, "SERVER NULL RESPONSE", 1).show();
                return;
            }
            try {
                //RESULT CAME FROM SERVER
                pdb.deleteCourseDetailsFromServer();//delete previous course details of our local database
                JSONObject j = new JSONObject(result);
                JSONArray ja = j.getJSONArray("New_User_Course_Deatails_DiscountsResult");
                for(int i=0; i<ja.length(); i++){
                    JSONObject jo = ja.getJSONObject(i);
                    String assignment_hours = jo.getString("Assignment_Hours");
                    String avg_rating = jo.getString("Avg_rating");
                    String course_id = jo.getString("CourseId");
                    String course_fee_inr_after_discount = jo.getString("Course_Fee_INR_After_Discount");
                    String course_fee_usd_after_discount = jo.getString("Course_Fee_Usd_After_Discount");
                    String course_display_name = jo.getString("Course_displayname");
                    String course_fee_inr = jo.getString("Course_fee_inr");
                    String course_fee_usd = jo.getString("Course_fee_usd");
                    String course_hours = jo.getString("Course_hours");
                    String total_tests = jo.getString("TotalTests");
                    String total_views = jo.getString("Total_views");

                    CourseDetailsFromServer c = new CourseDetailsFromServer();
                    c.setAssignment_hours(assignment_hours);
                    c.setAvg_rating(avg_rating);
                    c.setCourse_id(course_id);
                    c.setCourse_fee_inr_after_discount(course_fee_inr_after_discount);
                    c.setCourse_fee_usd_after_discount(course_fee_usd_after_discount);
                    c.setCourse_display_name(course_display_name);
                    c.setCourse_fee_inr(course_fee_inr);
                    c.setCourse_fee_usd(course_fee_usd);
                    c.setCourse_hours(course_hours);
                    c.setTotal_tests(total_tests);
                    c.setTotal_views(total_views);

                    pdb.insertCourseDetailsFromServer(c);
                }
                fillCoursesArray();//refresh source arraylist
                m.notifyDataSetChanged();//refresh course details
                removeNonSdCardVideos();
            } catch (JSONException e) {
                Toast.makeText(getActivity(), "Some thing went wrong, try later", 1).show();
            }

            super.onPostExecute(result);
        }

    }

    @Override
    public void onDestroy() {
        pdb.close();
        super.onDestroy();
    }

    public void removeNonSdCardVideos(){
                //IF PARAM 2 IS MY COURSES, SEARCH FOR COURSES IN SD CARD
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mParam2 != null && mParam2.equals("My Courses")) {
                            for (int i = 0; i < mycourses.size(); i++) {
                                boolean remove = true;
                                View vv = lv.getChildAt(i);
                                //vv.setVisibility(View.GONE);
                                ImageView iv = (ImageView) vv.findViewById(R.id.sdCardImage);

                                String course_name = mycourses.get(i).getCourse_display_name();
                                String course_id = mycourses.get(i).getCourse_id();

                                String course_fee_inr = mycourses.get(i).getCourse_fee_inr();
                                String course_fee_inr_after_discount = courses.get(i).getCourse_fee_inr_after_discount();
                                String course_fee_usd = mycourses.get(i).getCourse_fee_usd();
                                String course_fee_usd_after_discount = courses.get(i).getCourse_fee_usd_after_discount();

                                if (isSdCardAvailable()) {
                                    File f = new File(Environment.getExternalStorageDirectory() + "/palle_university/" + course_name + "/");
                                    Toast.makeText(getActivity(), "1st.." + Environment.getExternalStorageDirectory(), Toast.LENGTH_LONG).show();
                                    //getext
                                    if (f.exists() && f.isDirectory()) {
                                        //open purchsed sd card course content.
                                        vv.setVisibility(View.VISIBLE);
                                        iv.setVisibility(View.VISIBLE);
                                        remove = false;
                                    }

                                    //GET ALL SD CARD PATHS - secondary sd card - check for palle university in secondary sd cards
                                    //HashSet<String> sdcards = getExternalMounts();
                                    String[] sdcards = getStorageDirectories();
                                    for (String path : sdcards) {
                                        Toast.makeText(getActivity(), "2nd.." + path, Toast.LENGTH_LONG).show();
                                        File f1 = new File(path + "/palle_university/" + course_name + "/");
                                        if (f1.exists() && f1.isDirectory()) {
                                            vv.setVisibility(View.VISIBLE);
                                            iv.setVisibility(View.VISIBLE);
                                            remove = false;
                                        }
                                    }
                                }
                                if(remove){
                                    mycourses.remove(i);
                                    i--;
                                }
                            }

                            m.notifyDataSetChanged();
                            //SD CARD COURSE SEARCH ENDS

                        }

                    }
                }, 500);

    }
}
