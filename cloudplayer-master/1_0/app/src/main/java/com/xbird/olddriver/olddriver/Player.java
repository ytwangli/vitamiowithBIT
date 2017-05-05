package com.xbird.olddriver.olddriver;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;

import java.util.HashMap;
import java.util.Map;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

public class Player extends Activity {

    private VideoView mVideoView;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set window full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_player);

        Bundle data = getIntent().getExtras();
        String address = data.getString("address");
        String cookie = data.getString("cookie");
        context = this;
//        String url = "http://xfcd.ctfs.ftn.qq.com/ftn_handler/ba5ce38bc0c74da97445ed0509434dfbe562d5adf6d1f4f4845bb19d02ec9319ba89718187aa32e3a4b1d609a13a9274a6dd6cf52884d60bfca1a9ad132a8681?compressed=0&dtype=1&fname=m.mkv";
//        String cookie = "FTN5K=fc406104";
        Vitamio.isInitialized(this);
        vitamio_init(address,cookie);


    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    private void vitamio_init(String url, final String cookie) {

        //set cookie
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("headers","Cookie: "+cookie+"\r\n");
        Uri uri = Uri.parse(url);


        mVideoView = (VideoView) findViewById(R.id.player_view);
        mVideoView.setVideoURI(uri,headers);   //设置视频网络地址
        mVideoView.setMediaController(new MediaController(this)); //设置媒体控制器
        mVideoView.setVideoLayout(VideoView.VIDEO_LAYOUT_STRETCH, 0);   //设置视频的缩放参数,这里设置为拉伸
        mVideoView.requestFocus();
        //视频播放器的准备,此时播放器已经准备好了,此处可以设置一下播放速度,播放位置等等
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                //此处设置播放速度为正常速度1
                mediaPlayer.setPlaybackSpeed(1.0f);

            }
        });

        //不能播放时提示并退出
//        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
//            @Override
//            public boolean onError(MediaPlayer mp, int what, int extra) {
//
//                if (mVideoView.getWindowToken() != null) {
//                    int message = what == MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK ? getResources().getIdentifier("VideoView_error_text_invalid_progressive_playback", "string", context.getPackageName()) : getResources().getIdentifier("VideoView_error_text_unknown", "string", context.getPackageName());
//
//                    new AlertDialog.Builder(context).setTitle(getResources().getIdentifier("VideoView_error_title", "string", context.getPackageName())).setMessage(message).setPositiveButton(getResources().getIdentifier("VideoView_error_button", "string", context.getPackageName()), new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int whichButton) {
//                            finish();
//
//                        }
//                    }).setCancelable(false).show();
//                    return true;
//                }
//                return false;
//            }
//        });

        //当播放完成后,从头开始
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
               finish();
            }
        });

    }
}
