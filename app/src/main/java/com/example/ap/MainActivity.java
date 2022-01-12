package com.example.ap;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.Switch;


//android:theme="@style/AppTheme.NoActionBar" -----> "AndroidManifest"

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private WebView webView;
    private SwitchCompat myswitch;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        if (AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES)
        {
            setTheme(R.style.DarkTheme);
        }
        else setTheme(R.style.AppTheme);



        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
       setSupportActionBar(toolbar);


/////////////////////////////////////////////////////////////////////

        webView = (WebView) findViewById(R.id.Web);
        webView.setWebChromeClient(new MyChrome());
        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setSaveFormData(true);


/////////////////////////////////////////////////////////////////////

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();


/////////////////////////////////////////////////////////////////////
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu menu_nav=navigationView.getMenu();
        MenuItem item=menu_nav.findItem(R.id.nav_dark_theme);
        myswitch = (SwitchCompat) item.getActionView().findViewById(R.id.mswitch);




        if (AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES)
        {

            myswitch.setChecked(true);
        }

        myswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    restartApp();
                }
                else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    restartApp();
                }
            }
        });


        if (savedInstanceState == null)
        {
            webView.loadUrl("https://www.google.com/?gws_rd=ssl");
        }

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_google:
                webView.loadUrl("https://www.google.com/?gws_rd=ssl");
                break;

            case R.id.nav_youtube:
                webView.loadUrl("https://www.youtube.com");
                break;

            case R.id.nav_youtube_music:
                webView.loadUrl("https://music.youtube.com");
                break;

            case R.id.nav_Git:
                webView.loadUrl("https://github.com/MyroslavP?tab=repositories");
                break;

            case R.id.nav_samsung:
                webView.loadUrl("https://www.samsung.com/pl/");
                break;

            }
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }




    private class MyChrome extends WebChromeClient {

        private View CustomView;
        private WebChromeClient.CustomViewCallback CustomViewCallback;
        private int OriginalOrientation;
        private int OriginalSystemUiVisibility;

        MyChrome() {}

        public Bitmap getDefaultVideoPoster()
        {
            if (CustomView == null) {
                return null;
            }
            return BitmapFactory.decodeResource(getApplicationContext().getResources(), 2130837573);
        }

        public void onHideCustomView()
        {
            findViewById(R.id.toolbar).setVisibility(View.VISIBLE);
            ((FrameLayout)getWindow().getDecorView()).removeView(this.CustomView);
            this.CustomView = null;
            getWindow().getDecorView().setSystemUiVisibility(this.OriginalSystemUiVisibility);
            setRequestedOrientation(this.OriginalOrientation);
            this.CustomViewCallback.onCustomViewHidden();
            this.CustomViewCallback = null;
        }

        public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback)
        {
            if (this.CustomView != null)
            {
                onHideCustomView();
                return;
            }
            findViewById(R.id.toolbar).setVisibility(View.GONE);
            this.CustomView = paramView;
            this.OriginalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
            this.OriginalOrientation = getRequestedOrientation();
            this.CustomViewCallback = paramCustomViewCallback;
            ((FrameLayout)getWindow().getDecorView()).addView(this.CustomView, new FrameLayout.LayoutParams(-1, -1));
            getWindow().getDecorView().setSystemUiVisibility
                    (3846
                    |View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    |View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    |View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        webView.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        webView.restoreState(savedInstanceState);
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(Gravity.START)) {
            drawer.closeDrawer(Gravity.START);
        } else {
            if (webView.canGoBack()) {
                webView.goBack();
            }
        }
    }
    public void restartApp (){
        Intent i= new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
        finish();
    }
}
