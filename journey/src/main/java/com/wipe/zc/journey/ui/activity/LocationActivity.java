package com.wipe.zc.journey.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.wipe.zc.journey.R;
import com.wipe.zc.journey.ui.adapter.LocationAdapter;
import com.wipe.zc.journey.util.ToastUtil;

public class LocationActivity extends Activity implements OnClickListener, OnItemClickListener,
		OnMapStatusChangeListener {
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = null;
	private MapView mapView;
	protected BaiduMap baiduMap;
	protected LatLng locationLanLng = new LatLng(116.403963, 39.915125); // //天安门

	private List<PoiInfo> list = new ArrayList<PoiInfo>();
	private LocationAdapter adapter;
	private ListView lv_location;

	private boolean isFirstLocation = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_location);
		initSdk();
		init();
		locate();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mapView.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		mLocationClient.stop();
		baiduMap.setMyLocationEnabled(false);
		mapView.onDestroy();
		if (geoCoder != null) {
			geoCoder.destroy();
		}
		mapView = null;
	}

	private void init() {
		// 设置缩放级别,范围 2.X 3~19 1.X 3~18 3.5 3~20
		// 2.X 与 1.X 主要区别
		// ①修改了文件的格式，优化了数据，北京市（110m 15m）
		// ②增加一个级别，增加了3d效果（18 19）

		// BaiduMap，控制器，控制Mapview，操作：旋转、缩放、移动
		mapView = (MapView) findViewById(R.id.mapview);
		iv_location_cancle = (ImageView) findViewById(R.id.iv_location_cancle);
		iv_location_cancle.setOnClickListener(this);
		iv_location_ensure = (ImageView) findViewById(R.id.iv_location_ensure);
		iv_location_ensure.setOnClickListener(this);
		et_location = (EditText) findViewById(R.id.et_location);
		iv_location = (ImageView) findViewById(R.id.iv_location);
		iv_location.setOnClickListener(this);
		lv_location = (ListView) findViewById(R.id.lv_location);

		baiduMap = mapView.getMap();

		// 设置地图状态
		MapStatus mMapStatus = new MapStatus.Builder().zoom(18).build();
		MapStatusUpdate zoom = MapStatusUpdateFactory.newMapStatus(mMapStatus);// 默认缩放级别12
		// 改变地图状态
		baiduMap.setMapStatus(zoom);

		// 地图状态改变监听
		baiduMap.setOnMapStatusChangeListener(this);

		// 开启定位图层
		baiduMap.setMyLocationEnabled(true);

		// 定位图层显示方式
		currentMode = MyLocationConfiguration.LocationMode.NORMAL;

		baiduMap.setMyLocationConfigeration(new MyLocationConfiguration(currentMode, true, null));

		adapter = new LocationAdapter(list);
		lv_location.setAdapter(adapter);
		lv_location.setOnItemClickListener(this);
		// mapView.showZoomControls(false);// 去掉缩放按钮
		// mapView.showScaleControl(false);// 去掉标尺
	}

	private void initSdk() {
		// SDKInitializer.initialize(getApplicationContext());// 必须传递全局Context
		// 注册校验失败广播
		IntentFilter filter = new IntentFilter();
		filter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
		filter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
		registerReceiver(new MyReceiver(), filter);
	}

	private void locate() {
		mLocationClient = new LocationClient(this);
		myListener = new MyBDLocationListener();
		mLocationClient.registerLocationListener(myListener);

		LocationClientOption option = new LocationClientOption();
		option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		option.setIsNeedLocationPoiList(true);

		option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setOpenGps(true);// 可选，默认false,设置是否使用gps
		option.setScanSpan(1000);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		// option.setLocationNotify(true);//
		// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		// option.setIgnoreKillProcess(false);//
		// 可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
		mLocationClient.setLocOption(option);
		mLocationClient.start();

		// myListener = new MyBDLocationListener();
		// // BitmapDescriptor icon =
		// // BitmapDescriptorFactory.fromResource(R.drawable.icon_poi);
		// /**
		// * BitmapDescriptor customMarker 用户自定义定位图标 boolean enableDirection
		// * 是否允许显示方向信息 MyLocationConfiguration.LocationMode locationMode
		// 定位图层显示方式
		// */
		// MyLocationConfiguration config = new
		// MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL,
		// true,
		// null);
		// baiduMap.setMyLocationConfigeration(config);
	}

	private GeoCoder geoCoder;
	private LocationMode currentMode;
	private ImageView iv_location_ensure;
	private ImageView iv_location_cancle;
	private EditText et_location;
	private ImageView iv_location;
	private String city;

	class MyBDLocationListener implements BDLocationListener, OnGetGeoCoderResultListener {

		@Override
		public void onReceiveLocation(BDLocation result) {
			if (result == null || baiduMap == null) {
				return;
			}

			MyLocationData data = new MyLocationData.Builder().accuracy(result.getRadius())
					.direction(result.getDirection()).latitude(result.getLatitude())// 设置纬度
					.longitude(result.getLongitude())// 设置经度
					.build();
			baiduMap.setMyLocationData(data);// 设置定位数据, 只有先允许定位图层后设置数据才会生效，参见

			if (isFirstLocation) {
				isFirstLocation = false;
				LatLng ll = new LatLng(result.getLatitude(), result.getLongitude());
				MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(ll, 18);
				baiduMap.animateMapStatus(msu);

				// 获取坐标
				locationLanLng = new LatLng(result.getLatitude(), result.getLongitude());
				// 获取城市
				city = result.getCity();

				geoCoder = GeoCoder.newInstance();
				// 发起饭地址编码请求
				ReverseGeoCodeOption reverseGeoCodeOption = new ReverseGeoCodeOption();
				// 设置反地理编码位置坐标
				reverseGeoCodeOption.location(new LatLng(result.getLatitude(), result.getLongitude()));
				geoCoder.reverseGeoCode(reverseGeoCodeOption);
				// 设置查询结果监听者
				geoCoder.setOnGetGeoCodeResultListener(this);
			}

		}

		@Override
		public void onGetGeoCodeResult(GeoCodeResult arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		// 反地址编码回调函数
		public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reversGeoCodeResult) {
			List<PoiInfo> poiInfos = reversGeoCodeResult.getPoiList();
			// 数据更新
			if (poiInfos != null) {
				list.clear();
				list.addAll(poiInfos);
				// adapter = new LocationAdapter(list);
				// lv_location.setAdapter(adapter);
				adapter.notifyDataSetChanged();
			}

		}
	}

	class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
				ToastUtil.shortToast("网络错误");
			} else if (action.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
				ToastUtil.shortToast("校验失败");
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		String address = "(" + list.get(position).name + ")" + list.get(position).address;
		et_location.setText(address);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.iv_location_cancle:
			setResult(1, new Intent().putExtra("location", ""));
			finish();
			break;

		case R.id.iv_location_ensure:
			setResult(1, new Intent().putExtra("location", et_location.getText().toString()));
			finish();
			break;

		case R.id.iv_location:
			String location = et_location.getText().toString();
			if (!TextUtils.isEmpty(location)) {
				// 设置Poi搜索
				PoiSearch poiSearch = PoiSearch.newInstance();
				PoiCitySearchOption poicitySearchOption = new PoiCitySearchOption();
				poicitySearchOption.city(city);
				poicitySearchOption.keyword(location);
				poicitySearchOption.pageCapacity(10);
				poicitySearchOption.pageNum(1);
				poiSearch.searchInCity(poicitySearchOption);
				poiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
					@Override
					public void onGetPoiResult(PoiResult poiResult) {
						if (poiResult != null) {
							List<PoiInfo> list_poi = poiResult.getAllPoi();
							if (list_poi != null && list_poi.size() != 0) {
								LatLng latlng = list_poi.get(0).location;
								MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(latlng, 18);
								baiduMap.animateMapStatus(msu);
							}
						}
					}

					public void onGetPoiDetailResult(PoiDetailResult arg0) {
					}
				});
			}
			break;

		default:
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			setResult(1, new Intent().putExtra("location", et_location.getText().toString()));
		}
		
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onMapStatusChange(MapStatus arg0) {

	}

	@Override
	public void onMapStatusChangeFinish(MapStatus matStatus) {
		LatLng cenpt = matStatus.target;
		geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(cenpt));
	}

	@Override
	public void onMapStatusChangeStart(MapStatus arg0) {
		// TODO Auto-generated method stub

	}
}
