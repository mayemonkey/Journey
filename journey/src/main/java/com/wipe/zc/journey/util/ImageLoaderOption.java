package com.wipe.zc.journey.util;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.wipe.zc.journey.R;

/**
 * ImageLoader配置
 */
    public class ImageLoaderOption {
        //在显示小图的选项
        public static DisplayImageOptions list_options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.icon_image_error)// 加载过程中显示什么图片
                .showImageForEmptyUri(R.drawable.icon_image_error)// url为空的时候显示什么图片
                .showImageOnFail(R.drawable.icon_image_error)// 加载失败显示什么图片
                .cacheInMemory(true)// 在内存缓存
                .cacheOnDisk(true)// 在硬盘缓存
                .considerExifParams(true)// 会识别图片的方向信息
                .displayer(new FadeInBitmapDisplayer(300)).build();// 渐渐显示的动画
        // .displayer(new RoundedBitmapDisplayer(100)).build();//显示圆角或圆形图片


    //在显示大图的时候的选项，
    public static DisplayImageOptions pager_options = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.drawable.icon_image_error)
            .showImageOnFail(R.drawable.icon_image_error)
            .resetViewBeforeLoading(true)//在ImageView显示图片之前先清空已有的图片内容
            .cacheOnDisk(true)
            .imageScaleType(ImageScaleType.EXACTLY)//会进一步将按照ImageView的宽高来缩放
            .bitmapConfig(Bitmap.Config.RGB_565)//设置颜色的渲染模式,是比较节省内存的模式
            .considerExifParams(true)
            .displayer(new FadeInBitmapDisplayer(300)).build();

}
