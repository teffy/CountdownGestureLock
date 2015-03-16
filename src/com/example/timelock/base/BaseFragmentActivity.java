package com.example.timelock.base;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.timelock.GestureActivity;
import com.example.timelock.app.MApplication;
import com.example.timelock.receiver.ScreenObserver;
import com.example.timelock.receiver.ScreenObserver.ScreenStateListener;

/**
 * BaseFragmentActivity对FragmentActivity一些简单的封装
 * 
 * @File_name: BaseFragmentActivity.java
 * @Package_name: com.teffy.frame.ui.base
 * @Author lumeng
 * @Date : 2012-10-10下午12:09:49
 * @Version 1.0
 */
@SuppressWarnings({"static-access","unchecked","deprecation"})
public abstract class BaseFragmentActivity extends FragmentActivity implements
		OnClickListener {
	
	protected BaseFragmentActivity CTX = BaseFragmentActivity.this;
	public MApplication mApplication;
	protected LayoutInflater inflater;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		doBeforeSetContent();
		if(getContentViewID() == -1){
			setContentView(getContentView());
		}else{
			setContentView(getContentViewID());
		}

		mApplication = (MApplication) getApplication();

		mApplication.addActivity(CTX);
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
						| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		
		init(savedInstanceState);
	}



	/**
	 * 添加
	 * @param resID
	 *            容器view的id
	 * @param fragment
	 *            要替换为的fragment
	 * @param tag 绑定tag
	 */
	protected void addFragment(int resID, Fragment fragment, String tag) {
		FragmentTransaction fragmentTransaction = getSupportFragmentManager()
				.beginTransaction();
		fragmentTransaction.add(resID, fragment, tag);
		fragmentTransaction.commit();
	}
	/**
	 * 增加
	 * 
	 * @param @param resID  容器view的id
	 * @param @param fragment   要替换为的fragment
	 * @param @param tag   绑定tag
	 * @param @param addtoback_name   addToBackStack的name
	 * @return void
	 * @throws
	 */
	protected void addFragment(int resID, Fragment fragment, String tag,String addtoback_name) {
		FragmentTransaction fragmentTransaction = getSupportFragmentManager()
				.beginTransaction();
		fragmentTransaction.add(resID, fragment, tag);
		fragmentTransaction.addToBackStack(addtoback_name);
		fragmentTransaction.commit();
	}
	/**
	 * 替换
	 * @param resID
	 *            容器view的id
	 * @param fragment
	 *            要替换为的fragment
	 * @param tag  绑定tag
	 */
	protected void replaceFragment(int resID, Fragment fragment, String tag) {
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.replace(resID, fragment, tag);
		fragmentTransaction.commit();
	}
	/**
	 * 替换
	 * 
	 * @param @param resID  容器view的id
	 * @param @param fragment   要替换为的fragment
	 * @param @param tag   绑定tag
	 * @param @param addtoback_name   addToBackStack的name
	 * @return void
	 * @throws
	 */
	protected void replaceFragment(int resID, Fragment fragment, String tag,String addtoback_name) {
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.replace(resID, fragment, tag);
		fragmentTransaction.addToBackStack(addtoback_name);
		fragmentTransaction.commit();
	}
	/**
	 * 删除
	 * 
	 * @param @param fragment
	 * @return void
	 * @throws
	 */
	protected void removeFragment(Fragment fragment) {
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.remove(fragment);
		fragmentTransaction.commit();
	}


	/**
	 * 获取默认图片id
	 */
	protected int setDefaultImgId() {
		return -1;
	}

	/**
	 * 在设置 content内容前的操作，比如设置全屏
	 */
	protected void doBeforeSetContent() {
	}

	/**
	 * 获取 意图 intent中的数据，如果不需要获取，则可以不做处理
	 */
	protected void getIntentInfo() {
	}
	/**
	 * 没有layout的可以使用该方法为activity添加contentview
	 * 
	 * @param @return
	 * @return View
	 * @throws
	 */
	protected View getContentView() {
		return null;
	}
	/**
	 * 设置本界面 的Layout的id,如果是定义view，只要重新写该方法，让返回值为0即可
	 */
	protected abstract int getContentViewID();

	/**
	 * 设置监听事件，如果不用设置，则不用设置
	 */
	protected void setListener() {
	}

	/**
	 * 初始化所有View和数据
	 */
	private void init(Bundle savedInstanceState) {
		getIntentInfo();
		initView(savedInstanceState);
		setListener();
		getDatas();
		mScreenObserver = new ScreenObserver(this);
		mScreenObserver.requestScreenStateUpdate(new ScreenStateListener() {
			@Override
			public void onScreenStateChange(boolean isScreenOn) {
				if (isScreenOn) {
					doSomethingOnScreenOn();
				} else {
					doSomethingOnScreenOff();
				}
			}
		});
	}
	 private ScreenObserver mScreenObserver;
	 private void doSomethingOnScreenOn() {
//		 LogUtil.i("Screen is on");
	 }
	 private void doSomethingOnScreenOff() {
//		 LogUtil.i("Screen is off");
		 if(!GestureActivity.IS_SHOW){
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), GestureActivity.class);
				intent.putExtra(GestureActivity.INTENT_MODE, GestureActivity.GESTURE_MODE_VERIFY);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
	 }

	/**
	 * 获取数据,根据实际项目，放在onResume或者onPause中
	 */
	protected void getDatas() {
	}

	/**
	 * 初始化所有控件
	 */
	protected void initView(Bundle savedInstanceState) {
	}

	/**
	 * 根据资源id 获取View ，不用强制转换
	 * 
	 * @param id
	 *            资源id
	 * @return 返回id所指向的View
	 */
	protected <A extends View> A getView(int id) {
		return (A) findViewById(id);
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
	}
	
	
	@Override
	protected void onStart() {
		super.onStart();
		isForeGround = getBooleanInfof(CTX, ISFOREGROUND_KEY);
		if(!isForeGround){
			if (isVerify()) {
				mApplication.verify();
			}
		}
		if(!isForeGround){
			saveBoolean(CTX, ISFOREGROUND_KEY, true);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		isForeGround = getBooleanInfof(CTX, ISFOREGROUND_KEY);
	     if(!isForeGround){
	    	 if (isVerify()) {
	 			mApplication.verify();
	 		}
	     }
	     if(!isForeGround){
	    	 saveBoolean(CTX, ISFOREGROUND_KEY, true);
	     }
	}
	String ISFOREGROUND_KEY = "ISFOREGROUND_KEY";
	boolean isForeGround = true;
	@Override
	protected void onStop() {
		super.onStop();
		isForeGround = mApplication.isRunningForeground(CTX);
		
		if(!isForeGround){
			saveBoolean(CTX, ISFOREGROUND_KEY, isForeGround);
		}
	}
	
	
	
	private String SPNAME = "SPNAME";
	/**
	 * 保存Boolean类型
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public void saveBoolean(Context context, String key, Boolean value) {
		SharedPreferences sp = context.getSharedPreferences(
				SPNAME, Context.MODE_PRIVATE);
		sp.edit().putBoolean(key, value).commit();// 根据类型，强转，然后要commit
	}

	/**
	 * 根据key获取 Boolean类型的 数据信息 默认返回false
	 * 
	 * @param context
	 * @param key
	 * @return 默认返回false
	 */
	public  Boolean getBooleanInfof(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(
				SPNAME, Context.MODE_PRIVATE);
		return sp.getBoolean(key, false);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	private boolean isVerify = true;
	public boolean isVerify() {
		return isVerify;
	}
	public void setVerify(boolean isVerify) {
		this.isVerify = isVerify;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 停止监听screen状态
		mScreenObserver.stopScreenStateUpdate();
		mApplication.removeActivity(CTX);
	}

	/**

	/**
	 * 监听Click事件
	 */
	public void onClick(View v) {
	}





	/**
	 * 封装Intent跳转
	 * 
	 * @param clazz
	 *            要跳向的界面的class
	 * @param isCloseSelf
	 *            是否关闭本界面
	 */
	protected void skip(Class<?> clazz, boolean isCloseSelf) {
		Intent intent = new Intent(CTX, clazz);
		startActivity(intent);
		if (isCloseSelf) {
			CTX.finish();
		}
	}

	/**
	 * 封装Intent跳转
	 * 
	 * @param key
	 * @param value
	 * @param clazz
	 * @param isCloseSelf
	 */
	protected void skip(String key, String value, Class<?> clazz,
			boolean isCloseSelf) {
		Intent intent = new Intent();
		intent.setClass(CTX, clazz);
		if (!TextUtils.isEmpty(key)) {
			intent.putExtra(key, value);
		}
		startActivity(intent);
		if (isCloseSelf) {
			CTX.finish();
		}
	}

	/**
	 * 封装Intent跳转
	 * 
	 * @param key
	 * @param obj
	 * @param clazz
	 * @param isCloseSelf
	 */
	protected void skip(String key, Serializable obj, Class<?> clazz,
			boolean isCloseSelf) {
		Intent intent = new Intent();
		intent.setClass(CTX, clazz);
		if (!TextUtils.isEmpty(key)) {
			intent.putExtra(key, obj);
		}
		startActivity(intent);
		if (isCloseSelf) {
			CTX.finish();
		}
	}

	/**
	 * 封装Intent跳转
	 * 
	 * @param key
	 * @param obj
	 * @param clazz
	 * @param isCloseSelf
	 */
	protected void skip(String key, ArrayList<Parcelable> obj, Class<?> clazz,
			boolean isCloseSelf) {
		Intent intent = new Intent();
		intent.setClass(CTX, clazz);
		if (!TextUtils.isEmpty(key)) {
			intent.putParcelableArrayListExtra(key, obj);
		}
		startActivity(intent);
		if (isCloseSelf) {
			CTX.finish();
		}
	}

	/**
	 * 封装Intent跳转
	 * 
	 * @param key
	 * @param obj
	 * @param clazz
	 * @param isCloseSelf
	 */
	protected void skip(String key, int obj, Class<?> clazz, boolean isCloseSelf) {
		Intent intent = new Intent();
		intent.setClass(CTX, clazz);
		if (!TextUtils.isEmpty(key)) {
			intent.putExtra(key, obj);
		}
		startActivity(intent);
		if (isCloseSelf) {
			CTX.finish();
		}
	}
	
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		mApplication.setLastTouchTime();
		return super.dispatchTouchEvent(ev);
	}
	

}
