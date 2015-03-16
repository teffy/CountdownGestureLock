package com.example.timelock;

import com.example.timelock.base.BaseFragmentActivity;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Activity1 extends BaseFragmentActivity {

	@Override
	protected int getContentViewID() {
		return -1;
	}
	
	protected android.view.View getContentView() {
		Button bt = new Button(CTX);
		bt.setText(CTX.getClass().getSimpleName()+"-next");
		bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				skip(Activity2.class, false);
			}
		});
		return bt;
	};
}
