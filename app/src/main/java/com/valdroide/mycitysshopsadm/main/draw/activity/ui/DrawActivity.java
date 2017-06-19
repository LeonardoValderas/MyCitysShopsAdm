package com.valdroide.mycitysshopsadm.main.draw.activity.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.valdroide.mycitysshopsadm.R;
import com.valdroide.mycitysshopsadm.main.draw.Communicator;
import com.valdroide.mycitysshopsadm.main.draw.activity.adapters.SectionsPagerAdapter;
import com.valdroide.mycitysshopsadm.main.draw.fragments.draw.ui.DrawFragment;
import com.valdroide.mycitysshopsadm.main.draw.fragments.draw_list.ui.DrawListFragment;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DrawActivity extends AppCompatActivity implements Communicator {

    @Bind(R.id.tabs)
    TabLayout tabs;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    @Bind(R.id.main_content)
    CoordinatorLayout mainContent;

    @Inject
    SectionsPagerAdapter adapter;

    private static DrawActivity drawActivityRunningInstance;

    public static DrawActivity getInstace() {
        return drawActivityRunningInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
        ButterKnife.bind(this);
        setupInjection();
        setupNavigation();
        initToolBar();
        drawActivityRunningInstance = this;
    }

    private void initToolBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.draw_title);
    }

    private void setupNavigation() {
        viewPager.setAdapter(adapter);
        tabs.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                hideKeyBoard();
            }

            @Override
            public void onPageSelected(int position) {
                hideKeyBoard();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void hideKeyBoard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void setupInjection() {
        String[] titles = new String[]{getString(R.string.create_draw_fragment),
                getString(R.string.close_draw_fragment)};
        Fragment[] fragments = new Fragment[]{new DrawFragment(),
                new DrawListFragment()};
        adapter = new SectionsPagerAdapter(getSupportFragmentManager(), titles, fragments);
    }

    @Override
    public void refresh(boolean isBroadcast) {
        getDrawListFragment().refresh(isBroadcast);
    }

    public DrawListFragment getDrawListFragment() {
        FragmentManager manager = getSupportFragmentManager();
        DrawListFragment fragment = (DrawListFragment) manager
                .findFragmentByTag("android:switcher:" + viewPager.getId()
                        + ":" + 1);
        return fragment;
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            finish();
//            return false;
//        }
//        return false;
//    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if(android.Home)
//        return true;
//    }
}
