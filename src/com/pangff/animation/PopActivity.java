package com.pangff.animation;

import de.greenrobot.event.EventBus;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class PopActivity extends Activity {
	
	Button popBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pop);
		popBtn = (Button) findViewById(R.id.popBtn);
		popBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EventBus.getDefault().post(new CloseEvent());
			}
		});
	}
}
