package com.xbird.olddriver.olddriver.NET.zz;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.xbird.olddriver.olddriver.NET.CPURL;
import com.xbird.olddriver.olddriver.NET.VIDEO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ben on 2016/5/7.
 */
public class FindCILI {
    private String s = "";
    private String httpresponse= "";

    private VIDEO[] videos;

    private int nrOfVideos;
    
    public FindCILI(String name)
    {
        this.s = name;
    }
    
    public boolean getInfo()
    {
        if(!connect())
            return false;
        if(!RegExp())
            return false;

        return true;
    }

    public VIDEO[] getVideos()
    {
        return videos;
    }


    private boolean connect() {

        try
        {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(CPURL.SearchURL_2 + this.s).build();
            Response response = client.newCall(request).execute();

            if(response.isSuccessful())
            {
                httpresponse = response.body().string();
            }
        }
        catch(Exception e)
        {
            return false;
        }
        return true;
    }
    private boolean RegExp() {
        Pattern pattern = Pattern.compile("\"name\": \"([^\"]*)\",\"cili\": \"([^\"]*)\",\"size\": \"([^\"]*)\"");
        Matcher matcher = pattern.matcher(httpresponse);

        boolean find = false;
        while (matcher.find())
        {
            String name = matcher.group(1);
            String cili = matcher.group(2);
            String size = matcher.group(3);
            addVideo(name,cili,size);
            find = true;
        }

        return find;
    }

    private void addVideo(String name,String CILI,String size)
    {
        nrOfVideos++;
        VIDEO[] temp = new VIDEO[nrOfVideos];
        for(int n=0;n<nrOfVideos-1;n++)
        {
            temp[n] = videos[n];
        }
        videos = temp;
        videos[nrOfVideos-1] = new VIDEO();
        videos[nrOfVideos-1].ID = "000000";
        videos[nrOfVideos-1].name = name;
        videos[nrOfVideos-1].CILI = CILI;
        videos[nrOfVideos-1].size = size;
    }
}
