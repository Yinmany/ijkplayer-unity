using System;
using UnityEngine;
using UnityEngine.UI;

namespace IJKPlayer
{
    public class IJKPlayerBehaviour : MonoBehaviour
    {
        // 秒
        public const int Timeout = 10;

        private IIJKPlayerNative _playerNative;
        private float _time = 0;

        public RawImage renderObject;

        public string live_url = "rtmp://127.0.0.1/live/livestream";

        public bool autoPlay = false;

        public Action OnDisconnect;
        public Action OnPrepared;

        private void Start()
        {
            if (!autoPlay) return;

            Init();
            Play();
        }

        public void Init()
        {
            if (_playerNative != null)
            {
                _playerNative.Dispose();
                _playerNative = null;
            }

            switch (Application.platform)
            {
                case RuntimePlatform.Android:
#if UNITY_ANDROID
                    _playerNative = new IijkPlayerNativeAndroid(live_url);
#endif
                    break;

                case RuntimePlatform.IPhonePlayer:
#if UNITY_IPHONE
                    _playerNative = new IijkPlayerNativeIos(live_url);
#endif
                    renderObject.transform.rotation = Quaternion.Euler(180, 0, 0);
                    break;
            }

            if (_playerNative == null) return;

            _playerNative.OnPreparedAction = OnPreparedAction;
            _playerNative.OnCompletionAction = OnCompletionAction;

            SetPlayerOptions();
            renderObject.texture = _playerNative.UnityExternalTexture;
        }

        private void OnCompletionAction()
        {
            OnDisconnect?.Invoke();
            Debug.Log("网络断开...");
        }

        private void OnPreparedAction()
        {
            OnPrepared?.Invoke();
            Debug.Log("加载完成...");
        }

        private void SetPlayerOptions()
        {
            _playerNative.SetOptionIntValue(IJKFFOptionCategory.Player, "framedrop", 1);
            _playerNative.SetOptionIntValue(IJKFFOptionCategory.Player, "max_cached_duration", 0);
            _playerNative.SetOptionIntValue(IJKFFOptionCategory.Player, "packet-buffering", 0);
            _playerNative.SetOptionIntValue(IJKFFOptionCategory.Player, "start-on-prepared", 1);

            _playerNative.SetOptionIntValue(IJKFFOptionCategory.Format, "http-detect-rangs-support", 0);
            _playerNative.SetOptionIntValue(IJKFFOptionCategory.Format, "probesize", 1024);
            _playerNative.SetOptionIntValue(IJKFFOptionCategory.Format, "infbuf", 1);
            _playerNative.SetOptionValue(IJKFFOptionCategory.Format, "fflags", "nobuffer");

            _playerNative.SetOptionIntValue(IJKFFOptionCategory.Codec, "skip_loop_filter", 48);
        }

        public void Play()
        {
            _playerNative?.PrepareToPlay();
        }

        void Update()
        {
            // 只支持android和ios
            if (Application.platform != RuntimePlatform.Android && Application.platform != RuntimePlatform.IPhonePlayer)
                return;

            if (_playerNative == null)
            {
                return;
            }

            long dur = _playerNative.VideoCachedDuration;

            // 快进
            // if (dur > 1500)
            // {
            //     _playerNative.Speed = 200f;
            // }
            // else
            // {
            //     _playerNative.Speed = 1f;
            // }

            if (dur == 0)
            {
                _time += Time.deltaTime;

                // 缓存没有数据，持续指定秒数后，判定为超时。
                if (_time > Timeout)
                {
                    // 断开连接
                    OnDisconnect?.Invoke();

                    _time = 0;

                    Debug.Log("网络断开,超时...");
                }
            }
            else
            {
                _time = 0;
            }

            _playerNative.UpdateExternalTexture();
        }

        public void ReleasePlayer()
        {
            OnDisconnect = null;
            OnPrepared = null;

            _playerNative?.Dispose();
            _playerNative = null;
        }
    }
}