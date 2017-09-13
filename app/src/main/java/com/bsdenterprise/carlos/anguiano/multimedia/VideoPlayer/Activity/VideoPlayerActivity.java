package com.bsdenterprise.carlos.anguiano.multimedia.VideoPlayer.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import com.bsdenterprise.carlos.anguiano.multimedia.Multimedia.Utils.SquareImageView;
import com.bsdenterprise.carlos.anguiano.multimedia.R;
import com.bsdenterprise.carlos.anguiano.multimedia.VideoPlayer.Interface.IVideo;
import com.bsdenterprise.carlos.anguiano.multimedia.VideoPlayer.Interface.MediaPlayerControl;
import com.bsdenterprise.carlos.anguiano.multimedia.VideoPlayer.Utils.VideoControllerView;
import com.bsdenterprise.carlos.anguiano.multimedia.VideoPlayer.Utils.VideoPresenter;

import java.io.IOException;
import java.util.ArrayList;

public class VideoPlayerActivity extends AppCompatActivity implements IVideo.View, SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, MediaPlayerControl {

    private static final String TAG = VideoPlayerActivity.class.getSimpleName();
    public static final String EXTRA_MEDIA_PATHS_VIDEO = "extra_media_paths_video";
    public static final String EXTRA_NAME = "sendUser";
    public static final String CAPTURE_VIDEO = "capture_video";

    private IVideo.Presenter presenter;
    private Bundle bundle;
    private SurfaceView videoSurface;
    private SurfaceHolder videoHolder;
    private MediaPlayer player;
    private ArrayList<String> mImagePath;
    private String nameUserVideo;
    private Toolbar toolbar;
    private String videoPathString;
    private VideoControllerView controller;
    private boolean activateAlert = false;
    private Uri mImagePathVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: ");
        setContentView(R.layout.activity_videoplayer_multimedia);
        initView();
        initVariable();
        setupToolbar();
        if (getIntent().hasExtra(EXTRA_MEDIA_PATHS_VIDEO)) {
            checkIntent(getIntent().hasExtra(EXTRA_MEDIA_PATHS_VIDEO));
        }
        if (getIntent().hasExtra(CAPTURE_VIDEO)) {
            intentVideoCapture(getIntent().hasExtra(CAPTURE_VIDEO));
        }

    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.i(TAG, "surfaceCreated: ");
        if (!activateAlert) {
            player.setDisplay(surfaceHolder);
            player.prepareAsync();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        Log.i(TAG, "surfaceChanged: ");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.i(TAG, "surfaceDestroyed: ");
    }

    private void setupToolbar() {
        Log.i(TAG, "setupToolbar: ");
        if (getIntent().hasExtra(EXTRA_NAME)) {
            presenter.sendNameToolbar(bundle.getString(EXTRA_NAME));
        } else {
//            nameUserVideo = ApplicationSingleton.getInstance().getString(R.string.sendVideo);
            nameUserVideo = "Viodo";
        }
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
//        String value = ApplicationSingleton.getInstance().getString(R.string.titleMultimedia);
        String value = "videoK";
        String body = String.format(value, nameUserVideo);
        if (actionBar != null) {
            actionBar.show();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            String isnull = "null";
            if (body.contains(isnull)) {
                actionBar.setTitle("Enviar video");
            } else {
                actionBar.setTitle(body);
            }
        }

    }

    private void initView() {
        Log.i(TAG, "initView: ");
        videoSurface = findViewById(R.id.videoSurface);
        toolbar = findViewById(R.id.toolbarVideo);
    }

    private void initVariable() {
        Log.i(TAG, "initVariable: ");
        bundle = getIntent().getExtras();
        videoHolder = videoSurface.getHolder();
        videoHolder.addCallback(this);
        player = new MediaPlayer();
        controller = new VideoControllerView(this);
        presenter = new VideoPresenter(this);
        mImagePath = new ArrayList<>();
    }

    private void checkIntent(boolean data) {
        Log.i(TAG, "checkIntent: ");
        if (data) {
            presenter.checkIntentP(bundle.getStringArrayList(EXTRA_MEDIA_PATHS_VIDEO));
        } else {
            //Si el intent no contiene nada mostrar error (AlertDialog)
            presenter.checkIntentFailed();
        }
    }

