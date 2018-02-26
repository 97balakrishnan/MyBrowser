package com.example.balakrishnan.mybrowser;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class WebActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{



    Typeface regular,bold;
    FontChanger regularFontChanger,boldFontChanger;
    EditText urlET;
    ImageView downloadIV,sendIV;
    WebView webView;
    public static SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_web);
        getSupportActionBar().hide();
        init();
        //Changing the font throughout the activity
        regularFontChanger.replaceFonts((ViewGroup)this.findViewById(android.R.id.content));

        urlET.setText(getIntent().getStringExtra("url"));
        loadURL(getIntent().getStringExtra("url"));

        urlET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                urlET.setSelectAllOnFocus(true);

            }
        });

        urlET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_SEND) {

                    String url=urlET.getText().toString().trim();
                    loadURL(url);
                    handled = true;
                }
                return handled;
            }
        });

        downloadIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v);
            }
        });

        sendIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url=urlET.getText().toString().trim();
                loadURL(url);

            }
        });

    }

    public void init(){


        downloadIV = findViewById(R.id.downloadIV);
        sendIV = findViewById(R.id.sendIV);
        regular = Typeface.createFromAsset(getAssets(), "fonts/product_san_regular.ttf");
        bold = Typeface.createFromAsset(getAssets(),"fonts/product_sans_bold.ttf");
        regularFontChanger = new FontChanger(regular);
        boldFontChanger = new FontChanger(bold);
        urlET = findViewById(R.id.urlET);

        webView = findViewById(R.id.main_web_view);

        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setMinimumFontSize(10);
        webView.setWebViewClient(new NewWebViewClient());

        swipeRefreshLayout = findViewById(R.id.swipeContainer);

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

        if(webView.canGoBack()){
            webView.goBack();
        }
        else{
            
            supportFinishAfterTransition();
        }
        //To support reverse transitions when user clicks the device back button



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

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.download_menu, popup.getMenu());
        popup.show();
    }

    public void loadURL(String url){

        if(url.length()==0)
            Toast.makeText(getApplicationContext(),"Please Enter URL",Toast.LENGTH_SHORT).show();
        else if(url.contains("http://")||url.contains("https://"))
            webView.loadUrl(url);
        else if(url.contains("."))
            webView.loadUrl("http://"+url);
        else
            webView.loadUrl("http://google.com/search?q="+url);
        hideSoftKeyboard();
        urlET.setSelectAllOnFocus(true);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about: {

                return true;
            }
            case R.id.downloadALL: {

                return true;
            }
            default: {
                return false;
            }
        }
    }
}
