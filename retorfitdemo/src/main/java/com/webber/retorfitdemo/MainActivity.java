package com.webber.retorfitdemo;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.jakewharton.rxbinding.view.RxView;
import com.webber.retorfitdemo.image.GlideLoader;
import com.webber.retorfitdemo.net.ApiFactory;
import com.webber.retorfitdemo.net.OnProgressRequestCallback;
import com.webber.retorfitdemo.net.request.BaseRequest;
import com.webber.retorfitdemo.net.request.UserRequest;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private String baseUrl = "https://hk-stage.shop.yahoo.com/api/m/v1/";
    public static final String wssid = "uRuv5V69tJ4";
    public static final String cookie = "Y=v=1&n=588s97encs6d9&l=a26x2n6jf6x1x02oslxkjawuib05guai8ispus1a/o&p=m2r0000012000000&iz=&r=10d&lg=zh-Hant-HK&intl=hk&np=1; path=/; domain=.yahoo.com;T=z=bkoMYBbq9MYByl09jMHKU2iNk40NgY2N041NjU1TjU0NDIzNE&a=QAE&sk=DAAUWsQXlxsO7V&ks=EAAeXdR4e0SFBDKcVDhM.Nd9g--~F&kt=EAA9tbgq1ATxLbhm2zsuRAy.A--~F&d=c2wBTVRrek1RRXhNRGt5TVRJeU9USXpNelUwTXprd01Uay0BYQFRQUUBZwFBSVdaQkRSR1VPNFdQQUpSVFhJUjdCUEwzTQFzY2lkAUxxLl9nYWFJTzE5algwejlHUWZ4a3BNd3Fody0BYWMBQUVQSnc5LjBHcWxoTVJnLQFhbAFpZXdlYmJlcgFzYwF5aGtkZWFsc2J1eWVyAXp6AWJrb01ZQmdXQQFjcwFzZHBzLXIsc2RwcC13LHNkcHMtdyxhdWN0LXIsYXVjdC13LG1icgF0aXABY252YWJD; path=/; domain=.yahoo.com; HttpOnly";
    private Button mTestBt;
    private Retrofit defaultRetrofot;
    private ImageView mIv;
    private Dialog dialog;
    private Subscription productSubScription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTestBt = (Button) findViewById(R.id.text_bt);
        mIv = (ImageView) findViewById(R.id.test_iv);

        defaultRetrofot = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(genericClient())
                .build();
        //普通的Get请求
        //demo1();
        //post 发送json请求
        //demo2();
        //post 发送formUrl
        //demo3();

        RxView.clicks(mTestBt)
                .debounce(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        /*if (productSubScription != null && productSubScription.isUnsubscribed()) {
                            Log.d("respon", "取消订阅");
                            productSubScription.unsubscribe();
                        }
                        Log.d("webber", "点击了" + Thread.currentThread().getName());*/
                        demo4();
                    }
                });

    }

    /**
     * 加载一批图片
     */
    private void demo4() {
        UserRequest userRequest = new UserRequest("195303", "+LOCATIONS,+VARIANTS,+SHIPPINGINFO");

        ApiFactory.getProductAPI()
                .getProductDetail("195303", getRequestMap(userRequest))
                .subscribeOn(Schedulers.io())//IO线程进行网络访问
                .observeOn(AndroidSchedulers.mainThread())//主线程接收消息
                .subscribe(new OnProgressRequestCallback<Response<BaseProduct>>(getApplicationContext()) {
                    @Override
                    public void onResponse(Response<BaseProduct> response) {
                        Log.d("network:", "response:" + new Gson().toJson(response));
                        Snackbar.make(getWindow().getDecorView(), response.body().title, Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    private Map<String, String> getRequestMap(BaseRequest request) {
        return getParams(request);
    }

    protected Map<String, String> getParams(BaseRequest req) {
        Map<String, String> params = new HashMap<>();
        Class<? extends BaseRequest> c = req.getClass();
        Class supreClazz = c.getSuperclass();
        Field[] fields = c.getDeclaredFields();
        Field[] parentFields = supreClazz.getDeclaredFields();

        Field[] result = new Field[fields.length + parentFields.length];
        System.arraycopy(fields, 0, result, 0, fields.length);
        System.arraycopy(parentFields, 0, result, fields.length,
                parentFields.length);

        for (Field f : result) {
            f.setAccessible(true);
            try {
                String s = null;
                if (f.get(req) instanceof Integer) {
                    s = String.valueOf((Integer) f.get(req));
                }
                if (f.get(req) instanceof String) {
                    s = (String) f.get(req);
                }
                if (f.get(req) instanceof Double) {
                    s = String.valueOf((Double) f.get(req));
                }
                if (f.get(req) instanceof Float) {
                    s = String.valueOf((Float) f.get(req));
                }
                if (f.get(req) instanceof Boolean) {
                    s = String.valueOf((Boolean) f.get(req));
                }
                if (f.get(req) instanceof Long) {
                    s = String.valueOf((Long) f.get(req));
                }
                if (s != null) {
                    params.put(f.getName(), s);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }

        Log.d("network", "params:" + new Gson().toJson(params));
        return params;
    }

    private void demo3() {
       /* dialog = new AlertDialog.Builder(MainActivity.this)
                .setMessage("message")
                .setTitle("这是一行标题")
                .show();*/
        /*Glide.with(MainActivity.this)
                .load(baseProductResponse.body().images.get(0).schemes.get(0).uri).crossFade()
                .bitmapTransform(new BlurTransformation(MainActivity.this, 25)
                        , new CropCircleTransformation(MainActivity.this))
                .into(mIv);*//*    if (isHttpSucceed(baseProductResponse)) {

                    }*/
        productSubScription = ApiFactory.getProductAPI()
                .getProductDetail("195303", "+LOCATIONS,+VARIANTS,+SHIPPINGINFO", "195303")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Response<BaseProduct>>() {
                    @Override
                    public void call(Response<BaseProduct> baseProductResponse) {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //   dialog.dismiss();
                        Log.d("network:", "response" + new Gson().toJson(baseProductResponse));
                        Log.d("network:", "response" + baseProductResponse.code());
                        Snackbar.make(getWindow().getDecorView(), baseProductResponse.body().title + "", Snackbar.LENGTH_SHORT).show();
                        /*Glide.with(MainActivity.this)
                                .load(baseProductResponse.body().images.get(0).schemes.get(0).uri).crossFade()
                                .bitmapTransform(new BlurTransformation(MainActivity.this, 25)
                                        , new CropCircleTransformation(MainActivity.this))
                                .into(mIv);*/
                        GlideLoader.load(MainActivity.this, baseProductResponse.body().images.get(0).schemes.get(0).uri, mIv);
                    /*    if (isHttpSucceed(baseProductResponse)) {

                        }*/
                    }
                });
    }

    public class CustomInterceptor implements Interceptor {

        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            return null;
        }
    }

    private void demo2() {
        RxView.clicks(mTestBt)
                .debounce(1000, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Log.d("webber", "点击按钮");
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(baseUrl)
                                .addConverterFactory(GsonConverterFactory.create())
                                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                                .client(genericClient())
                                .build();
                        List<LikesItem> likeObjects = new ArrayList<>();
                        likeObjects.add(new LikesItem("193284"));
                        likeObjects.add(new LikesItem("178548"));
                        LikesResponse response = new LikesResponse();
                        response.likeObjects = likeObjects;
                        Log.d("network", "param:" + new Gson().toJson(response));

                        // RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(response));
                        retrofit.create(NetEngine.class).postLikeItem(response)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Action1<Response>() {
                                    @Override
                                    public void call(Response response) {
                                        Log.d("network", new Gson().toJson(response));
                                        Log.d("network", "code:" + response.code());
                                    }
                                });
                    }
                });

    }

    /**
     * 普通的GET请求
     * 需求：从服务器拉去数据
     */
    private void demo1() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                //自定义OkHttpClient
                //.client(genericClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        //https://hk.shop.yahoo.com/api/m/v1/products/630892?fields=%2BLOCATIONS%2C%2BVARIANTS%2C%2BSHIPPINGINFO&id=630892
        //https://hk.shop.yahoo.com/api/m/v1/products/491424?fields=%2BLOCATIONS%2C%2BVARIANTS%2C%2BSHIPPINGINFO&id=491424
        retrofit.create(NetEngine.class)
                .getProductDetail("648194", "+LOCATIONS,+VARIANTS,+SHIPPINGINFO", "648194")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Response<BaseProduct>>() {
                    @Override
                    public void call(Response<BaseProduct> baseProductResponse) {
                        Log.d("network", "url:" + baseProductResponse.raw().request().url().url());
                        Log.d("network", "response:" + new Gson().toJson(baseProductResponse));
                        Snackbar.make(getWindow().getDecorView(), baseProductResponse.body().title + "", Snackbar.LENGTH_SHORT).show();
                    }
                });
              /*  .enqueue(new Callback<BaseProduct>() {
            @Override
            public void onResponse(Call<BaseProduct> call, Response<BaseProduct> response) {
                Log.d("network", "url:" + response.raw().request().url().url());
                Log.d("network", "response:" + new Gson().toJson(response));
                Snackbar.make(getWindow().getDecorView(), response.body().title + "", Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<BaseProduct> call, Throwable t) {
                Snackbar.make(getWindow().getDecorView(), t.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
        });*/
    }

    private void initData() {

        IUserBiz iUserBiz = retrofit.create(IUserBiz.class);
        iUserBiz.getProfiles().enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.d("webber", "code:" + response.code());
                if (response.code() == 200) {
                    Log.d("webber", "response:" + new Gson().toJson(response.body()));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    private OkHttpClient genericClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        return new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("Cookie", cookie)
                                .addHeader("X-YahooWSSID-Authorization", wssid)
                                .build();
                        Log.d("network", "Method:" + request.method());
                        Log.d("network", "URL:" + request.url().url());
                        Log.d("network", "Params:" + new Gson().toJson(request.tag()));
                        return chain.proceed(request);
                    }
                })
                //.addInterceptor(interceptor)
                //设置连接超时
                .connectTimeout(30 * 1000, TimeUnit.MILLISECONDS)
                .readTimeout(30 * 1000, TimeUnit.MILLISECONDS)
                .build();
    }
}
