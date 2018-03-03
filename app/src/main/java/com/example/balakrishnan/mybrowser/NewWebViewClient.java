package com.example.balakrishnan.mybrowser;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
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

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.widget.Toast.LENGTH_LONG;
import static com.example.balakrishnan.mybrowser.MainActivity.adapter;
import static com.example.balakrishnan.mybrowser.MainActivity.dpath;
import static com.example.balakrishnan.mybrowser.MainActivity.hist;
import static com.example.balakrishnan.mybrowser.WebActivity.swipeRefreshLayout;

/**
 * Created by balakrishnan on 24/11/17.
 */

public class NewWebViewClient extends WebViewClient {

    private View mainView;
    private EditText et;
    private String[] aExt = {".pdf",".doc",".docx",".ppt",".pptx"};

    public void initVars()
    {
        et =mainView.findViewById(R.id.urlET);

    }
    @Override
    public boolean shouldOverrideUrlLoading(WebView wv, String url) {

        mainView = wv.getRootView();
        initVars();

        DownloadLinkChecker dlc = new DownloadLinkChecker(url,aExt);
        if(!dlc.isDownloadLink())
            et.setText(url);

        if (URLUtil.isValidUrl(url)) {
            System.out.println(url + " is valid");
            return false;
        }
        else {
            System.out.println(url + " is invalid");
        }

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
        super.onReceivedError(view, request, error);
        System.out.println("error occured");
    }


}

