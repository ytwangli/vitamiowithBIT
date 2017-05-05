package com.xbird.olddriver.olddriver.NET;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Ben on 2016/4/20.
 */
public class GETTRUEADDRESS {
    private final String[] head =
            {
                    "http://xfxa.ctfs.ftn.qq.com/",
                    "http://xfsh.ctfs.ftn.qq.com/",
                    "http://xfcd.ctfs.ftn.qq.com/",
                    "http://xa.ctfs.ftn.qq.com/",
                    "http://xa.btfs.ftn.qq.com/",
                    "http://tj.ctfs.ftn.qq.com/",
                    "http://szmail.tfs.ftn.qq.com/",
                    "http://sz.ctfs.ftn.qq.com/",
                    "http://sh.ctfs.ftn.qq.com/",
                    "http://sh.btfs.ftn.qq.com/",
                    "http://hz.ftn.qq.com/",
                    "http://cd.ctfs.ftn.qq.com/",
                     "http://xg.ctfs.ftn.qq.com/",
                    "http://xflx.xabtfs.ftn.qq.com/",
                    "http://xflx.hz.ftn.qq.com/",
                    "http://sh.yun.ftn.qq.com/",
                    "http://1.dc.ftn.qq.com/",
                    "http://nj.disk.ftn.qq.com/",
                     "http://xasrc.ctfs.ftn.qq.com/",
                    "http://cd-btfs.yunup.ftn.qq.com/",
                    "http://xflx.store.cd.qq.com/",
                    "http://xflx.sz.ftn.qq.com/",
                    "http://xfcd.ctfs.ftn.apiv.ga/",
                    "http://xfxa.ctfs.ftn.apiv.ga/"
            };
    private String address;
    private String cookie;

    private String trueAddress;
    public GETTRUEADDRESS(String address,String cookie)
    {
        this.address = address;
        this.cookie = cookie;
        trueAddress ="";
    }
    public boolean getaddress()
    {
        for(int n=0;n<head.length;n++)
        {
            try {
                String u = head[n] + "ftn_handler/" + address + "?compressed=0&dtype=1&fname=m.mkv";
                URL url = new URL(u);

                URLConnection urlConnection = url.openConnection();
                HttpURLConnection httpURLConnection = (HttpURLConnection)urlConnection;

                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("Cookie",cookie);

                httpURLConnection.getInputStream();

                if(httpURLConnection.getResponseCode()==HttpURLConnection.HTTP_OK)
                {
                    trueAddress = u;
                    return true;
                }

            }
            catch (Exception e)
            {
                continue;
            }
        }
        return true;
    }

    public String getTrueAddress()
    {
        return trueAddress;
    }
}
