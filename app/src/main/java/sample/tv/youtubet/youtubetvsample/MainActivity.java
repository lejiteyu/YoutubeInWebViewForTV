package sample.tv.youtubet.youtubetvsample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;

public class MainActivity extends FragmentActivity {
    Fragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragment = new YoutubeFragment();
        fragment = new YoutubePlayerFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.main_layout, fragment , "YoutubeFragment");
        transaction.commit();

        /**
         * 參考：https://blog.csdn.net/lsyz0021/article/details/51473194
         * 1.須建立WebView.iml 檔案
         * 2.來源路徑："file:///android_asset/"
         * 3.建立資料夾 assets
         * 4.
         */
//        String file="file:///android_asset/";
//        String videoId = "\'T4SimnaiktU\'";
//        Intent intent = new Intent(this, AssessWeb.class);
//        intent.putExtra("message", file+"youtube.html");//"https://staging3.video.friday.tw/act/TV_login/login.html");//
//        intent.putExtra("videoId", videoId);
//        startActivity(intent);
//        finish();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    //@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d("kevin","onKeyDown :"+event);
        Log.d("kevin","onKeyDown :"+keyCode);
        if(fragment instanceof YoutubePlayerFragment) {
            if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_ESCAPE) {
                onBackPressed();
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
