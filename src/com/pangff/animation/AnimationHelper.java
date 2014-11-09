package com.pangff.animation;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;

public class AnimationHelper {

	private static AnimationHelper animationHelper;
	
	private Animation animationListIn;
	private Animation animationListOut;
	private Animation windowIn;
	private Animation windowOut;
	private Animation scalSmall;
	
	public static AnimationHelper getInstance(){
		if(animationHelper==null){
			animationHelper = new AnimationHelper();
		}
		return animationHelper;
	}
	/**
	 * 获取targetView，要移动到的位置
	 * @return
	 */
	public  View getTargetView(ListView listView,int targetViewId){
		int firstPosition = listView.getFirstVisiblePosition() - listView.getHeaderViewsCount(); // This is the same as child #0
		int wantedChild = listView.getLastVisiblePosition() - firstPosition;
		if (wantedChild < 0 || wantedChild >= listView.getChildCount()) {
		  return null;
		}
		View wantedView = listView.getChildAt(wantedChild);
		return wantedView.findViewById(targetViewId);
	}
	
	public  Animation animationListIn(Context context){
		if(animationListIn==null){
			animationListIn = AnimationUtils.loadAnimation(context,R.anim.slide_in_from_bottom);
		}
		return animationListIn;
	}
	
	public  Animation animationListOut(Context context){
		if(animationListOut==null){
			animationListOut =  AnimationUtils.loadAnimation(context,R.anim.slide_out_to_bottom);
		}
		return animationListOut;
	}
	
	public  Animation windowIn(Context context){
		if(windowIn==null){
			windowIn=  AnimationUtils.loadAnimation(context,R.anim.zoom_in);
		}
		return windowIn;
	}
	
	public  Animation windowOut(Context context){
		if(windowOut==null){
			windowOut=  AnimationUtils.loadAnimation(context,R.anim.zoom_out);
		}
		return windowOut;
	}
	
	public  Animation scalSmall(Context context){
		if(scalSmall==null){
			scalSmall=  AnimationUtils.loadAnimation(context,R.anim.scale_small);
		}
		return scalSmall;
	}
}
