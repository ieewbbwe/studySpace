package com.webber.uploaddemo;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ParseException;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.webber.uploaddemo.FTP.FTP;

import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final int FILE_SELECT_CODE = 0x01;
    @Bind(R.id.chose_bt)
    Button choseBt;
    @Bind(R.id.chose_tv)
    TextView choseTv;
    @Bind(R.id.upload_bt)
    Button uploadBt;
    @Bind(R.id.activity_main)
    LinearLayout activityMain;
    @Bind(R.id.progress_ll)
    LinearLayout progressLl;
    @Bind(R.id.thread_et)
    EditText threadEt;
    @Bind(R.id.des_tv)
    TextView desTv;

    public static final String FTP_CONNECT_SUCCESSS = "ftp连接成功";
    public static final String FTP_CONNECT_FAIL = "ftp连接失败";
    public static final String FTP_DISCONNECT_SUCCESS = "ftp断开连接";
    public static final String FTP_FILE_NOTEXISTS = "ftp上文件不存在";

    public static final String FTP_UPLOAD_SUCCESS = "ftp文件上传成功";
    public static final String FTP_UPLOAD_FAIL = "ftp文件上传失败";
    public static final String FTP_UPLOAD_LOADING = "ftp文件正在上传";

    public static final String FTP_DOWN_LOADING = "ftp文件正在下载";
    public static final String FTP_DOWN_SUCCESS = "ftp文件下载成功";
    public static final String FTP_DOWN_FAIL = "ftp文件下载失败";

    public static final String FTP_DELETEFILE_SUCCESS = "ftp文件删除成功";
    public static final String FTP_DELETEFILE_FAIL = "ftp文件删除失败";
    @Bind(R.id.service_et)
    EditText serviceEt;
    @Bind(R.id.port_et)
    EditText portEt;
    @Bind(R.id.user_et)
    EditText userEt;
    @Bind(R.id.pwd_et)
    EditText pwdEt;

    private File file;
    private FTPUtil ftpUtil;
    private List<File> fileList;
    private WifiManager wifiManager;
    private WifiManager.MulticastLock multicastLock;
    private int threadCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ftpUtil = new FTPUtil();
        //ftpUtil.setConfig("10.27.0.127", 21, "webbermo", "151102");
        ftpUtil.setConfig("10.27.0.20", 6657, "admin", "123456");
        //ScanIp();
        init();
    }

    private String remoteStr = "/root_dir/share/data_disk1/";

    private void init() {
        choseBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image*//*");*/

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                try {
                    startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), FILE_SELECT_CODE);
                } catch (ActivityNotFoundException ex) {
                    Toast.makeText(MainActivity.this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        fileList = new ArrayList<>();
        fileList.add(file);
        uploadBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ftpUtil.setConfig(serviceEt.getText().toString().trim(), Integer.parseInt(portEt.getText().toString().trim()),
                        userEt.getText().toString().trim(), pwdEt.getText().toString().trim());
                if (file != null) {
                    progressLl.removeAllViews();
                    threadCount = Integer.parseInt(threadEt.getText().toString().trim());
                    for (int i = 0; i < threadCount; i++) {
                        createProgress();
                        new UploadThread(file, (ProgressBar) progressLl.getChildAt(i), i).start();
                    }
                    //uploadFile();
                } else {
                    Snackbar.make(getWindow().getDecorView(), "请选择文件", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    public class UploadThread extends Thread {

        private File mFile;
        private int threadId;
        private ProgressBar pb;
        private long step;

        public UploadThread(File file, ProgressBar pb, int threadId) {
            this.mFile = file;
            this.pb = pb;
            this.threadId = threadId;
        }

        public UploadThread(File file, int threadId) {
            this.mFile = file;
            this.threadId = threadId;
            this.pb = ((ProgressBar) progressLl.getChildAt(threadId));
            this.pb.setMax((int) file.length());
        }

        @Override
        public void run() {
            super.run();
            Log.d("start", "进度:" + threadId + "线程：" + Thread.currentThread().getName());
            try {
                FTP ftp = new FTP();
                FTP.UploadProgressListener listener = new FTP.UploadProgressListener() {
                    @Override
                    public void onUploadProgress(String currentStep, long uploadSize, File file) {
                        if (uploadSize != 0) {
                            step = uploadSize;
                           // Log.d("progress", "进度:" + threadId + "线程：" + Thread.currentThread().getName() + "进度：" + uploadSize);
                            pb.setProgress((int) uploadSize);
                        } else if (currentStep.equals(FTP_UPLOAD_SUCCESS)) {
                            Snackbar.make(getWindow().getDecorView(), currentStep, Snackbar.LENGTH_SHORT).show();
                            Log.d("end", "进度:" + threadId + "线程：" + Thread.currentThread().getName());
                        }
                        //Log.d("end", "信息：" + currentStep);
                        //Toast.makeText(MainActivity.this, currentStep, Toast.LENGTH_SHORT).show();
                    }
                };
                Log.d("end", "listener地址：" + listener.toString());
                ftp.uploadSingleFile(file, "../upload", listener);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void upload(File file, int i) {
        try {
            ProgressBar bar = ((ProgressBar) progressLl.getChildAt(i));
            bar.setMax((int) file.length());
            new Thread(new Runnable() {
                @Override
                public void run() {

                }
            }).start();
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "请输入整数", Toast.LENGTH_SHORT).show();
        }
    }

    private void createProgress() {
        ProgressBar mProgressBar = new ProgressBar(MainActivity.this);
        BeanUtils.setFieldValue(mProgressBar, "mOnlyIndeterminate", new Boolean(false));
        mProgressBar.setIndeterminate(false);
        mProgressBar.setProgressDrawable(getResources().getDrawable(android.R.drawable.progress_horizontal));
        mProgressBar.setIndeterminateDrawable(getResources().getDrawable(android.R.drawable.progress_indeterminate_horizontal));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 10, Gravity.CENTER_VERTICAL);
        layoutParams.setMargins(0, 5, 0, 5);
        mProgressBar.setLayoutParams(layoutParams);
        progressLl.addView(mProgressBar, 0);
    }

    private void uploadFile() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.27.0.20/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("application/otcet-stream"), file);
        ProgressRequestBody fileRequestBody = new ProgressRequestBody(file, new ProgressRequestBody.ProgressListener() {
            @Override
            public void onProgress(long hasWrittenLen, long totalLen, boolean hasFinish) {
                Log.d("webber", "上传进度：" + (hasWrittenLen / totalLen));
            }
        });


        MultipartBody.Part body =
                MultipartBody.Part.createFormData("aFile", file.getName(), requestFile);
        String descriptionString = "ForTest";

        RequestBody description =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), descriptionString);
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.login("admin", "123456");
            ftpClient.connect("10.27.0.20", 7795);
        } catch (IOException e) {
            e.printStackTrace();
        }
        retrofit.create(FileUploadService.class)
                .uploadFile(description, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Response<BaseResponse>>() {
                    @Override
                    public void call(Response<BaseResponse> baseResponseResponse) {
                        if (baseResponseResponse.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "上传成功！", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, baseResponseResponse.message(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    Log.d("webber", "uri:" + uri.getPath());
                    String path = getPath(this, uri);
                    Log.d("webber", "path:" + path);
                    if (path != null) {
                        file = new File(path);
                        choseTv.setText(path + "\n" + "文件大小：" + getDataSize(file.length()) + "\n" + "字节码长度：" + file.length());
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 返回byte的数据大小对应的文本
     *
     * @param size
     * @return
     */
    public static String getDataSize(long size) {
        Log.d("webber", "fileLength:" + size);
        DecimalFormat formater = new DecimalFormat("####.00");
        if (size < 1024) {
            return size + "bytes";
        } else if (size < 1024 * 1024) {
            float kbsize = size / 1024f;
            return formater.format(kbsize) + "KB";
        } else if (size < 1024 * 1024 * 1024) {
            float mbsize = size / 1024f / 1024f;
            return formater.format(mbsize) + "MB";
        } else if (size < 1024 * 1024 * 1024 * 1024) {
            float gbsize = size / 1024f / 1024f / 1024f;
            return formater.format(gbsize) + "GB";
        } else {
            return "size: error";
        }
    }


    public String getPath(Context context, Uri uri) {

        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
                cursor.close();
            } catch (Exception e) {
                // Eat it
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public void ScanIp() {
        Observable.create(new Observable.OnSubscribe<List<String>>() {
            @Override
            public void call(Subscriber<? super List<String>> subscriber) {
                try {
                    List<String> ipList = new ArrayList();
                    MulticastSocket mSocket = new MulticastSocket();
                    mSocket.joinGroup(InetAddress.getByName("239.0.1.1"));
                    mSocket.setTimeToLive(4);
                    mSocket.setLoopbackMode(true);

                    byte[] arrayOfByte = new byte[256];
                    DatagramPacket localDatagramPacket = new DatagramPacket(arrayOfByte, arrayOfByte.length);
                    wifiManager = ((WifiManager) getSystemService(WIFI_SERVICE));
                    multicastLock = wifiManager.createMulticastLock("UDPwifi");
                    multicastLock.acquire();
                    mSocket.receive(localDatagramPacket);
                    Log.d("webber", "receiver:");
                    String str1 = new String(localDatagramPacket.getData());
                    Log.d("webber", "str1:" + str1);
                    if (str1.indexOf(";") > 0) {
                        String str2 = str1.substring(5, str1.indexOf(";"));
                        Log.d("webber", "str2:" + str2);
                        ipList.add(str2);
                    }
                    multicastLock.release();
                    subscriber.onNext(ipList);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<String>>() {
                    @Override
                    public void call(List<String> list) {
                        Log.d("webber", "listSize:" + list.size());
                    }
                });
    }


}
