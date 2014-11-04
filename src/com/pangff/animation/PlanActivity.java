package com.pangff.animation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import de.greenrobot.event.EventBus;

public class PlanActivity extends Activity{

	Button planBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_plan);
		planBtn = (Button) findViewById(R.id.planBtn);
		planBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(PlanActivity.this, PopActivity.class);
				startActivity(intent);
			}
		});
		EventBus.getDefault().register(this);
	}
	
	public void onEvent(CloseEvent closeEvent){
		Log.e("ddddddddddd", "$$$$$$$$$$$$$$$$$$$$$$");
		this.finish();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
}
