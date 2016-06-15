package com.techpalle.poc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class PurchasedCourseContentFragment extends Fragment{
	
	private static final String ARG = "page_number";
	private static String course;
	
	public PurchasedCourseContentFragment(){
		
	}
	
	public static PurchasedCourseContentFragment newInstance(int page, String course)
	{
		PurchasedCourseContentFragment.course = course;
		PurchasedCourseContentFragment fragment = new PurchasedCourseContentFragment();
		Bundle args = new Bundle();
		args.putInt(ARG, page);
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.purchased_course_content_pager, null);
		TextView tv2 = (TextView) v.findViewById(R.id.textView2);
		int pos = getArguments().getInt(ARG);
		StringBuilder sb = new StringBuilder();
		InputStream is = null;

		if (pos != -1){
			switch (pos) {
				case 1: //set complete dot net course content to tv2. Pages are starting from index 1
					try {
						is = getActivity().getAssets().open("dotnet_complete.txt");
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
			}
		}

		if(pos == -1){
			switch(course){
				case "Full .NET":
					try {
						is = getActivity().getAssets().open("full_dotnet_content.txt");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case "Csharp for Freshers":
					try {
						is = getActivity().getAssets().open("csharp_fresher_content.txt");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case "Csharp for Professionals":
					try {
						is = getActivity().getAssets().open("csharp_exp_content.txt");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
                case "SQL Server (t-sql)":
                    try {
                        is = getActivity().getAssets().open("sql_server_content.txt");
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
			}
		}


		try {
			//is = getActivity().getAssets().open("dotnet_complete.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String str = null;
			do {
				str = br.readLine();
				sb.append(str);
			} while (str != null);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tv2.setText(Html.fromHtml(sb.toString()));



		return v;
	}
}
