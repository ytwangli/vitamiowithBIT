package com.xbird.olddriver.olddriver;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.xbird.olddriver.olddriver.NET.ParseCILI;
import com.xbird.olddriver.olddriver.NET.VIDEO;

public class VIDEOINFO_activity extends AppCompatActivity {

    private VIDEO video;
    private Button play;
    private TextView tv_name;
    private TextView tv_size;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoinfo_activity);

        context = this;
        Bundle bundle = getIntent().getExtras();
        video = new VIDEO();
        video.name = bundle.getString("name");
        video.CILI = bundle.getString("cili");
        video.ID = bundle.getString("id");
        video.size = bundle.getString("size");

        play = (Button)findViewById(R.id.bt_play);
        tv_name = (TextView)findViewById(R.id.edt_name);
        tv_size = (TextView)findViewById(R.id.edt_sie);
        play.setEnabled(false);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VIDEOINFO_activity.this, Player.class);
                Bundle data = new Bundle();
                data.putString("address", video.fullAddress);
                data.putString("cookie", video.cookie);
                intent.putExtras(data);
                startActivity(intent);
            }
        });
        tv_name.setText(video.name);
        tv_size.setText(video.size);

        new GETVIDEOINFO().execute();
    }

    class GETVIDEOINFO extends AsyncTask<String,String,String>
    {
        private ParseCILI parseCILI;
        @Override
        protected String doInBackground(String... params) {

            parseCILI = new ParseCILI(video.CILI);
            if(parseCILI.getInfo())
            {
                video.address = parseCILI.getAddress();
                video.cookie = parseCILI.getCookie();
                publishProgress();
            }
            else
            {
                publishProgress("1");
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            if(values.length==0)
            {
                Toast.makeText(context,"正在获得种子真实地址。。",Toast.LENGTH_LONG).show();
               new GETTRUEADDRESS().execute();
            }
            else
            {
                showWrong();
            }
            super.onProgressUpdate(values);
        }
    }

    class GETTRUEADDRESS extends AsyncTask<String,String,String>
    {

        private com.xbird.olddriver.olddriver.NET.GETTRUEADDRESS gettrueaddress;
        @Override
        protected String doInBackground(String... params) {
            gettrueaddress = new com.xbird.olddriver.olddriver.NET.GETTRUEADDRESS(video.address,video.cookie);
            if(gettrueaddress.getaddress())
            {
                video.fullAddress = gettrueaddress.getTrueAddress();
                publishProgress();
            }
            else
                publishProgress("1");
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            if(values.length==0)
            {

                play.setEnabled(true);
//                Intent intent = new Intent(VIDEOINFO_activity.this, Player.class);
//                Bundle data = new Bundle();
//                data.putString("address", video.fullAddress);
//                data.putString("cookie", video.cookie);
//                intent.putExtras(data);
//                startActivity(intent);
            }
            else
            {
                showWrong();
            }
            super.onProgressUpdate(values);
        }
    }
    public void showWrong()
    {
        Toast.makeText(this,"解析失败",Toast.LENGTH_LONG).show();

    }
}
