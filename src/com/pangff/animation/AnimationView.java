package com.pangff.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

public class AnimationView {

	View chatListLayout;
	View msgListLayout;
	View animationLayout;
	private View animationContentView;
	private ResultsAnimationView resultsAnimationView;
	private ListView chatListView;
	AnimationAdapter adapter;
	AnimationHelper animationHelper;
	Context mContext;
	/**
	 * ebiao
	 */
	AnimationListener animationListInAnimationListener = new AnimationListener(){

		@Override
		public void onAnimationStart(Animation animation) {
			
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			msgListLayout.setVisibility(View.VISIBLE);
			animationContentView.setVisibility(View.VISIBLE);
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			
		}
		
	};
	
	/**
	 * ebiao
	 */
	AnimationListener animationListOutAnimationListener = new AnimationListener(){

		@Override
		public void onAnimationStart(Animation animation) {
			
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			msgListLayout.setVisibility(View.INVISIBLE);
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			
		}
		
	};
	
	/**
	 * ebiao
	 */
	AnimationListener windowInAnimationListener = new AnimationListener(){

		@Override
		public void onAnimationStart(Animation animation) {
			
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			animationLayout.setVisibility(View.VISIBLE);
			animationContentView.setVisibility(View.VISIBLE);
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			
		}
		
	};
	

	/**
	 * ebiao
	 */
	AnimationListener windowDismmisAnimationListener = new AnimationListener(){

		@Override
		public void onAnimationStart(Animation animation) {
			
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			animationLayout.setVisibility(View.INVISIBLE);
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			
		}
	};
	
	public class MyAnimatorListenerAdapter extends AnimatorListenerAdapter{
		float initLeft;
		float initTop;
		public MyAnimatorListenerAdapter(float initLeft,float initTop){
			this.initLeft = initLeft;
			this.initTop = initTop;
		}
		@Override
		public void onAnimationEnd(Animator animation) {
			adapter.showInVisibal();
			resultsAnimationView.setVisibility(View.INVISIBLE);
			resultsAnimationView.setX(initLeft);
			resultsAnimationView.setY(initTop);
			animationLayout.setVisibility(View.INVISIBLE);
		}
	}
	
	public void showSendPlanMsgList(){
		msgListLayout.startAnimation(animationHelper.animationListIn(mContext));
	}
	
	public void hideSendPlanMsgList(){
		msgListLayout.startAnimation(animationHelper.animationListOut(mContext));
	}
	
	public void showSendPlanWindow(){
		animationLayout.startAnimation(animationHelper.windowIn(mContext));
	}
	
	public void cancelSendPlan(){
		animationLayout.startAnimation(animationHelper.windowOut(mContext));
	}
	
	public void doSendPlan(String data){
		adapter.addNewItem(data);
		chatListView.setSelection(adapter.getCount()-1);
		hideSendPlanMsgList();
		msgListLayout.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				resultsAnimationView.closeResults();
			}
		}, 100);
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				onClosed();
			}
		}, 250);
	}
	
	private void onClosed(){
		View targetView = animationHelper.getTargetView(chatListView,adapter.getTargetId());
		if(targetView!=null){
			int location_from[] = {0,0};
		    	resultsAnimationView.getLocationInWindow(location_from);
		    final float left = resultsAnimationView.getLeft();
		    final float top = resultsAnimationView.getTop();
		   
		    float toHeight = targetView.getHeight();
		    float toWidth = targetView.getWidth(); 
		    
		    float iHeight = resultsAnimationView.getHeight();
		    float iWidth = resultsAnimationView.getWidth();
		    
		    float scaleX = toWidth/iWidth;
		    float scaleY = toHeight/iHeight;
		    
			int location[] = {0,0};
			targetView.getLocationInWindow(location);
			float dy = (float) (resultsAnimationView.getHeight()/2*Math.cos(0.4f * Math.PI / 2));
			AnimatorSet animatorSet = new AnimatorSet();
			final Animator xAnimator = ObjectAnimator.ofFloat(resultsAnimationView, "x", left,left+location[0]-location_from[0]-iWidth/4);
			final Animator yAnimator = ObjectAnimator.ofFloat(resultsAnimationView, "y", top,top+location[1]-location_from[1]-dy);
//			final Animator rightAnimator = ObjectAnimator.ofFloat(resultsAnimationView, "right", left+iWidth,left+location[0]-location_from[0]-iWidth/4+toWidth);
//			final Animator bottomAnimator = ObjectAnimator.ofFloat(resultsAnimationView, "bottom", top+iHeight,top+location[1]-location_from[1]-dy+toHeight);
//			final Animator scalAnimatorX = ObjectAnimator.ofFloat(resultsAnimationView, "scaleX", 1,0.1f);
//			final Animator scalAnimatorY = ObjectAnimator.ofFloat(resultsAnimationView, "scaleY", 1,0.1f);
			Animator animatorsr[] = new Animator[2];
			animatorsr[0] = xAnimator;
			animatorsr[1] = yAnimator;
//			animatorsr[2] = rightAnimator;
//			animatorsr[3] = bottomAnimator;
			animatorSet.addListener(new MyAnimatorListenerAdapter(left,top));
			animatorSet.playTogether(animatorsr);
			animatorSet.start();
			
//			resultsAnimationView.startAnimation(animationHelper.scalSmall(mContext));
		}
	}
	
	public void init(Context context,View rootView,AnimationAdapter adapter) {
		this.mContext = context;
		this.adapter = adapter;
		animationHelper = AnimationHelper.getInstance();
		animationContentView = rootView.findViewById(R.id.animationContentView);
		msgListLayout= rootView.findViewById(R.id.msgListLayout);
		chatListLayout= rootView.findViewById(R.id.chatListLayout);
		animationLayout= rootView.findViewById(R.id.animationLayout);
		chatListView= (ListView) rootView.findViewById(R.id.chatListView);
		chatListView.setAdapter(adapter);
		resultsAnimationView = (ResultsAnimationView) rootView.findViewById(R.id.resultAnimationView);
		animationContentView.setVisibility(View.INVISIBLE);
		resultsAnimationView.setContentView(animationContentView);
		animationHelper.animationListIn(context).setAnimationListener(animationListInAnimationListener);
		animationHelper.animationListOut(context).setAnimationListener(animationListOutAnimationListener);
		animationHelper.windowIn(context).setAnimationListener(windowInAnimationListener);
		animationHelper.windowOut(context).setAnimationListener(windowDismmisAnimationListener);
		resultsAnimationView.setCallback(new ResultsAnimationView.AnimationEndCallback() {
            @Override
            public void callbackForOpened() {
            }
            @Override
            public void callbackForClosed() {
            
            }
        });
	}
}
