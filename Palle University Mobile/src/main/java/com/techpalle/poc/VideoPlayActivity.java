package com.techpalle.poc;

import android.app.Activity;		
import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.AsyncTask;
//import android.media.session.MediaController;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;

public class VideoPlayActivity extends AppCompatActivity {

	private VideoView videoView;
	private String currentVideoPath;
	private String decPath;
	private String tempPath; //where we will decrypt and store mp4 files.
	private String pid; //paper id to take test
	//MediaController mc;
	MediaController mc;
	//android.widget.MediaController 
	private MyVideoOverDialogFragment mvdfrag;
	private ActionBar ab;
	private ScrollView scv;
	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
	private GoogleApiClient client;

	@Override
	public void onStart() {
		super.onStart();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client.connect();
		Action viewAction = Action.newAction(
				Action.TYPE_VIEW, // TODO: choose an action type.
				"VideoPlay Page", // TODO: Define a title for the content shown.
				// TODO: If you have web page content that matches this app activity's content,
				// make sure this auto-generated web page URL is correct.
				// Otherwise, set the URL to null.
				Uri.parse("http://host/path"),
				// TODO: Make sure this auto-generated app deep link URI is correct.
				Uri.parse("android-app://com.techpalle.poc/http/host/path")
		);
		AppIndex.AppIndexApi.start(client, viewAction);
	}

	private class MyTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			int pos = currentVideoPath.lastIndexOf("/");
			//String temppath = currentVideoPath.substring(0, pos+1); //COMMENTED FOR TESTING
			//decPath = temppath+".satish.mp4"; //COMMENTED FOR TESTING
			//decrypted file is hidden in temp folder thats why a . in the front
			decPath = Environment.getExternalStorageDirectory().toString() + "/.lol.mp4";//tempPath+".satish.mp4";
			
			/*CryptUtility.decrypt(currentVideoPath, 
					decPath);*/
			CryptUtility.decryptFast(currentVideoPath,
					decPath);
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				videoView.setVideoPath(decPath);
				videoView.start();

				Toast.makeText(getApplicationContext(),
						"GOT IT.." + decPath,
						1).show();
			} else {
				Toast.makeText(getApplicationContext(),
						"wrong.." + decPath,
						1).show();
			}
			super.onPostExecute(result);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.video_play);
		ab = getSupportActionBar();
		ab.setDisplayShowTitleEnabled(false);
		ab.hide();
		//scv = (ScrollView) findViewById(R.id.scrollView1);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		videoView = (VideoView) findViewById(R.id.videoView1);
		mc = new MediaController(this);
		videoView.setMediaController(mc);

		//code for making video view full screen - start
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) videoView.getLayoutParams();
		params.width = metrics.widthPixels;
		params.height = metrics.heightPixels;
		params.leftMargin = 0;
		videoView.setLayoutParams(params);
		//code for making video view full screen - end

		mc.hide();//to hide media controller initially

		videoView.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				Toast.makeText(VideoPlayActivity.this, "Video done", 1).show();
				mvdfrag = new MyVideoOverDialogFragment();
				mvdfrag.show(getSupportFragmentManager(), "videoover");
			}
		});

		mc.setAnchorView(videoView);
		Intent in = getIntent();
		if (in != null) {
			Bundle bnd = in.getExtras();
			if (bnd != null) {
				currentVideoPath = bnd.getString("path");
				tempPath = bnd.getString("temppath");
				pid = bnd.getString("pid");
			}
		}
		if (currentVideoPath != null) {
			new MyTask().execute();
			//videoView.setVideoPath(currentVideoPath);
			//videoView.start();
		}
		// TODO Auto-generated method stub
		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
	}

	@Override
	protected void onStop() {
		File fp = new File(Environment.getExternalStorageDirectory().toString() + "/.lol.mp4");
		//Environment.getExternalStorageDirectory().toString()+"/.lol.mp4";
		if (fp.exists()) {
			fp.delete();
		}
		super.onStop();
		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		Action viewAction = Action.newAction(
				Action.TYPE_VIEW, // TODO: choose an action type.
				"VideoPlay Page", // TODO: Define a title for the content shown.
				// TODO: If you have web page content that matches this app activity's content,
				// make sure this auto-generated web page URL is correct.
				// Otherwise, set the URL to null.
				Uri.parse("http://host/path"),
				// TODO: Make sure this auto-generated app deep link URI is correct.
				Uri.parse("android-app://com.techpalle.poc/http/host/path")
		);
		AppIndex.AppIndexApi.end(client, viewAction);
		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client.disconnect();
	}

	private class MyVideoOverDialogFragment extends DialogFragment {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			Dialog d = null;
			AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
			View v = getActivity().getLayoutInflater().inflate(R.layout.video_over_dialog_new, null);
            Button b1 = (Button) v.findViewById(R.id.button1);
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mvdfrag.dismiss();
                    finish();
                }
            });
            LinearLayout taketestlayout = (LinearLayout) v.findViewById(R.id.taketestlayout);
            ImageView taketestimage = (ImageView) v.findViewById(R.id.imageView6);
            TextView taketesttextview = (TextView) v.findViewById(R.id.textView3);
			ImageView replay = (ImageView) v.findViewById(R.id.imageView2);
			replay.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
                    videoView.start();
                    mvdfrag.dismiss();
				}
			});

            View.OnClickListener taketestlistener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pid.trim().equals("")){
                        Toast.makeText(VideoPlayActivity.this, "TEST NOT AVAILABLE ", 1).show();
                        mvdfrag.dismiss();
                        finish();
                        return;
                    }
                    Intent in = new Intent(VideoPlayActivity.this, PurchasedTakeTestActivity.class);
                    in.putExtra("pid", pid);
                    startActivity(in);

					mvdfrag.dismiss();
					finish();
					//dismiss();
                }
            };
            taketestimage.setOnClickListener(taketestlistener);
            taketestlayout.setOnClickListener(taketestlistener);
            taketesttextview.setOnClickListener(taketestlistener);

			ab.setView(v);
			d = ab.create();
			return d;
		}
		/*@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.video_over_dialog, null);
			return v;
		}*/
	}

}
