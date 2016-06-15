package com.techpalle.poc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by skillgun on 12/10/2015.
 */

public class TakeTestInProgressFragment extends Fragment {
	private ArrayList<QuestionsAndAnswers> questionsList;
    private ArrayList<Tests> testPapers;
    private boolean isSQL;

	private int count = 0;
	private int pos = -1;
	private String pid;
	private TextView tv1, tv2, tv3, tv4, tv5, tv6;
    private WebView  wv3, wv4, wv5, wv6, wv7;
    private LinearLayout questionlayout;

	private Button b1, b2, b3;
	private RadioButton rb1, rb2, rb3, rb4;

	public TakeTestInProgressFragment(){

	}
	/*private class MyTask extends AsyncTask<Void, Void, Void>{
		@Override
		protected Void doInBackground(Void... params) {}
		@Override
		protected void onPostExecute(Void result) {
			fillData(0);
			super.onPostExecute(result);
		}
	}*/

	private OnSubmitResultSelected mCallBack;

	public interface OnSubmitResultSelected{
		public void onSubmitResultClicked(ArrayList<QuestionsAndAnswers> list);
	}

	@Override
	public void onAttach(Context context) {
		try{
			mCallBack = (OnSubmitResultSelected) context;
		}catch(ClassCastException e){
			Toast.makeText(getActivity(), "Parent must implement frag listener", Toast.LENGTH_LONG).show();
		}
		super.onAttach(context);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
        Bundle bnd = getArguments();
        if(bnd != null){
            pos = bnd.getInt("pos");
			pid = bnd.getString("pid");
            testPapers = PurchasedTestsFragment.testPapers;
        }

		if(pid != null){
			//MEANS USER IS TAKING TEST AFTER COMPLETING VIDEO
			for(int i=0; i<testPapers.size(); i++){
				if(testPapers.get(i).getPid().equals(pid)){
					pos = i;
					break;
				}
			}
		}
		//ELSE USER IS TAKING TEST DIRECTLY

        questionsList = new ArrayList<QuestionsAndAnswers>();
        if(pos != -1){
            Tests tests = testPapers.get(pos);
            ArrayList<Questions> questionsAndAnswers = tests.getQuestions();
            for(int i=0; i<questionsAndAnswers.size(); i++){
                Questions q = questionsAndAnswers.get(i);
                QuestionsAndAnswers qa = new QuestionsAndAnswers();
                qa.setQuestion(q.getQuestion());
                qa.setOp1(q.getOp1());
                qa.setOp2(q.getOp2());
                qa.setOp3(q.getOp3());
                qa.setOp4(q.getOp4());
                qa.setAns(q.getAns());

                questionsList.add(qa);
            }
        }
		super.onCreate(savedInstanceState);
	}


