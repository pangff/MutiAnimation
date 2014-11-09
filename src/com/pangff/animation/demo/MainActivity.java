package com.pangff.animation.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.pangff.animation.AnimationView;
import com.pangff.animation.R;

public class MainActivity extends Activity {

	Button showMsgListBtn;
	Button showEditWindowBtn;
	Button cancelMsgBtn;
	Button sendMsgBtn;
	AnimationDemoAdapter adapter;
	AnimationView animationView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		animationView = new AnimationView();
		adapter = new AnimationDemoAdapter(this);
		animationView.init(this, findViewById(R.id.rootView), adapter);
		showMsgListBtn  = (Button) findViewById(R.id.showMsgListBtn);
		showEditWindowBtn  = (Button) findViewById(R.id.showEditWindowBtn);
		cancelMsgBtn  = (Button) findViewById(R.id.cancelMsgBtn);
		sendMsgBtn  = (Button) findViewById(R.id.sendMsgBtn);
		
		showMsgListBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				animationView.showSendPlanMsgList();
			}
		});
		showEditWindowBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				animationView.showSendPlanWindow();
			}
		});
		cancelMsgBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				animationView.cancelSendPlan();
			}
		});
		sendMsgBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				animationView.doSendPlan("这是新添加的小飞机");
			}
		});
		
		adapter.refresh(DataUtil.getTextDataList());
	}
	
}
