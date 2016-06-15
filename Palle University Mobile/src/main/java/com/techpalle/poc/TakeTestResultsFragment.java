package com.techpalle.poc;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by skillgun on 12/10/2015.
 */

public class TakeTestResultsFragment extends Fragment {

	ArrayList<QuestionsAndAnswers> list;
	private TextView tv2, tv3, tv4, tv5, tv7;
	private int right = 0;
	private int attempted = 0;
	private ArrayList<Integer> wronglist;
	private ListView lv;
	private MyWrongAdapter mwa;
	private boolean isSQL;

	public TakeTestResultsFragment(){

	}
	public TakeTestResultsFragment(ArrayList<QuestionsAndAnswers> list){
		this.list = list;
		attempted = list.size();
		wronglist = new ArrayList<Integer>();

		for(int i=0; i<list.size(); i++){
			QuestionsAndAnswers qa = list.get(i);

			if(qa.getUserChoice() == 0){
				attempted--;
				wronglist.add(i);
				continue;
			}

			switch(qa.getUserChoice()){
			case 1:
				if(qa.getAns().equals(qa.getOp1()))
					right++;
				else
					wronglist.add(i);
				break;
			case 2:
				if(qa.getAns().equals(qa.getOp2()))
					right++;
				else
					wronglist.add(i);
				break;
			case 3:
				if(qa.getAns().equals(qa.getOp3()))
					right++;
				else
					wronglist.add(i);
				break;
			case 4:
				if(qa.getAns().equals(qa.getOp4()))
					right++;
				else
					wronglist.add(i);
				break;
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.purchased_take_test_results, null);
        isSQL = SDCardActivity.sharedCourse.equals("SQL Server (t-sql)");

		tv2 = (TextView) v.findViewById(R.id.textView2);
		tv3 = (TextView) v.findViewById(R.id.textView3);
		tv4 = (TextView) v.findViewById(R.id.textView4);
		tv5 = (TextView) v.findViewById(R.id.textView5);
		tv7 = (TextView) v.findViewById(R.id.textView7);
		lv = (ListView) v.findViewById(R.id.listView1);
		mwa = new MyWrongAdapter();
		lv.setAdapter(mwa);

		tv2.setText(tv2.getText().toString()+""+right);
		tv3.setText(tv3.getText().toString()+""+list.size());
		tv4.setText(tv4.getText().toString()+""+attempted);

		tv5.setText(""+right);
		tv7.setText(""+list.size());
		return v;
	}

	private class MyWrongAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return wronglist.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = getActivity().getLayoutInflater().inflate(R.layout.purchased_take_test_results_row, null);
			TextView tv1 = (TextView) v.findViewById(R.id.textView1);
			TextView tv2 = (TextView) v.findViewById(R.id.textView2);
			TextView tv3 = (TextView) v.findViewById(R.id.textView3);
			TextView tv4 = (TextView) v.findViewById(R.id.textView4);
			TextView tv5 = (TextView) v.findViewById(R.id.textView5);

			WebView wv1 = (WebView) v.findViewById(R.id.webView1);
			WebView wv2 = (WebView) v.findViewById(R.id.webView2);
			WebView wv3 = (WebView) v.findViewById(R.id.webView3);
			WebView wv4 = (WebView) v.findViewById(R.id.webView4);
			WebView wv5 = (WebView) v.findViewById(R.id.webView5);

			ImageView iv1 = (ImageView) v.findViewById(R.id.imageView1);
			ImageView iv2 = (ImageView) v.findViewById(R.id.imageView2);
			ImageView iv3 = (ImageView) v.findViewById(R.id.imageView3);
			ImageView iv4 = (ImageView) v.findViewById(R.id.imageView4);

			int pos = wronglist.get(position);
			QuestionsAndAnswers q = list.get(pos);

			tv1.setText(""+(position+1)+" "+q.getQuestion());
			tv2.setText("a."+q.getOp1());
			tv3.setText("b."+q.getOp2());
			tv4.setText("c."+q.getOp3());
			tv5.setText("d."+q.getOp4());

            if(isSQL) {
                String question = q.getQuestion();
                if (question != null && question.contains("</table>")) {
                    tv1.setVisibility(View.GONE);
                    wv1.setVisibility(View.VISIBLE);
                    wv1.loadDataWithBaseURL(null, ""+(position+1)+".\n"+question, "text/html", "utf-8", null);
                }

                String op1 = q.getOp1();
                if (op1 != null && op1.contains("</table>")) {
                    tv2.setVisibility(View.GONE);
                    wv2.setVisibility(View.VISIBLE);
                    wv2.loadDataWithBaseURL(null, "a."+op1, "text/html", "utf-8", null);
                }
                String op2 = q.getOp2();
                if (op2 != null && op2.contains("</table>")) {
                    tv3.setVisibility(View.GONE);
                    wv3.setVisibility(View.VISIBLE);
                    wv3.loadDataWithBaseURL(null, "b."+op2, "text/html", "utf-8", null);
                }
                String op3 = q.getOp3();
                if (op3 != null && op3.contains("</table>")) {
                    tv4.setVisibility(View.GONE);
                    wv4.setVisibility(View.VISIBLE);
                    wv4.loadDataWithBaseURL(null, "c."+op3, "text/html", "utf-8", null);
                }
                String op4 = q.getOp4();
                if (op4 != null && op4.contains("</table>")) {
                    tv5.setVisibility(View.GONE);
                    wv5.setVisibility(View.VISIBLE);
                    wv5.loadDataWithBaseURL(null, "d."+op4, "text/html", "utf-8", null);
                }
            }

			switch(q.getUserChoice()){
			case 1:
				iv1.setVisibility(View.VISIBLE);
				iv1.setImageDrawable(getResources().getDrawable(R.drawable.wrong));
				break;
			case 2:
				iv2.setVisibility(View.VISIBLE);
				iv2.setImageDrawable(getResources().getDrawable(R.drawable.wrong));
				break;
			case 3:
				iv3.setVisibility(View.VISIBLE);
				iv3.setImageDrawable(getResources().getDrawable(R.drawable.wrong));
				break;
			case 4:
				iv4.setVisibility(View.VISIBLE);
				iv4.setImageDrawable(getResources().getDrawable(R.drawable.wrong));
				break;
			}

			int correct = -1;

			if(q.getAns().equals(q.getOp1())){
				correct = 1;
			}else if(q.getAns().equals(q.getOp2())){
				correct = 2;
			}else if(q.getAns().equals(q.getOp3())){
				correct = 3;
			}else if(q.getAns().equals(q.getOp4())){
				correct = 4;
			}

			if(correct != -1){
				switch(correct){
				case 1:
					iv1.setVisibility(View.VISIBLE);
					iv1.setImageDrawable(getResources().getDrawable(R.drawable.right));
					break;
				case 2:
					iv2.setVisibility(View.VISIBLE);
					iv2.setImageDrawable(getResources().getDrawable(R.drawable.right));
					break;
				case 3:
					iv3.setVisibility(View.VISIBLE);
					iv3.setImageDrawable(getResources().getDrawable(R.drawable.right));
					break;
				case 4:
					iv4.setVisibility(View.VISIBLE);
					iv4.setImageDrawable(getResources().getDrawable(R.drawable.right));
					break;
				}
			}

			return v;
		}

	}

}
