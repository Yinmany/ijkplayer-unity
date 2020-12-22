package com.example.myapplication;

import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.IjkTimedText;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private MyPlugin player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyPlugin.__loadLibraries();

//        new Timer().scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                if(player == null) return;
//
//                long dur = player.getVideoCachedDuration();
//                if(dur> 1000){
//                    player.setSpeed(1.5f);
//                }else{
//                    player.setSpeed(1f);
//                }
//
////                Log.i(TAG,"Dur:" + player.getDuration());
////                Log.i(TAG,"CachedDur:" + player.getVideoCachedDuration());
////                Log.i(TAG,"CurrentPosition:" + player.getCurrentPosition());
////                Log.i(TAG,"Speed:" + player.getSpeed(1));
//            }
//        }, 0, 100);

        final TextureView tv = findViewById(R.id.show_screen);

        tv.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {

            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {

            }
        });

        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    EditText editText = findViewById(R.id.url);
                    String str = editText.getText().toString();

                    if (player != null) {
                        player.release();
                        player = null;
                    }

                    player = new MyPlugin();
                    player.initWithString(str);

                    // 设置为android的
                    player.getPlayer().setSurface(new Surface(tv.getSurfaceTexture()));

                    player.setOptionIntValue(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1);// 跳帧开关
                    player.setOptionIntValue(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "max_cached_duration", 0);
                    player.setOptionIntValue(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "packet-buffering", 0);
                    player.setOptionIntValue(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 1);

                    player.setOptionIntValue(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48);

                    player.setOptionIntValue(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 0);
                    player.setOptionIntValue(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "probesize", 1024);
                    player.setOptionIntValue(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "infbuf", 1);
                    player.setOptionValue(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "fflags", "nobuffer");

                    player.setOptionIntValue(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "reconnect", 1);
                    player.setOptionIntValue(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "timeout", 5000);


                    player.getPlayer().setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(IMediaPlayer iMediaPlayer) {
                            Log.e(TAG,"onPrepared");
                        }
                    });

                    player.getPlayer().setOnTimedTextListener(new IMediaPlayer.OnTimedTextListener() {
                        @Override
                        public void onTimedText(IMediaPlayer iMediaPlayer, IjkTimedText ijkTimedText) {
                            Log.e(TAG,"onTimedText"+ijkTimedText.getText());
                        }
                    });

                    player.getPlayer().setOnErrorListener(new IMediaPlayer.OnErrorListener() {
                        @Override
                        public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
                            Log.e(TAG,"错误："+i+" i1="+i1);
                            return false;
                        }
                    });

                    player.getPlayer().setOnNativeInvokeListener(new IjkMediaPlayer.OnNativeInvokeListener() {
                        @Override
                        public boolean onNativeInvoke(int i, Bundle bundle) {
                            Log.e(TAG,"OnNative====>>>："+i+" bundle"+bundle.toString());
                            return false;
                        }
                    });

                    player.getPlayer().setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(IMediaPlayer iMediaPlayer) {
                            Log.e(TAG,"网络断开....");
                        }
                    });

                    player.getPlayer().setOnBufferingUpdateListener(new IMediaPlayer.OnBufferingUpdateListener() {
                        @Override
                        public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {
                            Log.i(TAG,"BufferingUpdate...."+i);
                        }
                    });

                    player.getPlayer().setOnSeekCompleteListener(new IMediaPlayer.OnSeekCompleteListener() {
                        @Override
                        public void onSeekComplete(IMediaPlayer iMediaPlayer) {
                            Log.e(TAG,"onSeekComplete网络断开....");
                        }
                    });

                    player.getPlayer().setOnInfoListener(new IMediaPlayer.OnInfoListener() {
                        @Override
                        public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
                            Log.e(TAG,"onInfo："+i+" i1="+i1);
                            return false;
                        }
                    });

//                    player.getPlayer().setOnControlMessageListener(new IjkMediaPlayer.OnControlMessageListener() {
//                        @Override
//                        public String onControlResolveSegmentUrl(int i) {
//                            return null;
//                        }
//                    });

                    player.prepareToPlay();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Button btn1 = findViewById(R.id.btn_info);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long dur = player.getVideoCachedDuration();
                if(dur> 1500){
                    player.setSpeed(1.5f);
                }else{
                    player.setSpeed(1f);
                }

                Log.i(TAG,"Dur:" + player.getPlayer().getDuration());
                Log.i(TAG,"CachedDur:" + player.getVideoCachedDuration());
                Log.i(TAG,"CurrentPosition:" + player.getPlayer().getCurrentPosition());
                Log.i(TAG,"Speed:" + player.getPlayer().getSpeed(1));
            }
        });
    }
}
