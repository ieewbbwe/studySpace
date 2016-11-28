package com.webber.uploaddemo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.IOException;
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

    private File file;
    private FTPUtil ftpUtil;
    private List<File> fileList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ftpUtil = new FTPUtil();
        ftpUtil.setConfig("10.27.0.20", 21, "admin", "123456");
        init();
    }

    private String remoteStr = "/root_dir/share/data_disk1/";

    private void init() {
        choseBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                try {
                    startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), FILE_SELECT_CODE);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(MainActivity.this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        fileList = new ArrayList<>();
        fileList.add(file);
        uploadBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (file != null) {
                    Observable.create(new Observable.OnSubscribe<Object>() {
                        @Override
                        public void call(Subscriber<? super Object> subscriber) {
                            ftpUtil.upload(file);
                        }
                    }).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<Object>() {
                                @Override
                                public void call(Object o) {

                                }
                            });
                    //uploadFile();
                } else {
                    Snackbar.make(getWindow().getDecorView(), "请选择文件", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
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
                    String path = getPath(this, uri);
                    Log.d("webber", "path:" + path);
                    if (path != null) {
                        file = new File(path);
                        choseTv.setText(path);
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
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
}
