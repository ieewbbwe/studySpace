package com.webber.retorfitdemo;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private String baseUrl = "https://hk-stage.shop.yahoo.com/api/m/v1/";
    private String cookie = "Y=v=1&n=588s97encs6d9&l=a26x2n6jf6x1x02oslxkjawuib05guai8ispus1a/o&p=m2r0000012000000&iz=&r=10d&lg=zh-Hant-HK&intl=hk&np=1; path=/; domain=.yahoo.com;T=z=z.VLYBzErLYB7/DZnymIISnNk40NgY2N041NjU1TjU0NDIzNE&a=QAE&sk=DAAZJFpt0XZe/2&ks=EAAFao.AeLuut9VWOS8AkcbYQ--~F&kt=EAAvXtbNIMuKPtNDMyiyT_Auw--~F&d=c2wBTVRrek1RRXhNRGt5TVRJeU9USXpNelUwTXprd01Uay0BYQFRQUUBZwFBSVdaQkRSR1VPNFdQQUpSVFhJUjdCUEwzTQFzY2lkAWh4S0RLVDhmenBHcU0zRlRfSjJDRmtKLnZCdy0BYWMBQUFlVWhMV2RpUDlBckp3LQFhbAFpZXdlYmJlcgFzYwF5aGtkZWFsc2J1eWVyAXp6AXouVkxZQmdXQQFjcwFzZHBzLXIsc2RwcC13LHNkcHMtdyxhdWN0LXIsYXVjdC13LG1icgF0aXABY252YWJD; path=/; domain=.yahoo.com; HttpOnly";
    private String wssid = "irUe0dfbWW7";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //普通的Get请求
        //demo1();
        //post 发送json请求
        demo2();

    }

    public class CustomInterceptor implements Interceptor {

        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            return null;
        }
    }

    private void demo2() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(genericClient())
                .build();
        List<LikesItem> likeObjects = new ArrayList<>();
        likeObjects.add(new LikesItem("193284"));
        likeObjects.add(new LikesItem("178548"));
        Log.d("network", "param:" + new Gson().toJson(likeObjects));
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(likeObjects));
        retrofit.create(NetEngine.class).postLikeItem(body)
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
