package com.example.myapplication;

import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.Build;
import android.view.Surface;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class MyPlugin {
    private static final String TAG = "MyPlugin";

    private IjkMediaPlayer player = null;
    private MyPluginCallbackListener _listener;
    public IjkMediaPlayer getPlayer()
    {
        return player;
    }

    /***
     * 只调用一次
     */
    public static void __loadLibraries() {
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
    }

    public void initSDK(MyPluginCallbackListener listener){
        _listener = listener;
    }

    /***
     * 根据播放地址进行初始化播放器
     * @param url
     */
    public int initWithString(String url) {

        try{
            player = new IjkMediaPlayer();

            player.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(IMediaPlayer iMediaPlayer) {
                    if(_listener != null){
                        _listener.OnPrepared();
                    }
                    player.start();
                }
            });

            // 网络断开，会调用此接口。
            player.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(IMediaPlayer iMediaPlayer) {
                    if(_listener != null){
                        _listener.OnCompletion();
                    }
                }
            });

            player.setDataSource(url);
        } catch (Exception e)
        {
            return -2;
        }

        return 0;
    }

    /***
     * 加载并播放
     */
    public void prepareToPlay() {
        player.prepareAsync();
    }

    /***
     * 播放
     */
    public void play() {
        player.start();
    }

    /***
     * 设置播放器选项
     * @param categore
     * @param key
     * @param value
     */
    public int setOptionValue(int categore, String key, String value) {
        if(player == null)
            return  -1;
        player.setOption(categore, key, value);
        return  0;
    }

    /***
     * 设置播放器选择
     * @param categore
     * @param key
     * @param value
     */
    public int setOptionIntValue(int categore, String key, int value) {
        if(player == null)
        {
            return -1;
        }
        player.setOption(categore, key, value);
        return 0;
    }

    /**
     * 获取缓存中的可播放的时间
     *
     * @return
     */
    public long getVideoCachedDuration() {
        return player.getVideoCachedDuration();
    }

    /**
     * 播放速度
     *
     * @return
     */
    public float getSpeed() {
        return player.getSpeed(0);
    }

    /***
     * 设置播放速度
     * @param val
     */
    public void setSpeed(float val) {
        player.setSpeed(val);
    }

    /***
     * 释放
     */
    public int release() {

//        try{
//            mTexture2DExt.destory();
//            mTexture2DExt = null;
//
//            mUnityTexture.destory();
//            mUnityTexture = null;
//
//            mFBO.destroy();
//            mFBO = null;
//
//            _surfaceTexture.release();
//            _surfaceTexture = null;
//        } catch (Exception e)
//        {
//            return -1;
//        }

        player.reset();
        player.release();
        player = null;

        return 0;
    }
}
