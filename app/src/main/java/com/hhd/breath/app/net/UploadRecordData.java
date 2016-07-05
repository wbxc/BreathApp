package com.hhd.breath.app.net;

import android.util.Log;

import com.hhd.breath.app.CommonValues;
import com.hhd.breath.app.model.BreathTrainingResult;
import com.hhd.breath.app.model.RecordUnitData;
import com.hhd.breath.app.utils.FileUtils;
import com.hhd.breath.app.utils.MD5Util;
import com.hhd.breath.app.utils.ZipUtil;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 上传工具类
 *
 * @author spring sky<br>
 *         Email :vipa1888@163.com<br>
 *         QQ: 840950105<br>
 *         支持上传文件和参数
 */
public class UploadRecordData {
    private static UploadRecordData uploadUtil;
    private static final String BOUNDARY = UUID.randomUUID().toString(); // 边界标识
    // 随机生成
    private static final String PREFIX = "--";
    private static final String LINE_END = "\r\n";
    //Content-Type: application/zip
    private static final String CONTENT_TYPE = "multipart/form-data"; // 内容类型

    private int readTimeOut = 10 * 1000; // 读取超时
    private int connectTimeout = 10 * 1000; // 超时时间
    /***
     * 请求使用多长时间
     */
    private static int requestTime = 0;

    private static final String CHARSET = "utf-8"; // 设置编码



    private UploadRecordData() {

    }

    /**
     * 单例模式获取上传工具类
     *
     * @return
     */
    public static UploadRecordData getInstance() {
        if (null == uploadUtil) {
            uploadUtil = new UploadRecordData();
        }
        return uploadUtil;
    }



    /***
     * 上传成功
     */
    public static final int UPLOAD_SUCCESS_CODE = 1;
    /**
     * 文件不存在
     */
    public static final int UPLOAD_FILE_NOT_EXISTS_CODE = 2;
    /**
     * 服务器出错
     */
    public static final int UPLOAD_SERVER_ERROR_CODE = 3;

    protected static final int WHAT_TO_UPLOAD = 1;
    protected static final int WHAT_UPLOAD_DONE = 2;

    /**
     * android上传文件到服务器
     *
     * @param filePath   需要上传的文件的路径
     * @param fileKey    在网页上<input type=file name=xxx/> xxx就是这里的fileKey
     * @param RequestURL 请求的URL
     */
    public void uploadFile(String filePath, String fileKey, String RequestURL, Map<String, String> param) {
        if (filePath == null) {
            sendMessage(UPLOAD_FILE_NOT_EXISTS_CODE, "文件不存在");
            return;
        }
        try {
            File file = new File(filePath);
            uploadFile(file, fileKey, RequestURL, param);
        } catch (Exception e) {
            sendMessage(UPLOAD_FILE_NOT_EXISTS_CODE, "文件不存在");
            e.printStackTrace();
            return;
        }
    }

    /**
     * 上传数据
     *
     * @param breathTrainingResult
     */
    public void uploadRecordData(BreathTrainingResult breathTrainingResult) {
        Map<String,String> params = new HashMap<String,String>() ;
        StringBuffer sb = new StringBuffer() ;
        sb.append("user_id").append("=").append(breathTrainingResult.getUser_id()).append("&")
                .append("breath_type").append("=").append(breathTrainingResult.getBreath_type()).append("&")
                .append("train_group").append("=").append(breathTrainingResult.getTrain_group()).append("&")
                .append("train_time").append("=").append(breathTrainingResult.getTrain_time()).append("&")
                .append("train_last").append("=").append(breathTrainingResult.getTrain_last()).append("&")
                .append("train_result").append("=").append(breathTrainingResult.getTrain_result()).append("&")
                .append("difficulty").append("=").append(breathTrainingResult.getDifficulty()).append("&")
                .append("suggestion").append("=").append(breathTrainingResult.getDifficulty()).append("&")
                .append("platform").append("=").append(breathTrainingResult.getPlatform()).append("&")
                .append("device_sn").append("=").append(breathTrainingResult.getDevice_sn()) ;

        //路径是     用户id+/+测试的文件名称+/+文件
        String filepath =CommonValues.PATH_ZIP+breathTrainingResult.getUser_id()+"/"+breathTrainingResult.getFname();
        String fileName = breathTrainingResult.getFname() ;
        writeTextFile(sb.toString(),filepath+"/",fileName);
        String file_zip_path = CommonValues.PATH_ZIP+breathTrainingResult.getUser_id()+"/"+fileName+"_zip" ;
        String file_zip = file_zip_path+"/"+fileName+".zip" ;
        File zipFilePath = new File(file_zip_path) ;
        if (!zipFilePath.exists())
            zipFilePath.mkdirs() ;
        try {
            ZipUtil.zipFolder(filepath,file_zip);
            File zipFile = new File(file_zip) ;

            params.put("file_size",FileUtils.getFileSize(file_zip)+"") ;
            //params.put("file_type",FileUtils.getMimeType(file_zip)) ;
            //
            params.put("file_type","application/zip") ;

            params.put("file_md5",MD5Util.getFileMD5String(new File(file_zip))) ;
            params.put("file_id",breathTrainingResult.getFile_id()) ; // userid_filename
            //params.put("fname",fileName) ;

            params.put("user_id",breathTrainingResult.getUser_id()) ;
            params.put("breath_type",breathTrainingResult.getBreath_type()) ;
            params.put("train_group",breathTrainingResult.getTrain_group()) ;
            params.put("train_time",breathTrainingResult.getTrain_time()) ;
            params.put("train_last",breathTrainingResult.getTrain_last()) ;
            params.put("train_result",breathTrainingResult.getTrain_result()) ;
            params.put("difficulty",breathTrainingResult.getDifficulty()) ;
            params.put("suggestion",breathTrainingResult.getSuggestion()) ;
            params.put("platform",breathTrainingResult.getPlatform()) ;
            params.put("device_sn",breathTrainingResult.getDevice_sn()) ;
            //fname 是key
            mUploadRunnable = new UploadRunnable(zipFile,"fname",NetConfig.URL_UPLOAD_RECORD,params) ;
            new Thread(mUploadRunnable).start();
            //ThreadPoolWrap.getThreadPool().executeTask(mUploadRunnable);
        }catch (Exception e){

        }
    }



