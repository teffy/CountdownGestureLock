package com.example.timelock;

import java.util.List;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.example.timelock.app.ToastUtil;
import com.example.timelock.base.BaseFragmentActivity;
import com.example.timelock.lockview.LockPatternUtils;
import com.example.timelock.lockview.LockPatternView;
import com.example.timelock.lockview.LockPatternView.Cell;
import com.example.timelock.lockview.LockPatternView.DisplayMode;
import com.example.timelock.lockview.LockPatternView.OnPatternListener;
/**
 * 手势界面
 * 
 * @Package_Name : com.eastedge.cctv.ui.commen
 * @ClassName: GestureActivity
 * @author lumeng
 * @date 2013-11-18 下午4:51:19
 * @version V1.0
 */
public class GestureActivity extends BaseFragmentActivity implements OnClickListener {
//	public final static String SP_GESTURE = LockPatternUtils.KEY_LOCK_PWD;// 存储密码
	public final static String INTENT_MODE= "mode";
	public final static int GESTURE_MODE_SET = 0x1;// 设置密码
//	public final static int GESTURE_MODE_LOGIN = 0x2;// 登陆
	public final static int GESTURE_MODE_VERIFY = 0x3;// 验证
	
	public static boolean IS_SHOW = false;// 是否运行了
	@Override
	protected int getContentViewID() {
		return R.layout.activity_gesture;
	}
	int MODE;
	@Override
	protected void getIntentInfo() {
		super.getIntentInfo();
		MODE = getIntent().getIntExtra(INTENT_MODE,-1);
		if(MODE == -1){
			throw new RuntimeException("请输入跳转模式");
		}
	}
	
	private LockPatternView lockPatternView;
	private TextView tv_tip_gesture;
	
	private String user_key ="user_key";
	@Override
	protected void initView(Bundle savedInstanceState) {
		super.initView(savedInstanceState);
		lockPatternView = getView(R.id.lpv_lock);
		tv_tip_gesture = getView(R.id.tv_tip_gesture);
		
		IS_SHOW = true;
		setVerify(false);
		mApplication.stopVerify();
	}
	
	
	int verify_count = 0;
	boolean isFirstSet = true;
	@Override
	protected void setListener() {
		super.setListener();
		
		lockPatternView.setOnPatternListener(new OnPatternListener() {
			public void onPatternStart() {
			}

			public void onPatternDetected(List<Cell> pattern) {
				if(MODE == GESTURE_MODE_SET){
					if(isFirstSet){
						if(pattern.size()<=2){
							ToastUtil.showShort(CTX, "密码太简单了，请重新设置");
						}else{
							isFirstSet = false;
							LockPatternUtils.getInstance(CTX).saveLockPattern(pattern,user_key);
							ToastUtil.showShort(CTX, "请再次绘制密码");
							tv_tip_gesture.setText("请再次绘制密码");
						}
					}else{
						int result = LockPatternUtils.getInstance(CTX).checkPattern(pattern,user_key);
						if (result!= 1) {
							if(result==0){
								ToastUtil.showShort(CTX, "与上次绘制不一致!");
							}else{
								ToastUtil.showShort(CTX,"请设置密码");
							}
						} else {
							ToastUtil.showShort( CTX, "设置成功!");
							LockPatternUtils.getInstance(CTX).saveLockPattern(pattern,user_key);
							skip(Activity1.class, true);
						}
					}
				}
				else if(MODE == GESTURE_MODE_VERIFY){
					int result = LockPatternUtils.getInstance(CTX).checkPattern(pattern,user_key);
					if (result!= 1) {
						if(result==0){
							if(verify_count >= 4){
							}else{
								verify_count++;
								tv_tip_gesture.setText("密码错误，您可以再试"+(5-verify_count)+"次");
								lockPatternView.setDisplayMode(DisplayMode.Wrong);
								ToastUtil.showShort(CTX, "密码错误!");
							}
						}else{
							ToastUtil.showShort(CTX,"请设置密码");
						}
					} else {
						ToastUtil.showShort( CTX, "密码正确!");
						CTX.finish();
					}
				}
				lockPatternView.clearPattern();
			}

			public void onPatternCleared() {
			}

			public void onPatternCellAdded(List<Cell> pattern) {
			}
		});
	}
	
	
	@Override
	protected void onDestroy() {
		IS_SHOW = false;
		mApplication.startVerify();
		super.onDestroy();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
