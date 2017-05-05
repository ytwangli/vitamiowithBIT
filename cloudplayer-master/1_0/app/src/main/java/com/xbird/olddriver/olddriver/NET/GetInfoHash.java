package com.xbird.olddriver.olddriver.NET;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ben on 2016/4/18.
 */
public class GetInfoHash
{
    private String id;
    private String httpResponse;

    private String Cili;
    private String size;

    public GetInfoHash(String ID)
    {
        this.id = ID;
        this.httpResponse = "";
    }

    public boolean getInfo()
    {
        if(connect()) {
            if (RegExp()) {
                return true;
            }
        }
        return false;

    }

    private boolean connect() {
        try {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(CPURL.GETCILI+id).build();
            Response response = client.newCall(request).execute();
            if(response.isSuccessful())
            {
                httpResponse = response.body().string();
            }
        }
        catch (Exception e) {
            return false;
        }

        return true;

    }
    private boolean RegExp()
    {
        Pattern pattern = Pattern.compile("\"cili\": \"([^\"]*)\",\"size\": \"([^\"]*)\"");
        Matcher matcher = pattern.matcher(httpResponse);

        if(matcher.find())
        {
            Cili = matcher.group(1);
            size = matcher.group(2);
            return true;
        }
        return false;
    }

    public String getCili()
    {
        return this.Cili;
    }
    public String getSize()
    {
        return this.size;
    }
}
