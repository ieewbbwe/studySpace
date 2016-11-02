package com.webber.myapplication;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android_mobile.core.adapter.OnItemChildClickListener;
import com.android_mobile.core.adapter.OnItemChildLongClickListener;
import com.android_mobile.core.adapter.OnRVItemClickListener;
import com.android_mobile.core.base.BaseActivity;
import com.android_mobile.core.enums.ModalDirection;
import com.android_mobile.core.ui.comp.pullListView.ListViewComponent;
import com.android_mobile.core.ui.listener.IMediaPicturesListener;
import com.android_mobile.core.utiles.BitmapUtils;
import com.android_mobile.core.utiles.Lg;
import com.android_mobile.core.utiles.TimerUtils;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;


public class MainActivity extends BaseActivity {

    private ListViewComponent loadComponent;
    private NormalRecycleAdapter normalRecycleAdapter;
    private List<BuyerInfoModel> buyerInfoModels;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigationBar.hidden();
        //navigationBar.hidden();
     /*   Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        if (Build.VERSION.SDK_INT > 21) {
            toolbar.setOutlineProvider(ViewOutlineProvider.BOUNDS);
        }*/

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    @Override
    protected void initComp() {
        Lg.print("webber", "timeUtileTest:" + TimerUtils.formatTime(System.currentTimeMillis()));
        loadComponent = new ListViewComponent(this, findViewById(R.id.home_list_fl));
        normalRecycleAdapter = new NormalRecycleAdapter(loadComponent.getRecycleView());
        loadComponent.setAdapter(normalRecycleAdapter);

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
                imageView.setBackground(new BitmapDrawable(BitmapUtils.processBitmapBlur(BitmapUtils.obtainBitmap(imagePath))));
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
                    Toast.makeText(MainActivity.this,"成功",Toast.LENGTH_SHORT).show();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Toast.makeText(MainActivity.this,"失败",Toast.LENGTH_SHORT).show();

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

        loadComponent.setListener(new BGARefreshLayout.BGARefreshLayoutDelegate() {
            @Override
            public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadComponent.endRefresh();
                        normalRecycleAdapter.clear();
                        normalRecycleAdapter.addNewData(buyerInfoModels);
                    }
                }, 2000);

            /*    async(new IBasicAsyncTask() {
                    @Override
                    public void callback(Object result) {
                        loadComponent.endRefresh();
                        if (result != null) {
                            User user = (User) result;
                            //normalRecycleAdapter.addItem(0, user.purchaseInfo);
                            normalRecycleAdapter.clear();
                            normalRecycleAdapter.addNewData(buyerInfoModels);
                            Lg.print("webber", "user:" + user.toString());
                        }
                    }
                }, new BaseRequest(), new PersionInfoService());*/
            }

            @Override
            public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
                if (normalRecycleAdapter.getItemCount() >= 20) {
                    return false;
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadComponent.endLoadMore();
                        normalRecycleAdapter.addMoreData(buyerInfoModels);
                    }
                }, 2000);
                return true;
            }
        });
    }

    @Override
    protected void initData() {
        loadComponent.doRefresh();
        buyerInfoModels = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            buyerInfoModels.add(new BuyerInfoModel("buyerFirstName" + i, "buyerLastName" + i));
        }
    }


}
