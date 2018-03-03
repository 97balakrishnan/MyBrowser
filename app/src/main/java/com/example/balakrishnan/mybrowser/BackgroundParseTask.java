package com.example.balakrishnan.mybrowser;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import static com.example.balakrishnan.mybrowser.BackgroundTask.bcnt;
import static com.example.balakrishnan.mybrowser.MainActivity.cont;

/**
 * Created by balakrishnan on 2/3/18.
 */

public class BackgroundParseTask extends AsyncTask<String, Void, String> {
    public static int cnt;
    private int dcnt;
    private int rcnt;
    private String msg;
    private String urlString;
    private ArrayList<String> ListofLinks=new ArrayList<>();
    private Activity act;
    public void showNotification()
    {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(act.getApplicationContext())
                .setSmallIcon(R.drawable.download_icon)
                .setContentTitle("Download Complete")
                .setContentText("Downloaded "+cnt+" files \n"+dcnt+" Duplicate files ignored")
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(act.getApplicationContext());
        notificationManager.notify(777, mBuilder.build());


    }
    BackgroundParseTask()
    {

    }
    BackgroundParseTask(Activity act)
    {
        this.act=act;
    }
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        ListofLinks.clear();
        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                System.out.println("post execution");
                Toast.makeText(act.getApplicationContext(),"Downloaded "+cnt+" files \n "+dcnt+" Duplicate files ignored",Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    protected String doInBackground(String... args) {
        cnt=0;
        urlString = args[0];
        String exts=args[1];
        DHandler(urlString,exts);
        /*try {

            if(duplFlag==1 ) {
                if (cnt > 0 && dcnt == 0)
                    msg = "Downloading " + cnt + " files ..";
                else if (cnt == 0 && dcnt == 0)
                    msg = "No files to download ! ";
                else if (cnt > 0 && dcnt > 0)
                    msg = "Downloading " + cnt + " files .." + dcnt + " duplicate files excluded ..";
                else if (dcnt > 0 && cnt == 0)
                    msg = dcnt + "duplicate files .. not downloaded";
            }
            if(duplFlag==0 && replFlag==0)
            {
                msg= "Downloading "+cnt+" files ..";
            }
            else if(duplFlag==0 && replFlag ==1)
            {
                if(rcnt>0 && cnt>0)
                    msg = "Downloading "+cnt+" files .."+"Replacing "+rcnt+" files .. ";
                else if(rcnt>0 && cnt==0)
                    msg="Replacing "+rcnt+" files ..";
            }
            runOnUiThread(new Runnable() {
                public void run() {

                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            System.out.println(e);
        }*/
        return null;
    }


    String line;
    public void DHandler(String urlString,String exts) {

        try {
            URL url = new URL(urlString);
            BufferedReader reader = null;

            try {
                reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
                cnt =0;
                dcnt=0;
                rcnt=0;
                String[] aExt = exts.split(" ");
                String longLine;
                for (; (longLine = reader.readLine()) != null; ) {

                    String[] lines=longLine.split(" ");
                    for(int i=0;i<lines.length;i++)
                    {line=lines[i];
                    System.out.println(line);
                    for(int z=0;z<aExt.length;z++) {

                        String fileLink;
                            if((fileLink=compute(line,aExt[z]))!=null)
                            {
                                System.out.println("fileLink:"+fileLink);
                                DownloadLinkChecker dlc = new DownloadLinkChecker(fileLink,aExt);
                                if(dlc.isDownloadLink())
                                    cnt++;

                            }

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
            System.out.println(e);
        }
    }
    private String compute(String line,String ext)
    {

        if (line.startsWith("href=")&&line.contains(ext+"\"")){

            String fileLink=line.substring(6,line.indexOf(ext)+ext.length());
            System.out.println("FileLink"+fileLink);

            if(!(fileLink.startsWith("http://")||fileLink.startsWith("https://")))
            {
                if(!fileLink.startsWith("/"))
                    fileLink="/"+fileLink;
                fileLink=getDomain(urlString)+fileLink;
            }

            if(!ListofLinks.contains(fileLink)) {
                ListofLinks.add(fileLink);
                return fileLink;
            }
            else
                dcnt++;

        }
        else if(line.startsWith("href=")&&line.contains(ext))
        {

            String fileLink=line.substring(5,line.indexOf(ext)+ext.length());
            System.out.println("filelink"+fileLink);
            if(!(fileLink.startsWith("http://")||fileLink.startsWith("https://")))
            {
                if(!fileLink.startsWith("/"))
                    fileLink="/"+fileLink;
                fileLink=getDomain(urlString)+fileLink;
            }

            if(!ListofLinks.contains(fileLink)) {
                ListofLinks.add(fileLink);
                return fileLink;
            }
            else
                dcnt++;

        }
        return null;
    }
    private String getDomain(String url)
    {
        String domain,prot="";
        if(url.startsWith("https://"))
            prot="https://";
        else if(url.startsWith("http://"))
            prot="http://";
        int end=url.indexOf('/',prot.length());
        if(end!=-1) {
            domain = url.substring(0, end);
            System.out.println("domain:" + domain);
            return domain;
        }
        else
        {
            return null;
        }
    }

}
