package com.example.timelock.app;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast工具类
 * @File_name: ToastUtil.java
 * @Package_name: com.eastedge.safeinhand.utils
 * @Author lumeng
 * @Date : 2012-10-10下午12:08:39
 * @Version 1.0
 */
public class ToastUtil {
	static Toast mToast;

	/**
	 * 显示测试Toast
	 * 
	 * @param context
	 * @param msg
	 * @param duration
	 */
	public static void showTest(Context context, String msg) {
		if (true) {
			if (mToast == null) {
				mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
			} else {
				mToast.setText(msg);
				mToast.setDuration(Toast.LENGTH_SHORT);
			}
			mToast.show();
			// Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 短时间显示Toast
	 * 
	 * @param context
	 * @param msg
	 */
	public static void showShort(Context context, int resid) {
		// 解决Toast连点显示不及时的问题
		if (mToast == null) {
			mToast = Toast.makeText(context, resid, Toast.LENGTH_SHORT);
		} else {
			mToast.setText(resid);
			mToast.setDuration(Toast.LENGTH_SHORT);
		}
		mToast.show();
		// 正常
		// Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 短时间显示Toast
	 * 
	 * @param context
	 * @param msg
	 */
	public static void showShort(Context context, String msg) {
		// 解决Toast连点显示不及时的问题
		if (mToast == null) {
			mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		} else {
			mToast.setText(msg);
			mToast.setDuration(Toast.LENGTH_SHORT);
		}
		mToast.show();
		// 正常
		// Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 长时间显示Toast
	 * 
	 * @param context
	 * @param msg
	 */
	public static void showLong(Context context, String msg) {
		if (mToast == null) {
			mToast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
		} else {
			mToast.setDuration(Toast.LENGTH_LONG);
			mToast.setText(msg);
		}
		mToast.show();
		// Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}

	/**
	 * 长时间显示Toast
	 * 
	 * @param context
	 * @param msg
	 */
	public static void showLong(Context context, int resid) {
		if (mToast == null) {
			mToast = Toast.makeText(context, resid, Toast.LENGTH_LONG);
		} else {
			mToast.setDuration(Toast.LENGTH_LONG);
			mToast.setText(resid);
		}
		mToast.show();
		// Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}
}
