package com.xbird.olddriver.olddriver.NET;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ben on 2016/4/17.
 */
public class VideoSearch
{
    private String s = "";
    private String httpresponse= "";

    private VIDEO[] videos;

    private int nrOfVideos;
    public VideoSearch(String s)
    {
        this.s = s;
        nrOfVideos = 0;
    }

    public boolean getInfo()
    {
        if(!connect())
            return false;
        if(!RegExp())
            return false;

        return true;
    }
    private boolean connect()
    {
        try
        {
            String get = URLEncoder.encode(s,"UTF-8");
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(CPURL.SearchURL+get).build();
            Response response = client.newCall(request).execute();

            if(response.isSuccessful())
            {
                httpresponse = response.body().string();
            }
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }

    private boolean RegExp()
    {
        Pattern pattern = Pattern.compile("\"name\":\"([^\"]*)\"id:\"([^\"]*)\"");
        Matcher matcher = pattern.matcher(httpresponse);

        boolean find = false;
        while (matcher.find())
        {
            String name = matcher.group(1);
            String ID = matcher.group(2);

            addVideo(name,ID);
            find = true;
        }

        return find;
    }

    private void addVideo(String name,String ID)
    {
        nrOfVideos++;
        VIDEO[] temp = new VIDEO[nrOfVideos];
        for(int n=0;n<nrOfVideos-1;n++)
        {
            temp[n] = videos[n];
        }
        videos = temp;
        videos[nrOfVideos-1] = new VIDEO();
        videos[nrOfVideos-1].name = name;
        videos[nrOfVideos-1].ID = ID;
    }

    public VIDEO[] getVideos()
    {
        return videos;
    }
}
