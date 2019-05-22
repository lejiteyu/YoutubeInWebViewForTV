package sample.tv.youtubet.youtubetvsample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeIntents;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by i_hfuhsu on 2017/7/11.
 */

public class YoutubeFragment extends Fragment {

    String TAG = YoutubeFragment.class.getName();
    private static final String API_KEY = "AIzaSyBoRXUY2uBnV1RxJEAceUq3Itf2Ref9HgA";
    private String video_idd[] = {"gxmgcXgNWcg"};
    private static List<String> VIDEO_ID2;
    String videoUrl;
    Button play_pause;
    YouTubePlayer youTubePlayer;
    YouTubePlayer.OnInitializedListener onInitializedListener;

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

        View view = inflater.inflate(R.layout.youtube_layout, container, false);
        VIDEO_ID2 = new ArrayList<>();
        for (int i = 0; i < video_idd.length; i++) {
            VIDEO_ID2.add(video_idd[i]);
        }

        //get youtube fragment api
        YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.youtube_layout, youTubePlayerFragment).commit();

        youTubePlayerFragment.initialize(API_KEY, new OnInitializedListener() {

            // YouTubeプレーヤーの初期化成功
            @Override
            public void onInitializationSuccess(Provider provider, YouTubePlayer player, boolean wasRestored) {
                if (!wasRestored) {
                    player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                    player.loadVideos(VIDEO_ID2);
                    player.play();
                    youTubePlayer = player;
                }
            }

            // YouTubeプレーヤーの初期化失敗
            @Override
            public void onInitializationFailure(Provider provider, YouTubeInitializationResult error) {
                // YouTube error
                String errorMessage = error.toString();
                Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                Log.d("errorMessage:", errorMessage);

//                Intent intent = YouTubeIntents.
//                        createPlayVideoIntentWithOptions(getActivity(), VIDEO_ID2.get(0), false, true);
//                startActivity(intent);
/**
 * 參考：https://blog.csdn.net/lsyz0021/article/details/51473194
 * 1.須建立WebView.iml 檔案
 * 2.來源路徑："file:///android_asset/"
 * 3.建立資料夾 assets
 * 4.
 */
                String file="file:///android_asset/";
                Intent intent = new Intent(getActivity(), AssessWeb.class);
                intent.putExtra("message", file+"youtube.html");//"https://staging3.video.friday.tw/act/TV_login/login.html");//
                getActivity().startActivity(intent);
            }
        });
        play_pause = (Button)  view.findViewById(R.id.play_pause);
        play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (youTubePlayer.isPlaying()) {
                    youTubePlayer.pause();
                } else {
                    youTubePlayer.play();
                }
            }
        });

        return view;
    }

    private void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

}
