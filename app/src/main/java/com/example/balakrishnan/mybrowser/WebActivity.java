package com.example.balakrishnan.mybrowser;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class WebActivity extends AppCompatActivity {


    ImageView backgroundIV,sendIV;
    Typeface regular,bold;
    FontChanger regularFontChanger,boldFontChanger;
    EditText urlET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        getSupportActionBar().hide();
        init();
        loadBackgroundImage();
        regularFontChanger.replaceFonts((ViewGroup)this.findViewById(android.R.id.content));

        urlET.setText(getIntent().getStringExtra("url"));

    }

    public void init(){
        backgroundIV = findViewById(R.id.backgroundIV);
        backgroundIV.setDrawingCacheEnabled(true);
        sendIV = findViewById(R.id.sendIV);
        regular = Typeface.createFromAsset(getAssets(), "fonts/product_san_regular.ttf");
        bold = Typeface.createFromAsset(getAssets(),"fonts/product_sans_bold.ttf");
        regularFontChanger = new FontChanger(regular);
        boldFontChanger = new FontChanger(bold);
        //Changing the font throughout the activity

        urlET = findViewById(R.id.urlET);

    }

    public void loadBackgroundImage(){


        final Handler handler = new Handler();
        WebActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Picasso.with(getApplicationContext())
                        .load("https://source.unsplash.com/random")
                        .skipMemoryCache().error(R.drawable.nointernet)
                        .into(backgroundIV, new Callback() {
                            @Override
                            public void onSuccess() {
                                if(backgroundIV.getDrawingCache()!=null){
                                    //Changing the color of send icon
                                    sendIV.setColorFilter(getDominantColor(backgroundIV.getDrawingCache()));
                                }
                            }

                            @Override
                            public void onError() {

                            }
                        });

                handler.postDelayed(this,30000);
            }
        });
    }

    //Getting dominant color from wallpaper
    public static int getDominantColor(Bitmap bitmap) {
        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, 1, 1, true);
        final int color = newBitmap.getPixel(0, 0);
        newBitmap.recycle();
        return color;
    }

    @Override
    public void onBackPressed() {
        //To support reverse transitions when user clicks the device back button
        supportFinishAfterTransition();

    }
}
