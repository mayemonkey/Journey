package com.wipe.zc.journey.ui.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.wipe.zc.journey.ui.fragment.TotalViewFragment;

public class TotalPagerAdapter extends FragmentPagerAdapter {

	private ViewPager viewPager;
//	private int year;

	public TotalPagerAdapter(FragmentManager fm, ViewPager viewPager) {
		super(fm);
		this.viewPager = viewPager;
//		this.year = year;
	}

	@Override
	public Fragment getItem(int arg0) {
		TotalViewFragment tvFragment = new TotalViewFragment();
		Bundle bundle = new Bundle();
//		bundle.putInt("year", year);
		bundle.putInt("position", arg0);
		tvFragment.setArguments(bundle);
		tvFragment.setViewPager(viewPager);
		return tvFragment;
	}

	@Override
	public int getCount() {
		return 12;
	}

	//
	// @Override
	// public int getCount() {
	// return 12;
	// }
	//
	// @Override
	// public boolean isViewFromObject(View view, Object obj) {
	// return view == obj;
	// }
	//
	// @Override
	// public void destroyItem(ViewGroup container, int position, Object object)
	// {
	// container.removeView((View) object);
	// }
	//
	// @Override
	// public Object instantiateItem(ViewGroup container, int position) {
	// Context context = MyApplication.getContext();
	// View view = View.inflate(context, R.layout.layout_total_viewpager, null);
	// // 数字
	// TextView tv_total_done = (TextView)
	// view.findViewById(R.id.tv_total_done);
	// TextView tv_total_wait = (TextView)
	// view.findViewById(R.id.tv_total_wait);
	// TextView tv_total_free = (TextView)
	// view.findViewById(R.id.tv_total_free);
	// // 进度条
	// final ProgressBar pb_total_done = (ProgressBar)
	// view.findViewById(R.id.pb_total_done);
	// ProgressBar pb_total_wait = (ProgressBar)
	// view.findViewById(R.id.pb_total_wait);
	// ProgressBar pb_total_free = (ProgressBar)
	// view.findViewById(R.id.pb_total_free);
	//
	// //进度条动画
	// int radom = (int) (Math.random()*100);
	// ValueAnimator va = ValueAnimator.ofInt(0,radom);
	// va.addUpdateListener(new AnimatorUpdateListener() {
	// public void onAnimationUpdate(ValueAnimator arg0) {
	// int i = ((Integer) arg0.getAnimatedValue()).intValue();
	// pb_total_done.setProgress(i);
	// }
	// });
	// va.setDuration(300);
	// va.start();
	//
	//
	// final int index = position;
	// // 左移
	// ImageView iv_total_left = (ImageView)
	// view.findViewById(R.id.iv_total_left);
	// iv_total_left.setOnClickListener(new OnClickListener() {
	// public void onClick(View v) {
	// viewPager.setCurrentItem(index - 1);
	// }
	// });
	// // 右移
	// ImageView iv_total_right = (ImageView)
	// view.findViewById(R.id.iv_total_right);
	// iv_total_right.setOnClickListener(new OnClickListener() {
	// public void onClick(View v) {
	// viewPager.setCurrentItem(index + 1);
	// }
	// });
	// // 一月不存在左移项
	// if (position == 0) {
	// iv_total_left.setVisibility(View.INVISIBLE);
	// iv_total_left.setEnabled(false);
	// }
	// // 十二月不存在右移项
	// if (position == 11) {
	// iv_total_right.setVisibility(View.INVISIBLE);
	// iv_total_right.setEnabled(false);
	// }
	//
	// container.addView(view);
	// return view;
	// }

}
