package com.wipe.zc.journey.ui.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.GridView;
import android.widget.TextView;

import com.wipe.zc.journey.R;
import com.wipe.zc.journey.domain.ImageItem;
import com.wipe.zc.journey.lib.materialspinner.MaterialSpinner;
import com.wipe.zc.journey.ui.adapter.AlbumAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 相册图片选择
 */
public class AlbumActivity extends Activity implements View.OnClickListener, MaterialSpinner.OnItemSelectedListener {

    private MaterialSpinner ms_album;

    private ArrayList<String> list_send = new ArrayList<>();

    //选中图片集合
    private List<String> list_selected_other = new ArrayList<>();

    //当前页选中
    private List<String> list_selected_curr = new ArrayList<>();

    //选中图片文件夹名称
    private List<String> list_selected_before = new ArrayList<>();

    //图片路径集合
    private List<String> list_dir = new ArrayList<>();

    //当前GridView中显示内容List集合
    private List<ImageItem> list = new ArrayList<>();

    //设备中所有图片的文件夹及文件夹内Image
    private Map<String, List<ImageItem>> map = new HashMap<>();

    private AlbumAdapter adapter;

    //Handler标示What全局定义
    private static final int BIND_SPINNER = 0;

    private static final int CHANGE_ENSURE_ENABLE = 1;

    private static final int CHANGE_ENSURE_DISABLE = 2;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case BIND_SPINNER:
                    getDirList();
                    ms_album.setItems(list_dir);
                    ms_album.setSelectedIndex(0);
                    break;

                case CHANGE_ENSURE_ENABLE:
                    setEnsureEnable(true);
                    setEnsureText("确认(" + (list_selected_other.size() + list_selected_curr.size()) + ")");
                    break;