	public void fillData(int pos){
		if(pos<0 || pos>questionsList.size()-1){
			return;
		}
        //Toast.makeText(getActivity(), SDCardActivity.sharedCourse, Toast.LENGTH_LONG).show();
        tv2.setVisibility(View.VISIBLE);
        //wv2.setVisibility(View.GONE);
        WebView wv22 = (WebView) questionlayout.findViewWithTag("dynamic");
        if(wv22 != null){
            //PREVIOUS WEB VIEW IS THERE, REMOVE IT
            questionlayout.removeView(wv22);
        }

        tv3.setVisibility(View.VISIBLE);
        wv3.setVisibility(View.GONE);
        tv4.setVisibility(View.VISIBLE);
        wv4.setVisibility(View.GONE);
        tv5.setVisibility(View.VISIBLE);
        wv5.setVisibility(View.GONE);
        tv6.setVisibility(View.VISIBLE);
        wv6.setVisibility(View.GONE);

		rb1.setChecked(false);
		rb2.setChecked(false);
		rb3.setChecked(false);
		rb4.setChecked(false);

		int userchoice = questionsList.get(pos).getUserChoice();
		if(userchoice != 0){
			switch(userchoice){
			case 1:rb1.setChecked(true);
				break;
			case 2:rb2.setChecked(true);
				break;
			case 3:rb3.setChecked(true);
				break;
			case 4:rb4.setChecked(true);
				break;
			}
		}

		b1.setBackground(getResources().getDrawable(R.drawable.after_video_shape));
		b2.setBackground(getResources().getDrawable(R.drawable.after_video_shape));

		if(pos == questionsList.size()-2){
			b3.setVisibility(View.GONE);
		}
		if(pos == questionsList.size()-1){
			b3.setVisibility(View.VISIBLE);
			b2.setBackground(getResources().getDrawable(R.drawable.after_video_shape_light));
		}
		if(pos == 0){
			b1.setBackground(getResources().getDrawable(R.drawable.after_video_shape_light));
		}

		QuestionsAndAnswers qa = questionsList.get(pos);
		tv1.setText(""+(pos+1));
		//format the question
		if(qa.getQuestion().contains("{")){
			int tab = 0;
			String q = qa.getQuestion();
			StringBuilder sb = new StringBuilder();
			int position = q.indexOf("{");

			//put line after {
			while(position != -1){
				sb.append(q.substring(0, position+1));
				sb.append("\n");
				q = q.substring(position+1);
				position = q.indexOf("{");
			}
			sb.append(q);

			//put line before }
			q = sb.toString();
			sb.delete(0, sb.length()-1);
			position = q.indexOf("}");
			while(position != -1){
				sb.append(q.substring(0, position));
				sb.append("\n");
				sb.append("}");
				q = q.substring(position+1);
				position = q.indexOf("}");
			}
			sb.append(q);

			//put line after ;
			q = sb.toString();
			sb.delete(0, sb.length()-1);
			position = q.indexOf(";");
			while(position != -1){
				sb.append(q.substring(0, position+1));
				sb.append("\n");
				q = q.substring(position+1);
				position = q.indexOf(";");
			}
			sb.append(q);


			tv2.setText(sb.toString());

            if(isSQL && sb.toString().contains("</table>")){
                //IT IS SQL, FORMAT THE QUESTION
                tv2.setVisibility(View.GONE);
                WebView wv2 = new WebView(getActivity());
                wv2.setTag("dynamic");
                wv2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                wv2.loadDataWithBaseURL(null, sb.toString(), "text/html", "utf-8", null);
                questionlayout.addView(wv2,1);
                //wv2.setVisibility(View.VISIBLE);

            }

		}else{
			String question = qa.getQuestion();
			tv2.setText(question);

			if(isSQL && question.contains("</table>")){
				//IT IS SQL, FORMAT THE QUESTION
                question = question.trim();
                tv2.setVisibility(View.GONE);
                WebView wv2 = new WebView(getActivity());
                wv2.setTag("dynamic");
                wv2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                wv2.loadDataWithBaseURL(null, question, "text/html", "utf-8", null);
                questionlayout.addView(wv2,1);
			}
		}

        String op1 = qa.getOp1();
		tv3.setText(op1);
        String op2 = qa.getOp2();
		tv4.setText(op2);
        String op3 = qa.getOp3();
		tv5.setText(op3);
        String op4 = qa.getOp4();
		tv6.setText(op4);

        if(isSQL){
            if(op1!=null && op1.contains("</table>")){
                tv3.setVisibility(View.GONE);
                wv3.setVisibility(View.VISIBLE);
                wv3.loadDataWithBaseURL(null, op1, "text/html", "utf-8", null);
            }
            if(op2!=null && op2.contains("</table>")){
                tv4.setVisibility(View.GONE);
                wv4.setVisibility(View.VISIBLE);
                wv4.loadDataWithBaseURL(null, op2, "text/html", "utf-8", null);
            }
            if(op3!=null && op3.contains("</table>")){
                tv5.setVisibility(View.GONE);
                wv5.setVisibility(View.VISIBLE);
                wv5.loadDataWithBaseURL(null, op3, "text/html", "utf-8", null);
            }
            if(op4!=null && op4.contains("</table>")){
                tv6.setVisibility(View.GONE);
                wv6.setVisibility(View.VISIBLE);
                wv6.loadDataWithBaseURL(null, op4, "text/html", "utf-8", null);
            }
        }

	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.purchased_take_test_in_progress, null);
        isSQL = SDCardActivity.sharedCourse.equals("SQL Server (t-sql)");

