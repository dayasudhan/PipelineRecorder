package com.kuruvatech.pipelinerecorder;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.kuruvatech.pipelinerecorder.fragments.CameraFragment;
import com.kuruvatech.pipelinerecorder.fragments.LogoutFragment;
import com.kuruvatech.pipelinerecorder.fragments.MainFragment;
import com.kuruvatech.pipelinerecorder.fragments.MultiLineFragment;
import com.kuruvatech.pipelinerecorder.fragments.Settingfragment;
import com.kuruvatech.pipelinerecorder.fragments.ShareAppFragment;
import com.kuruvatech.pipelinerecorder.fragments.SingleLineFragment;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.kuruvatech.pipelinerecorder.utils.SessionManager;
import com.splunk.mint.Mint;
public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {
    private RelativeLayout layout;
    private DrawerLayout dLayout;
    //SessionManager session;
    RelativeLayout navHead;
    TextView name,email,phno;
    private boolean isMainFragmentOpen;
    private boolean isdrawerbackpressed;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private boolean fromUser=true;
    ViewPagerAdapter adapter;
    Toolbar tb;
    SessionManager session;
    private ProgressDialog mProgressDialog;
    public boolean isOnline(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            Toast.makeText(context, "No Internet connection!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mint.initAndStartSession(this.getApplication(), "4c38f221");
        session = new SessionManager(getApplicationContext());
        session.checkLogin();
//        {
//            startService(new Intent(this, NotificationListener.class));
//        }
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
//
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(this);
        if (!isOnline(MainActivity.this))
        {
            try {
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();

                alertDialog.setTitle("Info");
                alertDialog.setMessage(getString(R.string.internet_not_available));
                alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //    finish();

                    }
                });

                alertDialog.show();
            }
            catch(Exception e)
            {

            }
        }

        setNavigationDrawer();
        setToolBar();
        isMainFragmentOpen = true;
        isdrawerbackpressed = false;
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MainFragment(), getString(R.string.home));
        adapter.addFragment(new MultiLineFragment(), getString(R.string.map));
        adapter.addFragment(new SingleLineFragment(), getString(R.string.myline));
        adapter.addFragment(new CameraFragment(), getString(R.string.camera));
        adapter.addFragment(new ShareAppFragment(), getString(R.string.share));
        adapter.addFragment(new Settingfragment(), getString(R.string.settings));
        adapter.addFragment(new LogoutFragment(), getString(R.string.logout));
        viewPager.setAdapter(adapter);
    }
    private void setToolBar() {
        tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.mipmap.ic_menu_selector);
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         int id = item.getItemId();

        switch(id) {

            case android.R.id.home: {
                dLayout.openDrawer(GravityCompat.START);
                return true;
            }
            case R.id.action_settings: {
               // dLayout.openDrawer(GravityCompat.START);
                return true;
            }

        }
        return super.onOptionsItemSelected(item);
    }

    private void setNavigationDrawer() {
        dLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView) findViewById(R.id.navigation);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        navView.setItemIconTintList(null);
        View hView =  navView.inflateHeaderView(R.layout.header);
        navHead = (RelativeLayout) hView.findViewById(R.id.profileinfo);

        isMainFragmentOpen =  true;
        transaction.commit();
        fromUser=false;

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                Fragment frag = null;

                int itemId = menuItem.getItemId();
                if (itemId == R.id.main) {
                    viewPager.setCurrentItem(0);
                    isMainFragmentOpen =  true;
                }
                else if (itemId == R.id.myline) {
                    viewPager.setCurrentItem(2);
                    isMainFragmentOpen =  false;
                }
                else if (itemId == R.id.multiline) {
                    viewPager.setCurrentItem(1);
                    isMainFragmentOpen =  false;
                }
                else if(itemId == R.id.camera)
                {
                    viewPager.setCurrentItem(3);
                    //frag = new AboutFragment();
                    isMainFragmentOpen =  false;
                }
                else if(itemId == R.id.share)
                {
                    viewPager.setCurrentItem(4);
                    //frag = new AboutFragment();
                    isMainFragmentOpen =  false;
                }
                else if (itemId == R.id.settings) {
                    viewPager.setCurrentItem(5);
                    isMainFragmentOpen =  false;
                }
                else if (itemId == R.id.logout) {
                    viewPager.setCurrentItem(5);
                    isMainFragmentOpen =  false;
                }
                dLayout.closeDrawers();
                return true;
            }
        });
    }
    private void setTheme(int themecolor,int toolbacolor,int statusbarcolor)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
        {
            tb.setBackground(getResources().getDrawable(toolbacolor));
            tabLayout.setBackground(getResources().getDrawable(themecolor));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(statusbarcolor);
        }
    }
    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        if(tab.getPosition() == 0)
        {
            setTheme(R.color.background,R.color.toolbar,
                    Color.parseColor("#"+ Integer.toHexString(ContextCompat.getColor(getApplicationContext(), R.color.background))));
        }
        else if(tab.getPosition() == 1)
        {
            //  getActionBar().setTitle();
            setTheme(R.color.Amber,R.color.amber_tool_bar_color,
                    Color.parseColor("#"+ Integer.toHexString(ContextCompat.getColor(getApplicationContext(), R.color.Amber))));
        }
        else if(tab.getPosition() == 2)
        {
            setTheme(R.color.grapefruit2,R.color.grapefruit1,
                    Color.parseColor("#"+ Integer.toHexString(ContextCompat.getColor(getApplicationContext(), R.color.grapefruit2))));
        }
        else if(tab.getPosition() == 3)
        {
            setTheme(R.color.bluejeans2,R.color.bluejeans1,
                    Color.parseColor("#"+ Integer.toHexString(ContextCompat.getColor(getApplicationContext(), R.color.bluejeans2))));
        }
        else if(tab.getPosition() == 4)
        {
            setTheme(R.color.pinkrose2,R.color.pinkrose1,
                    Color.parseColor("#"+ Integer.toHexString(ContextCompat.getColor(getApplicationContext(), R.color.pinkrose2))));
        }
        else if(tab.getPosition() == 5)
        {
            setTheme(R.color.lavender2,R.color.lavender1,
                    Color.parseColor("#"+ Integer.toHexString(ContextCompat.getColor(getApplicationContext(), R.color.lavender2))));
        }
        else if(tab.getPosition() == 6)
        {
            setTheme(R.color.sunflower2,R.color.sunflower1,
                    Color.parseColor("#"+ Integer.toHexString(ContextCompat.getColor(getApplicationContext(), R.color.sunflower2))));
        }
        else if(tab.getPosition() ==7)
        {
            setTheme(R.color.mint2,R.color.mint1,
                    Color.parseColor("#"+ Integer.toHexString(ContextCompat.getColor(getApplicationContext(), R.color.mint2))));
        }
        else if(tab.getPosition() ==8)
        {
            setTheme(R.color.bittersweet2,R.color.bittersweet1,
                    Color.parseColor("#"+ Integer.toHexString(ContextCompat.getColor(getApplicationContext(), R.color.bittersweet2))));
        }
        else if(tab.getPosition() ==0)
        {
            setTheme(R.color.Cobalt,R.color.NavyBlue,
                    Color.parseColor("#"+ Integer.toHexString(ContextCompat.getColor(getApplicationContext(), R.color.Cobalt))));
        }
        else if(tab.getPosition() ==10)
        {
            setTheme(R.color.sunflower2,R.color.sunflower1,
                    Color.parseColor("#"+ Integer.toHexString(ContextCompat.getColor(getApplicationContext(), R.color.sunflower2))));
        }
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (dLayout.isDrawerOpen(GravityCompat.START)) {
            dLayout.closeDrawer(GravityCompat.START);
        } else if (isMainFragmentOpen == false) {
            if(!isdrawerbackpressed) {
                dLayout.openDrawer(GravityCompat.START);
                isdrawerbackpressed = true;
            }
        } else {
            //super.onBackPressed();
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }
    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
    private static boolean isEnglish = true;

    private void refreshUI() {
        mProgressDialog.show();
        recreate();
        mProgressDialog.hide();
    }
    public void setLocale(String lang) { //call this in onCreate()
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(getApplicationContext(), "Camera main activity  onActivityResult", Toast.LENGTH_LONG).show();
    }
}
