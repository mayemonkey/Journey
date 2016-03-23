package com.wipe.zc.journey.ui.activity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMChatManager;
import com.easemob.exceptions.EaseMobException;
import com.wipe.zc.journey.domain.User;
import com.wipe.zc.journey.http.AppURL;
import com.wipe.zc.journey.lib.CircleImageView;
import com.wipe.zc.journey.util.HttpUtil;
import com.wipe.zc.journey.util.ToastUtil;
import com.wipe.zc.journey.util.ViewUtil;
import com.wipe.zc.journey.R;

public class RegisterActivity extends Activity implements OnClickListener {

    private ImageView iv_register_cancel;
    private CircleImageView civ_icon;
    private CircleImageView civ_icon_add;
    private EditText et_register_nickname;
    private View ve_register_nickname;
    private EditText et_register_email;
    private View ve_register_email;
    private EditText et_register_password;
    private View ve_register_password;
    private EditText et_register_repassword;
    private View ve_register_repassword;
    private EditText et_register_phone;
    private View ve_register_phone;
    private TextView tv_register;
    private TextView tv_to_login;
    // 获取的图片对象
    private Bitmap bmp = null;
    // 是否选择图片
    private boolean flag_icon = false;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            int i = msg.what;
            switch (i) {
                case 1:
                    String result = (String) msg.obj;
                    if (result != null) {
                        if (result.equals("注册成功")) {
                            ToastUtil.shortToast("注册成功");
                            finish();
                        }
                        if (result.equals("注册失败")) {
                            ToastUtil.shortToast("注册失败，请检查网络");
                        }
                    } else {
                        ToastUtil.shortToast("请检查网络连接");
                    }
                    break;

                default:
                    break;
            }

        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();

    }

    /**
     * 初始化控件
     */
    private void initView() {
        // 取消注册
        iv_register_cancel = (ImageView) findViewById(R.id.iv_register_cancel);
        iv_register_cancel.setOnClickListener(this);
        // 头像显示
        civ_icon = (CircleImageView) findViewById(R.id.civ_icon);
        // 添加头像
        civ_icon_add = (CircleImageView) findViewById(R.id.civ_icon_add);
        civ_icon_add.setOnClickListener(this);
        // 表单数据
        et_register_nickname = (EditText) findViewById(R.id.et_register_nickname);
        et_register_email = (EditText) findViewById(R.id.et_register_email);
        et_register_password = (EditText) findViewById(R.id.et_register_password);
        et_register_repassword = (EditText) findViewById(R.id.et_register_repassword);
        et_register_phone = (EditText) findViewById(R.id.et_register_phone);
        // 注册按钮
        tv_register = (TextView) findViewById(R.id.tv_register);
        tv_register.setOnClickListener(this);
        // 直接登录
        tv_to_login = (TextView) findViewById(R.id.tv_to_login);
        tv_to_login.setOnClickListener(this);

        ve_register_nickname = findViewById(R.id.ve_register_nickname);
        ve_register_email = findViewById(R.id.ve_register_email);
        ve_register_password = findViewById(R.id.ve_register_password);
        ve_register_repassword = findViewById(R.id.ve_register_repassword);
        ve_register_phone = findViewById(R.id.ve_register_phone);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_register_cancel: // 取消注册
                finish();
                break;

            case R.id.tv_register: // 提交注册数据
                checkAndUploadForm();
                break;

            case R.id.civ_icon_add: // 添加头像
                Intent intent = new Intent(Intent.ACTION_PICK);
//			intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "选择图片"), 0);
                break;

            case R.id.tv_to_login: // 直接登录
                finish();
                break;

            default:
                break;
        }
    }

    /**
     * 检查表单
     */
    private void checkAndUploadForm() {
        if (flag_icon) { // 判断是否选择头像
            // 昵称输入空检查
            if (ViewUtil.checkEmptyData(et_register_nickname, ve_register_nickname)) {
                if (checkData(et_register_nickname.getText().toString(), 1)) {
                    // 邮箱输入空检查
                    if (ViewUtil.checkEmptyData(et_register_email, ve_register_email)) {
                        if (checkData(et_register_email.getText().toString(), 2)) {
                            // 密码输入空检查
                            if (ViewUtil.checkEmptyData(et_register_password,
                                    ve_register_password)) {
                                if (checkData(et_register_password.getText().toString(), 3)) {
                                    // 密码确认输入空检查
                                    if (ViewUtil.checkEmptyData(et_register_repassword,
											ve_register_repassword)) {
                                        if (checkData(et_register_repassword.getText().toString()
												, 4)) {
                                            String phone = "";
                                            if (et_register_phone.getText().toString() != null) {
                                                if (checkData(et_register_phone.getText()
														.toString(), 5)) {
                                                    phone = et_register_phone.getText().toString();
                                                } else { // 电话格式不符合
                                                    ToastUtil.shortToast("请输入正确格式的手机号码");
                                                }
                                            }
                                            // 所有数据检查完毕
                                            uploadData(phone);

                                        } else { // 重复密码格式不符合
                                            ToastUtil.shortToast("请保证两次输入的密码一致");
                                        }
                                    } else { // 未输入重复密码
                                        ToastUtil.shortToast("请再次输入密码");
                                    }
                                } else { // 密码格式不符合要求
                                    ToastUtil.shortToast("请输入正确格式的密码");
                                }
                            } else { // 未输入密码
                                ToastUtil.shortToast("请输入密码");
                            }
                        } else { // 邮箱格式不符合要求
                            ToastUtil.shortToast("请输入正确格式的邮箱地址");
                        }
                    } else { // 未输入邮箱
                        ToastUtil.shortToast("请输入邮箱地址");
                    }
                } else { // 昵称格式不符合要求
                    ToastUtil.shortToast("请输入正确格式的昵称");
                }
            } else { // 未输入昵称
                ToastUtil.shortToast("请输入昵称");
            }
        } else { // 未选择头像
            ToastUtil.shortToast("请从相册中选择头像");
        }
    }

    /**
     * 提交数据
     */
    private void uploadData(String phone) {

        // 提交表单中数据
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    EMChatManager.getInstance()
                            .createAccountOnServer(et_register_nickname.getText().toString(),
                                    et_register_password.getText().toString());

                    // 上传头像
                    uploadImage(AppURL.upLoad, et_register_nickname.getText().toString(), bmp);
                    // 提交表单
                    User user = new User();
                    user.setNickname(et_register_nickname.getText().toString());
                    user.setEmail(et_register_email.getText().toString());
                    user.setPassword(et_register_password.getText().toString());
                    user.setPhone(et_register_phone.getText().toString());
                    // TODO 服务端图片路径拼接
                    user.setIcon("E:\\icon\\" + et_register_nickname.getText().toString() + ".jpg");
                    String result = HttpUtil.requestByPost(AppURL.reg, user);
                    // Handler
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = result;
                    handler.sendMessage(msg);
                } catch (EaseMobException e) {
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = "注册失败";
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 表单数据合理性检查
     *
     * @param string
     * @param i
     */
    private boolean checkData(String string, int i) {
        switch (i) {
            case 1: // 昵称长度判断
                return (string.length() <= 20);

            case 2: // 邮箱格式
                Pattern pattern_email = Pattern
                        .compile("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
                Matcher match_email = pattern_email.matcher(string);
                return match_email.matches();

            case 3: // 密码长度
                return (string.length() <= 20);

            case 4: // 密码重复
                return (string.equals(et_register_password.getText().toString()));

            case 5: // 电话格式
                Pattern pattern_phone = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
                Matcher match_phone = pattern_phone.matcher(string);
                return match_phone.matches();

            default:
                return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // 选择图片
            Uri uri = data.getData();
            ContentResolver cr = this.getContentResolver();
            try {
                if (bmp != null)// 如果不释放的话，不断取图片，将会内存不够
                    bmp.recycle();
                bmp = BitmapFactory.decodeStream(cr.openInputStream(uri));
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println("the bmp toString: " + bmp);
            // TODO 修改处
            civ_icon.setImageBitmap(bmp);
            flag_icon = true;
        } else {
            flag_icon = false;
            Toast.makeText(RegisterActivity.this, "请重新选择图片", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 上传照片
     *
     * @param url
     * @param photoName
     * @param photoBitmap
     */
    private void uploadImage(String url, String photoName, Bitmap photoBitmap) {
        // TODO 图片上传保存路径
        File file = new File(getCacheDir().getAbsolutePath() + "//" + photoName + ".jpg");// 将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        HttpUtil.uploadImage(url, file);
    }

}
