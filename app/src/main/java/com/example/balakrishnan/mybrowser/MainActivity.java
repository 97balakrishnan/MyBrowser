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
    int backFlag=0;
    public static SwipeRefreshLayout mySwipeRefreshLayout;
    TextView txt;
    ImageView imageView;
    WebView wv;
    BitmapDrawable drawable;
    AutoCompleteTextView et;
    boolean isOpen = false, isOpen1 = false;
    boolean fl;
    Animation FabOpen, FabClose, FabRC, FabRAC;
    Animation FabOpen1, FabClose1, FabRC1, FabRAC1;
    TextView t, w;
    ArrayList<String> al;
    public static ArrayList<String> hist = new ArrayList<>();

    public static ArrayAdapter<String> adapter;
    String[] WEBSITES = {"http://facebook.com","http://twitter.com","http://youtube.com","http://hotstar.com","http://www.ssn.net","http://flipkart.com","http://amazon.in","http://www.google.com"};
    String[] WEBSITES1 = {"http://facebook.com","http://twitter.com","http://youtube.com","http://hotstar.com","http://www.ssn.net","http://flipkart.com","http://amazon.in","http://www.google.com"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        txt=(TextView)findViewById(R.id.txt);
        mySwipeRefreshLayout = (SwipeRefreshLayout)this.findViewById(R.id.swipeContainer);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        wv.reload();

                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                }
        );

/*
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, WEBSITES);
        AutoCompleteTextView textView = (AutoCompleteTextView)findViewById(R.id.editText2);
        textView.setAdapter(adapter);
*/
    //new BackgroundTask().execute(getApplicationContext());
        BackgroundTask.cont=getApplicationContext();

        final FloatingActionButton fab_option, fab_refresh, fab_forward, fab_back, fab_more;
        fab_option = (FloatingActionButton) findViewById(R.id.fab_option);
        fab_refresh = (FloatingActionButton) findViewById(R.id.fab_refresh);
        fab_forward = (FloatingActionButton) findViewById(R.id.fab_forward);
        fab_back = (FloatingActionButton) findViewById(R.id.fab_back);
        fab_more = (FloatingActionButton) findViewById(R.id.fab_more);
        //fab_option.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#55000000")));


        FabOpen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        FabClose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        FabRC = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);
        FabRAC = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_anticlockwise);

        fab_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt.setBackgroundColor(Color.parseColor("#77000000"));
                mySwipeRefreshLayout.bringToFront();
                mySwipeRefreshLayout.invalidate();
                    wv.bringToFront();
                    wv.invalidate();
                    t.setVisibility(View.INVISIBLE);
                    w.setVisibility(View.INVISIBLE);

                    String url=et.getText().toString().trim();
                    if(url.length()==0)
                        Toast.makeText(getApplicationContext(),"Please Enter URL",Toast.LENGTH_SHORT).show();
                    else if(url.contains("http://")||url.contains("https://"))
                        wv.loadUrl(url);
                    else if(url.contains("."))
                        wv.loadUrl("http://"+url);
                    else
                        wv.loadUrl("http://google.com/search?q="+url);
                    hideSoftKeyboard();
                    et.setSelectAllOnFocus(true);




            }
        });
        fab_option.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (isOpen) {
                    fab_back.startAnimation(FabClose);
                    fab_forward.startAnimation(FabClose);
                    fab_refresh.startAnimation(FabClose);
                    fab_option.startAnimation(FabRAC);
                    fab_back.setClickable(false);
                    fab_forward.setClickable(false);
                    fab_refresh.setClickable(false);
                    isOpen = false;
                } else if (isOpen == false) {
                    fab_back.startAnimation(FabOpen);
                    fab_forward.startAnimation(FabOpen);
                    fab_refresh.startAnimation(FabOpen);
                    fab_option.startAnimation(FabRC);
                    fab_back.setClickable(true);
                    fab_forward.setClickable(true);
                    fab_refresh.setClickable(true);
                    isOpen = true;
                }
                return true;
            }
        });
        final TextView about = (TextView) findViewById(R.id.about);
        final TextView clear = (TextView) findViewById(R.id.clear);
        FabOpen1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        FabClose1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        FabRC1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);
        FabRAC1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_anticlockwise);

        fab_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOpen1) {
                    about.startAnimation(FabClose1);
                    clear.startAnimation(FabClose1);
                    fab_more.startAnimation(FabRAC1);
                    about.setClickable(false);
                    clear.setClickable(false);
                    isOpen1 = false;
                } else if (isOpen1 == false) {
                    about.startAnimation(FabOpen1);
                    clear.startAnimation(FabOpen1);
                    fab_more.startAnimation(FabRC1);
                    about.setClickable(true);
                    clear.setClickable(true);
                    isOpen1 = true;
                }
            }
        });

        fl = false;
        wv = (WebView) findViewById(R.id.main_web_view);
        WebSettings ws = wv.getSettings();
        ws.setJavaScriptEnabled(true);
        wv.getSettings().setUseWideViewPort(true);
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.getSettings().setBuiltInZoomControls(true);
        wv.getSettings().setMinimumFontSize(10);
        //wv.loadUrl("https://source.unsplash.com/random");
        //wv.loadDataWithBaseURL(null,"<!DOCTYPE html><html><body style = \"text-align:center\"><img style=\"border-style:solid;border-width:5px;border-color:black;width:99%;\" src= https://source.unsplash.com/random alt=\"page Not Found\"></body></html>","text/html", "UTF-8", null);
        imageView = (ImageView) findViewById(R.id.imageView);

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                System.out.println("greener1");
                fab_more.setBackgroundColor(getDominantColor(bitmap));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {}

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {}
        };


        Picasso.with(this)
                .load("https://source.unsplash.com/random").error(R.drawable.nointernet).into(imageView, new Callback() {
            @Override public void onSuccess() {
                imageView.getDrawable();
            }

            @Override
            public void onError() {
                System.out.println("error");
            }
        });


        //imageView.setColorFilter(Color.GRAY, PorterDuff.Mode.LIGHTEN);
       // wv.setWebViewClient(new WebViewClient());
        wv.setWebViewClient(new NewWebViewClient());

        et = (AutoCompleteTextView) findViewById(R.id.editText2);

        et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et.setSelectAllOnFocus(true);

            }
        });

        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_SEND) {
                    txt.setBackgroundColor(Color.parseColor("#77000000"));
                    mySwipeRefreshLayout.bringToFront();
                    mySwipeRefreshLayout.invalidate();
                    wv.bringToFront();
                    wv.invalidate();
                    t.setVisibility(View.INVISIBLE);
                    w.setVisibility(View.INVISIBLE);
                    String url=et.getText().toString().trim();
                    if(url.length()==0)
                        Toast.makeText(getApplicationContext(),"Please Enter URL",Toast.LENGTH_SHORT).show();
                    else if(url.contains("http://")||url.contains("https://"))
                    wv.loadUrl(url);
                    else if(url.contains("."))
                        wv.loadUrl("http://"+url);
                    else
                    wv.loadUrl("http://google.com/search?q="+url);
                    hideSoftKeyboard();
                    et.setSelectAllOnFocus(true);

                    handled = true;
                }
                return handled;
            }
        });

        //imageView.setVisibility(View.INVISIBLE);

        w = (TextView) findViewById(R.id.welcome);
        t = (TextView) findViewById(R.id.clock);
        DigitalClock clock = (DigitalClock) findViewById(R.id.dclock);
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

        clear.setOnClickListener(new View.OnClickListener() {
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
        AutoCompleteTextView et = (AutoCompleteTextView) findViewById(R.id.editText2);
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