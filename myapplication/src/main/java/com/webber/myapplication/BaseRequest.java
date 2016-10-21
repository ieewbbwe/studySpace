package com.webber.myapplication;


import com.android_mobile.core.net.http.ServiceRequest;

/**
 * Created by mxh on 2016/6/30.
 * 请求基类
 */
public class BaseRequest extends ServiceRequest {
    //方便開發 模擬首頁不需要登錄的場景
    public String cookie = "Y=v=1&n=588s97encs6d9&l=a26x2n6jf6x1x02oslxkjawuib05guai8ispus1a/o&p=m2r0000012000000&iz=&r=10d&lg=zh-Hant-HK&intl=hk&np=1; path=/; domain=.yahoo.com;T=z=ohcCYBonxCYBXWpoy.2G45vNk40NgY2N041NjU1TjU0NDIzNE&a=QAE&sk=DAARLPq70Ce32K&ks=EAABNnHkJksUkn.RDpU_nXEHA--~E&kt=EAAxtXEJIHEpBeTsOYlMZUG1A--~F&d=c2wBTVRrek1RRXhNRGt5TVRJeU9USXpNelUwTXprd01Uay0BYQFRQUUBZwFBSVdaQkRSR1VPNFdQQUpSVFhJUjdCUEwzTQFzY2lkATl1U0xsRTFuRDhyWHNNWDBZRnNRWUVDcHJDYy0BYWMBQUFYRTNhZl9mdVNNUEdVLQFhbAFpZXdlYmJlcgFzYwF5aGtkZWFsc2J1eWVyAXp6AW9oY0NZQmdXQQFjcwFzZHBzLXIsc2RwcC13LHNkcHMtdyxhdWN0LXIsYXVjdC13LG1icgF0aXABY252YWJD; path=/; domain=.yahoo.com; HttpOnly";
    public String wssid = "Nkm7BmRPee2";//SharedPrefManager.getWssid("com.yahoo.hkdeals" + CacheConstants.WSSID);

}
