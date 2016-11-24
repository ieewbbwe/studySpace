package com.webber.topnew;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.android_mobile.core.base.BaseActivity;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.webber.topnew.mine.MineFragment;
import com.webber.topnew.news.SlidNewsFragment;
import com.webber.topnew.topnews.TopNewsFragment;
import com.webber.topnew.video.VideoFragmnet;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @Bind(R.id.main_content_fl)
    FrameLayout mainContentFl;
    @Bind(R.id.bottom_navigation)
    BottomNavigationBar bottomNavigation;
    private FloatingActionButton mFab;
    private Toolbar mTollBar;
    private FragmentManager mFragmentManager;
    private String mCurrentFragmentTag;
    private int currentCheckedId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigationBar.setTitle(R.string.app_name);

    }

    @Override
    protected void initComp() {
        //super.initComp();
        ButterKnife.bind(this);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        bottomNavigation
                .setMode(BottomNavigationBar.MODE_FIXED)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE)
                .addItem(new BottomNavigationItem(R.mipmap.ic_launcher, R.string.label_bottom_new))
                .addItem(new BottomNavigationItem(R.mipmap.ic_nav_fav_orange, R.string.label_bottom_good))
                .addItem(new BottomNavigationItem(R.mipmap.search_icon, R.string.label_bottom_video))
                .addItem(new BottomNavigationItem(R.mipmap.ic_nav_new_orange, R.string.label_bottom_min))
                .initialise();
        bottomNavigation.setFab(mFab);
        toast(bottomNavigation.getCurrentSelectedPosition() + "");
        bottomNavigation.setFirstSelectedPosition(1);
        mFragmentManager = getSupportFragmentManager();
    }

    @Override
    protected void initListener() {
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        bottomNavigation.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                navigationBar.display();
                String tag = null;
                Fragment fragment = null;
                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                if (mCurrentFragmentTag != null) {
                    Fragment currentFragment = mFragmentManager.findFragmentByTag(mCurrentFragmentTag);
                    if (currentFragment != null) {
                        fragmentTransaction.hide(currentFragment);
                    }
                }
                switch (position) {
                    case 0:
                        currentCheckedId = 0;
                        navigationBar.display();
                        tag = TopNewsFragment.class.getSimpleName();
                        Fragment menu0 = mFragmentManager.findFragmentByTag(tag);
                        if (menu0 != null) {
                            fragment = menu0;
                        } else {
                            fragment = new TopNewsFragment();
                        }
                        break;
                    case 1:
                        currentCheckedId = 1;
                        navigationBar.display();
                        tag = SlidNewsFragment.class.getSimpleName();
                        Fragment menu1 = mFragmentManager.findFragmentByTag(tag);
                        if (menu1 != null) {
                            fragment = menu1;
                        } else {
                            fragment = new SlidNewsFragment();
                        }
                        break;
                    case 2:
                        currentCheckedId = 2;
                        navigationBar.display();
                        tag = VideoFragmnet.class.getSimpleName();
                        Fragment menu2 = mFragmentManager.findFragmentByTag(tag);
                        if (menu2 != null) {
                            fragment = menu2;
                        } else {
                            fragment = new VideoFragmnet();
                        }
                        break;
                    case 3:
                        currentCheckedId = 3;
                        navigationBar.display();
                        tag = VideoFragmnet.class.getSimpleName();
                        Fragment menu3 = mFragmentManager.findFragmentByTag(tag);
                        if (menu3 != null) {
                            fragment = menu3;
                        } else {
                            fragment = new MineFragment();
                        }
                        break;
                }
                if (fragment != null && fragment.isAdded()) {
                    fragmentTransaction.show(fragment);
                } else {
                    fragmentTransaction.add(R.id.main_content_fl, fragment, tag);
                }
                fragmentTransaction.commit();
                mCurrentFragmentTag = tag;
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }

    @Override
    protected void initData() {

    }

}
