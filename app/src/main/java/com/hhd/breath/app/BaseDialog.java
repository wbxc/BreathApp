package com.hhd.breath.app;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/1/11.
 */
public class BaseDialog extends Dialog {

    private String content ;
    private String title ;
    private String type ;


    public BaseDialog(Context context) {
        super(context,android.R.style.Theme_Translucent_NoTitleBar);
    }

    public BaseDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public BaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    public void setTitleContent(String title ,String content){
        this.title = title ;
        this.content = content ;
    }

    public BaseDialog(Context mContext,int theme,String type,String content){
        super(mContext,theme);
        this.content = content ;
        this.type = type ;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.5f;
        getWindow().setAttributes(lpWindow);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


        if (type.equals("progress_dialog")){
            setContentView(R.layout.progress_dialog);
            TextView tvWaiting = (TextView) findViewById(R.id.tvWaiting);
            tvWaiting.setText(content);
        }
    }
}