    private void intentVideoCapture(boolean b) {
        if (b) {
            mImagePathVideo = Uri.parse(bundle.getString(CAPTURE_VIDEO));
            presenter.checkIntentVideoCapture(mImagePathVideo);
        } else {
            presenter.checkIntentFailed();
        }
    }

    @Override
    public void sendValuePath(ArrayList<String> path) {
        Log.i(TAG, "sendValuePath: ");
        this.mImagePath = path;

        if (mImagePath != null) {
            //Convierte un array en string
            presenter.addDataArray(mImagePath);
        }
    }

    @Override
    public void sendErrorPath(String error) {
        Log.i(TAG, "sendErrorPath: ");
        //Verifica por que este metodo quedara sin funcionar en el envio del sendValuePath
    }

    @Override
    public void showDataToolbar(String string) {
        Log.i(TAG, "showDataToolbar: ");
        nameUserVideo = string;
    }

    @Override
    public void showErrorPathAlert() {
        Log.i(TAG, "showErrorPathAlert: ");
        //Se usa cuando existe un error al mostrar el video
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.error_video);
        alertDialogBuilder.setMessage(R.string.error_occurred);
        alertDialogBuilder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                onBackPressed();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void getValueOfString(String videoPathString) {
        Log.i(TAG, "getValueOfString: ");
        // Toma el valor convertido del Array en string
        this.videoPathString = videoPathString;
        try {
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setDataSource(this, Uri.parse(videoPathString));
            player.setOnPreparedListener(this);
        } catch (IllegalArgumentException | IOException | IllegalStateException | SecurityException e) {
            activateAlert = true;
            errorListener();
        }
    }


    @Override
    public boolean canPause() {
        Log.i(TAG, "canPause: ");
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        Log.i(TAG, "canSeekBackward: ");
        return true;
    }

    @Override
    public boolean canSeekForward() {
        Log.i(TAG, "canSeekForward: ");
        return true;
    }

    @Override
    public int getBufferPercentage() {
        Log.i(TAG, "getBufferPercentage: ");
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        Log.i(TAG, "getCurrentPosition: ");
        return player.getCurrentPosition();
    }

    @Override
    public int getDuration() {
        Log.i(TAG, "getDuration: ");
        return player.getDuration();
    }

    @Override
    public boolean isPlaying() {
        Log.i(TAG, "isPlaying: ");
        return player.isPlaying();
    }

    @Override
    public void pause() {
        Log.i(TAG, "pause: ");
        player.pause();
    }

    @Override
    public void seekTo(int i) {
        Log.i(TAG, "seekTo: ");
        player.seekTo(i);
    }

    @Override
    public void start() {
        Log.i(TAG, "start: ");
        player.start();
    }

    @Override
    public boolean isFullScreen() {
        Log.i(TAG, "isFullScreen: ");
        return false;
    }

    @Override
    public void toggleFullScreen() {
        Log.i(TAG, "toggleFullScreen: ");
    }

    @Override
    public void errorListener() {
        Log.i(TAG, "errorListener: ");
        controller.setErrorVideo();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart: ");
        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            player.setDataSource(this, Uri.parse(videoPathString));
            player.setOnPreparedListener(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (player != null) {
            player.stop(); //error
            player.reset();
            player.release();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "onCreateOptionsMenu: ");
        MenuItem menuItem;
        menuItem = menu.add(Menu.NONE, R.id.info_multimedia, Menu.NONE, R.string.Done);
        menuItem.setIcon(R.drawable.ic_done);
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "onOptionsItemSelected: ");
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            finish();
            return true;
        } else if (id == R.id.info_multimedia) {
            if (videoPathString != null) {
                Intent intent = new Intent(this, SquareImageView.class);
                intent.putExtra(EXTRA_MEDIA_PATHS_VIDEO, mImagePath);
                setResult(RESULT_OK, intent);
                finish();
            }
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG, "onTouchEvent: ");
        controller.show();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        Log.i(TAG, "onPrepared: ");
        controller.setMediaPlayer(this);
        controller.setAnchorView((FrameLayout) findViewById(R.id.videoSurfaceContainer));
        controller.show();
    }
}

