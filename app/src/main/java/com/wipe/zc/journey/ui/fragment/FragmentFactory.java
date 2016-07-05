package com.wipe.zc.journey.ui.fragment;

import android.support.v4.app.Fragment;

public class FragmentFactory {
	
	public static Fragment createFactory(int i){
		Fragment fragment = null;
		switch (i) {
		case 0:
			fragment = new HomeFragment();
			break;
		case 1:
			fragment = new CalendarFragment();
			break;
		case 2:
			fragment = new TotalFragment();
			break;
		default:
			fragment = null;
			break;
		}
		return fragment;
	}
}