		tv1 = (TextView) v.findViewById(R.id.textView1);
		tv2 = (TextView) v.findViewById(R.id.textView2);
		tv3 = (TextView) v.findViewById(R.id.textView3);
		tv4 = (TextView) v.findViewById(R.id.textView4);
		tv5 = (TextView) v.findViewById(R.id.textView5);
		tv6 = (TextView) v.findViewById(R.id.textView6);
        //wv2 = (WebView) v.findViewById(R.id.webView2);
        wv3 = (WebView) v.findViewById(R.id.webView3);
        wv4 = (WebView) v.findViewById(R.id.webView4);
        wv5 = (WebView) v.findViewById(R.id.webView5);
        wv6 = (WebView) v.findViewById(R.id.webView6);
        questionlayout = (LinearLayout) v.findViewById(R.id.questionLayout);

		b1 = (Button) v.findViewById(R.id.button1);
		b2 = (Button) v.findViewById(R.id.button2);
		b3 = (Button) v.findViewById(R.id.button3);
		rb1 = (RadioButton) v.findViewById(R.id.radioButton1);
		rb2 = (RadioButton) v.findViewById(R.id.radioButton2);
		rb3 = (RadioButton) v.findViewById(R.id.radioButton3);
		rb4 = (RadioButton) v.findViewById(R.id.radioButton4);

		/*if(questionsList.size() != 0){
			QuestionsAndAnswers qa = questionsList.get(count);
			tv2.setText(qa.getQuestion());
			tv3.setText(qa.getOp1());
			tv4.setText(qa.getOp2());
			tv5.setText(qa.getOp3());
			tv6.setText(qa.getOp4());
		}*/


		if(questionsList.size() > 1){
			//apply different color for next button
			b2.setBackground(getResources().getDrawable(R.drawable.after_video_shape));
		}

		fillData(count);

		b1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Toast.makeText(getActivity(), "b1 clicked", 1).show();
				if(count == 0)
					return;
				count--;
				fillData(count);
			}
		});
		b2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Toast.makeText(getActivity(), "b2 clicked", 1).show();
				if(count == questionsList.size()-1)
					return;
				count++;
				fillData(count);
			}
		});
		b3.setOnClickListener(new View.OnClickListener() {
			@SuppressLint("ShowToast")
			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), "b3 clicked", Toast.LENGTH_LONG).show();
				mCallBack.onSubmitResultClicked(questionsList);
			}
		});

		rb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(!isChecked)
					return;

				QuestionsAndAnswers currentQuestion = questionsList.get(count);
				currentQuestion.setUserChoice(1);

				rb1.setChecked(true);
				rb2.setChecked(false);
				rb3.setChecked(false);
				rb4.setChecked(false);
			}
		});
		rb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(!isChecked)
					return;

				QuestionsAndAnswers currentQuestion = questionsList.get(count);
				currentQuestion.setUserChoice(2);

				rb1.setChecked(false);
				rb2.setChecked(true);
				rb3.setChecked(false);
				rb4.setChecked(false);
			}
		});
		rb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(!isChecked)
					return;

				QuestionsAndAnswers currentQuestion = questionsList.get(count);
				currentQuestion.setUserChoice(3);

				rb1.setChecked(false);
				rb2.setChecked(false);
				rb3.setChecked(true);
				rb4.setChecked(false);
			}
		});
		rb4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(!isChecked)
					return;

				QuestionsAndAnswers currentQuestion = questionsList.get(count);
				currentQuestion.setUserChoice(4);

				rb1.setChecked(false);
				rb2.setChecked(false);
				rb3.setChecked(false);
				rb4.setChecked(true);
			}
		});
		return v;
	}
}
