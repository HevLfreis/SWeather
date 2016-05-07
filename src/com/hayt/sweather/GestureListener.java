package com.hayt.sweather;

import android.content.Context;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

public class GestureListener extends SimpleOnGestureListener{

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		//System.out.println("scroll1:"+ e1.getAction()+"-"+distanceY);
		//System.out.println("scroll2:"+ e2.getAction()+"-"+distanceY);
		if (distanceY>20) {
			//System.out.println("scroll1:"+ e1.getAction()+"-"+distanceY);
			return true;
		}
		else {
			return false;
		}

	}
	
	

}
