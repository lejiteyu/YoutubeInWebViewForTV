package sample.tv.youtubet.youtubetvsample;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by Lyon on 2015/10/21.
 */
//20160301 word cloud Lyon start
public class AssessWeb extends Activity {
    WebView mWebView = null;
    String TAG = AssessWeb.class.getName();
    //Vickie 20150829 add
    String URL = null;
    Activity activity;
    TextView url_txt;
    public static final String LOG_TAG = "AppsFlyerSampleAppWeb 2";
    Context context;
    //20160330 webView Lyon start
    String dramaapp="m/dramaapp/detail/";
    String movieapp="m/movieapp/detail/";
    String openApp="intent";
    RelativeLayout webViewLayout;
    ImageButton menuBackBtn;
    //                                國會頻道          蔡佩軒         鄧芷琪         東森新聞
    private String video_idd[] = {"MBNStMnw-dg","gxmgcXgNWcg","T4SimnaiktU","dxpWqjvEKaM"};

    String videoId="gxmgcXgNWcg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.assess_web);
        Bundle bundle =this.getIntent().getExtras();
        activity=this;
        context = this;

        webViewLayout = (RelativeLayout) findViewById(R.id.webViewLayout);
        TextView titleView = (TextView) findViewById(R.id.titleView);
        titleView.setText(getIntent().getStringExtra(URL));
        mWebView = new WebView(context){

        };//WebView)findViewById(R.id.webView);

//        URL = "https://www.youtube.com/embed/" + videoId +"enablejsapi=1?autoplay=1&loop=1";//+ "?enablejsapi=1&html5=1";
        URL = "file:///android_asset/youtube.html";

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mWebView.setLayoutParams(params);
        url_txt = (TextView)findViewById(R.id.url_txt);
        url_txt.setTextColor(getResources().getColor(R.color.text_gary_light));
        url_txt.setText(URL);
        url_txt.setVisibility(View.GONE);
        mWebView.setWebViewClient(getWebViewClient());
        mWebView.setWebChromeClient(getWebChromeClient());
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);// 设置允许JS弹窗
        mWebView.addJavascriptInterface(new AndroidtoJs(), "AssessWeb");//AndroidtoJS类对象映射到js的test对象
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheMaxSize(1024 * 2);
        webSettings.setAppCacheEnabled(true);
        webSettings.setUseWideViewPort(true);//WebView 自適螢幕大小
        webSettings.setLoadWithOverviewMode(true);//WebView 自適螢幕大小
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        mWebView.loadUrl(URL);
        mWebView.setFocusable(false);

        webViewLayout.addView(mWebView);
        //20160330 webView Lyon start
        mWebView.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
                    Log.d(TAG,"goback");
                    if(mWebView.canGoBack())
                        mWebView.goBack();
                    return true;
                }
                return false;
            }
        });

        Button button = (Button)findViewById(R.id.sendToJs);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playVideo(mWebView);
            }
        });
        button.requestFocus();

        Button button1 = (Button)findViewById(R.id.play1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.loadUrl("javascript:showInfoFromJava(\'"+video_idd[0]+"\')");
            }
        });

        Button button2 = (Button)findViewById(R.id.play2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.loadUrl("javascript:showInfoFromJava(\'"+video_idd[1]+"\')");
            }
        });

        //20160330 webView Lyon end

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK ||keyCode== KeyEvent.KEYCODE_ESCAPE){
            Log.d(TAG,"onKeyDown KEYCODE_BACK");
//            mWebView.goBack();
//            mWebView.destroy();
            finish();
            return true;
        }
        if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN){
//            if(menuBackBtn.isFocused()){
//                mWebView.requestFocus();
//            }
        }

        return super.onKeyDown(keyCode, event);
    }

    //20160926 Lyon turn off the youtube when finish
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy()");
        //为了使WebView退出时音频或视频关闭
        mWebView.destroy();
    }


    public void onResume(){
        super.onResume();
        mWebView.onResume();
    }

    public void onPause(){
        super.onPause();

        //20160926 Lyon turn off the youtube when finish
        mWebView.onPause();
        try {
            Class.forName("android.webkit.WebView")
                    .getMethod("onPause", (Class[]) null)
                    .invoke(mWebView, (Object[]) null);

        } catch(ClassNotFoundException e) {
            Log.d(TAG,"ClassNotFoundException():"+e);
        } catch(NoSuchMethodException e) {
            Log.d(TAG,"NoSuchMethodException():"+e);
        } catch(InvocationTargetException e) {
            Log.d(TAG,"onDeInvocationTargetExceptionstroy(:"+e);
        } catch (IllegalAccessException e) {
            Log.d(TAG,"IllegalAccessException():"+e);
        }
    }



    WebViewClient getWebViewClient() {
        return new EventWebViewClient();
    }
    WebChromeClient getWebChromeClient() {
        return new EventWebChromeClient();
    }
    private class EventWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int progress) {
            Log.d(TAG,"progress:"+progress);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            Log.d(TAG,"youtube onJsAlert message:"+message);
            new AlertDialog.Builder(context)
                    .setMessage(message)
                    .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            result.confirm();
                        }
                    })
                    .setCancelable(false)
                    .show();
            return true;
        }

        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
            new AlertDialog.Builder(AssessWeb.this)
                    .setMessage(message)
                    .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            result.confirm();
                        }
                    })
                    .setNegativeButton(getString(R.string.check_user_adult_no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            result.cancel();
                        }
                    })
                    .setCancelable(false)
                    .show();
