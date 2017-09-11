package com.bsdenterprise.carlos.anguiano.multimedia.VideoPlayer.Interface;

/**
 * Created by Carlos Anguiano on 07/09/17.
 * For more info contact: c.joseanguiano@gmail.com
 */

public interface MediaPlayerControl {
    void start();

    void pause();

    int getDuration();

    int getCurrentPosition();

    void seekTo(int pos);

    boolean isPlaying();

    int getBufferPercentage();

    boolean canPause();

    boolean canSeekBackward();

    boolean canSeekForward();

    boolean isFullScreen();

    void toggleFullScreen();

    void errorListener();

}