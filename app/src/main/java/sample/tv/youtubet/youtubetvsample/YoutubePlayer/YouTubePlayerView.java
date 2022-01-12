package sample.tv.youtubet.youtubetvsample.YoutubePlayer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.PlaybackParams;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.widget.Toast;

import com.google.android.youtube.player.internal.s;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class YouTubePlayerView extends AbstractWebPlayer implements PlaybackControls{
    private static final String TAG = YouTubePlayerView.class.getSimpleName();

    private static final String API_KEY = "";
    int isShowControlBar=0;
    int isLoop=0;

    private boolean isVideoStarted;
    private boolean isVideoPlaying;
    private String mVideoId;
    private List<TvPlayer.Callback> mCallbackList = new ArrayList<>();

    private Runnable checkPlaybackStatusRunnable = new Runnable() {
        @Override
        public void run() {
            if (isVideoStarted) {
                runJavascript("if (player && player.ended) { Android.videoEnded(); }");
                runJavascript("if (player) { Android.updatePosition(player.currentTime); }");
                new Handler(Looper.getMainLooper()).postDelayed(this, 200);
            }
            if (isVideoPlaying) {
                runJavascript("if (player) { player.play() }"); // Verify that our video should be playing.
            }
            if (!isVideoPlaying) {
                runJavascript("if (player) { player.pause() }"); // Verify that our video should be paused.
            }
        }
    };

    public YouTubePlayerView(Context context) {
        super(context);
    }

    public YouTubePlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public YouTubePlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public YouTubePlayerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void loadVideo(String videoId) {
        mVideoId = videoId;
        setVideoUrlTo("https://www.youtube.com/embed/" + videoId + "?key="+API_KEY+"&enablejsapi=1&html5=1&controls="+isShowControlBar+"&autoplay="+1+"&loop="+isLoop+"");

    }

    public String getVideoId() {
        return mVideoId;
    }

    //AbstractWebPlayer
    @Override
    protected void onPlayVideo() {
        // Now first, we need to do more JS injection to get the right element.
        Log.d(TAG, "Get ready to play.");
        runJavascript("window.player = document.querySelector('.html5-main-video');");
        runJavascript("player.click();");
        runJavascript("player.play();");
        runJavascript("Android.updateDuration(player.getDuration());");
        setVolume(1);
        isVideoStarted = true;
        isVideoPlaying = true;
        checkPlaybackStatusRunnable.run();
        for (Callback callback : mCallbackList) {
            callback.onStarted();
        }
        AlertDialog.Builder alertDialog =
                new AlertDialog.Builder(getContext());
        alertDialog.setTitle("這是標題");
        alertDialog.setMessage("文字在此");
        alertDialog.setNeutralButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "取消", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    //AbstractWebPlayer
    @Override
    protected void onEndVideo() {
        isVideoStarted = false;
        isVideoPlaying = false;
        for (Callback callback : mCallbackList) {
            callback.onCompleted();
        }
    }

    //PlaybackControls
    @Override
    public void play() {
        runJavascript("player.click();");
        runJavascript("player.playVideo()");
        isVideoPlaying = true;
        checkPlaybackStatusRunnable.run();
        for (Callback callback : mCallbackList) {
            callback.onResumed();
        }

    }

    //AbstractWebPlayer ext TvPlayer
    @Override
    public void registerCallback(Callback callback) {
        Log.d(TAG,"registerCallback:"+callback);
        if (mCallbackList == null) {
            mCallbackList = new ArrayList<>();
        }
        mCallbackList.add(callback);
    }

    //AbstractWebPlayer ext TvPlayer
    @Override
    public void unregisterCallback(Callback callback) {
        if (mCallbackList != null) {
            mCallbackList.remove(callback);
        }
    }

    //AbstractWebPlayer ext TvPlayer
    @Override
    public void setSurface(Surface surface) {

    }

    //PlaybackControls
    @Override
    public void seekTo(long position) {
        skip(position);
    }

    //PlaybackControls
    @Override
    public void setPlaybackParams(PlaybackParams params) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            runJavascript("player.playbackRate = " + params.getSpeed());
        }
    }



    //PlaybackControls
    @Override
    public void setVolume(float volume) {
        runJavascript("player.volume = " + volume);
    }

    //PlaybackControls
    @Override
    public void pause() {
        runJavascript("player.pause()");
        isVideoPlaying = false;
        for (TvPlayer.Callback callback : mCallbackList) {
            callback.onPaused();
        }
    }

    //PlaybackControls
    @Override
    public void skip(long ms) {
        runJavascript("player.currentTime = " + (ms * 1000));
    }
}
