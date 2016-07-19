package com.wipe.zc.journey.util;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.wipe.zc.journey.global.MyApplication;

/**
 * 常用工具包
 */
public class CommonUtil {

    /**
     * 通过系统Uri获取文件路径
     *
     * @param uri 路径
     */
    public static String fromUriToPath(Uri uri) {
        if (uri == null) {
            return null;
        }
        String data = "";
        String scheme = uri.getScheme();
        //当Uri无格式或格式为文件形式时，不需要进行转换
        if (scheme == null || ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        }
        //当Uri为内容格式时，进行相应的转换
        else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = MyApplication.getContext().getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null,
                    null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
            }
            if(cursor != null)
                cursor.close();
        }
        return data;
    }
}
