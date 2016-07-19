package com.wipe.zc.journey.ui.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.wipe.zc.journey.ui.fragment.TotalViewFragment;

public class TotalPagerAdapter extends FragmentPagerAdapter {

	private ViewPager viewPager;


	public TotalPagerAdapter(FragmentManager fm, ViewPager viewPager) {
		super(fm);
		this.viewPager = viewPager;
	}

	@Override
	public Fragment getItem(int arg0) {
		TotalViewFragment tvFragment = new TotalViewFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("position", arg0);
		tvFragment.setArguments(bundle);
		tvFragment.setViewPager(viewPager);
		return tvFragment;
	}

	@Override
	public int getCount() {
		return 12;
	}

}
