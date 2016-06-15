package com.techpalle.poc;


import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Frag1 extends Fragment {
	   EditText et1,et2,et3,et4,et5;
	     TextView tv1,tv2,tv3,tv4,tv0,tv6,tv7,tv8;
	     Button b;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v=inflater.inflate(R.layout.frag1, null);
		//et1=(EditText) v.findViewById(R.id.editText1);
		//et2=(EditText) v.findViewById(R.id.editText2);
		et3=(EditText) v.findViewById(R.id.editText3);
		//et4=(EditText) v.findViewById(R.id.editText4);
		et5=(EditText) v.findViewById(R.id.editText5);
		//tv0=(TextView) v.findViewById(R.id.textView1);
		//tv1=(TextView) v.findViewById(R.id.textView0);
		//tv2=(TextView) v.findViewById(R.id.textView3);
		//tv3=(TextView) v.findViewById(R.id.textView4);
		tv4=(TextView) v.findViewById(R.id.textView5);
		tv6=(TextView) v.findViewById(R.id.textView6);
		tv8=(TextView) v.findViewById(R.id.textView8);

		b=(Button) v.findViewById(R.id.button1);
		SpannableString spanString = new SpannableString("click here");
		spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
		spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
		spanString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spanString.length(), 0);
		tv6.setText(spanString);

		SpannableString spanString2 = new SpannableString("sign up");
		spanString2.setSpan(new UnderlineSpan(), 0, spanString2.length(), 0);
		spanString2.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString2.length(), 0);
		spanString2.setSpan(new StyleSpan(Typeface.ITALIC), 0, spanString2.length(), 0);
		tv8.setText(spanString2);

		Drawable originalDrawable = et3.getBackground();
		
		
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		             if((et3.getText().toString()).equals("")){
						 et3.setHint("please enter a valid mail id");
		        		 et3.setBackgroundResource(R.drawable.edittext_red);
					}
		             if((et5.getText().toString()).equals("")){
						 et5.setHint("please enter password");
		        		 et5.setBackgroundResource(R.drawable.edittext_red);
		 			}
					if(et3.getText().toString().trim().equals("") || et5.getText().toString().trim().equals(""))
					{
						return;
					}

	              if((et3.getText().toString()).matches("\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b")){
		            	  et3.setBackgroundResource(R.drawable.shape_edittext);
				  }else{
		    				//et4.setError("password should not contain symbols");
		    				et3.setText("");
		    				et3.setBackgroundResource(R.drawable.edittext_red);
		    				et3.setHint("please enter a valid mail id");
					  		et3.requestFocus();
					  		return;
		    		}

				CoursePurchaseActivity cp = (CoursePurchaseActivity) getActivity();
				cp.loginButtonClicked(et3.getText().toString(), et5.getText().toString());
		             }
			
		});
		
		
		
		/*tv2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				MainActivity m=(MainActivity) getActivity();
				m.pressed();
				
				
			}
		});*/
		
tv6.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				CoursePurchaseActivity m=(CoursePurchaseActivity) getActivity();
				m.pressedprivacy();
				
				
			}
		});

		tv8.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				CoursePurchaseActivity m=(CoursePurchaseActivity) getActivity();
				m.pressedsignup();


			}
		});

		return v;
	}
	
}
