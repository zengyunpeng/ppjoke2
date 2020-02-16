package com.teemo.ppjoke2.exoplayer;

import android.app.Application;
import android.view.LayoutInflater;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.teemo.libcommon.AppGloble;
import com.teemo.ppjoke2.R;

public class PageListPlay {
    public SimpleExoPlayer exoPlayer;
    public PlayerView playerView;
    public PlayerControlView controlView;
    public String playUrl;

    public PageListPlay() {
        Application application = AppGloble.getApplication();
        exoPlayer = ExoPlayerFactory.newSimpleInstance(application, new DefaultRenderersFactory(application), new DefaultTrackSelector(), new DefaultLoadControl());

        playerView = (PlayerView) LayoutInflater.from(application).inflate(R.layout.layout_exo_player_view, null, false);

        controlView = (PlayerControlView) LayoutInflater.from(application).inflate(R.layout.layout_exo_player_contorller_view, null, false);


        playerView.setPlayer(exoPlayer);
        controlView.setPlayer(exoPlayer);
        playerView.setTransitionName("exoPlayerView");
        controlView.setTransitionName("exoControlView");
    }

    public void release() {

        if (exoPlayer != null) {
            exoPlayer.setPlayWhenReady(false);
            exoPlayer.stop(true);
            exoPlayer.release();
            exoPlayer = null;
        }

        if (playerView != null) {
            playerView.setPlayer(null);
            playerView = null;
        }

        if (controlView != null) {
            controlView.setPlayer(null);
            controlView.setVisibilityListener(null);
            controlView = null;
        }
    }

    public void switchPlayerView(PlayerView playerView) {
        if (playerView != null && playerView != this.playerView) {
            this.playerView.setPlayer(null);
            playerView.setPlayer(this.exoPlayer);
        } else {
            this.playerView.setPlayer(this.exoPlayer);
        }
    }
}