// 返回布尔值：判断点击时确认还是取消
// true表示点击了确认；false表示点击了取消；
            return true;
        }
    }

    private class EventWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            url_txt.setText(url);
            Log.d(TAG,"shouldOverrideUrlLoading:"+url);
                mWebView.loadUrl(url);
            return false ;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            Log.e(TAG,"onReceivedError errorCode:"+errorCode+",description:"+description+" ,failingUrl:"+failingUrl);
        }

        @Override
        public void onReceivedSslError(WebView view, @NonNull SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            Log.e(TAG,"onReceivedSslError error:"+error);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Log.e(TAG,"youtube onPageFinished :"+url);
            //網頁開啟完畢 設定要播放的youtube
            mWebView.loadUrl("javascript:showInfoFromJava(\'"+video_idd[0]+"\')");

        }
    }

    //在java中调用js代码
    public void playVideo(WebView mWebView) {
        //调用js中的函数：showInfoFromJava(msg)
        Log.d(TAG,"playVideo()");
        mWebView.loadUrl("javascript:playPauseVideo()");
        getplayDuration(mWebView);
        getCurrentTime(mWebView);
    }

    public void pauseVideo(WebView mWebView) {
        //调用js中的函数：showInfoFromJava(msg)
        mWebView.loadUrl("javascript:pauseVideo()");
    }

    public void getplayDuration(WebView mWebView){
        mWebView.loadUrl("javascript:getDuration()");
    }

    public void getCurrentTime(WebView mWebView){
        mWebView.loadUrl("javascript:getCurrentTime()");
    }

    public void onListenerPlayerDuration(){

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
            Log.d(TAG,"JS调用了Android的方法 PlayerState:"+PlayerState);
        }

        @JavascriptInterface
        public void onListenerPlayerDuration(String Duration) {
            Log.d(TAG,"JS调用了Android的方法  Duration:"+Duration);
        }


        @JavascriptInterface
        public void onListenerPlayerCurrentTime(String CurrentTime) {
            Log.d(TAG,"JS调用了Android的方法  CurrentTime:"+CurrentTime);
        }

        @JavascriptInterface
        public void showInfoFromJava(String msg){
            Log.d(TAG,"JS调用了Android的方法  showInfoFromJava:"+msg);
        }

        @JavascriptInterface
        public void onPlayerReady(String event){
            Log.d(TAG,"JS调用了Android的方法  onPlayerReady:"+event);
        }
    }
}
