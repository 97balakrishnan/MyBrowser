package com.example.balakrishnan.mybrowser;
import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.method.KeyListener;
import android.view.LayoutInflater;
import android.view.inputmethod.InputMethodManager;

import android.app.ActionBar;
import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.DrawableRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.DigitalClock;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;

import static android.os.Environment.DIRECTORY_DOWNLOADS;


public class MainActivity extends AppCompatActivity {




    public static SwipeRefreshLayout mySwipeRefreshLayout;
    public static ArrayList<String> hist = new ArrayList<>();
    public static ArrayAdapter<String> adapter;

    private TextView t, w;
    private TextView tAbout;
    private TextView tDownloadAll;

    private ImageView imageView;
    private WebView wv;
    private EditText et;
    private DigitalClock clock;

    private boolean isOpenGo = false;
    private boolean isOpenMore = false;

    private int backFlag = 0,duplFlag=1,replFlag=0;
    private boolean isFirstLoad = true;


    public static String dpath;
    public static Context cont;
    public ArrayList<String> FileList;
    public ArrayList<String> DownloadList;

    Animation FabOpen, FabClose, FabRC, FabRAC;
    Animation FabOpen1, FabClose1, FabRC1, FabRAC1;

    FloatingActionButton fab_go, fab_refresh, fab_forward, fab_back, fab_more;

    public void initFabs() {
        fab_go = findViewById(R.id.fab_go);
        fab_refresh = findViewById(R.id.fab_refresh);
        fab_forward = findViewById(R.id.fab_forward);
        fab_back = findViewById(R.id.fab_back);
        fab_more = findViewById(R.id.fab_more);


        tAbout = findViewById(R.id.about);
        tDownloadAll = findViewById(R.id.download_all);

    }

