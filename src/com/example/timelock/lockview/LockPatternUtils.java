package com.example.timelock.lockview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
/**
 * 图像密码工具类
 * 
 * @Package_Name : com.eastedge.cctv.widget.lock
 * @ClassName: LockPatternUtils
 * @author lumeng
 * @date 2013-11-19 上午10:43:36
 * @version V1.0
 */
public class LockPatternUtils {

//	public final static String KEY_LOCK_PWD = "lock_pwd";
	public final static String LOCK_PWD = "_lock_pwd";
	private static Context mContext;
	private static SharedPreferences preference;
	private static LockPatternUtils lockPatternUtils;
	
	public static LockPatternUtils getInstance(Context context){
		if(lockPatternUtils == null){
			lockPatternUtils = new LockPatternUtils();
		}
		mContext = context;
		preference = PreferenceManager.getDefaultSharedPreferences(mContext);
		return lockPatternUtils;
	}
	
	/**
	 * Deserialize a pattern.
	 * 
	 * @param string
	 *            The pattern serialized with {@link #patternToString}
	 * @return The pattern.
	 */
	public static List<LockPatternView.Cell> stringToPattern(String string) {
		List<LockPatternView.Cell> result = new ArrayList<LockPatternView.Cell>();

		final byte[] bytes = string.getBytes();
		for (int i = 0; i < bytes.length; i++) {
			byte b = bytes[i];
			result.add(LockPatternView.Cell.of(b / 3, b % 3));
		}
		return result;
	}

	/**
	 * Serialize a pattern.
	 * 
	 * @param pattern
	 *            The pattern.
	 * @return The pattern in string form.
	 */
	public static String patternToString(List<LockPatternView.Cell> pattern) {
		if (pattern == null) {
			return "";
		}
		final int patternSize = pattern.size();

		byte[] res = new byte[patternSize];
		for (int i = 0; i < patternSize; i++) {
			LockPatternView.Cell cell = pattern.get(i);
			res[i] = (byte) (cell.getRow() * 3 + cell.getColumn());
		}
		return Arrays.toString(res);
	}
	/**
	 * 保存手势密码
	 * 
	 * @param @param pattern
	 * @param @param KEY_LOCK_PWD
	 * @return void
	 * @throws
	 */
	public void saveLockPattern(List<LockPatternView.Cell> pattern,String KEY_LOCK_PWD) {
		Editor editor = preference.edit();
		editor.putString(KEY_LOCK_PWD+LOCK_PWD, patternToString(pattern));
		editor.commit();
	}
	/**
	 * 获取当前用户手势密码
	 * 
	 * @param @param KEY_LOCK_PWD
	 * @param @return
	 * @return String
	 * @throws
	 */
	public String getLockPaternString(String KEY_LOCK_PWD) {
		return preference.getString(KEY_LOCK_PWD+LOCK_PWD, "");
	}
	/**
	 * 检查手势密码是否正确
	 * 
	 * @param @param pattern
	 * @param @param KEY_LOCK_PWD
	 * @param @return
	 * @return int
	 * @throws
	 */
	public int checkPattern(List<LockPatternView.Cell> pattern,String KEY_LOCK_PWD) {
		String stored = getLockPaternString(KEY_LOCK_PWD);
		if (!stored.isEmpty()) {
			return stored.equals(patternToString(pattern)) ? 1 : 0;
		}
		return -1;
	}
	/**
	 * 清空手势密码
	 * 
	 * @param @param KEY_LOCK_PWD
	 * @return void
	 * @throws
	 */
	public void clearLock(String KEY_LOCK_PWD) {
		saveLockPattern(null,KEY_LOCK_PWD+LOCK_PWD);
	}

}
