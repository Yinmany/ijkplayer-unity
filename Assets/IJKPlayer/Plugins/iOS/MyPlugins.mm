//
//  IJKMediaDemo
//
//  Created by 丧尸喵 on 2020/4/3.
//  Copyright © 2020 bilibili. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <IJKMediaFramework/IJKMediaFramework.h>

static IJKFFMoviePlayerController* _player;

extern "C"
{
    // 初始化SDK
    void init()
    {
        IJKFFOptions *options = [IJKFFOptions optionsByDefault];
        [options setPlayerOptionIntValue:1 forKey:@"framedrop"];
        [options setPlayerOptionIntValue:0 forKey:@"max_cached_duration"];
        [options setPlayerOptionIntValue:0 forKey:@"packet-buffering"];
        [options setPlayerOptionIntValue:1 forKey:@"start-on-prepared"];
        
        [options setCodecOptionIntValue:48 forKey:@"skip_loop_filter"];
        
        [options setFormatOptionIntValue:0 forKey:@"http-detect-rangs-support"];
        [options setFormatOptionIntValue:1024 forKey:@"probesize"];
        [options setFormatOptionIntValue:1 forKey:@"infbuf"];
        [options setFormatOptionValue:@"nobuffer" forKey:@"fflags"];
        
        _player = [IJKFFMoviePlayerController alloc];
        [_player initWithContentURLString:@"rtmp://122.51.160.218/live/livestream" withOptions:options];
        
        [_player prepareToPlay];
        [_player play];
        [_player setShouldShowHudView:true];
    }
    
    void setOptionValue(const int category,const char* key,const char* value)
    {
        [_player setOptionValue:[NSString stringWithCString:value encoding:[NSString defaultCStringEncoding]] forKey:[NSString stringWithCString:key encoding:[NSString defaultCStringEncoding]] ofCategory:(IJKFFOptionCategory)category];
    }

    void setOptionIntValue(const int category,const char* key,const int value)
    {
        [_player setOptionIntValue:value forKey:[NSString stringWithCString:key encoding:[NSString defaultCStringEncoding]] ofCategory: (IJKFFOptionCategory)category];
    }

    long getVideoCachedDuration()
    {
        return [_player getVideoCachedDuration];
    }

    void release(){
        if(_player){
            [_player shutdown];
            _player = nil;
        }
    }

    void initWithString(const char* curl){
        if(_player){
            release();
        }
        
        _player = [IJKFFMoviePlayerController alloc];
        
        NSString* url = [NSString stringWithCString:curl encoding:[NSString defaultCStringEncoding]];
        
        // 生成Options
        IJKFFOptions* ijkOptions = [IJKFFOptions optionsByDefault];
        
        _player = [IJKFFMoviePlayerController alloc];
        
        [_player initWithContentURLString:url withOptions:ijkOptions];
    }

    void prepareToPlay(){
       if(_player)
       {
           [_player prepareToPlay];
       }
    }
    
    void play(){
        if(_player){
            [_player play];
        }
    }
    
    float getSpeed(){
       return  [_player playbackRate];
    }
    
    void setSpeed(float val){
        return [_player setPlaybackRate:val];
    }

    int getUnityExternalTexture()
    {
        return [_player getUnityExternalTexture];
    }
    
    void updateTexture()
    {
//        int position =(int)(_player.currentPlaybackTime + 0.5);
//        int duration =(int)(_player.duration + 0.5);
//        int playableDuration =(int)(_player.playableDuration + 0.5);
//        int i = (int)_player.bufferingProgress;
//        int d = (int)(getVideoCachedDuration());
//        
//        NSLog(@"position:%i,duration:%i, playableDuration:%i,progress:%i,cacheDura:%i",position,duration,playableDuration,i,d);
        
        [_player updateTexture];
    }
}