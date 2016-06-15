package com.techpalle.poc;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyDialog extends DialogFragment {
	Button b1,b2;
    ImageView iv1, iv2;
	TextView tv1,tv2,tv3,tv4,tv5,s1,s2,s3,s4,coursename;
	LinearLayout lv;
	String course, course_id;
	String priceINR, priceUSD, price_inr_after_discount, price_usd_after_discount;
    int purchaseMode = - 1; //0-sdcard, 1-pendrive, 2-both

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		Bundle bnd = getArguments();
		course = bnd.getString("course");
		course_id = bnd.getString("course_id");
		priceINR = bnd.getString("priceINR");
		priceUSD = bnd.getString("priceUSD");
		price_inr_after_discount = bnd.getString("price_inr_after_discount");
		price_usd_after_discount = bnd.getString("price_usd_after_discount");
        purchaseMode = bnd.getInt("purchaseWith");


		Dialog d=null;
		AlertDialog.Builder ab=new AlertDialog.Builder(getActivity());
		//ab.setTitle("hai");
		View v=getActivity().getLayoutInflater().inflate(R.layout.dialog, null);
		ab.setView(v);
		b1=(Button) v.findViewById(R.id.button1);
		b2=(Button) v.findViewById(R.id.button2);
		tv1=(TextView) v.findViewById(R.id.textView10);
		tv2=(TextView) v.findViewById(R.id.textView11);
		tv3=(TextView) v.findViewById(R.id.textView12);
		tv4=(TextView) v.findViewById(R.id.textView13);
		tv5=(TextView) v.findViewById(R.id.textView14);
		coursename = (TextView) v.findViewById(R.id.textView5);
		coursename.setText(course);

        iv1 = (ImageView) v.findViewById(R.id.sdCardImage);
        iv2 = (ImageView) v.findViewById(R.id.penDriveImage);

        switch(purchaseMode){
            case 0://purchase only sdcard
                iv2.setVisibility(View.INVISIBLE);
                break;
            case 1://purchase only pendrive
                iv1.setVisibility(View.INVISIBLE);
                break;
        }
		s1=(TextView) v.findViewById(R.id.textView6);
		s2=(TextView) v.findViewById(R.id.textView7);
		s3=(TextView) v.findViewById(R.id.textView16);
		s4=(TextView) v.findViewById(R.id.textView17);

		lv=(LinearLayout) v.findViewById(R.id.linr);

		s1.setText("\u20B9 "+priceINR);
		s1.setPaintFlags(s1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        s2.setText("$ " + priceUSD);
		s2.setPaintFlags(s2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        s3.setText("\u20B9 " + price_inr_after_discount);
		s4.setText("$ "+price_usd_after_discount);

		//s1.setText(s1.getText().toString()+"\n 7991");
		b1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if((tv1.getText().toString()).equals("")){
					//mTextView.setText(content);
					//mTextView.setText("This text will be underlined");
					lv.setVisibility(View.VISIBLE);
					SpannableString spanString = new SpannableString("login ");
					spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
					spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
					spanString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spanString.length(), 0);
					tv2.setText(spanString);
					SpannableString spansString = new SpannableString("signup ");
					spansString.setSpan(new UnderlineSpan(), 0, spansString.length(), 0);
					spansString.setSpan(new StyleSpan(Typeface.BOLD), 0, spansString.length(), 0);
					spansString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spansString.length(), 0);
					tv4.setText(spansString);

					//login - ccavenue
					tv2.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
                            Intent in = new Intent(getActivity(), CoursePurchaseActivity.class);
							in.putExtra("course",course);
							in.putExtra("course_id", course_id);
							in.putExtra("price_inr_before_discount",priceINR);
							in.putExtra("priceINR",price_inr_after_discount);
							in.putExtra("price_usd_before_discount",priceUSD);
							in.putExtra("priceUSD",price_usd_after_discount);
                            in.putExtra("purchaseMode",purchaseMode);
							in.putExtra("action","login");
							in.putExtra("pay","ccavenue");
                            startActivity(in);
					                         				                   }
					});
					//signup - ccavenue
					tv4.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
                            Intent in = new Intent(getActivity(), CoursePurchaseActivity.class);
                            in.putExtra("course",course);
							in.putExtra("course_id", course_id);
							in.putExtra("price_inr_before_discount",priceINR);
							in.putExtra("priceINR",price_inr_after_discount);
							in.putExtra("price_usd_before_discount",priceUSD);
							in.putExtra("priceUSD",price_usd_after_discount);
                            in.putExtra("purchaseMode",purchaseMode);
							in.putExtra("action","signup");
							in.putExtra("pay","ccavenue");
                            startActivity(in);
					                        }
					});

					tv1.setText("Please ");
				//tv2.setText("login ");
				tv3.setText("or ");
               // tv4.setText("signup ");
                tv5.setText(" to buy course");
				}else{
					lv.setVisibility(View.INVISIBLE);
					tv1.setText("");
					tv2.setText("");
					tv3.setText("");
					tv4.setText("");
					tv5.setText("");
				}
			}
		});
		b2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if((tv1.getText().toString()).equals("")){
					//mTextView.setText(content);
					//mTextView.setText("This text will be underlined");
					lv.setVisibility(View.VISIBLE);
					SpannableString spanString = new SpannableString("login ");
					spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);
					spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
					spanString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spanString.length(), 0);
					tv2.setText(spanString);
					SpannableString spansString = new SpannableString("signup ");
					spansString.setSpan(new UnderlineSpan(), 0, spansString.length(), 0);
					spansString.setSpan(new StyleSpan(Typeface.BOLD), 0, spansString.length(), 0);
					spansString.setSpan(new StyleSpan(Typeface.ITALIC), 0, spansString.length(), 0);
					tv4.setText(spansString);
                    //login - paypal
                    tv2.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
							Intent in = new Intent(getActivity(), CoursePurchaseActivity.class);
							in.putExtra("course",course);
							in.putExtra("course_id", course_id);
							in.putExtra("price_inr_before_discount",priceINR);
							in.putExtra("priceINR",price_inr_after_discount);
							in.putExtra("price_usd_before_discount",priceUSD);
							in.putExtra("priceUSD",price_usd_after_discount);
                            in.putExtra("purchaseMode",purchaseMode);
							in.putExtra("action","login");
							in.putExtra("pay","paypal");
							startActivity(in);
                        }
                    });
                    //signup - paypal
                    tv4.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
							Intent in = new Intent(getActivity(), CoursePurchaseActivity.class);
							in.putExtra("course",course);
							in.putExtra("course_id", course_id);
							in.putExtra("price_inr_before_discount",priceINR);
							in.putExtra("priceINR",price_inr_after_discount);
							in.putExtra("price_usd_before_discount",priceUSD);
							in.putExtra("priceUSD",price_usd_after_discount);
                            in.putExtra("purchaseMode",purchaseMode);
							in.putExtra("action","signup");
							in.putExtra("pay","paypal");
							startActivity(in);
                        }
                    });

					tv1.setText("Please ");
				//tv2.setText("login ");
				tv3.setText("or ");
               // tv4.setText("signup ");
                tv5.setText(" to buy course");
				}else{
					lv.setVisibility(View.INVISIBLE);
					tv1.setText("");
					tv2.setText("");
					tv3.setText("");
					tv4.setText("");
					tv5.setText("");
				}
			}
		});
		d=ab.create();
		return d;
	}

}
