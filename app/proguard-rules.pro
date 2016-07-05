# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Program Files\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-keepclassmembers class * {
    public <init>(org.json.JSONObject);
}

#把[您的应用包名] 替换成您自己的包名，如"com.example.R$*"。
-keep public class com.hhd.breath.app.R$*{
    public static final int *;
}

# 如"com.example.R$*"
-keep public class com.hhd.breath.app.R$*{
    public static final int *;
}

-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
