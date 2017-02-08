package bell.beaconapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
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

    private final static int REQUEST_LOCATION_PERMISSION = 289374646;

    /* UI */
    BottomNavigationView mBottomNavigation;
    LockableViewPager mContentPager;
    Toolbar mAppBar;

    boolean mInit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            init();
        }
    }

    private void init() {
        if (mInit) return;
        assignViews();
        prepareAppBar();
        prepareBottomNavigation();
        preparePager();
        mInit = true;
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    init();

                } else {

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
