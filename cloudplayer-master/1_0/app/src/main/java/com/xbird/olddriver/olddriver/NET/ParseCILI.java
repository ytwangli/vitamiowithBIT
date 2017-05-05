package com.xbird.olddriver.olddriver.NET;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ben on 2016/4/20.
 */
public class ParseCILI {
    private String CILI;
    private String httpResponse;
    private String cookie;
    private String address;

    public ParseCILI(String cili)
    {
        this.CILI = cili;
        cookie = "";
        address = "";
    }

    public boolean getInfo()
    {
        if(connect())
        {
            if(RegExp())
                return true;
        }
        return false;
        
    }


    private boolean connect() {

        try
        {
//            URL url = new URL(CPURL.ParseCILI+this.CILI);
//
//            URLConnection urlConnection = url.openConnection();
//            HttpURLConnection httpURLConnection = (HttpURLConnection)urlConnection;
//
//            httpURLConnection.setDoInput(true);
//            httpURLConnection.setRequestMethod("GET");
//
//            InputStream is = httpURLConnection.getInputStream();
//
//            BufferedReader bf = new BufferedReader(new InputStreamReader(is));
//            StringBuffer sb = new StringBuffer("");
//            while(bf.ready())
//            {
//                sb.append(bf.readLine());
//            }
//
//            httpResponse = sb.toString();

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(CPURL.ParseCILI+CILI).build();
            Response response = client.newCall(request).execute();
            if(response.isSuccessful())
            {
                httpResponse = response.body().string();
            }
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }
    private boolean RegExp() {

        Pattern pattern = Pattern.compile("\"magnet\": \"([^\"]+)\",\"([^\"]+)\": \"([^\"]+)\"");
        Matcher matcher = pattern.matcher(httpResponse);
        if(matcher.find())
        {
            address = matcher.group(1);
            cookie = matcher.group(2)+"="+matcher.group(3);
            return true;
        }
        return false;
    }

    public String getAddress()
    {
        return this.address;
    }
    public String getCookie()
    {
        return this.cookie;
    }
}
