package com.example.balakrishnan.mybrowser;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class HomeActivity extends AppCompatActivity {

    ImageView backgroundIV,sendIV;
    Typeface regular,bold;
    FontChanger regularFontChanger,boldFontChanger;
    EditText urlET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();

        init();
        loadBackgroundImage();
        boldFontChanger.replaceFonts((ViewGroup)this.findViewById(android.R.id.content));
        sendIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,WebActivity.class);
                intent.putExtra("url",urlET.getText().toString().trim());
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(HomeActivity.this, urlET,urlET.getTransitionName());
                startActivity(intent,optionsCompat.toBundle());
            }
        });
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
        HomeActivity.this.runOnUiThread(new Runnable() {
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

    //To show keyboard
    public void showSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }

    //To  hide keyboard
    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }

    }
}