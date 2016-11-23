package com.webber.myapplication;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android_mobile.core.adapter.OnItemChildClickListener;
import com.android_mobile.core.adapter.OnItemChildLongClickListener;
import com.android_mobile.core.adapter.OnRVItemClickListener;
import com.android_mobile.core.base.BaseActivity;
import com.android_mobile.core.enums.ModalDirection;
import com.android_mobile.core.helper.image.ImageLoadFactory;
import com.android_mobile.core.ui.EmptyLayout;
import com.android_mobile.core.ui.comp.pullListView.ILoadMoreViewListener;
import com.android_mobile.core.ui.comp.pullListView.ListViewComponent;
import com.android_mobile.core.ui.listener.IMediaPicturesListener;
import com.android_mobile.core.utiles.BitmapUtils;
import com.android_mobile.core.utiles.Lg;
import com.android_mobile.core.utiles.TimerUtils;
import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.bumptech.glide.Glide;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity {

    private ListViewComponent loadComponent;
    private NormalRecycleAdapter normalRecycleAdapter;
    private List<BuyerInfoModel> buyerInfoModels;
    private ImageView imageView;
    private BottomNavigationBar bottomBar;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawable_main);
        //setTitle("demo1");
        navigationBar.hidden();
        initBottomView();
        initSnackBar();
        initSlidMenu();
    }

    private void initSlidMenu() {
    /*    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();*/

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_camera:
                    case R.id.nav_gallery:
                    case R.id.nav_manage:
                    case R.id.nav_send:
                    case R.id.nav_share:
                    case R.id.nav_view:
                    case R.id.nav_slideshow:
                        Snackbar.make(getWindow().getDecorView(), item.getTitle(), Snackbar.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
    }

    private void initSnackBar() {
      /*  toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void initBottomView() {
        bottomBar = (BottomNavigationBar) findViewById(R.id.bottom_bar);
        BadgeItem badgeItem = new BadgeItem().setBackgroundColor(Color.RED).setText("99").setGravity(Gravity.RIGHT);
        BottomNavigationItem item = new BottomNavigationItem(R.mipmap.ic_launcher, "item1");
        item.setBadgeItem(badgeItem);

        bottomBar.setMode(BottomNavigationBar.MODE_FIXED)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE)
                .addItem(new BottomNavigationItem(R.drawable.home_radio_bt_fav, "最新动态"))
                .addItem(new BottomNavigationItem(R.drawable.home_radio_bt_recent, "我的最爱"))
                .addItem(new BottomNavigationItem(R.drawable.home_radio_bt_recent, "我的最爱"))
                .addItem(new BottomNavigationItem(R.drawable.home_radio_bt_recent, "我的最爱"))
                .initialise();
        bottomBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                Log.d("bottomBar", "onTabSelected" + position);
            }

            @Override
            public void onTabUnselected(int position) {
                Log.d("bottomBar", "onTabUnselected" + position);
            }

            @Override
            public void onTabReselected(int position) {
                Log.d("bottomBar", "onTabReselected" + position);
            }
        });

    }

    @Override
    protected void initComp() {
        Lg.print("webber", "timeUtileTest:" + TimerUtils.formatTime(System.currentTimeMillis()));
        loadComponent = new ListViewComponent(this, findViewById(R.id.home_list_fl));
        normalRecycleAdapter = new NormalRecycleAdapter(loadComponent.getRecycleView());
        loadComponent.setAdapter(normalRecycleAdapter);
        //loadComponent.setLoadMoreEnable(false);

        imageView = (ImageView) findViewById(R.id.chose_pic_iv);
        findViewById(R.id.chose_pic_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushModalView(showPictureComponent(), ModalDirection.BOTTOM, dip2px(150));
            }
        });

        setMediaPictureListener(new IMediaPicturesListener() {
            @Override
            public void mediaPicturePath(String imagePath) {
                Lg.print("webber", "img:" + imagePath);
                Lg.print("webber", "degree:" + BitmapUtils.obtainBitmapDegree(imagePath));
                //UniversalImageLoad.getInstance().displayImage(imagePath, imageView);
                ImageLoadFactory.getInstance().getImageLoadHandler().displayImage(MainActivity.this, imagePath, imageView);
                Glide.with(MainActivity.this).load(imagePath).crossFade()
                        //.bitmapTransform(new CropCircleTransformation(MainActivity.this))
                        .into(imageView);
                //imageView.setBackground(new BitmapDrawable(BitmapUtils.processBitmapBlur(BitmapUtils.obtainBitmap(imagePath))));
            }
        });

        findViewById(R.id.dialog_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog
                        .Builder(MainActivity.this, R.style.MyAlertDialogStyle)
                        .setIcon(R.mipmap.ic_launcher)
                        .setTitle("AppCompatDialog")
                        .setMessage("Lorem ipsum dolor...")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.M)
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String[] strs = {Manifest.permission.READ_SMS};
                                ActivityCompat.requestPermissions(MainActivity.this, strs, 100);
                                //AndPermission.with(MainActivity.this).requestCode(100).permission(Manifest.permission.READ_SMS).send();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                builder.show();
            }
        });

        findViewById(R.id.dialog_my_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppDialog appDialog = new AppDialog(MainActivity.this);
                appDialog.show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // 只需要调用这一句，剩下的AndPermission自动完成。
        switch (requestCode) {
            case 100: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "成功", Toast.LENGTH_SHORT).show();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Toast.makeText(MainActivity.this, "失败", Toast.LENGTH_SHORT).show();

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
        //AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    // 成功回调的方法，用注解即可，里面的数字是请求时的requestCode。
    @PermissionYes(100)
    private void getLocationYes() {
        // 申请权限成功，可以去做点什么了。
        Toast.makeText(this, "获取定位权限成功", Toast.LENGTH_SHORT).show();
    }

    // 失败回调的方法，用注解即可，里面的数字是请求时的requestCode。
    @PermissionNo(100)
    private void getLocationNo() {
        // 申请权限失败，可以提醒一下用户。
        Toast.makeText(this, "获取定位权限失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void initListener() {
        normalRecycleAdapter.setOnItemChildLongClickListener(new OnItemChildLongClickListener() {
            @Override
            public boolean onItemChildLongClick(ViewGroup parent, View childView, int position) {
                Lg.print("webber", "onItemChildLongClick:" + position);
                return true;
            }
        });
        normalRecycleAdapter.setOnRVItemClickListener(new OnRVItemClickListener() {
            @Override
            public void onRVItemClick(ViewGroup parent, View itemView, int position) {
                Lg.print("webber", "onRVItemClick:" + position);

            }
        });
        normalRecycleAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(ViewGroup parent, View childView, int position) {
                switch (childView.getId()) {
                    case R.id.tv_item_normal_delete:
                        childView.setClickable(false);
                        normalRecycleAdapter.removeItem(position);
                        break;
                }
            }
        });

        loadComponent.setListener(new ILoadMoreViewListener() {
            @Override
            public void startRefresh() {
                mErrorLl.setErrorType(EmptyLayout.STATE_NETWORK_LOADING);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mErrorLl.setErrorType(EmptyLayout.STATE_HIDE_LAYOUT);
                        loadComponent.endRefresh();
                        normalRecycleAdapter.clear();
                        normalRecycleAdapter.addNewData(buyerInfoModels);
                    }
                }, 2000);
            }

            @Override
            public void startLoadMore() {
                if (normalRecycleAdapter.getItemCount() >= 20) {
                    Snackbar.make(loadComponent.getRecycleView(), "没有更多了", Snackbar.LENGTH_SHORT).show();
                    mErrorLl.setErrorType(EmptyLayout.STATE_NODATA_ENABLE_CLICK);
                    loadComponent.setLoadMoreEnable(false);
                    return;
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadComponent.endLoadMore();
                        normalRecycleAdapter.addMoreData(buyerInfoModels);
                    }
                }, 2000);
            }
        });
    }

    @Override
    protected void initData() {
        mErrorLl.setErrorType(EmptyLayout.STATE_NETWORK_LOADING);
        loadComponent.doRefresh();
        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.hsv).setAlpha(0.89f);

            }
        }, 1000);*/

        buyerInfoModels = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            buyerInfoModels.add(new BuyerInfoModel("buyerFirstName" + i, "buyerLastName" + i, arr[i]));
        }
    }

    Integer[] arr = {R.mipmap.bga_refresh_loading01,
            R.mipmap.ic_ctn_pgdot_orange,
            R.mipmap.search_icon,
            R.mipmap.ic_nav_new_normal,
            R.mipmap.navigation_bar_bg};
}
