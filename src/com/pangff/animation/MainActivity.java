package com.pangff.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends Activity {

	Button animationBtn;
	Button button2;
	Button button3;
	Button button4;
	LinearLayout layout1;
	LinearLayout layout2;
	FrameLayout layout3;
	private View contentView;
	private ResultsAnimationView resultsAnimationView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final Animation animationListIn = AnimationUtils.loadAnimation(this,
				R.anim.slide_in_from_bottom);
		final Animation animationListOut = AnimationUtils.loadAnimation(this,
				R.anim.slide_out_to_bottom);
		final Animation animationPop = AnimationUtils.loadAnimation(this,
				R.anim.zoom_in);
		final Animation animationDismiss = AnimationUtils.loadAnimation(this,
				R.anim.zoom_out);
		animationBtn = (Button) findViewById(R.id.animationBtn);
		button2 = (Button) findViewById(R.id.button2);
		button3 = (Button) findViewById(R.id.button3);
		button4 = (Button) findViewById(R.id.button4);
		layout1 = (LinearLayout) findViewById(R.id.layout1);
		layout2 = (LinearLayout) findViewById(R.id.layout2);
		layout3 = (FrameLayout) findViewById(R.id.layout3);
		contentView = this.findViewById(R.id.contentView);
		//contentView.setVisibility(View.INVISIBLE);
		resultsAnimationView = (ResultsAnimationView) this.findViewById(R.id.resultAnimationView);
		resultsAnimationView.setContentView(contentView);
		animationListIn.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				layout2.setVisibility(View.VISIBLE);
			}
		});
		animationListOut.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				layout2.setVisibility(View.INVISIBLE);
			}
		});
		animationPop.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				layout3.setVisibility(View.VISIBLE);
			}
		});
		animationDismiss.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				layout3.setVisibility(View.INVISIBLE);
			}
		});
		animationBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				layout2.startAnimation(animationListIn);
			}
		});
		button2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				layout3.startAnimation(animationPop);
			}
		});
		button3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				layout3.startAnimation(animationDismiss);
			}
		});
		
		float left = resultsAnimationView.getLeft();
		float top = resultsAnimationView.getTop();
		final Animator animator = ObjectAnimator.ofFloat(resultsAnimationView, "x", left,left+200);
		final Animator animator2 = ObjectAnimator.ofFloat(resultsAnimationView, "y", top,top+200);
		button4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				layout2.startAnimation(animationListOut);
				layout2.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						resultsAnimationView.closeResults();
						animator.start();
						animator2.start();
					}
				}, 100);
			}
		});
		
		
		resultsAnimationView.setCallback(new ResultsAnimationView.AnimationEndCallback() {
            @Override
            public void callbackForOpened() {
                Toast.makeText(getApplicationContext(), "执行自定义打开后动画显示", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void callbackForClosed() {
                Toast.makeText(getApplicationContext(), "执行自定义关闭后动画显示", Toast.LENGTH_SHORT).show();
            }
        });
	}
}
