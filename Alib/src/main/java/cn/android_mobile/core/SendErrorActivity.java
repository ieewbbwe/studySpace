package cn.android_mobile.core;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.TextView;
import cn.android_mobile.core.net.http.Http;
import cn.android_mobile.toolkit.Lg;

/**
 * 
 * @author fangzhen    E-mail:fangzhen@pingan.com.cn
 * @version 1.1.0
 */
public class SendErrorActivity extends Activity {
    private String stacktrace;
    public final static String url="http://10.27.0.250:8080/yjweb/upload";
    private TextView text;
    private String defaultText="很抱歉,程序出现异常,我们会尽快修复";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_error);
        text = (TextView) findViewById(R.id.error_context);
        // 这里把刚才异常堆栈信息写入SD卡的Log日志里面
        stacktrace=getIntent().getStringExtra("msg");
        findViewById(R.id.senderror_btn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
        findViewById(R.id.sendmail_btn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendMailByIntent(stacktrace, "fyygw@126.com");
            }
        });
        findViewById(R.id.read_btn).setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                if(text.getText().toString().endsWith(defaultText)){
                    text.setText(stacktrace);
                }else{
                    text.setText(defaultText);
                }
            }
        });
    }
    
    @Override
    protected void onPause() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            String sdcardPath = Environment.getExternalStorageDirectory()
                    .getPath();
            writeLog(stacktrace, sdcardPath + "/"
                    + getPackageInfo().packageName);
        }
        super.onPause();
    }
    /**
     * 意见反馈 邮件发送
     * */
    public void sendMailByIntent(String msg, String email) {
        Intent data = new Intent(Intent.ACTION_SENDTO);
        data.setData(Uri.parse("mailto:" + email));// 收件人
        // data.putExtra(Intent.EXTRA_EMAIL, new
        // String[]{"fyygw10@126.com"});//更多的收件人
        // data.putExtra(Intent.EXTRA_CC, new
        // String[]{"zhenfang@wistronits.com"});//抄送
        // data.putExtra(Intent.EXTRA_BCC, new
        // String[]{"fangyygw@gmail.com"});//秘密发送
        data.putExtra(Intent.EXTRA_SUBJECT, "[意见反馈]");
        data.putExtra(Intent.EXTRA_TEXT, msg);
        startActivity(data);
        finish();
    }
    // 写入Log信息的方法，写入到SD卡里面
        private void writeLog(String log, String name) {
            CharSequence timestamp = DateFormat.format("yyyy-MM-dd_kk-mm-ss",
                    System.currentTimeMillis());
            String filename = name + "_" + timestamp + ".log";
            try {
                FileOutputStream stream = new FileOutputStream(filename);
                OutputStreamWriter output = new OutputStreamWriter(stream);
                BufferedWriter bw = new BufferedWriter(output);
                // 写入相关Log到文件
                bw.write(log);
                bw.newLine();
                bw.close();
                output.close();
                stream.close();
                new SendCrashLog(this, getPackageInfo().packageName + "_"
                        + timestamp, filename).execute();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

            }
        }
        public PackageInfo getPackageInfo() {
            // 获取packagemanager的实例
            PackageManager packageManager = getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = null;
            try {
                packInfo = packageManager.getPackageInfo(getPackageName(), 0);
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
            return packInfo;
        }
        
        // 发送日志到网络
        public class SendCrashLog extends AsyncTask<Void, Void, Void> {
            private String fileName;
            private String filePath;
            private Context c;

            public SendCrashLog(Context cxt, String fileName, String filePath) {
                c = cxt;
                this.fileName = fileName;
                this.filePath = filePath;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... arg0) {
                Http h = new Http(url);
                h.addFile(filePath);
                h.addParam("file", fileName);
                String res = h.execute();
                Lg.print(res);
                BasicApplication.closeApp();
                return null;
            }
        }
}
