package com.example.balakrishnan.mybrowser;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class WebActivity extends AppCompatActivity{



    Typeface regular,bold;
    FontChanger regularFontChanger,boldFontChanger;
    EditText urlET;
    ImageView downloadIV,sendIV;
    WebView webView;

    public static String dpath;
    public static Context cont;
    public ArrayList<String> FileList;
    public ArrayList<String> DownloadList;
    public static SwipeRefreshLayout swipeRefreshLayout;
    private int backFlag = 0,duplFlag=1,replFlag=0;
    private boolean isFirstLoad = true;

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


        initElements();
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
                alertBoxWindow();
                return false;
            }
        });

    }

    public void initElements()
    {

        DownloadList = new ArrayList<>();

        dpath="/Download";
        cont = this.getApplicationContext();



    }

    public void loadURL(String url) {

        if (url.length() == 0)                                          //url field left empty
            Toast.makeText(getApplicationContext(), "Please Enter URL", Toast.LENGTH_SHORT).show();
        else if (url.contains("http://") || url.contains("https://"))  //is a valid url
            webView.loadUrl(url);

        else if(url.contains("."))                                  //is an url but does'nt start with http or https
            webView.loadUrl("http://"+url);

        else if (url.contains("."))                                  //is an url but doesnt start with http or https
            webView.loadUrl("http://" + url);

        else                                                        // not an url therefore searched on google
            webView.loadUrl("http://google.com/search?q=" + url);
        hideSoftKeyboard();

    }


    public static String exts = ".pdf .ppt .pptx .PDF .doc .docx";
    private EditText vDpath;
    private EditText edt;
    Switch cb;
    Switch cb2;

    public void alertBoxWindow()
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_layout, null);
        dialogBuilder.setView(dialogView);

        edt   = dialogView.findViewById(R.id.edit1);
        vDpath= dialogView.findViewById(R.id.edit2);

        cb =dialogView.findViewById(R.id.checkBox);
        cb2=dialogView.findViewById(R.id.checkBox2);

        cb.setChecked(true);
        cb2.setChecked(false);


        edt.setText(exts);
        dialogBuilder.setTitle("Download All Menu");

        vDpath.setText(dpath);
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                duplFlag=(cb.isChecked())?1:0;
                replFlag=(cb2.isChecked())?1:0;
                dpath=vDpath.getText().toString();
                CreateDir(dpath);

                exts=edt.getText().toString().trim();

                BackgroundParseTask b=new BackgroundParseTask(WebActivity.this);
                b.execute(urlET.getText().toString().trim(),exts);
                System.out.println("status:"+b.getStatus().toString());
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    public void CreateDir(String s)
    {
        File dir = new File(s);
        if(dir.exists())
        {
            System.out.println("Directory "+s+" exists");
        }
        else {
            try {
                if (dir.mkdir()) {
                    System.out.println("Directory created");
                } else {
                    System.out.println("Directory is not created");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        dpath=dir.getPath();
    }

    public void FileListFunction()
    {

        FileList=new ArrayList<>();
        FileList.clear();

        File f = Environment.getExternalStoragePublicDirectory(dpath);
        if(f.listFiles()==null)
            return;
        System.out.println(dpath);
        File[] g = f.listFiles();

        for(File x:g)
        {
            String fname=x.getAbsoluteFile().getName();
            if(!FileList.contains(fname))
                FileList.add(fname);

        }
        try {
            Class.forName("android.os.AsyncTask");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                System.out.println("Permission is granted");
                return true;
            } else {

                System.out.println("Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            System.out.println("PERMISSION GRANTED!");
            return true;
        }
    }


}
