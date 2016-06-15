package com.techpalle.poc;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class Privacy extends Fragment{
	WebView w;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v=inflater.inflate(R.layout.privacypolicy, null);
		w=(WebView) v.findViewById(R.id.webView1);
		w.loadUrl("http://palleuniversity.com/Privacypolicy.aspx");
		w.getSettings().setJavaScriptEnabled(true);
		return v;
	}
	

}
