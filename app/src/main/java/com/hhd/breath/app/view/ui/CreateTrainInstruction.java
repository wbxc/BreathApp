package com.hhd.breath.app.view.ui;

import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hhd.breath.app.BaseActivity;
import com.hhd.breath.app.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CreateTrainInstruction extends BaseActivity {

    @Bind(R.id.back_re)
    RelativeLayout layoutBack ;
    @Bind(R.id.topText)
    TextView topText ;
    @Bind(R.id.instructionContent)
    WebView instructionContent ;

    //http://101.201.39.122/ftpuser01/app/shuoming.html
    private String requestUrl = "" ;
    private String strTopText="" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_train_instruction);
        ButterKnife.bind(this);
        requestUrl = getIntent().getExtras().getString("request_url") ;
        strTopText = getIntent().getExtras().getString("str_top_text") ;
        initEvent();

    }


    @Override
    protected void initEvent() {
        super.initEvent();

        topText.setText(strTopText);

        WebSettings mWebSettings = instructionContent.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setSupportZoom(true);
        mWebSettings.setBuiltInZoomControls(true);
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setPluginState(WebSettings.PluginState.ON);
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        mWebSettings.setAllowFileAccess(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }

        instructionContent.setWebChromeClient(new WebChromeClient());
        showProgressDialog("加载中......");
        instructionContent.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                hideProgress();
                Toast.makeText(CreateTrainInstruction.this,"加载错误",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                hideProgress();

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }


        });
        instructionContent.loadUrl(requestUrl);
        layoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(20);
                CreateTrainInstruction.this.finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(20);
        super.onBackPressed();
    }
}
