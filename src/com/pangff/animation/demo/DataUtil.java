package com.pangff.animation.demo;

import java.util.ArrayList;
import java.util.List;

public class DataUtil {
	
	public static List<String> getTextDataList(){
		List<String> dataList = new ArrayList<String>();
		for(int i=0;i<100;i++){
			dataList.add("第"+i+"个item");
		}
		return dataList;
	}

}
