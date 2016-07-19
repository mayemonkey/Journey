package com.wipe.zc.journey.ui.adapter;

import java.util.List;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.wipe.zc.journey.http.AppURL;
import com.wipe.zc.journey.ui.activity.HomeActivity;
import com.wipe.zc.journey.util.HttpUtil;
import com.wipe.zc.journey.view.MySwipeLayout;
import com.wipe.zc.journey.R;
import com.wipe.zc.journey.domain.Journey;
import com.wipe.zc.journey.global.MyApplication;
import com.wipe.zc.journey.lib.CircleImageView;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class JourneyAdapter extends BaseAdapter {

    private HomeActivity homeActivity;
    private List<Journey> list;
    private int what;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            SweetAlertDialog dialog = (SweetAlertDialog) msg.obj;
            int position = msg.arg1;
            switch (msg.what) {
                case 0:
                    showErrorDialog(dialog);
                    break;

                case 1:
                    showSuccessDialog(dialog, position);
                    break;
            }
        }
    };

    /**
     * 显示正确
     */
    private void showSuccessDialog(SweetAlertDialog dialog, int position) {
        dialog.setTitleText("删除")
                .setContentText("指定的行程已经删除")
                .setConfirmText("完成")
                .showCancelButton(false)
                .setConfirmClickListener(null)
                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
        list.remove(position);
        notifyDataSetChanged();
    }

    /**
     * 显示错误
     */
    private void showErrorDialog(SweetAlertDialog dialog) {
        dialog.setTitleText("删除")
                .setContentText("删除遇到错误，未成功")
                .setConfirmText("完成")
                .showCancelButton(false)
                .setConfirmClickListener(null)
                .changeAlertType(SweetAlertDialog.ERROR_TYPE);
    }


    public JourneyAdapter(List<Journey> list, int what, HomeActivity homeActivity) {
        this.list = list;
        this.what = what;
        this.homeActivity = homeActivity;
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

        if (convertView == null) {
            convertView = View.inflate(MyApplication.getContext(), R.layout.layout_list_journey,
                    null);
        }

        ViewHolder holder = ViewHolder.getHolderInstance(convertView);

        //可滑动条目设置
        holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

        //删除记录及条目
        holder.tv_journey_delete.setTag(position);
        holder.tv_journey_delete.setOnClickListener(deleteOnClickListner);


        holder.tv_journey_title.setText(journey.getName());
        if (what == 0) {
            holder.tv_journey_time.setText(journey.getStime() + "-" + journey.getEtime());
        } else if (what == 1) {
            holder.tv_journey_time.setText(journey.getDate());
        }
        holder.tv_journey_location.setText(journey.getLocation());

//		holder.civ_journey_image.setImageBitmap(bm);

        return convertView;
    }

    public static class ViewHolder {

        MySwipeLayout swipeLayout;
        TextView tv_journey_title;
        TextView tv_journey_time;
        TextView tv_journey_delete;
        TextView tv_journey_location;
        CircleImageView civ_journey_image;

        public ViewHolder(View convertView) {
            swipeLayout = (MySwipeLayout) convertView.findViewById(R.id.swipeLayout);
            tv_journey_title = (TextView) convertView.findViewById(R.id.tv_journey_title);
            tv_journey_time = (TextView) convertView.findViewById(R.id.tv_journey_time);
            tv_journey_delete = (TextView) convertView.findViewById(R.id.tv_journey_delete);
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

    //删除按钮点击
    private View.OnClickListener deleteOnClickListner = new View.OnClickListener() {
        public void onClick(View view) {
            final int position = (int) view.getTag();

            //显示对话框
            new SweetAlertDialog(homeActivity, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("删除行程")
                    .setContentText("删除的行程将无法恢复!")
                    .setConfirmText("删除")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(final SweetAlertDialog sDialog) {

                            //删除行程
                            final Journey journey = list.get(position);
                            //开启子线程执行网络请求
                            new Thread(new Runnable() {
                                public void run() {
                                    String result = HttpUtil.requestOkHttp(AppURL.deletejourney,
                                            journey);
                                    Message msg = Message.obtain();
                                    msg.arg1 = position;
                                    msg.obj = sDialog;
                                    if (result == null) {
                                        msg.what = 0;
                                    } else {
                                        if (result.equals("删除失败")) {
                                            msg.what = 0;

                                        } else if (result.equals("删除成功")) {
                                            msg.what = 1;
                                        }
                                    }
                                    handler.sendMessage(msg);
                                }
                            }).start();
                        }
                    })
                    .setCancelText("取消")
                    .showCancelButton(true)
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.cancel();
                        }
                    }).show();
        }
    };


}
