package com.hhd.breath.app.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;

/**
 * 文件管理类
 */
public class FileUtils {

    /**
     * 获取文件的类型
     * @param fileUrl
     * @return
     * @throws java.io.IOException
     */
    public static String getMimeType(String fileUrl)
            throws java.io.IOException {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String type = fileNameMap.getContentTypeFor(fileUrl);

        return type;
    }


    /**
     * 获取文件的大小
     * @param filePath
     * @return
     */
    public static long getFileSize(String filePath){
        File file = new File(filePath) ;
        return file.length() ;
    }

    public static int getFileSizeInt(String filePath){
        FileInputStream fis= null;
        try{
            File f= new File(filePath);
            fis= new FileInputStream(f);
            return fis.available() ;
        }catch(Exception e){
        } finally{
            if (null!=fis){
                try {
                    fis.close();
                } catch (IOException e) {
                }
            }
        }
        return  0 ;
    }

    /**
     * 删除文件
     * @param filePath
     * @return
     */
    private static boolean deleteFile(String filePath){

        File file = new File(filePath) ;
        if (file.isFile() && file.exists()){
            return file.delete() ;
        }
        return false ;
    }

    /**
     * 删除文件夹
     * @param filesPath
     * @return
     */
    private static boolean deleteDirectory(String filesPath){

        if (!filesPath.endsWith(File.separator)){
            filesPath = filesPath+File.separator ;
        }
        File dirFile = new File(filesPath) ;
        if (!dirFile.exists() || !dirFile.isDirectory())
            return false ;

        File[] files = dirFile.listFiles() ;
        for (int i=0 ; i<files.length ; i++){
            if (files[i].isFile())
                deleteFile(files[i].getAbsolutePath()) ;
            else
                deleteDirectory(files[i].getAbsolutePath()) ;
        }
        return dirFile.delete() ;
    }


    /**
     * 删除文件或者文件夹
     * @param filePath
     * @return
     */
    public static boolean deleteFolder(String filePath){

        File file = new File(filePath) ;
        if (!file.exists()){
            return false ;
        }else {

            if (file.isFile())
                return deleteFile(filePath) ;
            else
                return deleteDirectory(filePath) ;
        }
    }
}