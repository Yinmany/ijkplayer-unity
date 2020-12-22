package com.example.mylibrary;

import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.Build;
import android.util.Log;
import android.view.Surface;
import com.unity3d.player.UnityPlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class MyPlugin {
    private static final String TAG = "MyPlugin";
    private int _textureId = 0;
    private SurfaceTexture _surfaceTexture;
    private IjkMediaPlayer player = null;
    private Texture2DExt mTexture2DExt;
    private Texture2D mUnityTexture;
    private FBO mFBO;
    private float[] mMVPMatrix = new float[16];

    private MyPluginCallbackListener _listener;

    /***
     * 只调用一次
     */
    public static void __loadLibraries() {
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
    }

    /**
     * 创建扩展贴图
     *
     * @return
     */
    private int createExternalTexture() {
        int[] textureIdContainer = new int[1];
        GLES20.glGenTextures(1, textureIdContainer, 0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                textureIdContainer[0]);

        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        return textureIdContainer[0];
    }

    public void initSDK(MyPluginCallbackListener listener){
        _listener = listener;
    }

    /***
     * 根据播放地址进行初始化播放器
     * @param url
     */
    public void initWithString(String url) {
        try {

            mTexture2DExt = new Texture2DExt(UnityPlayer.currentActivity,0,0);
            _surfaceTexture = new SurfaceTexture(mTexture2DExt.getTextureID());

            mUnityTexture = new Texture2D(UnityPlayer.currentActivity,1280,720);
            mFBO = new FBO(mUnityTexture);
            _textureId = mUnityTexture.getTextureID();

            player = new IjkMediaPlayer();

            player.setSurface(new Surface(_surfaceTexture));

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

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
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
    public void setOptionValue(int categore, String key, String value) {
        player.setOption(categore, key, value);
    }

    /***
     * 设置播放器选择
     * @param categore
     * @param key
     * @param value
     */
    public void setOptionIntValue(int categore, String key, int value) {
        player.setOption(categore, key, value);
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

    /**
     * 更新纹理
     */
    public int updateTexture() {
        synchronized (this){
            _surfaceTexture.updateTexImage();

            Matrix.setIdentityM(mMVPMatrix,0);
            mFBO.FBOBegin();
            GLES20.glViewport(0, 0, 1280 , 720);
            mTexture2DExt.draw(mMVPMatrix);
            mFBO.FBOEnd();

            Point size = new Point();
            if (Build.VERSION.SDK_INT >= 17) {
                UnityPlayer.currentActivity.getWindowManager().getDefaultDisplay().getRealSize(size);
            } else {
                UnityPlayer.currentActivity.getWindowManager().getDefaultDisplay().getSize(size);
            }
            GLES20.glViewport(0,0,size.x,size.y);

            return mUnityTexture.getTextureID();
        }
    }

    /***
     * 获取Unity扩展贴图
     * @return
     */
    public int getUnityExternalTexture() {
        return _textureId;
    }

    /***
     * 释放
     */
    public void release() {

        // 释放扩展贴图
        int[] textureIdContainer = new int[]{
                _textureId
        };

        mTexture2DExt.destory();
        mUnityTexture.destory();
        mFBO.destroy();

        mTexture2DExt = null;
        mUnityTexture = null;
        mFBO = null;

        _surfaceTexture.release();
        _surfaceTexture = null;

        player.release();
        player = null;
        _textureId = 0;
    }
}
