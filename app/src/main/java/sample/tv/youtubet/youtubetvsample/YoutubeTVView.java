package sample.tv.youtubet.youtubetvsample;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class YoutubeTVView extends WebView {
    String TAG = YoutubeTVView.class.getSimpleName();
    public YoutubeTVView(Context context) {
        super(context);
        init();
    }

    public YoutubeTVView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public YoutubeTVView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        WebSettings webSettings = getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);// 设置允许JS弹窗
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheMaxSize(1024 * 2);
        webSettings.setAppCacheEnabled(true);
        webSettings.setUseWideViewPort(true);//WebView 自適螢幕大小
        webSettings.setLoadWithOverviewMode(true);//WebView 自適螢幕大小
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        addJavascriptInterface(new AndroidtoJs(), "AssessWeb");//AndroidtoJS类对象映射到js的test对象
        String URL = "file:///android_asset/youtube.html";
        loadUrl(URL);
        setFocusable(false);
    }

    public void setLoadUrl(String videoId){
        loadUrl("javascript:showInfoFromJava(\'"+videoId+"\')");
    }

    public void playVideo() {
        //调用js中的函数：showInfoFromJava(msg)
        loadUrl("javascript:playPauseVideo()");
        getplayDuration(this);
        getCurrentTime(this);
    }

    public void getplayDuration(WebView mWebView){
        mWebView.loadUrl("javascript:getDuration()");
    }

    public void getCurrentTime(WebView mWebView){
        mWebView.loadUrl("javascript:getCurrentTime()");
    }

    public class AndroidtoJs extends Object {
        // 定义JS需要调用的方法
        // 被JS调用的方法必须加入@JavascriptInterface注解
        @JavascriptInterface
        public void onListenerPlayerState(String PlayerState) {
            int unstarted=-1;
            int ended=0;
            int playing=1;
            int paused=2;
            int buffering=3;
            int videoCued=5;
            Log.d(TAG,"JS调用了Android的hello方法 PlayerState:"+PlayerState);
        }

        @JavascriptInterface
        public void onListenerPlayerDuration(String Duration) {
            Log.d(TAG,"JS调用了Android的hello方法 Duration:"+Duration);
        }


        @JavascriptInterface
        public void onListenerPlayerCurrentTime(String CurrentTime) {
            Log.d(TAG,"JS调用了Android的hello方法 CurrentTime:"+CurrentTime);
        }
    }
}
