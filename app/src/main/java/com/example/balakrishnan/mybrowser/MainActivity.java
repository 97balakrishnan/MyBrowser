package com.example.balakrishnan.mybrowser;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.method.KeyListener;
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
import android.widget.DigitalClock;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

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
    private boolean fl;
    private int backFlag=0;
    private boolean isFirstLoad=true;
    
    Animation FabOpen, FabClose, FabRC, FabRAC;
    Animation FabOpen1, FabClose1, FabRC1, FabRAC1;

    FloatingActionButton fab_go, fab_refresh, fab_forward, fab_back, fab_more;

    public void initFabs()
    {
        fab_go = findViewById(R.id.fab_go);
        fab_refresh = findViewById(R.id.fab_refresh);
        fab_forward = findViewById(R.id.fab_forward);
        fab_back = findViewById(R.id.fab_back);
        fab_more = findViewById(R.id.fab_more);


        tAbout = findViewById(R.id.about);
        tDownloadAll = findViewById(R.id.download_all);

    }
    public void initAnimations()
    {
        FabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        FabClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        FabRC = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);
        FabRAC = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_anticlockwise);

        FabOpen1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        FabClose1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        FabRC1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);
        FabRAC1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_anticlockwise);

    }
    public void initSwipeLayout()
    {
        mySwipeRefreshLayout = findViewById(R.id.swipeContainer);
    }
    public void initHomeScreenElements()
    {
        w = findViewById(R.id.welcome);
        t = findViewById(R.id.clock);
        imageView =findViewById(R.id.imageView);
        clock = findViewById(R.id.dclock);
    }
    public void initWebView()
    {
        wv = (WebView) findViewById(R.id.main_web_view);
        WebSettings ws = wv.getSettings();
        ws.setJavaScriptEnabled(true);
        wv.getSettings().setUseWideViewPort(true);
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.getSettings().setBuiltInZoomControls(true);
        wv.getSettings().setMinimumFontSize(10);

        wv.setWebViewClient(new NewWebViewClient());

    }
    public void initURLfield()
    {
        et = findViewById(R.id.url_field);
    }
    public void loadHomeScreenImage()
    {

        Picasso.with(this)
                .load("https://source.unsplash.com/random").error(R.drawable.nointernet).into(imageView, new Callback() {
            @Override public void onSuccess() {
                imageView.getDrawable();
            }

            @Override
            public void onError() {
                Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_LONG).show();
            }
        });

    }
    public void setClockTime()
    {

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
    public void reloadCurrentPage()
    {
        wv.reload();
        mySwipeRefreshLayout.setRefreshing(false);
    }
    public void hideHomeScreenElements()
    {
        t.setVisibility(View.INVISIBLE);
        w.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.INVISIBLE);
    }
    public void loadURL(String url)
    {

        if(url.length()==0)                                          //url field left empty
            Toast.makeText(getApplicationContext(),"Please Enter URL",Toast.LENGTH_SHORT).show();
        else if(url.contains("http://")||url.contains("https://"))  //is a valid url
            wv.loadUrl(url);
        else if(url.contains("."))                                  //is an url but doesnt start with http or https
            wv.loadUrl("http://"+url);
        else                                                        // not an url therefore searched on google
            wv.loadUrl("http://google.com/search?q="+url);
        hideSoftKeyboard();

    }
    public void closeGoOptionsWithAnimation()
    {
        fab_back.startAnimation(FabClose);
        fab_forward.startAnimation(FabClose);
        fab_refresh.startAnimation(FabClose);
        fab_go.startAnimation(FabRAC);
        fab_back.setClickable(false);
        fab_forward.setClickable(false);
        fab_refresh.setClickable(false);
    }
    public void openGoOptionsWithAnimation()
    {
        fab_back.startAnimation(FabOpen);
        fab_forward.startAnimation(FabOpen);
        fab_refresh.startAnimation(FabOpen);
        fab_go.startAnimation(FabRC);
        fab_back.setClickable(true);
        fab_forward.setClickable(true);
        fab_refresh.setClickable(true);

    }
    public void openMoreOptionsWithAnimation()
    {
        tAbout.startAnimation(FabOpen1);
        tDownloadAll.startAnimation(FabOpen1);
        fab_more.startAnimation(FabRC1);
        tAbout.setClickable(true);
        tDownloadAll.setClickable(true);

    }
    public void closeMoreOptionsWithAnimation()
    {
        tAbout.startAnimation(FabClose1);
        tDownloadAll.startAnimation(FabClose1);
        fab_more.startAnimation(FabRAC1);
        tAbout.setClickable(false);
        tDownloadAll.setClickable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        
        initFabs();
        initAnimations();
        initSwipeLayout();
        initHomeScreenElements();
        initWebView();
        initURLfield();

        setClockTime();
        loadHomeScreenImage();

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
                
                    if(isFirstLoad) {                             // If first load then hide Home Screen elements
                        isFirstLoad=false;
                        hideHomeScreenElements();
                    }                 
                    String url=et.getText().toString().trim();    // Loading the URL
                    loadURL(url);

            }
        });
        
        fab_go.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (!isOpenGo) {
                    openGoOptionsWithAnimation();
                    isOpenGo = true;
                } 
                else {
                    closeGoOptionsWithAnimation();
                    isOpenGo =false;
                }
                return true;
            }
        });
        
        
        fab_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isOpenMore) {
                    openMoreOptionsWithAnimation();
                    isOpenMore = true;
                } else{
                    closeMoreOptionsWithAnimation();
                    isOpenMore = false;
                }
            }
        });

        fl = false;



        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_SEND) {

                    if(isFirstLoad) {                             // If first load then hide Home Screen elements
                        isFirstLoad=false;
                        hideHomeScreenElements();
                    }
                    String url=et.getText().toString().trim();    // Loading the URL
                    loadURL(url);
                    handled = true;
                }
                return handled;
            }
        });


        

        BackgroundTask.cont=getApplicationContext();
        tDownloadAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BackgroundTask().execute(et.getText().toString().trim());
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
                if(wv.canGoForward())
                    wv.goForward();
            }
        });
        fab_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wv.reload();
            }
        });
        /*et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSoftKeyboard(view);
                if(!et.getText().toString().equals("http://"))
                et.setSelectAllOnFocus(true);
                ArrayList<String> al = new ArrayList<String>();
                for(int x=0;x<WEBSITES1.length;x++)
                {
                    if(WEBSITES1[x].contains(et.getText().toString().trim()))
                    {
                        al.add(WEBSITES1[x]);
                    }
                }
                WEBSITES = al.toArray(new String[0]);
                adapter.notifyDataSetChanged();
                et.showDropDown();
            }
        });


      et.addTextChangedListener(new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

          }

          @Override
          public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

          }

          @Override
          public void afterTextChanged(Editable editable) {

              ArrayList<String> al = new ArrayList<String>();
              for(int x=0;x<WEBSITES1.length;x++)
              {
                  if(WEBSITES1[x].contains(et.getText().toString().trim()))
                  {
                      al.add(WEBSITES1[x]);
                  }
              }
              WEBSITES = al.toArray(new String[0]);
              adapter.notifyDataSetChanged();
              et.showDropDown();
          }
      });
*/
    }

    @Override
    public void onBackPressed() {

        //super.onBackPressed();

        if (wv.canGoBack())
        { wv.goBack();
            backFlag=0;}
        else if(backFlag==0)
        {
            Toast.makeText(getApplicationContext(),"Press back again to exit",Toast.LENGTH_SHORT).show();
            backFlag=1;
        }
        else
        {
            this.onDestroy();
        }
    }

    public void buttonClick(View v) {
        EditText et = findViewById(R.id.editText2);
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

        }

        }
    public void showSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }
    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }


    }

    public static int getDominantColor(Bitmap bitmap) {

        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, 1, 1, true);
        final int color = newBitmap.getPixel(0, 0);
        newBitmap.recycle();
        System.out.println("greener2"+color);
        return color;
    }






}
    class BackgroundTask extends AsyncTask<String, Void, String> {
     public static Context cont;
     String urlString;
        @Override
        protected String doInBackground(String... args) {
            urlString=args[0];
            DHandler();

            return null;
        }
        public void downloader(String durl,String name,String ext)
        {

            String url = durl;
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setDescription("Some description");
            request.setTitle(name);
// in order for this if to run, you must use the android 3.2 to compile your app
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            }
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, name+ext);


