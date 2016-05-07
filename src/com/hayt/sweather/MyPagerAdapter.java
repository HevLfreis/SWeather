package com.hayt.sweather;

import java.util.List;
import java.util.UUID;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.view.ViewGroup;


public class MyPagerAdapter extends FragmentPagerAdapter{
	 private List<Fragment> fragmentList;
	 private FragmentManager fm;
	
	public MyPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
		super(fm);
		this.fm=fm;
		
		this.fragmentList = fragmentList;
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return (fragmentList == null || fragmentList.size() == 0) ? null : fragmentList.get(arg0);

		//throw new IllegalStateException("No fragment at position " + arg0);
	}
   

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		//System.out.println("pageradapt:"+position);
		return super.instantiateItem(container, position);
	}
	
	

	@Override
	public int getItemPosition(Object object) {
		// TODO Auto-generated method stub
		return POSITION_NONE;
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Application.TAB_COUNT;
	}
	
	
}

