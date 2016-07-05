package com.hhd.breath.app.utils;

public class StringUtils {

	public static boolean isNotEmpty(String str){
		if(str !=null && !str.equals("") ){
			return true ;
		}
		return false ;
	}
   public static boolean isNotStringEmpty(String str){
		
		if (str.equals("") || str == null) {
			return false ;
		}
		return true ;
	}
   public static boolean publicTopicStringLength(String content){
	   
	   if (isNotEmpty(content)) {
		if (content.length()>=30) {
			return true ;
		}else {
			return false ;
		}
	 }
	 return false ;
   }
   public static boolean replayZhiyouStringLength(String content){
	   if (isNotEmpty(content)) {
			if (content.length()>=10) {
				return true ;
			}else {
				return false ;
			}
		 }
		 return false ;
   }
   
}