    public void initAnimations() {
        FabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        FabClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        FabRC = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);
        FabRAC = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_anticlockwise);

        FabOpen1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        FabClose1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        FabRC1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);
        FabRAC1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_anticlockwise);

    }

    public void initSwipeLayout() {
        mySwipeRefreshLayout = findViewById(R.id.swipeContainer);
    }

    public void initHomeScreenElements() {
        w = findViewById(R.id.welcome);
        t = findViewById(R.id.clock);
        imageView = findViewById(R.id.imageView);
        clock = findViewById(R.id.dclock);
    }

    public void initWebView() {
        wv = (WebView) findViewById(R.id.main_web_view);
        WebSettings ws = wv.getSettings();
        ws.setJavaScriptEnabled(true);
        wv.getSettings().setUseWideViewPort(true);
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.getSettings().setBuiltInZoomControls(true);
        wv.getSettings().setMinimumFontSize(10);

        wv.setWebViewClient(new NewWebViewClient());

    }

    public void initURLfield() {
        et = findViewById(R.id.url_field);
    }

    public void loadHomeScreenImage() {

        Picasso.with(this)
                .load("https://source.unsplash.com/random").error(R.drawable.nointernet).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                imageView.getDrawable();
            }

            @Override
            public void onError() {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void setClockTime() {

        clock.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                t.setText(editable.toString());
            }
        });
    }

    public void reloadCurrentPage() {
        wv.reload();
        mySwipeRefreshLayout.setRefreshing(false);
    }

    public void hideHomeScreenElements() {
        t.setVisibility(View.INVISIBLE);
        w.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.INVISIBLE);
    }

    public void loadURL(String url) {

        if (url.length() == 0)                                          //url field left empty
            Toast.makeText(getApplicationContext(), "Please Enter URL", Toast.LENGTH_SHORT).show();
        else if (url.contains("http://") || url.contains("https://"))  //is a valid url
            wv.loadUrl(url);
<<<<<<< HEAD
        else if(url.contains("."))                                  //is an url but does'nt start with http or https
            wv.loadUrl("http://"+url);
=======
        else if (url.contains("."))                                  //is an url but doesnt start with http or https
            wv.loadUrl("http://" + url);
>>>>>>> d48e5fba48dd4d4772d375c6229d1e78aa1d0dd9
        else                                                        // not an url therefore searched on google
            wv.loadUrl("http://google.com/search?q=" + url);
        hideSoftKeyboard();

    }

    public void closeGoOptionsWithAnimation() {
        fab_back.startAnimation(FabClose);
        fab_forward.startAnimation(FabClose);
        fab_refresh.startAnimation(FabClose);
        fab_go.startAnimation(FabRAC);
        fab_back.setClickable(false);
        fab_forward.setClickable(false);
        fab_refresh.setClickable(false);
    }

    public void openGoOptionsWithAnimation() {
        fab_back.startAnimation(FabOpen);
        fab_forward.startAnimation(FabOpen);
        fab_refresh.startAnimation(FabOpen);
        fab_go.startAnimation(FabRC);
        fab_back.setClickable(true);
        fab_forward.setClickable(true);
        fab_refresh.setClickable(true);

    }

    public void openMoreOptionsWithAnimation() {
        tAbout.startAnimation(FabOpen1);
        tDownloadAll.startAnimation(FabOpen1);
        fab_more.startAnimation(FabRC1);
        tAbout.setClickable(true);
        tDownloadAll.setClickable(true);

    }

    public void closeMoreOptionsWithAnimation() {
        tAbout.startAnimation(FabClose1);
        tDownloadAll.startAnimation(FabClose1);
        fab_more.startAnimation(FabRAC1);
        tAbout.setClickable(false);
        tDownloadAll.setClickable(false);
    }
    public void initElements()
    {
        backFlag = 0;duplFlag=1;replFlag=0;
        DownloadList = new ArrayList<>();

        dpath="/Download";
        cont = this.getApplicationContext();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        initFabs();
        initAnimations();
        initSwipeLayout();

        isStoragePermissionGranted();
        initHomeScreenElements();
        initWebView();
        initURLfield();
        initElements();
        setClockTime();
        loadHomeScreenImage();
        FileListFunction();
        mySwipeRefreshLayout.setOnRefreshListener(                // Pull down to refresh function
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        reloadCurrentPage();
                    }
                }
        );

        fab_go.setOnClickListener(new View.OnClickListener() {    // OnClick Listener for go button
            @Override
            public void onClick(View view) {

                if (isFirstLoad) {                             // If first load then hide Home Screen elements
                    isFirstLoad = false;
                    hideHomeScreenElements();
                }
                String url = et.getText().toString().trim();    // Loading the URL
                loadURL(url);

            }
        });

        fab_go.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (!isOpenGo) {
                    openGoOptionsWithAnimation();
                    isOpenGo = true;
                } else {
                    closeGoOptionsWithAnimation();
                    isOpenGo = false;
                }
                return true;
            }
        });

        tAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"App made by S Balakrishnan ",Toast.LENGTH_SHORT).show();
            }
        });

        fab_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isOpenMore) {
                    openMoreOptionsWithAnimation();
                    isOpenMore = true;
                } else {
                    closeMoreOptionsWithAnimation();
                    isOpenMore = false;
                }
            }
        });



        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_SEND) {

                    if (isFirstLoad) {                             // If first load then hide Home Screen elements
                        isFirstLoad = false;
                        hideHomeScreenElements();
                    }
                    String url = et.getText().toString().trim();    // Loading the URL
                    loadURL(url);
                    handled = true;
                }
                return handled;
            }
        });


        tDownloadAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isStoragePermissionGranted()) {
                    alertBoxWindow();

                }

            }
        });
        fab_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (wv.canGoBack())
                    wv.goBack();
            }
        });
        fab_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (wv.canGoForward())
                    wv.goForward();
            }
        });
        fab_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wv.reload();
            }
        });

    }

    @Override
    public void onBackPressed() {

        //super.onBackPressed();

        if (wv.canGoBack()) {
            wv.goBack();
            backFlag = 0;
        } else if (backFlag == 0) {
            Toast.makeText(getApplicationContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
            backFlag = 1;
        } else {
            this.onDestroy();
        }
    }

<<<<<<< HEAD
    public void buttonClick(View v) {
        //EditText et = findViewById(R.id.editText2);
        String s = et.getText().toString();
        if (fl == false && s.length() == 0) {

            Picasso.with(this).load("https://source.unsplash.com/random").skipMemoryCache().error(R.drawable.nointernet).into(imageView);
            //imageView.setColorFilter(Color.GRAY, PorterDuff.Mode.LIGHTEN);
        } else if (s.length() != 0) {
            fl = true;
            WebView wv = (WebView) findViewById(R.id.main_web_view);
            imageView.setVisibility(View.INVISIBLE);
            t.setVisibility(View.INVISIBLE);
            w.setVisibility(View.INVISIBLE);
            wv.loadUrl(s);
=======
>>>>>>> d48e5fba48dd4d4772d375c6229d1e78aa1d0dd9

    public void showSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }


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
        dialogBuilder.setTitle("DownloadAll Menu");

        vDpath.setText(dpath);
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                duplFlag=(cb.isChecked())?1:0;
                replFlag=(cb2.isChecked())?1:0;
                dpath=vDpath.getText().toString();
                CreateDir(dpath);

                exts=edt.getText().toString().trim();

                new BackgroundParseTask().execute(et.getText().toString().trim(),exts);
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