                case CHANGE_ENSURE_DISABLE:
                    setEnsureEnable(false);
                    setEnsureText("确认");
                    break;
            }
        }
    };
    private TextView tv_album_ensure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_album);

        initSelected();

        initWidget();

        new Thread(new Runnable() {
            @Override
            public void run() {
                //获取所有图片信息
                getImageDir();
                list_dir.clear();
                //提示数据刷新
                handler.sendEmptyMessage(BIND_SPINNER);
            }
        }).start();

    }

    /**
     * 获取之前已选中图片
     */
    private void initSelected() {
        Intent intent = getIntent();
        ArrayList<String> selected = intent.getStringArrayListExtra("selected");
        if (selected != null) {
            list_selected_before.addAll(selected);
        }
    }

    /**
     * 初始化控件
     */
    private void initWidget() {
        TextView tv_album_cancel = (TextView) findViewById(R.id.tv_album_cancel);
        tv_album_cancel.setOnClickListener(this);

        ms_album = (MaterialSpinner) findViewById(R.id.ms_album);
        ms_album.setOnItemSelectedListener(this);

        tv_album_ensure = (TextView) findViewById(R.id.tv_album_ensure);
        tv_album_ensure.setOnClickListener(this);

        GridView gv_album = (GridView) findViewById(R.id.gv_album);
        adapter = new AlbumAdapter(this, list, list_selected_curr, list_selected_other);
        gv_album.setAdapter(adapter);
    }

    /**
     * 获取所有图片路径
     */
    private void getImageDir() {
        String columns[] = new String[]{
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.PICASA_ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.TITLE,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        ContentResolver cr = getContentResolver();

        Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, null);

        if (cur == null) {
            return;
        }

        //获取设备中所有保存的图片信息（由系统数据库收集）
        while (cur.moveToNext()) {
            int photoIDIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            int photoPathIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            int photoNameIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
            int photoTitleIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
            int photoSizeIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE);
            int bucketDisplayNameIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            int bucketIdIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID);
            int picasaIdIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.PICASA_ID);

            String _id = cur.getString(photoIDIndex);
            String name = cur.getString(photoNameIndex);
            String path = cur.getString(photoPathIndex);
            String title = cur.getString(photoTitleIndex);
            String size = cur.getString(photoSizeIndex);
            String bucketName = cur.getString(bucketDisplayNameIndex);
            String bucketId = cur.getString(bucketIdIndex);
            String picasaId = cur.getString(picasaIdIndex);

            Log.i("TAG", _id + ", bucketId: " + bucketId + ", picasaId: " + picasaId + " name:" + name + " path:" + path + " title: " + title + " " +
                    "size: " + size + " bucket: " + bucketName + "---");

            ImageItem imageItem = new ImageItem();
            imageItem.setImageId(_id);
            imageItem.setImagePaht(path);
            //设置ImageItem之前选中状态
            if (list_selected_before != null) {
                if (list_selected_before.contains(path)) {
                    imageItem.setSelected(true);
                }
            }

            //添加数据至Map集合中，将文件夹名作为key存储
            //获取新的文件夹
            if (!map.containsKey(bucketName)) {
                List<ImageItem> list = new ArrayList<>();
                list.add(imageItem);
                map.put(bucketName, list);
            } else {
                List<ImageItem> list = map.get(bucketName);
                list.add(imageItem);
                map.put(bucketName, list);
            }
        }
        cur.close();

        getDirSelectedBefore();
    }

    /**
     * 通过MAP获取路径List
     */
    private void getDirList() {
        Set<String> set = map.keySet();
        for (String dir : set) {
            list_dir.add(dir);
        }
    }

    /**
     * 设置  确认键  可用性
     *
     * @param enable Button可用状态
     */
    public void setEnsureEnable(boolean enable) {
        tv_album_ensure.setEnabled(enable);
        if (enable) {
            tv_album_ensure.setTextColor(Color.WHITE);
        } else {
            tv_album_ensure.setTextColor(Color.parseColor("#666666"));
        }
    }

    /**
     * 设置   确认键  文本
     *
     * @param text Button显示Text
     */
    public void setEnsureText(String text) {
        tv_album_ensure.setText(text);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_album_cancel:
                finish();
                break;

            case R.id.tv_album_ensure:
                setSendList();
                Intent intent = new Intent();
                intent.putStringArrayListExtra("album", list_send);
                setResult(0, intent);
                finish();
                break;
        }
    }

    /**
     * 设置发送集合
     */
    private void setSendList() {
        list_send.clear();
        if (list_selected_curr.size() > 0) {
            for (int i = 0; i < list_selected_curr.size(); i++) {
                list_send.add(list_selected_curr.get(i));
            }
        }
        if (list_selected_other.size() > 0) {
            for (int i = 0; i < list_selected_other.size(); i++) {
                list_send.add(list_selected_other.get(i));
            }
        }
    }

    @Override
    public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
        //TODO  MaterialSpinner  中  list_selected

        //清空当前页选中
        list_selected_curr.clear();
        //清空其他页选中
        list_selected_other.clear();

        //遍历Map确定选中图片的路径
        for (Map.Entry<String, List<ImageItem>> entry : map.entrySet()) {
            //一个Image路径
            List<ImageItem> list_item = entry.getValue();
            boolean flag_selected = false;
            for (ImageItem imageItem : list_item) {
                if (imageItem.isSelected()) {
                    flag_selected = true;

                    if (list_dir.get(position).equals(entry.getKey())) {              //当前页
                        list_selected_curr.add(imageItem.getImagePaht());
                    } else {                                                          //非当前页
                        list_selected_other.add(imageItem.getImagePaht());
                    }
                    ms_album.addSelectedList(entry.getKey());
                }
            }

            //判断路径是否从List移除
            if (!flag_selected) {
                ms_album.removeSelectedList(entry.getKey());
            }
        }

        //清除选中状态
        list.clear();
        String dir = list_dir.get(position);
        List<ImageItem> imageItems = map.get(dir);
        list.addAll(imageItems);
        adapter.notifyDataSetChanged();
    }

    /**
     * 获取之前路径选择
     *
     */
    public void getDirSelectedBefore() {
        int i = 0;
        for (Map.Entry<String, List<ImageItem>> entry : map.entrySet()) {
            //单个路径下图片信息集合
            List<ImageItem> list_item = entry.getValue();
            boolean flag_selected = false;
            for (ImageItem imageItem : list_item) {
                if (imageItem.isSelected()) {
                    if (i == 0) {
                        list_selected_curr.add(imageItem.getImagePaht());
                    } else {
                        list_selected_other.add(imageItem.getImagePaht());
                    }
                    flag_selected = true;
                }
            }
            if (flag_selected) {
                ms_album.addSelectedList(entry.getKey());
            }
            i++;
        }

        if (list_selected_curr.size() + list_selected_other.size() > 0) {
            //子线程中，需要通知主线程刷新
            handler.sendEmptyMessage(CHANGE_ENSURE_ENABLE);
        } else {
            //子线程中，需要通知主线程刷新
            handler.sendEmptyMessage(CHANGE_ENSURE_DISABLE);
        }
    }
}
