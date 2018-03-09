package com.example.balakrishnan.mybrowser;

/**
 * Created by balakrishnan on 2/3/18.
 */

public class DownloadLinkChecker {

    private String url;
    private String[] aExt;

    DownloadLinkChecker(String url,String[] aExt)
    {
        this.url=url;
        this.aExt=aExt;
    }

    public boolean isDownloadLink()
    {
        boolean isDownloadFileLink=false;
        for (int z = 0; z < aExt.length; z++) {
            if (url.endsWith(aExt[z])) {


                String fileLink = null;
                if (url.startsWith("http://")||url.startsWith("https://")) {
                    isDownloadFileLink=true;
                    fileLink = url;
                    String fileName;
                    if(fileLink.endsWith(aExt[z]))
                        fileName = fileLink.substring(fileLink.lastIndexOf("/") + 1, fileLink.lastIndexOf(aExt[z]));
                    else if(fileLink.endsWith(aExt[z].toUpperCase()))
                        fileName = fileLink.substring(fileLink.lastIndexOf("/") + 1, fileLink.lastIndexOf(aExt[z].toUpperCase()));
                    else
                        break;


                    new BackgroundTask().execute(fileLink, fileName, aExt[z]);
                    return true;
                }

            }


        }
        return isDownloadFileLink;
    }
}
