package com.valdroide.mycitysshopsadm.main.offer.activities.ui;

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
import com.valdroide.mycitysshopsadm.main.draw.fragments.draw_list.ui.DrawListFragment;
import com.valdroide.mycitysshopsadm.main.offer.Communicator;
import com.valdroide.mycitysshopsadm.main.offer.activities.adapters.SectionsPagerAdapter;
import com.valdroide.mycitysshopsadm.main.offer.fragments.offer.ui.OfferFragment;
import com.valdroide.mycitysshopsadm.main.offer.fragments.offer_list.ui.OfferListFragment;
import com.valdroide.mycitysshopsadm.utils.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class OfferActivity extends AppCompatActivity implements Communicator {

    @Bind(R.id.tabs)
    TabLayout tabs;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    @Bind(R.id.main_content)
    CoordinatorLayout mainContent;

    SectionsPagerAdapter adapter;

    private static OfferActivity drawActivityRunningInstance;

    public static OfferActivity getInstace() {
        return drawActivityRunningInstance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
        ButterKnife.bind(this);
        setPagerAdapter();
        setupNavigation();
        initToolBar();
        drawActivityRunningInstance = this;
    }

    private void initToolBar() {
        Utils.writelogFile(this, "initToolBar(OfferActivity)");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.offer_title);
    }

    private void setupNavigation() {
        Utils.writelogFile(this, "setupNavigation(OfferActivity)");
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
        Utils.writelogFile(this, "hideKeyBoard(OfferActivity)");
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void setPagerAdapter() {
        Utils.writelogFile(this, "setPagerAdapter(OfferActivity)");
        String[] titles = new String[]{getString(R.string.create_draw_fragment),
                getString(R.string.edit_offer)};
        Fragment[] fragments = new Fragment[]{new OfferFragment(),
                new OfferListFragment()};
        adapter = new SectionsPagerAdapter(getSupportFragmentManager(), titles, fragments);
    }

    @Override
    public void refresh() {
        Utils.writelogFile(this, "refresh(OfferActivity)");
        getOfferListFragment().refresh();
    }

    public OfferListFragment getOfferListFragment() {
        Utils.writelogFile(this, "getOfferListFragment(OfferActivity)");
        FragmentManager manager = getSupportFragmentManager();
        OfferListFragment fragment = (OfferListFragment) manager
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
