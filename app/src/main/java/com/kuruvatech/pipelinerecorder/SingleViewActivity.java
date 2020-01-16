package com.kuruvatech.pipelinerecorder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
//import com.google.android.gms.ads.MobileAds;
import com.kuruvatech.pipelinerecorder.utils.Constants;
import com.kuruvatech.pipelinerecorder.utils.ImageLoader;

import java.util.ArrayList;

public class SingleViewActivity extends AppCompatActivity {

    public ImageLoader imageLoader;
    Button btnBack;
  //  private AdView mAdView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_view);
//        MobileAds.initialize(this, Constants.ADMOBAPPID);
//        mAdView = findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
        imageLoader = new ImageLoader(getApplicationContext(),500,500);
      //  btnBack = (Button) findViewById(R.id.back_button);
        // Get intent data
        Intent i = getIntent();

        // Selected image id
        final String url = i.getExtras().getString("url");

        ImageView imageView = (ImageView) findViewById(R.id.SingleView);
        ImageView imageShareView = (ImageView) findViewById(R.id.imagesharebutton2);
        imageLoader.DisplayImage(url, imageView);
        imageShareView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent shareIntent = new Intent();

                ArrayList<Uri> imageUris = new ArrayList<Uri>();
                imageUris.add(Uri.parse(imageLoader.getFilePath(url)));
                shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
                shareIntent.setType("image/*");
                shareIntent.setAction(Intent.ACTION_SEND);
                startActivity(Intent.createChooser(shareIntent, "Share it ...."));
            }



        });
        setToolBar(getString(R.string.titletext));

    }
//    private void setToolBar(String areaClicked) {
//        Toolbar tb = (Toolbar) findViewById(R.id.toolbar2);
//        setSupportActionBar(tb);
//
//        ActionBar ab = getSupportActionBar();
//        ab.setHomeAsUpIndicator(R.mipmap.ic_back);
//        ab.setDisplayHomeAsUpEnabled(true);
//        ab.setTitle(areaClicked);
////        tb.setTitleTextColor(Color.rgb(Constants.TITLE_TEXT_COLOR_RED,
////                Constants.TITLE_TEXT_COLOR_GREEN, Constants.TITLE_TEXT_COLOR_BLUE));
//    }
    private void setToolBar(String areaClicked) {
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.mipmap.ic_back);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(areaClicked);
//        tb.setTitleTextColor(Color.rgb(Constants.TITLE_TEXT_COLOR_RED,
//                Constants.TITLE_TEXT_COLOR_GREEN, Constants.TITLE_TEXT_COLOR_BLUE));
    }
    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
