package com.techpalle.poc;

import java.util.ArrayList;

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class DemoVideosFragment extends Fragment{
	private String course;
	private static final String ARG = "page_number";
	private ListView lv;
	ArrayList<RowClass> al;
	MyAdapter ma;
	private ConnectivityManager connMgr;
    private String[] full_dot_net = {
            "C# program compilation/ltgDdukzQ7I/18:47",
            "C# data types/L_gFuuSp4V0/17:53",
            "C# class/l1C4FZGCab0/10:48",
            "C# class as virtual entity/HSdIq3k51bg/9:15",
            "Objects in c#/SM_QqUdMXY0/22:14",
            "Debugging in visual studio/8hXH5HxQfFU/10:41",
            "C# Arrays/CLnA6OAlNPk/24:50",
            "Declaring and Modifying data in c# arrays/O2QI3YFupxM/9:06",
            "Arrays Assignment/Zt85ireWQWw/7:49",
            "Loops/u_k75WcEpHM/5:51",
            "for-loop/isSp9xjw0LQ/23:46",
            "C# Strings/MGCZ8AHhwDA/21:02",
            "Constructors/5h8Tu68VXsI/5:08",
            "Problems When Constructor is Not Used/Yij6AUta4yI/19:41",
            "Constructors Example/Gr6qsjTIifA/21:07",
            "Inheritence/TOBLe0qoA_o/9:24",
            "Inheritence Part 2/T7G8NFXDXFE/24:07",
            "base keyword/WaAbIMz2dqg/22:08",
            "Overriding Intro/w6ldKhR4YUs/23:31",
            "overriding an override method/fdPslUmRqm0/12:52",
            "Static Variables/zvk_hS4vEOw/9:27",
            "what is the use of properties in c#/UdiU6sp68Tc/16:16",
            "c# properties/5nHmt5Zi7l8/15:03",
            "c# constants/9_fH7R6rPaU/8:57",
            "Constants Part 2/Wn20d0KBk1o/8:22",
            "Read only variables/YfYkswfGo_k/9:27",
            "Params Keyword/wZ_bBpsd10o/16:27",
            "Exceptions/wBxVpzaTXkc/17:16",
            "Exception Handling Part 2/mTF-ct2IkGU/9:25",
            "GC Terminology/eyytXkQqOV0/17:16",
            "Dead objects/YZyKaiM5kuc/8:29",
            "GC Working mechanisam/v-VTR9Xm6Vk/9:51",
            "C# Generics/-xfzPrvKFL0/10:09",
            "C# Generics and Reusability/7g9aPw1kI1o/15:32",
            "C# Delegates/k0evYKS62Tg/7:27",
            "C# Delegates example/Vjc9UHV6tTM/22:38"
    };
	private String[] csharp_freshers_demo_videos = {
            "C# program compilation/ltgDdukzQ7I/18:47",
            "C# data types/L_gFuuSp4V0/17:53",
            "C# class/l1C4FZGCab0/10:48",
            "C# class as virtual entity/HSdIq3k51bg/9:15",
            "Objects in c#/SM_QqUdMXY0/22:14",
            "Debugging in visual studio/8hXH5HxQfFU/10:41",
            "C# Arrays/CLnA6OAlNPk/24:50",
            "Declaring and Modifying data in c# arrays/O2QI3YFupxM/9:06",
            "Arrays Assignment/Zt85ireWQWw/7:49",
            "Loops/u_k75WcEpHM/5:51",
            "for-loop/isSp9xjw0LQ/23:46",
            "C# Strings/MGCZ8AHhwDA/21:02",
            "Constructors/5h8Tu68VXsI/5:08",
            "Problems When Constructor is Not Used/Yij6AUta4yI/19:41",
            "Constructors Example/Gr6qsjTIifA/21:07",
            "Inheritence/TOBLe0qoA_o/9:24",
            "Inheritence Part 2/T7G8NFXDXFE/24:07",
            "base keyword/WaAbIMz2dqg/22:08",
            "Overriding Intro/w6ldKhR4YUs/23:31",
            "overriding an override method/fdPslUmRqm0/12:52"
    };

    private String[] csharp_exp_demo_videos = {
            "C# program compilation/ltgDdukzQ7I/18:47",
            "Debugging in visual studio/8hXH5HxQfFU/10:41",
            "Inheritence/TOBLe0qoA_o/9:24",
            "Inheritence Part 2/T7G8NFXDXFE/24:07",
            "base keyword/WaAbIMz2dqg/22:08",
            "Overriding Intro/w6ldKhR4YUs/23:31",
            "overriding an override method/fdPslUmRqm0/12:52",
            "Static Variables/zvk_hS4vEOw/9:27",
            "what is the use of properties in c#/UdiU6sp68Tc/16:16",
            "c# properties/5nHmt5Zi7l8/15:03",
            "c# constants/9_fH7R6rPaU/8:57",
            "Constants Part 2/Wn20d0KBk1o/8:22",
            "Read only variables/YfYkswfGo_k/9:27",
            "Params Keyword/wZ_bBpsd10o/16:27",
            "Exceptions/wBxVpzaTXkc/17:16",
            "Exception Handling Part 2/mTF-ct2IkGU/9:25",
            "GC Terminology/eyytXkQqOV0/17:16",
            "Dead objects/YZyKaiM5kuc/8:29",
            "GC Working mechanisam/v-VTR9Xm6Vk/9:51",
            "C# Generics/-xfzPrvKFL0/10:09",
            "C# Generics and Reusability/7g9aPw1kI1o/15:32",
            "C# Delegates/k0evYKS62Tg/7:27",
            "C# Delegates example/Vjc9UHV6tTM/22:38"
    };
    private String[] sql_server_freshers_demo_videos = {
            "SQLServer Overview/Kdc84lpF4GM/16:25",
            "Normalization/7Dc7_I48ZTg/14:30",
            "Orderby clause/quuwLXzZl3g/6:59",
            "Delete drop and truncate statements/yZNyUzSMsT8/6:58",
            "Aggregate Functions/2IYykxAXaB8/13:54",
            "Group by clause/qx0j5iWajqg/20:22",
            "Joins and Inner Join with simple explanation/i0vwTFFHTU8/33:02",
            "stored procedure/jmZsXlAYe7Y/17:11",
            "User defined functions in sql/8cJFtDESxiQ/9:01",
            "Indexes/hrVpqW_Bh2o/21:09"
    };
    private String[] sql_server_exp_demo_videos = {
            "SQLServer Overview/Kdc84lpF4GM/16:25",
            "Normalization/7Dc7_I48ZTg/14:30",
            "Orderby clause/quuwLXzZl3g/6:59",
            "Delete drop and truncate statements/yZNyUzSMsT8/6:58",
            "Aggregate Functions/2IYykxAXaB8/13:54",
            "Group by clause/qx0j5iWajqg/20:22",
            "Joins and Inner Join with simple explanation/i0vwTFFHTU8/33:02",
            "stored procedure/jmZsXlAYe7Y/17:11",
            "User defined functions in sql/8cJFtDESxiQ/9:01",
            "Indexes/hrVpqW_Bh2o/21:09"
    };

    private class MyAdapter extends BaseAdapter{
		@Override
		public int getCount() {
			return al.size();
		}
		@Override
		public Object getItem(int position) {
			return al.get(position);
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, 
				View convertView, ViewGroup parent) {
			View v = getActivity().getLayoutInflater().inflate(R.layout.demo_video_pager_row, null);
			CardView cv = (CardView) v.findViewById(R.id.cv0);
			TextView tv1 = (TextView) v.findViewById(R.id.textView1);
			TextView tv2 = (TextView) v.findViewById(R.id.textView2);
			TextView tv3 = (TextView) v.findViewById(R.id.textView3);
			Button b1 = (Button) v.findViewById(R.id.button1);
			
			cv.setTag(position);//for testing
			b1.setTag(position);//for tracing position
			
			RowClass r = al.get(position);
			
			tv1.setText(""+(position+1));
			tv2.setText(r.videoTitle);
			tv3.setText(r.videoDuration);
			
			cv.setOnClickListener(new OnClickListener() {	
				@Override
				public void onClick(View v) {
					
					if(!check_network_connection()){
						Toast.makeText(getActivity(),"IT REQUIRES INTERNET CONNECTION..THERE IS NO INTERNET..PLZ CHECK YOUR CONNECTION", 
								Toast.LENGTH_LONG).show();
						return;
					}
					
					Integer pos = (Integer) v.getTag();
					Toast.makeText(getActivity(), 
							"CARD VIEW CLICK.."+pos, 
							Toast.LENGTH_LONG).show();
					Intent in = new Intent(
							getActivity(),
							YouTubePalyActivity.class);
                    RowClass r = (RowClass) lv.getAdapter().getItem(pos);
                    in.putExtra("code",r.videoCode);

                    startActivity(in);
					
				}
			});
			
			b1.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//Start playing free video

					if(!check_network_connection()){
						Toast.makeText(getActivity(),"IT REQUIRES INTERNET CONNECTION..THERE IS NO INTERNET..PLZ CHECK YOUR CONNECTION",
								Toast.LENGTH_LONG).show();
						return;
					}

					Integer pos = (Integer) v.getTag();
					Toast.makeText(getActivity(), 
							"button CLICK.."+pos,
							Toast.LENGTH_LONG).show();
					Intent in = new Intent(
							getActivity(),
							YouTubePalyActivity.class);
                    RowClass r = (RowClass) lv.getAdapter().getItem(pos);
                    in.putExtra("code",r.videoCode);

                    startActivity(in);

				}
			});
			return v;
		}
	}
	
	public DemoVideosFragment(){
	}
	
	public static DemoVideosFragment newInstance(int page, String course){
		DemoVideosFragment fragment = new DemoVideosFragment();
		Bundle args = new Bundle();
		args.putInt(ARG, page);
		args.putString("course", course);
		fragment.setArguments(args);
		return fragment;
	}
	
	/*int item;
	public PageFragment(int item){
		this.item = item;
	}*/
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        course = getArguments().getString("course");
        int len = 0;
        switch(course){
            case "Full .NET":
                len = full_dot_net.length;
                al = new ArrayList<RowClass>();
                for(int i=0; i<len; i++){
                    String str = full_dot_net[i];
                    String title = str.substring(0,str.indexOf("/"));
                    str = str.substring(str.indexOf("/")+1);
                    String code = str.substring(0,str.indexOf("/"));
                    String dur = str.substring(str.indexOf("/")+1);

                    RowClass r = new RowClass();
                    r.videoTitle=title;
                    r.videoCode = code;
                    r.videoDuration = "duration "+dur;
                    al.add(r);
                }
                break;
            case "Csharp for Freshers":
                len = csharp_freshers_demo_videos.length;
                al = new ArrayList<RowClass>();
                for(int i=0; i<len; i++){
                    String str = csharp_freshers_demo_videos[i];
                    String title = str.substring(0,str.indexOf("/"));
                    str = str.substring(str.indexOf("/")+1);
                    String code = str.substring(0,str.indexOf("/"));
                    String dur = str.substring(str.indexOf("/")+1);

                    RowClass r = new RowClass();
                    r.videoTitle=title;
                    r.videoCode = code;
                    r.videoDuration = "duration "+dur;
                    al.add(r);
                }
                break;
            case "Csharp for Professionals":
                len = csharp_exp_demo_videos.length;
                al = new ArrayList<RowClass>();
                for(int i=0; i<len; i++){
                    String str = csharp_exp_demo_videos[i];
                    String title = str.substring(0,str.indexOf("/"));
                    str = str.substring(str.indexOf("/")+1);
					String code = str.substring(0,str.indexOf("/"));
					String dur = str.substring(str.indexOf("/")+1);

                    RowClass r = new RowClass();
                    r.videoTitle=title;
					r.videoCode = code;
                    r.videoDuration = "duration "+dur;
                    al.add(r);
                }
                break;
            case "SQL Server (t-sql)":
                len = sql_server_freshers_demo_videos.length;
                al = new ArrayList<RowClass>();
                for(int i=0; i<len; i++){
                    String str = sql_server_freshers_demo_videos[i];
                    String title = str.substring(0,str.indexOf("/"));
                    str = str.substring(str.indexOf("/")+1);
                    String code = str.substring(0,str.indexOf("/"));
                    String dur = str.substring(str.indexOf("/")+1);

                    RowClass r = new RowClass();
                    r.videoTitle=title;
                    r.videoCode = code;
                    r.videoDuration = "duration "+dur;
                    al.add(r);
                }
                break;

        }


        ma = new MyAdapter();

        super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.demo_video_pager, null);
		lv = (ListView) v.findViewById(R.id.listView1);
		lv.setAdapter(ma);
		lv.setOnItemClickListener(
				new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//Start playing free video
				if(!check_network_connection()){
					Toast.makeText(getActivity(),"IT REQUIRES INTERNET CONNECTION..THERE IS NO INTERNET..PLZ CHECK YOUR CONNECTION",
							Toast.LENGTH_LONG).show();
					return;
				}

				Toast.makeText(getActivity(), 
						"LIST VIEW CLICK",
						Toast.LENGTH_LONG).show();
				Intent in = new Intent(
						getActivity(),
						YouTubePalyActivity.class);
                RowClass r = (RowClass) lv.getAdapter().getItem(position);
                in.putExtra("code",r.videoCode);
				startActivity(in);
			}
		});
		return v;
	}
	
	public boolean check_network_connection(){
		//check internet status first, if there is no internet display a dialog to user
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if(networkInfo != null && networkInfo.isConnected()){
			return true;
		}else{
			return false;
		}

	}

}
