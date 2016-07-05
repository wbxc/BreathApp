package com.hhd.breath.app.net;

import android.os.Bundle;
import android.util.Log;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by familylove on 2016/4/21.
 */
public class HttpUtil {


    private static int BUFFER_SIZE = 8192;
    public static final String PACKET_DATA = "data";
    public static final String PACKET_HEADER = "header";
    public static final String PACKET_RESPONSE_HEADER = "response_header";
    public static Bundle RespHeaderBundle;
    public static String POST = "POST";
    public static String GET = "GET";

    public static JSONObject getJSONByGet(String url, Bundle getParams, int keepAlive) throws Exception {
        String request = "" ;
        if (getParams!=null) {
            StringBuilder body = new StringBuilder();
            for (String key : getParams.keySet()) {
                body.append(key).append("=").append(getParams.getString(key)).append("&");
            }
            String result = body.toString() ;
            request = url+"?"+result.substring(0, result.length()-1) ;
            Log.e("verRunable", request) ;
        }else{
            request = url ;
        }

        return getJSONByPost(request,null, keepAlive);
    }

    public static JSONObject getJSONByPost(String url,Bundle params, int keepAlive) throws Exception {
        URL urlTemp;
        urlTemp = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlTemp.openConnection();
        InputStream in;
        try {
            in = internalPost(connection, params, keepAlive);
            JSONObject json = StreamToJSON(in);
            return json;
        } finally {
            // connection.disconnect();
        }

    }



    private static InputStream internalPost(HttpURLConnection connection, Bundle params, int keepAlive)
            throws Exception {
        connection.setConnectTimeout(30000);
        connection.setReadTimeout(30000);
        if (keepAlive > 0) {
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Keep-Alive", String.valueOf(keepAlive));
        }
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        if (params != null) {
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            StringBuilder body = new StringBuilder();
            for (String key : params.keySet()) {
                body.append(key).append("=").append(params.getString(key)).append("&");
            }
            String result = body.toString() ;

            if (result.length()>0) {
                result = result.substring(0, result.length()-1) ;
            }
            Log.e("result_post", result) ;

            byte[] bytes = body.toString().getBytes();
            connection.getOutputStream().write(bytes);
        }
        connection.connect();
        InputStream is = connection.getInputStream();
        return is;
    }

    private static String InputStreamToString(InputStream in, String encoding) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        int count = -1;
        while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
            outStream.write(data, 0, count);
        data = null;
        return new String(outStream.toByteArray(), encoding);
    }

    private static JSONObject StreamToJSON(InputStream in) throws Exception {
        String content = InputStreamToString(in, "UTF-8");
        if (content == null) {
            return null;
        } else if (content.equals("")) {
            JSONObject jo = new JSONObject();
            return jo;
        }
        JSONObject json = new JSONObject(content);
        return json;
    }

}
