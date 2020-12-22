using System;
using System.Runtime.InteropServices;
using UnityEngine;

namespace IJKPlayer
{
    /// <summary>
    /// IOS实现
    /// </summary>
    public class IijkPlayerNativeIos : IIJKPlayerNative
    {
        #region IOS Native API

        [DllImport("__Internal")]
        private static extern void setOptionValue(int category, string key, string value);

        [DllImport("__Internal")]
        private static extern void setOptionIntValue(int category, string key, int value);

        [DllImport("__Internal")]
        private static extern long getVideoCachedDuration();

        [DllImport("__Internal")]
        private static extern void release();

        [DllImport("__Internal")]
        private static extern void initWithString(string url);

        [DllImport("__Internal")]
        private static extern void prepareToPlay();

        [DllImport("__Internal")]
        private static extern void play();

        [DllImport("__Internal")]
        private static extern float getSpeed();

        [DllImport("__Inernal")]
        private static extern void setSpeed(float value);

        [DllImport("__Internal")]
        private static extern void updateTexture();

        [DllImport("__Internal")]
        private static extern int getUnityExternalTexture();

        #endregion

        private Texture2D _texture;

        public float Speed
        {
            get => getSpeed();
            set => setSpeed(value);
        }

        public long VideoCachedDuration => getVideoCachedDuration();
        public Action OnPreparedAction { get; set; }
        public Action OnCompletionAction { get; set; }

        public Texture2D UnityExternalTexture
        {
            get
            {
                if (_texture) return _texture;

                var id = (IntPtr) getUnityExternalTexture();
                if (id != IntPtr.Zero)
                {
                    _texture = Texture2D.CreateExternalTexture(1920, 1080, TextureFormat.RGB24, false, true, id);
                }

                return _texture;
            }
        }

        public IijkPlayerNativeIos(string url)
        {
            initWithString(url);
        }

        public void SetOptionValue(int category, string key, string value)
        {
            setOptionValue(category, key, value);
        }

        public void SetOptionIntValue(int category, string key, int value)
        {
            setOptionIntValue(category, key, value);
        }

        public void PrepareToPlay()
        {
            prepareToPlay();
        }

        public void Play()
        {
            play();
        }

        public void UpdateExternalTexture()
        {
            updateTexture();
        }

        public void Dispose()
        {
            release();
            _texture = null;
        }
    }
}