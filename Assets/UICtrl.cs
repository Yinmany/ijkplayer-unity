using System.Collections;
using System.Collections.Generic;
using IJKPlayer;
using UnityEngine;
using UnityEngine.UI;

public class UICtrl : MonoBehaviour
{
    public Button startBtn;
    public Button releaseBtn;
    public InputField input;
    public IJKPlayerBehaviour player;

    void Start()
    {
        startBtn.onClick.AddListener(() =>
        {
            player.live_url = input.text;
            player.Init();
            player.Play();
        });

        releaseBtn.onClick.AddListener(() => { player.ReleasePlayer(); });
    }

    void Update()
    {
    }
}