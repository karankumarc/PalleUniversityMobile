package com.techpalle.poc;

import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class PurchasedTestsFragment extends Fragment{
	
	private static final String ARG = "page_number";
	private ListView lv;
	public static ArrayList<Tests> testPapers;
	MyAdapter ma;
	private ConnectivityManager connMgr;
	private static ArrayList<String> myVideosList;
	private static String path;
	private static String tempPath;
	
	private class MyAdapter extends BaseAdapter{
		@Override
		public int getCount() {
			return testPapers.size();
		}
		@Override
		public Object getItem(int position) {
			return testPapers.get(position);
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, 
				View convertView, ViewGroup parent) {
            Tests tests = testPapers.get(position);

			View v = getActivity().getLayoutInflater().inflate(R.layout.purchased_tests_pager_row, null);
			CardView cv = (CardView) v.findViewById(R.id.cv0);
			TextView tv1 = (TextView) v.findViewById(R.id.textView1);
			TextView tv2 = (TextView) v.findViewById(R.id.textView2);
			TextView tv3 = (TextView) v.findViewById(R.id.textView3);
            ImageView iv = (ImageView) v.findViewById(R.id.imageView1);
			
			cv.setTag(position);//for testing

			//RowClass r = al.get(position);
			
			tv1.setText(""+(position+1));
			tv2.setText(tests.getTestname());
			tv3.setText("Questions : "+tests.getQuestions().size());
			
			cv.setOnClickListener(new OnClickListener() {	
				@Override
				public void onClick(View v) {
					
					Integer pos = (Integer) v.getTag();
					Toast.makeText(getActivity(), 
							"CARD VIEW CLICK.."+pos, 
							Toast.LENGTH_LONG).show();
					
					Intent in = new Intent(getActivity(),
							PurchasedTakeTestActivity.class);
					in.putExtra("pos", pos);
					startActivity(in);
				}
			});

			return v;
		}
	}
	
	public PurchasedTestsFragment(){
		ma = new MyAdapter();
	}
	
	public static PurchasedTestsFragment newInstance(int page, ArrayList<Tests> testpapers,
                                                     String path, String tempPath)
	{
		PurchasedTestsFragment.testPapers = testpapers;
		PurchasedTestsFragment.path = path;
		PurchasedTestsFragment.tempPath = tempPath;
		
		PurchasedTestsFragment fragment = new PurchasedTestsFragment();
		Bundle args = new Bundle();
		args.putInt(ARG, page);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.purchased_tests_pager, null);
		lv = (ListView) v.findViewById(R.id.listView1);
		lv.setAdapter(ma);
		return v;
	}
}
