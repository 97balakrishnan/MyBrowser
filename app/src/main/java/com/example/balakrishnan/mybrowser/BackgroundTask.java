package com.example.balakrishnan.mybrowser;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;

import static com.example.balakrishnan.mybrowser.WebActivity.dpath;

/**
 * Created by balakrishnan on 2/3/18.
 */

class BackgroundTask extends AsyncTask<String, Void, String> {
    String dirpath=dpath;
    @Override
    protected String doInBackground(String... strings) {
        String fileLink = strings[0];
        String fileName= strings[1];
        String extension=strings[2];

        String url = fileLink;
        System.out.println("Downloader : "+url);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription("Some description");
        request.setTitle(fileName);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }


        request.setDestinationInExternalPublicDir(dirpath, fileName + extension);

        DownloadManager manager = (DownloadManager) WebActivity.cont.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);

        return null;
    }
}
