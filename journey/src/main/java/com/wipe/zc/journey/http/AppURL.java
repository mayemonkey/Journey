package com.wipe.zc.journey.http;

public class AppURL {

	// 主机地址
	 public static String HostUrl = "http://121.42.185.7:8080/Journey/";
//	public static String HostUrl = "http://10.0.3.2:8080/Journey/";
//	 public static String HostUrl = "http://120.27.97.82:8080/Journey/";

	// 上传地址
	public static String upLoad = HostUrl + "upload";

	// 登陆
	public static String login = HostUrl + "login";

	// 注册表单
	public static String reg = HostUrl + "reg";

	// 获取用户信息
	public static String getuser = HostUrl + "getuser";

	// 获取服务端图片
	public static String getimage = HostUrl + "getimage";

	// 获取服务端行程
	public static String getjourney = HostUrl + "getjourney";

	// 获取服务端行程
	public static String getjourneydate = HostUrl + "getjourneydate";

	// 添加行程
	public static String addjourney = HostUrl + "addjourney";

	//删除行程
	public static String deletejourney = HostUrl + "deletejourney";

	//显示图片
	public static String showimage = HostUrl + "showimage";

}
