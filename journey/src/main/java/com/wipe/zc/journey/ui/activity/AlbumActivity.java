package com.wipe.zc.journey.ui.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.GridView;
import android.widget.TextView;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.wipe.zc.journey.R;
import com.wipe.zc.journey.domain.ImageItem;
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

    //图片路径集合
    private List<String> list_dir = new ArrayList<>();

    //当前GridView中显示内容List集合
    private List<ImageItem> list = new ArrayList<>();

    //设备中所有图片的文件夹及文件夹内Image
    private Map<String, List<ImageItem>> map = new HashMap<>();

    private AlbumAdapter adapter;


    //Handler标示What全局定义
    private static final int BIND_SPINNER = 0;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case BIND_SPINNER:
                    getDirList();
                    ms_album.setItems(list_dir);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_album);

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
     * 初始化控件
     */
    private void initWidget() {
        TextView tv_album_cancel = (TextView) findViewById(R.id.tv_album_cancel);
        tv_album_cancel.setOnClickListener(this);

        ms_album = (MaterialSpinner) findViewById(R.id.ms_album);
        ms_album.setOnItemSelectedListener(this);


        TextView tv_album_ensure = (TextView) findViewById(R.id.tv_album_ensure);
        tv_album_ensure.setOnClickListener(this);

        GridView gv_album = (GridView) findViewById(R.id.gv_album);
        adapter = new AlbumAdapter(list);
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

        if(cur == null){
            return;
        }

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



//            130077,
//            bucketId: 97354131,
//            picasaId: null,
//            name:1a05408d553c39aa.jpg,
//            path:/storage/emulated/0/Tencent/QQ_Images/1a05408d553c39aa.jpg,
//            title: 1a05408d553c39aa,
//            size: 114350,
//            bucket: QQ_Images---,

//            ImageBucket bucket = bucketList.get(bucketId);
//            if (bucket == null) {
//                bucket = new ImageBucket();
//                bucketList.put(bucketId, bucket);
//                bucket.imageList = new ArrayList<ImageItem>();
//                bucket.bucketName = bucketName;
//            }
//            bucket.count++;
//            ImageItem imageItem = new ImageItem();
//            imageItem.imageId = _id;
//            imageItem.imagePath = path;
//            imageItem.thumbnailPath = thumbnailList.get(_id);
//            bucket.imageList.add(imageItem);
        }
        cur.close();
    }

    /**
     * 通过MAP获取路径List
     */
    private void getDirList() {
        Set<String> set = map.keySet();
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            String dir = iterator.next();
            list_dir.add(dir);
        }
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
        list.clear();
        String dir = list_dir.get(position);
        List<ImageItem> imageItems = map.get(dir);
        list.addAll(imageItems);
        adapter.notifyDataSetChanged();
    }
}
