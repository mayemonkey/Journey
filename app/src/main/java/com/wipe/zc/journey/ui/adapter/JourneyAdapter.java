package com.wipe.zc.journey.ui.adapter;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wipe.zc.journey.R;
import com.wipe.zc.journey.domain.Journey;
import com.wipe.zc.journey.global.MyApplication;
import com.wipe.zc.journey.lib.CircleImageView;

public class JourneyAdapter extends BaseAdapter {

	private List<Journey> list;
	private int what;

	public JourneyAdapter(List<Journey> list,int what) {
		this.list = list;
		this.what = what;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		Journey journey = list.get(position);
		
		if(convertView == null){
			convertView = View.inflate(MyApplication.getContext(), R.layout.layout_listview_journey, null);
		}
		
		ViewHolder holder = ViewHolder.getHolderInstance(convertView);
		holder.tv_journey_title.setText(journey.getName());
		if(what == 0){
			holder.tv_journey_time.setText(journey.getStime() +"-"+ journey.getEtime());
		}else if(what == 1){
			holder.tv_journey_time.setText(journey.getDate());
		}
		holder.tv_journey_location.setText(journey.getLocation());
		
//		holder.civ_journey_image.setImageBitmap(bm);
		
		return convertView;
	}

	public static class ViewHolder {

		TextView tv_journey_title;
		TextView tv_journey_time;
		TextView tv_journey_location;
		CircleImageView civ_journey_image;

		public ViewHolder(View convertView) {
			tv_journey_title = (TextView) convertView.findViewById(R.id.tv_journey_title);
			tv_journey_time = (TextView) convertView.findViewById(R.id.tv_journey_time);
			tv_journey_location = (TextView) convertView.findViewById(R.id.tv_journey_location);
			civ_journey_image = (CircleImageView) convertView.findViewById(R.id.civ_journey_image);
		}

		public static ViewHolder getHolderInstance(View convertView) {
			ViewHolder holder = (ViewHolder) convertView.getTag();
			if (holder == null) {
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			}
			return holder;
		}

	}

}
