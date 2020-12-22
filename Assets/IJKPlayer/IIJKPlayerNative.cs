using System;
using UnityEngine;

namespace IJKPlayer
{
    public interface IIJKPlayerNative : IDisposable
    {
        /// <summary>
        /// 播放速度
        /// </summary>
        float Speed { get; set; }
        
        long VideoCachedDuration { get; }
        
        Action OnPreparedAction { get; set; }
        Action OnCompletionAction { get; set; }
        
        Texture2D UnityExternalTexture { get; }

        void UpdateExternalTexture();

        void SetOptionValue(int category, string key, string value);

        void SetOptionIntValue(int category, string key, int value);

        void PrepareToPlay();

        void Play();
    }
}