package com.example.balakrishnan.mybrowser;

import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class AboutActivity extends AppCompatActivity {

    ImageView backgroundIV;

    Typeface regular,bold;
    FontChanger regularFontChanger,boldFontChanger;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_about);
        init();

        loadBackgroundImage();
        regularFontChanger.replaceFonts((ViewGroup)this.findViewById(android.R.id.content));

    }

    public void init(){
        backgroundIV = findViewById(R.id.backgroundIV);
        backgroundIV.animate().alpha(0).start();
        regular = Typeface.createFromAsset(getAssets(), "fonts/product_san_regular.ttf");
        bold = Typeface.createFromAsset(getAssets(),"fonts/product_sans_bold.ttf");
        regularFontChanger = new FontChanger(regular);
        boldFontChanger = new FontChanger(bold);
    }

    public void loadBackgroundImage(){


        final Handler handler = new Handler();
        AboutActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Picasso.with(getApplicationContext())
                        .load("https://source.unsplash.com/collection/319663")
                        .skipMemoryCache()
                        .into(backgroundIV, new Callback() {
                            @Override
                            public void onSuccess() {

                                Animation zoomin= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoomin);
                                zoomin.setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {
                                        backgroundIV.animate().alpha(1).setDuration(2000).start();
                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        backgroundIV.animate().alpha(0).setDuration(2000).start();
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {

                                    }
                                });
                                backgroundIV.setAnimation(zoomin);
                                backgroundIV.startAnimation(zoomin);

                            }


                            @Override
                            public void onError() {
                                Toast.makeText(getApplicationContext(),"No internet!",Toast.LENGTH_LONG).show();
                            }
                        });

                handler.postDelayed(this,20000);
            }
        });

    }
}
