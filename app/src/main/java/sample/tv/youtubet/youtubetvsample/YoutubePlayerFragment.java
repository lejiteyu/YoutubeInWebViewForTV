package sample.tv.youtubet.youtubetvsample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import java.util.ArrayList;
import java.util.List;

import sample.tv.youtubet.youtubetvsample.YoutubePlayer.TvPlayer;
import sample.tv.youtubet.youtubetvsample.YoutubePlayer.YouTubePlayerView;

public class YoutubePlayerFragment extends Fragment {
    String TAG = YoutubeFragment.class.getName();
    private YouTubePlayerView mPlayer;
    private String vid;
    boolean isplay=false;

    //                                國會頻道    東森新聞      蔡佩軒         鄧芷琪             中天新聞        CDNyputube
    private String video_idd[] = {"MBNStMnw-dg","dxpWqjvEKaM","gxmgcXgNWcg","T4SimnaiktU", "wUPPkSANpyo","-DMBcCfhN4M"};

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.youtube_player_layout, container, false);
        vid="wUPPkSANpyo";

        mPlayer = (YouTubePlayerView) view.findViewById(R.id.player_youtube);
        mPlayer.loadVideo(vid);
        mPlayer.registerCallback(new TvPlayer.Callback() {
            @Override
            public void onCompleted() {
                super.onCompleted();
                mPlayer.loadVideo(vid);
            }
        });
        mPlayer.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                Log.d("kevin","mPlayer keyEvent :"+keyEvent);
                if(keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK ||keyEvent.getKeyCode()==KeyEvent.KEYCODE_ESCAPE){
                    getActivity().onBackPressed();
                    getActivity().finish();
                }
                return getActivity().onKeyDown(keyEvent.getKeyCode(),keyEvent);
            }
        });

        Button playPauseBtn = (Button) view.findViewById(R.id.playpauseBtn);
        playPauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlayer != null) {
                    if(isplay) {
                        mPlayer.play();
                        isplay=false;
                    }
                    else {
                        mPlayer.pause();
                        isplay=true;
                    }
                }
            }
        });
        playPauseBtn.requestFocus();
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPlayer != null) {
            mPlayer.pause();
        }
    }

}
