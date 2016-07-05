package com.hhd.breath.app.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityCollector {

	public static List<Activity> activities = new ArrayList<Activity>() ;

	public static void addActivity(Activity activity){
		activities.add(activity) ;
	}
	
	public static void removeActivity(Activity activity){
		activities.remove(activity) ;
	}
	
	public static void finshAll(){
		for (Activity activity :activities) {
			if (!activity.isFinishing()) {
				activity.finish();
			}
		}
		System.exit(0);
	}
}