// get download service and enqueue file
            DownloadManager manager = (DownloadManager)cont.getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(request);


        }

        public void DHandler()
        {
            try {
                //Toast.makeText(getApplication(), "HELLO1", Toast.LENGTH_SHORT).show();
                URL url = new URL(urlString);
                BufferedReader reader = null;
                System.out.println("starting");
                StringBuilder builder = new StringBuilder();
                try {
                    reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
                    for (String line; (line = reader.readLine()) != null; ) {
                        if(line.contains(".pdf")||line.contains(".PDF")){
                            int locn = -1;
                            locn=(line.contains(".pdf"))?(line.indexOf(".pdf")):(line.indexOf(".PDF"));

                            int i=locn;
                            while(line.charAt(i)!='\"' && i>=0)i--;
                            String fileLink=null;
                            if(i!=0)
                            {
                                fileLink=line.substring(i+1,locn+4);
                                System.out.println(".!." + fileLink);
                                String name = fileLink.substring(fileLink.lastIndexOf("/")+1,fileLink.length());
                                System.out.println("filename:"+name);
                                downloader(fileLink,name,".pdf");
                            }
                        }
                    }

                } finally {
                    if (reader != null) try {
                        reader.close();
                    } catch (IOException logOrIgnore) {
                    }

                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }


    }