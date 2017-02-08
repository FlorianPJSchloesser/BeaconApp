package bell.beaconapp;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import bell.beaconapp.ui.adapter.ContentPagerAdapter;
import bell.beaconapp.ui.views.LockableViewPager;

public class MainActivity extends AppCompatActivity {

    /* UI */
    BottomNavigationView mBottomNavigation;
    LockableViewPager mContentPager;
    Toolbar mAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        assignViews();
        prepareAppBar();
        prepareBottomNavigation();
        preparePager();
    }

    private void prepareAppBar() {
        if (mAppBar != null) {
            setSupportActionBar(mAppBar);
        }
    }

    private void prepareBottomNavigation () {
        mBottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_scan:
                        mContentPager.setCurrentItem(0, true);
                        return true;
                    case R.id.action_emulate:
                        mContentPager.setCurrentItem(1, true);
                        return true;
                }
                return false;
            }
        });
    }

    private void preparePager () {
        PagerAdapter pagerAdapter = new ContentPagerAdapter(getSupportFragmentManager());
        mContentPager.setAdapter(pagerAdapter);
        mContentPager.setSwipeLocked(true);
    }

    private void assignViews () {
        mBottomNavigation = (BottomNavigationView) findViewById(R.id.main_bottom_navigation);
        mContentPager = (LockableViewPager) findViewById(R.id.main_view_pager);
        mAppBar = (Toolbar) findViewById(R.id.main_appbar);
    }
}
