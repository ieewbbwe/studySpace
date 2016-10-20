package com.webber.retorfitdemo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        retrofit = new Retrofit.Builder()
                .baseUrl("https://hk.secure.shop.yahoo.com/api/m/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(genericClient())
                .build();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(v.getContext(), "snack click", Toast.LENGTH_SHORT).show();
                            }
                        }).show();
            }
        });

        initData();
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
        return new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("Cookie", "Y=v=1&n=588s97encs6d9&l=a26x2n6jf6x1x02oslxkjawuib05guai8ispus1a/o&p=m2r0000012000000&iz=&r=10d&lg=zh-Hant-HK&intl=hk&np=1; path=/; domain=.yahoo.com;T=z=vwtBYBv2CCYBhb/wzSnT8yANk40NgY2N041NjU1TjU0NDIzNE&a=QAE&sk=DAAB15zIRI9/EM&ks=EAAXgJyCCpKpcqf5ZDfzEFGiQ--~E&kt=EAASfzR6iynIGQxJhPiUY3lyw--~F&d=c2wBTVRrek1RRXhNRGt5TVRJeU9USXpNelUwTXprd01Uay0BYQFRQUUBZwFBSVdaQkRSR1VPNFdQQUpSVFhJUjdCUEwzTQFzY2lkAThtcDZkM3REMHEySWk1OVZfeWdaVzViSzJQVS0BYWMBQUEyS01nRVBEZVlubG9nLQFhbAFpZXdlYmJlcgFzYwF5aGtkZWFsc2J1eWVyAXp6AXZ3dEJZQmdXQQFjcwFzZHBzLXIsc2RwcC13LHNkcHMtdyxhdWN0LXIsYXVjdC13LG1icgF0aXABY252YWJD; path=/; domain=.yahoo.com; HttpOnly")
                                .addHeader("X-YahooWSSID-Authorization", "Mt538HeQ42i")
                                .build();
                        return chain.proceed(request);
                    }
                })
                .build();
    }
}
