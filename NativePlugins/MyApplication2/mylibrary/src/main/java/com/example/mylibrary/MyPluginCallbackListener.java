package com.example.mylibrary;

/***
 * 回调
 */
public interface MyPluginCallbackListener {

    // 加载完成可以播放了
    void OnPrepared();

    // 网络断开会调用此方法
    void OnCompletion();
}
