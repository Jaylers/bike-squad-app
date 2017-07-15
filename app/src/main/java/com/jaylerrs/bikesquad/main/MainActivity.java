package com.jaylerrs.bikesquad.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jaylerrs.bikesquad.R;
import com.jaylerrs.bikesquad.biking.BikingActivity;
import com.jaylerrs.bikesquad.events.fragment.MyPostsFragment;
import com.jaylerrs.bikesquad.events.fragment.MyTopPostsFragment;
import com.jaylerrs.bikesquad.events.fragment.RecentPostsFragment;
import com.jaylerrs.bikesquad.main.task.UserTask;
import com.jaylerrs.bikesquad.route.NewRouteActivity;
import com.jaylerrs.bikesquad.route.fragment.RecentRouteFragment;
import com.jaylerrs.bikesquad.route.fragment.TopRouteFragment;
import com.jaylerrs.bikesquad.settings.SettingsActivity;
import com.jaylerrs.bikesquad.users.ProfileActivity;
import com.jaylerrs.bikesquad.utility.dialog.DialogLoading;
import com.jaylerrs.bikesquad.utility.dialog.DialogNewsMessages;
import com.jaylerrs.bikesquad.utility.manager.ColorManager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private Activity activity;
    private GoogleApiClient mGoogleApiClient;
    private View mHeaderLayout;
    private Progressing progressing;
    private ActionBar actionBar;
    private Toolbar toolbar;

    private FragmentPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        activity = this;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),
                        getString(R.string.app_message_select_route),
                        Toast.LENGTH_LONG).show();
                route();
            }
        });

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        actionBar = getSupportActionBar();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        ColorManager colorManager = new ColorManager();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        mHeaderLayout = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        mHeaderLayout.setBackground(colorManager.getGradientDrawable(colorManager.parser("#FFA000")));
        actionBar.setBackgroundDrawable(colorManager.getColorDrawable(colorManager.parser("#FFA000")));

        UserTask userTask = new UserTask(mHeaderLayout, this);
        userTask.setUserProfile();

//        setNews();
        setOnClick();
        event();
    }

    private void setNews(){
        News news = new News(activity);
        news.show();
    }

    private void setOnClick(){
        mHeaderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    int t = 0;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
                if (t==0){
                    t++;
                    Toast.makeText(getApplicationContext(), getString(R.string.message_back_press),
                            Toast.LENGTH_SHORT).show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            t = 0;
                        }
                    }, 2500);
                }else if (t >= 1){
                    super.onBackPressed();
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_friends) {
            actionBar.setTitle(getString(R.string.menu_main_friends));
            friends();
        } else if (id == R.id.nav_event) {
            actionBar.setTitle(getString(R.string.menu_main_event));
            event();
        } else if (id == R.id.nav_route) {
            actionBar.setTitle(getString(R.string.menu_main_route));
            route();
        } else if (id == R.id.nav_bike) {
            actionBar.setTitle(getString(R.string.menu_main_Bike));
            bike();
        } else if (id == R.id.nav_settings) {
            settings();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void friends(){
        Intent intent = new Intent(MainActivity.this, NewRouteActivity.class);
        startActivity(intent);
//        mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
//            private final Fragment[] mFragments = new Fragment[] {
//                    new RecentPostsFragment(),
//                    new MyPostsFragment(),
//                    new MyTopPostsFragment(),
//            };
//            private final String[] mFragmentNames = new String[] {
//                    getString(R.string.menu_main_friends),
//            };
//            @Override
//            public Fragment getItem(int position) {
//                return mFragments[position];
//            }
//            @Override
//            public int getCount() {
//                return mFragments.length;
//            }
//            @Override
//            public CharSequence getPageTitle(int position) {
//                return mFragmentNames[position];
//            }
//        };
//        // Set up the ViewPager with the sections adapter.
//        mViewPager = (ViewPager) findViewById(R.id.container);
//        mViewPager.setAdapter(mPagerAdapter);
//        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
//        tabLayout.setupWithViewPager(mViewPager);
    }

    private void event(){
        mPagerAdapter = null;
        mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            private final Fragment[] mFragments = new Fragment[] {
                    new RecentPostsFragment(),
                    new MyPostsFragment(),
                    new MyTopPostsFragment(),
            };
            private final String[] mFragmentNames = new String[] {
                    getString(R.string.heading_recent),
                    getString(R.string.heading_my_posts),
                    getString(R.string.heading_my_top_posts)
            };
            @Override
            public Fragment getItem(int position) {
                return mFragments[position];
            }
            @Override
            public int getCount() {
                return mFragments.length;
            }
            @Override
            public CharSequence getPageTitle(int position) {
                return mFragmentNames[position];
            }
        };
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.route_container);
        mViewPager.setVisibility(View.GONE);
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setVisibility(View.VISIBLE);
        mViewPager.setAdapter(mPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void route(){

        mPagerAdapter = null;
        mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            private final Fragment[] mFragments = new Fragment[] {
                    new RecentRouteFragment(),
                    new TopRouteFragment(),
            };
            private final String[] mFragmentNames = new String[] {
                    getString(R.string.heading_route),
                    getString(R.string.heading_top_route)
            };
            @Override
            public Fragment getItem(int position) {
                return mFragments[position];
            }
            @Override
            public int getCount() {
                return mFragments.length;
            }
            @Override
            public CharSequence getPageTitle(int position) {
                return mFragmentNames[position];
            }
        };
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setVisibility(View.GONE);
        mViewPager = (ViewPager) findViewById(R.id.route_container);
        mViewPager.setVisibility(View.VISIBLE);
        mViewPager.setAdapter(mPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void bike(){
        Intent intent = new Intent(MainActivity.this, BikingActivity.class);
        startActivity(intent);
    }

    private void settings(){
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }



    private class Progressing extends DialogLoading{

        public Progressing(Activity activity, String message) {
            super(activity, message);
        }
    }

    public class News extends DialogNewsMessages {

        public News(Activity activity) {
            super(activity);
        }
    }
}
