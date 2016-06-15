package com.techpalle.poc;


import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 */
public class SalientFeaturesFragment extends Fragment implements TextureView.SurfaceTextureListener {

    private TextureView tv;
    private MediaPlayer mMediaPlayer;

    public SalientFeaturesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_salient_features, container, false);
        tv = (TextureView) v.findViewById(R.id.textureView1);

        Matrix txform = new Matrix();
        tv.getTransform(txform);
        float scale = getActivity().getResources().getDisplayMetrics().density;
        Toast.makeText(getActivity(), "" + scale, Toast.LENGTH_SHORT).show();

        txform.setScale((float) 1.3f, (float) 1.4f);
        txform.postTranslate(0, 0);
        tv.setTransform(txform);

        tv.setSurfaceTextureListener(SalientFeaturesFragment.this);

        return v;
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Surface s = new Surface(surface);

        try {
            mMediaPlayer= new MediaPlayer();
            mMediaPlayer.setDataSource(getActivity(),  Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.salientfeatures));

            //vw.setVideoURI(Uri.parse(salientpath)););
            //mMediaPlayer.setDataSource("http://daily3gp.com/vids/747.3gp");

            mMediaPlayer.setSurface(s);
            mMediaPlayer.prepare();
            mMediaPlayer.setOnBufferingUpdateListener(null);
            mMediaPlayer.setOnCompletionListener(null);
            mMediaPlayer.setOnPreparedListener(null);
            mMediaPlayer.setOnVideoSizeChangedListener(null);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

/*
            mMediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                @Override
                public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {

                }
            });
*/

            //mMediaPlayer.setDisplay();
            mMediaPlayer.start();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        /*mCamera = Camera.open();

        try {
            mCamera.setPreviewTexture(surface);
            mCamera.startPreview();
        } catch (IOException ioe) {
            // Something bad happened
        }*/
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }
}
