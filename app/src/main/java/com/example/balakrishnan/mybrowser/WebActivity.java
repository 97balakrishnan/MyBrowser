package com.example.balakrishnan.mybrowser;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class WebActivity extends AppCompatActivity{



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

        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        webView.reload();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );

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
        webView.getSettings().setDisplayZoomControls(false);
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
        popup.getMenu().getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                new DownloadAsyncTask().execute(urlET.getText().toString().trim());
                return false;
            }
        });

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


    public class DownloadAsyncTask extends AsyncTask<String, Void, String> {

        String urlString;

        @Override
        protected String doInBackground(String... args) {

            urlString = args[0];
            DHandler();

            return null;
        }

        public void downloader(String durl, String name, String ext) {

            String url = durl;

            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setDescription("Some description");
            request.setTitle(name);

            //Min SDK should be greater than Honeycomb SDK
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            }
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, name + ext);


            //Get download service and enqueue file
            DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(request);


        }

        public void DHandler() {
            try {

                URL url = new URL(urlString);
                BufferedReader reader = null;

                System.out.println("starting");

                //StringBuilder builder = new StringBuilder();

                try {

                    reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));

                    for (String line; (line = reader.readLine()) != null; ) {

                        if (line.contains(".pdf") || line.contains(".PDF")) {

                            int locn = -1;

                            locn = (line.contains(".pdf")) ? (line.indexOf(".pdf")) : (line.indexOf(".PDF"));

                            int i = locn;

                            while (line.charAt(i) != '\"' && i >= 0) i--;

                            String fileLink = null;

                            if (i != 0) {

                                fileLink = line.substring(i + 1, locn + 4);
                                System.out.println(".!." + fileLink);
                                String name = fileLink.substring(fileLink.lastIndexOf("/") + 1, fileLink.length());
                                System.out.println("filename:" + name);
                                downloader(fileLink, name, ".pdf");

                            }
                        }
                    }

                } finally {
                    if (reader != null) try {

                        reader.close();

                    } catch (IOException logOrIgnore) {
                        logOrIgnore.printStackTrace();
                    }

                }
            } catch (Exception e) {

                System.out.println(e.getMessage());

            }
        }
    }
}
