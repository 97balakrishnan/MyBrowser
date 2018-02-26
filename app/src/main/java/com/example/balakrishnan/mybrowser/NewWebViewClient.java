package com.example.balakrishnan.mybrowser;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.URLUtil;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.widget.Toast.LENGTH_LONG;
import static com.example.balakrishnan.mybrowser.MainActivity.adapter;
import static com.example.balakrishnan.mybrowser.MainActivity.hist;
import static com.example.balakrishnan.mybrowser.WebActivity.swipeRefreshLayout;

/**
 * Created by balakrishnan on 24/11/17.
 */

public class NewWebViewClient extends WebViewClient {
    View mainView;
    @Override
    public boolean shouldOverrideUrlLoading(WebView wv, String url){
        //Toast.makeText(wv.getContext(),url,LENGTH_LONG).show();
        System.out.println("greener "+url);
        mainView = wv.getRootView();
        EditText et = mainView.findViewById(R.id.urlET);
        et.setText(url);

        if(URLUtil.isValidUrl(url))
        {System.out.println("greener "+url+" is valid");return false;}
        else
            System.out.println("greener "+url+" is invalid");
            //Toast.makeText(wv.getContext(),"InvalidURL",LENGTH_LONG).show();
       /* Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
        wv.getContext().startActivity(intent);*/
        return true;

    }


    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        swipeRefreshLayout.setRefreshing(true);
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        swipeRefreshLayout.setRefreshing(false);
        super.onPageFinished(view, url);
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        //Toast.makeText(mainView.getContext(),"Error"+error,Toast.LENGTH_SHORT).show();

        super.onReceivedError(view, request, error);
        System.out.println("greener  error occured");
    }

    private boolean isValidUrl(String url) {
        Pattern p = Patterns.WEB_URL;
        Matcher m = p.matcher(url.toLowerCase());
        return m.matches();
    }
}

