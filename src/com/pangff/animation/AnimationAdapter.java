package com.pangff.animation;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class AnimationAdapter extends BaseAdapter{
	protected List<Integer> inVisiblePositonList = new ArrayList<Integer>();
	protected List<Object> dataList = new ArrayList<Object>();
	@Override
	public  int getCount(){
		return dataList.size();
	}

	@Override
	public  Object getItem(int position){
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position){
		return position;
	}
	
	public abstract int getTargetId();

	@Override
	public abstract View getView(int position, View convertView, ViewGroup parent);
	
	public class BaseViewHolder{
		public View targetView;
	}
	
	public List<Object> getList(){
		return dataList;
	}
	
	public void setTargetStatus(View targetView,int position){
		if(inVisiblePositonList.contains(position)){
			targetView.setVisibility(View.INVISIBLE);
		}else{
			targetView.setVisibility(View.VISIBLE);
		}
	}

	public void addNewItem(Object object){
		dataList.add(object);
		inVisiblePositonList.add(dataList.size()-1);
		notifyDataSetChanged();
	}
	
	
	public void showInVisibal(){
		inVisiblePositonList.clear();
		notifyDataSetChanged();
	}
}
