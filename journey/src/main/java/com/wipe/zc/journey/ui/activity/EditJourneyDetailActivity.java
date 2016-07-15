package com.wipe.zc.journey.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.wipe.zc.journey.R;
import com.wipe.zc.journey.ui.adapter.EditJourneyDetailAdapter;
import com.wipe.zc.journey.util.HttpUtil;
import com.wipe.zc.journey.util.ToastUtil;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class EditJourneyDetailActivity extends Activity implements View.OnClickListener {

    private static final int TAKE_PHOTO = 1;
    private static final int CHOOSE_PHOTO = 2;

    private List<String> list = new ArrayList<>();

    private EditText et_edit_journey_text;
    private GridView gv_edit_journey;
    private TextView tv_edit_journey_info_time;

    private EditJourneyDetailAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_edit_journey);

        initWidget();

        initInfo();
    }

    /**
     * 初始化信息
     */
    private void initInfo() {
        Intent intent = getIntent();
        String time = intent.getStringExtra("time");
        String title = intent.getStringExtra("title");

        if (!TextUtils.isEmpty(time)) {
            tv_edit_journey_info_time.setText(time);
        }

        if (!TextUtils.isEmpty(title)) {
            et_edit_journey_text.setHint("输入'" + title + "'行程内容...");
        }
    }

    /**
     * 初始化控件
     */
    private void initWidget() {
        ImageView iv_edit_journey_cancle = (ImageView) findViewById(R.id.iv_edit_journey_cancle);
        iv_edit_journey_cancle.setOnClickListener(this);

        tv_edit_journey_info_time = (TextView) findViewById(R.id.tv_edit_journey_info_time);

        ImageView iv_edit_journey_ensure = (ImageView) findViewById(R.id.iv_edit_journey_ensure);
        iv_edit_journey_ensure.setOnClickListener(this);

        et_edit_journey_text = (EditText) findViewById(R.id.et_edit_journey_text);

        gv_edit_journey = (GridView) findViewById(R.id.gv_edit_journey);
        list.add("add");
        adapter = new EditJourneyDetailAdapter(this, list);
        gv_edit_journey.setAdapter(adapter);

        ImageView iv_edit_journey_takephoto = (ImageView) findViewById(R.id.iv_edit_journey_takephoto);
        iv_edit_journey_takephoto.setOnClickListener(this);

        ImageView iv_edit_journey_choosephoto = (ImageView) findViewById(R.id.iv_edit_journey_choosephoto);
        iv_edit_journey_choosephoto.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_edit_journey_cancle:
                finish();
                break;

            case R.id.iv_edit_journey_ensure:   //上传EditJourney内容
                String text = et_edit_journey_text.getText().toString();
                if (TextUtils.isEmpty(text)) {      //文本内容为空
                    ToastUtil.shortToast("未输入行程记录");
                    return;
                } else {
                    if (list.size() > 1) {    //存在图片数据
                        //上传图片数据
                        //多文件打包上传
                        //TODO 子线程上传
                        HttpUtil.uploadBatch("",text,  list, new TextView(this), new Callback() {
                            @Override
                            public void inProgress(float progress, long total, int id) {
                                super.inProgress(progress, total, id);

                                Intent intent = new Intent();
                                intent.putExtra("progress", progress);
                                intent.setAction("com.zc.journey.upload.progress");
                                sendBroadcast(intent);
                            }

                            @Override
                            public Object parseNetworkResponse(Response response, int id) throws Exception {
                                return null;
                            }

                            @Override
                            public void onError(Call call, Exception e, int id) {
                            }

                            @Override
                            public void onResponse(Object response, int id) {
                                Intent intent = new Intent();
                                intent.putExtra("response", response.toString());
                                intent.setAction("com.zc.journey.upload.result");
                                sendBroadcast(intent);
                            }
                        });
                    }
                }
                break;

            case R.id.iv_edit_journey_takephoto:
                //调用系统相机，路径添加至list


                break;

            case R.id.iv_edit_journey_choosephoto:
                Intent intent = new Intent();
                intent.setClass(this, AlbumActivity.class);
                intent.putStringArrayListExtra("selected", (ArrayList<String>) list);
                startActivityForResult(intent, CHOOSE_PHOTO);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            return;
        }
        //数据获取
        ArrayList<String> list_album = data.getStringArrayListExtra("album");
        if (list_album != null && list_album.size() > 0) {
            list.clear();
            list.addAll(0, list_album);
            list.add("add");
            adapter.notifyDataSetChanged();
        }
    }
}
