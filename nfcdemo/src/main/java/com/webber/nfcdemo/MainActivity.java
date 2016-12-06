package com.webber.nfcdemo;

import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private NfcAdapter nfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView mNfcTv = (TextView) findViewById(R.id.nfc_tv);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            mNfcTv.setText("不支持NFC！");
        } else if (!nfcAdapter.isEnabled()) {
            mNfcTv.setText("NFC不可用，请先开启！");
        }
    }
}