    public void zip(){
        String filepath =CommonValues.PATH_ZIP+"/"+"test" ;
        String filepath2 =CommonValues.PATH_ZIP+"/"+"test1.zip" ;
        try {
           // ZipUtil.zipFolder(filepath,filepath2);
        }catch (Exception e){
            Log.e("uploadRecordData" , "fail") ;
        }
    }
    private void writeTextFile(String stringContent , String filePath , String fileName){

        makeFile(filePath,fileName);
        try {

            File file = new File(filePath+fileName) ;
            if (!file.exists()){
                file.getParentFile().mkdirs() ;
                file.createNewFile() ;
            }

            RandomAccessFile raf = new RandomAccessFile(file,"rwd") ;
            raf.seek(file.length());
            raf.write(stringContent.getBytes());
            raf.close();
        }catch (Exception e){


        }


    }

    /**
     * 生成文件
     * @param filePath
     * @param fileName
     */
    private void makeFile(String filePath , String fileName){
        makeRootDirectory(filePath);
        File file = null;
        try {

            file = new File(filePath+fileName) ;
            if (file.exists()){
                file.createNewFile() ;
            }
        }catch (Exception e){

        }finally {

        }
    }
    // 生成文件夹
    private void makeRootDirectory(String filePath){

        File file = null ;
        try {
            file = new File(filePath) ;
            if (!file.exists()){
                file.mkdir() ;
            }
        }catch (Exception e){

        }
    }




