package com.example.timelock;

import com.example.timelock.base.BaseFragmentActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class MainActivity extends BaseFragmentActivity {
	
	@Override
	protected int getContentViewID() {
		return R.layout.activity_main;
	}
	
	@Override
	protected void initView(Bundle savedInstanceState) {
		super.initView(savedInstanceState);
		// 过滤掉本activity 这个界面不用监听
		setVerify(false);
		
		Button bt = (Button) findViewById(R.id.button1);
		bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				skip(GestureActivity.INTENT_MODE,GestureActivity.GESTURE_MODE_SET,GestureActivity.class, true);
			}
		});
	}
}
