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
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import org.w3c.dom.Text;


public class PurchasedVideosFragment extends Fragment{
	
	private static final String ARG = "page_number";
	//private ListView lv;
    TextView tv;
	ExpandableListView el;
    MyExplandableListAdapter mea;
	ArrayList<RowClass> al;
    private int prevItem = -1;
    private View prevView;
	//MyAdapter ma;
	private ConnectivityManager connMgr;
	//private static ArrayList<String> myVideosList;
	//private static ArrayList<Topics> myTopics;
    private static Map<String, ArrayList<VideoNames>> topicAndVideos;

	private static String path;
	private static String tempPath;

    private class MyExplandableListAdapter extends BaseExpandableListAdapter{
        @Override
        public int getGroupCount() {
            return topicAndVideos.size();
        }
        @Override
        public int getChildrenCount(int groupPosition) {
            String item = (String) getGroup(groupPosition);
            return topicAndVideos.get(item).size();
        }
        @Override
        public Object getGroup(int groupPosition) {
            return (String) topicAndVideos.keySet().toArray()[groupPosition];
        }
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            String item = (String) getGroup(groupPosition);
            return topicAndVideos.get(item).get(childPosition).getVideo_disp_name();
        }
        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }
        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }
        @Override
        public boolean hasStableIds() {
            return true;
        }
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            View v = null;
            if(convertView != null){
                v = convertView;
            }else{
                v = getActivity().getLayoutInflater().inflate(R.layout.parentrow, null);
            }
            TextView t = (TextView) v.findViewById(R.id.textView1);
            ImageView iv = (ImageView) v.findViewById(R.id.imageView1);
            String parent_item = (String) getGroup(groupPosition);
            t.setText(parent_item);
            if(isExpanded){
                iv.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
            }else{
                iv.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
            }
            return v;
        }
        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View v = null;
            if(convertView != null){
                v = convertView;
            }else{
                v = getActivity().getLayoutInflater().inflate(R.layout.childrow, null);
            }
            TextView t = (TextView) v.findViewById(R.id.textView1);
            String parent_item = (String) getChild(groupPosition, childPosition);
            t.setText(parent_item);
            return v;
        }
        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
	/*private class MyAdapter extends BaseAdapter{
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
			View v = getActivity().getLayoutInflater().inflate(R.layout.purchased_video_pager_row, null);
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
					
					Integer pos = (Integer) v.getTag();
					Toast.makeText(getActivity(), 
							"CARD VIEW CLICK.."+pos, 
							Toast.LENGTH_LONG).show();
					
					Intent in = new Intent(getActivity(),
							VideoPlayActivity.class);
					RowClass r = al.get(pos);
					String filename = r.videoTitle;
					in.putExtra("path", path+filename);
					in.putExtra("temppath",tempPath);
					startActivity(in);

				}
			});
			
			b1.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//Start playing free video

					Integer pos = (Integer) v.getTag();
					Toast.makeText(getActivity(), 
							"button CLICK.."+pos, 
							Toast.LENGTH_LONG).show();
					Intent in = new Intent(
							getActivity(),
							YouTubePalyActivity.class);
					startActivity(in);

				}
			});
			return v;
		}
	}
	*/
	public PurchasedVideosFragment(){
		
		/*al = new ArrayList<RowClass>();
		for(int i=0; i<myTopics.size(); i++){
			ArrayList<Videos> v = myTopics.get(i).getVideos();
			for(int j=0; j<v.size(); j++) {
				RowClass r = new RowClass();
				r.videoTitle = v.get(j).getVideo_disp_name();
				//int dur = (int) (Math.random()*15);
				//r.videoDuration = "duration "+dur;
				al.add(r);
			}
		}*/
		
		mea = new MyExplandableListAdapter();
	}
	
	public static PurchasedVideosFragment newInstance(int page, Map<String, ArrayList<VideoNames>> t, String path, String tempPath)
	{
		PurchasedVideosFragment.topicAndVideos = t;
		PurchasedVideosFragment.path = path;
		PurchasedVideosFragment.tempPath = tempPath;
		
		PurchasedVideosFragment fragment = new PurchasedVideosFragment();
		Bundle args = new Bundle();
		args.putInt(ARG, page);
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
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.purchased_video_pager, null);
		el = (ExpandableListView) v.findViewById(R.id.expandableListView1);
        tv = (TextView) v.findViewById(R.id.textView1);

        int topics = topicAndVideos.size();
        int videos = 0;
        for(int i=0; i<topics; i++){
            String topicname = (String) topicAndVideos.keySet().toArray()[i];
            ArrayList<VideoNames> al = topicAndVideos.get(topicname);
            videos = videos + al.size();
        }

        tv.setText(topics + " topics, " + videos + " videos.");
        el.setAdapter(mea);

        el.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                /*Toast.makeText(getActivity(), "lol", 1).show();
                v.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_bright));
                if (prevView != null) {
                    prevView.setBackgroundColor(getResources().getColor(android.R.color.white));
                }*/
                prevView = v;
                return false;
            }
        });
        el.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (prevItem != -1 && prevItem != groupPosition) {
                    el.collapseGroup(prevItem);
                }
                prevItem = groupPosition;
            }
        });
        el.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent in = new Intent(getActivity(), VideoPlayActivity.class);
                String topicname = (String) topicAndVideos.keySet().toArray()[groupPosition];
                ArrayList<VideoNames> videos = topicAndVideos.get(topicname);
                String filename = videos.get(childPosition).getEnc_file_name();
				String pid = videos.get(childPosition).getPaper_id();
                in.putExtra("path", path+filename);
                in.putExtra("temppath",tempPath);
				in.putExtra("pid",pid);
                startActivity(in);

                return false;
            }
        });

		/*lv.setAdapter(ma);
		lv.setOnItemClickListener(
				new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//Start playing free video

				Toast.makeText(getActivity(), 
						"LIST VIEW CLICK", 
						Toast.LENGTH_LONG).show();
				Intent in = new Intent(
						getActivity(),
						YouTubePalyActivity.class);
				startActivity(in);
			}
		});*/
		return v;
	}
}
