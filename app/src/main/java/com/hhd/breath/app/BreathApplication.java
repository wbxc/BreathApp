package com.hhd.breath.app;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.fb.push.FeedbackPush;


/**
 * Created by Administrator on 2015/11/23.
 */
public class BreathApplication extends Application {



    private static boolean DEBUG = true ;
    @Override
    public void onCreate() {
        super.onCreate();

        //获取APP ID并将以下代码复制到项目Application类onCreate()中，Bugly会为自动检测环境并完成配置：
        CrashReport.initCrashReport(this, "900038004", false);
        // 反馈服务初始化
        // init(false) 在应用中只集成了友盟用户反馈SDK，没有集成友盟消息推送SDK
        FeedbackPush.getInstance(this).init(false);
        /*if (Constants.Config.DEVELOPER_MODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());
        }*/
        initImageLoader(getApplicationContext());
    }

    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }


    public static void  toast(Context context ,boolean flag ,String msg){
        if (flag && isNotEmpty(msg)){
            Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
        }
    }

    public static void  toast(Context context ,String msg){
        if (isNotEmpty(msg)){
            Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
        }
    }



    public static void toastTest(Context context ,String mes){

        if (DEBUG && isNotEmpty(mes)){
            Toast.makeText(context,mes,Toast.LENGTH_SHORT).show();
        }
    }

    private static boolean isNotEmpty(String mes){
        if (mes!=null && !"".equals(mes)){
            return true ;
        }
        return false ;
    }

}
