package com.pangff.animation.demo;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pangff.animation.AnimationAdapter;
import com.pangff.animation.R;

public class AnimationDemoAdapter extends AnimationAdapter {

	Context mContext;
	
	public AnimationDemoAdapter(Context context){
		this.mContext = context;
	}

	public void refresh(List<String> list) {
		inVisiblePositonList.clear();
		dataList.clear();
		dataList.addAll(list);
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item, null);
			viewHolder.targetView = (TextView) convertView.findViewById(R.id.targetView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (inVisiblePositonList.contains(position)) {
			viewHolder.targetView.setVisibility(View.INVISIBLE);
		} else {
			viewHolder.targetView.setVisibility(View.VISIBLE);
		}
		((TextView)viewHolder.targetView).setText(dataList.get(position).toString());
		return convertView;
	}

	class ViewHolder extends BaseViewHolder {
		
	}

	@Override
	public int getTargetId() {
		return R.id.targetView;
	}
}
