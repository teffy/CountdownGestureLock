package com.example.timelock.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.example.timelock.GestureActivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;

/**
 * Application
 * 
 * @File_name: MApplication.java
 * @Package_name: 
 * @Author teffy
 * @Date : 2013-04-10下午12:07:09
 * @Version 1.0
 */
public class MApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		getDensity();
	}
	
	public static  int width;
	public static  int height;
	public static  float density;
	public static  int densityDpi;
	/**
	 * 根据构造函数获得当前手机的屏幕系数
	 */
	public void getDensity() {
		// 获取当前屏幕
		DisplayMetrics dm = new DisplayMetrics();
		dm = getApplicationContext().getResources().getDisplayMetrics();

		width = dm.widthPixels;
		height = dm.heightPixels;
		density = dm.density;
		densityDpi = dm.densityDpi;

	}
	
	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}
	
	// 上一次触碰时间
	long lastTimeMillis = 0;
	public void setLastTouchTime(){
		// 最新的触碰时间
		long currentTimeMillis = System.currentTimeMillis();
		if(lastTimeMillis == 0){
			// 第一次触碰
			lastTimeMillis = currentTimeMillis;
			// 开启监听
			startVerify();
		}else{
			//时间差
			long temp = currentTimeMillis - lastTimeMillis;
			// 如果时间差小于5分钟，就先停掉前一次的监听，再重新开启
			if(temp < 1000 * 60 * 5){
				stopVerify();
				startVerify();
			}
			// else 如果大于,那么上一次的监听在运行着，5分钟之后自然会锁定
		}
	}
	
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			verify();
			ToastUtil.showTest(getApplicationContext(), "30秒锁定一次");
		}
	};
	/**
	 * 开启检测界面
	 * 
	 * @param 
	 * @return void
	 * @throws
	 */
	public void verify() {
		boolean isTopRunning = isRunningForeground(getApplicationContext());
		if(isTopRunning){
			// 判断检测界面是否已经运行
			if(!GestureActivity.IS_SHOW){
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), GestureActivity.class);
				intent.putExtra(GestureActivity.INTENT_MODE, GestureActivity.GESTURE_MODE_VERIFY);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		}
	}
	private static Timer timer ;
	private static TimerTask timerTask;
	private static boolean isRunning;
	/**
	 * 5分钟一次弹出
	 * 
	 * @param 
	 * @return void
	 * @throws
	 */
	public void startVerify(){
		if(timer == null){
			timer = new Timer();
		}
		if(timerTask == null){
			timerTask = new TimerTask() {
				@Override
				public void run() {
					mHandler.sendEmptyMessage(0);
				}
			};
		}
		if(!isRunning){
			timer.schedule(timerTask, 30*1000, 30*1000);
			isRunning = true;
		}
	}
	/**
	 * 停止检测
	 * 
	 * @param 
	 * @return void
	 * @throws
	 */
	public static void stopVerify(){
		if(timer != null){
			timer.cancel();
			timer = null;
		}
		if(timerTask != null){
			timerTask.cancel();
			timerTask = null;
		}
		isRunning = false;
	}
	
	/**
	 * 是否在前台运行
	 * 
	 * @param @param context
	 * @param @return
	 * @return boolean
	 * @throws
	 */
	public boolean isRunningForeground (Context context)
	{
	    ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
	    ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
	    String currentPackageName = cn.getPackageName();
//	    ComponentInfo{com.android.systemui/com.android.systemui.recent.RecentsActivity}
//	    ComponentInfo{com.android.systemui/com.android.systemui.recent.RecentAppFxActivity}
	    if(!TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(getPackageName()))
	    {
	        return true ;
	    }
	    return false ;
	}
	
	/*************************************** Activity管理 *************************************/
	/**
	 * 正在运行的Activity
	 */
	public static List<Activity> runingActivities = new ArrayList<Activity>();

	/**
	 * 添加Activity
	 * 
	 * @param activity
	 */
	public static void addActivity(Activity activity) {
		runingActivities.add(activity);
	}

	/**
	 * 移除Activity
	 * 
	 * @param activity
	 */
	public static void removeActivity(Activity activity) {
		runingActivities.remove(activity);
	}

	/**
	 * 退出应用
	 * 
	 * @param context
	 */
	public static void exitAllActivity(Context context) {
		if (runingActivities != null) {
			for (int i = 0; i < runingActivities.size(); i++) {
				Activity item = runingActivities.get(i);
				item.finish();
				runingActivities.remove(item);
				i--;
			}
		}
		stopVerify();
		System.exit(0);
	}

	/**
	 * 清空activity栈,出了当前activity
	 * 
	 * @param context
	 */
	public static void clearAllActivity(Activity context) {
		for (int i = 0; i < runingActivities.size(); i++) {
			Activity item = runingActivities.get(i);
			if (context.getClass().getSimpleName()
					.equals(item.getClass().getSimpleName())) {
				continue;
			}
			item.finish();
			runingActivities.remove(item);
		}
	}
	
	
	

}
