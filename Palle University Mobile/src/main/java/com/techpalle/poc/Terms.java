package com.techpalle.poc;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Terms extends Fragment {
	ActionBar ab;
	WebView w;
	View v;
	@Override
	
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		 v=inflater.inflate(R.layout.termsandconditions, null);
		
		 w=(WebView) v.findViewById(R.id.web);
		 w.loadUrl("http://palleuniversity.com/TermsandConditions.aspx");
		w.getSettings().setJavaScriptEnabled(true);
		return v;
	}
	/*
     @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	 super.onViewCreated(view, savedInstanceState);
    	 w=(WebView) v.findViewById(R.id.web);
    	 ab.setDisplayHomeAsUpEnabled(true);
    	 w.loadUrl("http://palleuniversity.com/TermsandConditions.aspx");
    	
    }
	
*/
}