    private void writeFileSdcard(String fileName, String message) {
        try {
            FileOutputStream fout = new FileOutputStream(fileName);
            byte[] bytes = message.getBytes();
            fout.write(bytes);
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private UploadRunnable mUploadRunnable = null;

    /**
     * android上传文件到服务器
     *
     * @param file       需要上传的文件
     * @param fileKey    在网页上<input type=file name=xxx/> xxx就是这里的fileKey
     * @param RequestURL 请求的URL
     */
    public void uploadFile(final File file, final String fileKey, final String RequestURL,
                           final Map<String, String> param) {
        if (file == null || (!file.exists())) {
            sendMessage(UPLOAD_FILE_NOT_EXISTS_CODE, "文件不存在");
            return;
        }
        mUploadRunnable = new UploadRunnable(file, fileKey, RequestURL, param);
        ThreadPoolWrap.getThreadPool().executeTask(mUploadRunnable);
    }

    /**
     * 文件数据上传
     */
    private class UploadRunnable implements Runnable {

        private File file;
        private String fileKey;
        private String requestUrl;
        private Map<String, String> param;

        public UploadRunnable() {
        }

        public UploadRunnable(File file, String fileKey, String requestUrl, Map<String, String> param) {

            this.file = file;
            this.fileKey = fileKey;
            this.requestUrl = requestUrl;
            this.param = param;
        }

        @Override
        public void run() {
            toUploadFile(file, fileKey, requestUrl, param);
        }
    }


    private void toUploadFile(File file, String fileKey, String RequestURL, Map<String, String> param) {
        String result = null;
        requestTime = 0;
        long requestTime = System.currentTimeMillis();
        long responseTime = 0;
        try {
            URL url = new URL(RequestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(readTimeOut);
            conn.setConnectTimeout(connectTimeout);
            conn.setDoInput(true); // 允许输入流
            conn.setDoOutput(true); // 允许输出流
            conn.setUseCaches(false); // 不允许使用缓存
            conn.setRequestMethod("POST"); // 请求方式
            conn.setRequestProperty("Charset", CHARSET); // 设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
            // conn.setRequestProperty("Content-Type",
            // "application/x-www-form-urlencoded");

            /**
             * 当文件不为空，把文件包装并且上传
             */
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            StringBuffer sb = null;
            String params = "";

            /***
             * 以下是用于上传参数
             *
             * name = user_id
             */
            if (param != null && param.size() > 0) {
                Iterator<String> it = param.keySet().iterator();
                while (it.hasNext()) {
                    sb = null;
                    sb = new StringBuffer();
                    String key = it.next();
                    String value = param.get(key);
                    sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
                    sb.append("Content-Disposition: form-data; name=\"").append(key).append("\"").append(LINE_END)
                            .append(LINE_END);
                    sb.append(value).append(LINE_END);
                    params = sb.toString();
                    dos.write(params.getBytes());
                    // dos.flush();
                }
            }

            sb = null;
            params = null;
            sb = new StringBuffer();

		/*	Content-Disposition: form-data; name="fname"; filename="å½æ¡£.zip"
			Content-Type: application/zip
			*/

            /**
             * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件   fname
             * filename是文件的名字，包含后缀名的 比如:abc.png
             */
            sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
            sb.append("Content-Disposition:form-data; name=\"" + fileKey + "\"; filename=\"" + file.getName() + "\"" + LINE_END);
            sb.append("Content-Type:application/zip" + LINE_END); // 这里配置的Content-type很重要的
            // ，用于服务器端辨别文件的类型的
            sb.append(LINE_END);
            params = sb.toString();
            sb = null;

            dos.write(params.getBytes());
            /** 上传文件 */
            InputStream is = new FileInputStream(file);
            onUploadProcessListener.initUpload((int) file.length());
            byte[] bytes = new byte[1024];
            int len = 0;
            int curLen = 0;
            while ((len = is.read(bytes)) != -1) {
                curLen += len;
                dos.write(bytes, 0, len);
                onUploadProcessListener.onUploadProcess(curLen);
            }
            is.close();

            dos.write(LINE_END.getBytes());
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
            dos.write(end_data);
            dos.flush();
            //
            // dos.write(tempOutputStream.toByteArray());
            /**
             * 获取响应码 200=成功 当响应成功，获取响应的流
             */
            int res = conn.getResponseCode();
            responseTime = System.currentTimeMillis();
            this.requestTime = (int) ((responseTime - requestTime) / 1000);
            if (res == 200) {
                InputStream input = conn.getInputStream();
                StringBuffer sb1 = new StringBuffer();
                int ss;
                while ((ss = input.read()) != -1) {
                    sb1.append((char) ss);
                }
                result = sb1.toString();
                sendMessage(UPLOAD_SUCCESS_CODE, result);
                return;
            } else {
                sendMessage(UPLOAD_SERVER_ERROR_CODE, result);
                return;
            }
        } catch (MalformedURLException e) {
            sendMessage(UPLOAD_SERVER_ERROR_CODE, result);
            e.printStackTrace();
            return;
        } catch (IOException e) {
            sendMessage(UPLOAD_SERVER_ERROR_CODE, result);
            e.printStackTrace();
            return;
        }
    }

    public void removeRunnable() {

        if (mUploadRunnable!=null){
            ThreadPoolWrap.getThreadPool().removeTask(mUploadRunnable);
        }
    }

    /**
     * 发送上传结果
     *
     * @param responseCode
     * @param responseMessage
     */
    private void sendMessage(int responseCode, String responseMessage) {
        onUploadProcessListener.onUploadDone(responseCode, responseMessage);
    }

    /**
     * 下面是一个自定义的回调函数，用到回调上传文件是否完成
     *
     * @author shimingzheng
     */
    public interface OnUploadProcessListener {
        /**
         * 上传响应
         *
         * @param responseCode
         * @param message
         */
        void onUploadDone(int responseCode, String message);

        /**
         * 上传中
         *
         * @param uploadSize
         */
        void onUploadProcess(int uploadSize);

        /**
         * 准备上传
         *
         * @param fileSize
         */
        void initUpload(int fileSize);
    }

    private OnUploadProcessListener onUploadProcessListener;

    public void setOnUploadProcessListener(OnUploadProcessListener onUploadProcessListener) {
        this.onUploadProcessListener = onUploadProcessListener;
    }

    public int getReadTimeOut() {
        return readTimeOut;
    }

    public void setReadTimeOut(int readTimeOut) {
        this.readTimeOut = readTimeOut;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    /**
     * 获取上传使用的时间
     *
     * @return
     */
    public static int getRequestTime() {
        return requestTime;
    }


}