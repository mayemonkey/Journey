package com.wipe.zc.journey.ui.adapter;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.wipe.zc.journey.R;
import com.wipe.zc.journey.global.MyApplication;

public class LocationAdapter extends BaseAdapter {

	private List<PoiInfo> list;

	public LocationAdapter(List<PoiInfo> list) {
		this.list = list;
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
		if (convertView == null) {
			convertView = View.inflate(MyApplication.getContext(), R.layout.layout_list_location, null);
		}
		PoiInfo poiInfo = list.get(position);
		TextView tv = (TextView) convertView.findViewById(R.id.tv_listview_location);
		String address = poiInfo.address;
		String name = poiInfo.name;
		tv.setText("(" + name + ")" + address);

		return convertView;
	}

}
