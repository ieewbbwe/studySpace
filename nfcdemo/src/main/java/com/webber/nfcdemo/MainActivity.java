package com.webber.nfcdemo;

import android.net.Uri;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    private NfcAdapter nfcAdapter;
    private VideoView mVv;
    private SeekBar mSb;
    private MediaController mMc;
    private String videoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       /* TextView mNfcTv = (TextView) findViewById(R.id.nfc_tv);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            mNfcTv.setText("不支持NFC！");
        } else if (!nfcAdapter.isEnabled()) {
            mNfcTv.setText("NFC不可用，请先开启！");
        }*/
        Log.d("webber", getAssets().getLocales()[0]);
        videoPath = Uri.parse("file:\\\\android_asset\\ios_buy.mov").toString();
        //mMc = (MediaController) findViewById(R.id.test_mc);
        MediaController mMc = new MediaController(MainActivity.this);
        mVv = (VideoView) findViewById(R.id.test_vv);
        mSb = (SeekBar) findViewById(R.id.test_sb);
        mVv.setVideoPath(videoPath);
        mVv.setMediaController(mMc);
        mVv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVv.isPlaying()) {
                    mVv.pause();
                } else {
                    mVv.start();
                }
            }
        });
    }
}
