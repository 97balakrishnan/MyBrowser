package com.example.balakrishnan.mybrowser;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import static com.example.balakrishnan.mybrowser.MainActivity.cont;

/**
 * Created by balakrishnan on 2/3/18.
 */

public class BackgroundParseTask extends AsyncTask<String, Void, String> {
    int cnt;
    int dcnt;
    int rcnt;
    String msg;

    @Override
    protected String doInBackground(String... args) {

        String urlString = args[0];
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

                for (; (line = reader.readLine()) != null; ) {


                    for(int z=0;z<aExt.length;z++) {
                        if (line.contains(aExt[z]+"\"") ){

                            int locn = -1;
                            locn = (line.indexOf(aExt[z]+"\"")+aExt[z].length());

                            int i = locn-1;
                            while (line.charAt(i) != '\"' && i >= 0) i--;
                            i++;

                            String fileLink=line.substring(i,locn);
                            System.out.println(fileLink);

                            DownloadLinkChecker dlc = new DownloadLinkChecker(fileLink,aExt);
                            if(dlc.isDownloadLink())
                                cnt++;

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
