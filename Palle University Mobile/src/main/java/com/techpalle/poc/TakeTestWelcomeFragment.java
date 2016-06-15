package com.techpalle.poc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by skillgun on 12/10/2015.
 */

public class TakeTestWelcomeFragment extends Fragment {

	private OnStartTestSelected mCallBack;

	public TakeTestWelcomeFragment(){

	}
	public interface OnStartTestSelected{
		public void onStartTestClicked();
	}

	@Override
	public void onAttach(Context context) {
		try{
			mCallBack = (OnStartTestSelected) context;
		}catch(ClassCastException e){
			Toast.makeText(getActivity(), "Parent must implement frag listener", Toast.LENGTH_LONG).show();
		}
		super.onAttach(context);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.purchased_take_test_welcome, null);
		TextView tv2 = (TextView) v.findViewById(R.id.textView2);
		Button b = (Button) v.findViewById(R.id.button1);

		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(
							getActivity().getAssets().open("take_test_welcome.txt")));
			String s = null;
			StringBuilder sb = new StringBuilder();

			do{
				s = br.readLine();
				if(s != null)
					sb.append(s);
			}while(s != null);

			tv2.setText(Html.fromHtml(sb.toString()));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mCallBack.onStartTestClicked();
			}
		});

		return v;
	}
}
