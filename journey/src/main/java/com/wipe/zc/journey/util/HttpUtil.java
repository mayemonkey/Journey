package com.wipe.zc.journey.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import com.wipe.zc.journey.domain.Journey;
import com.wipe.zc.journey.domain.User;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.Callback;

public class HttpUtil {

    /**
     * get请求
     *
     * @return  请求结果
     */
    public static String requestByGet(String url_path) {
        try {
            URL url = new URL(url_path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设定连接设置
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestMethod("GET");

            // 创建连接
            int code = conn.getResponseCode();
            if (code == 200) {
                InputStream is = conn.getInputStream();
                return getResult(is);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取位图对象
     *
     * @param url_paht    请求地址
     * @param file        保存图片文件对象
     * @return            图片Bitmap对象
     */
    public static Bitmap getImage(String url_paht, File file, String nickname) {
        URL url;
        try {
            url = new URL(url_paht + "?nickname=" + URLEncoder.encode(nickname, "utf-8"));

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(5000);
            conn.setRequestMethod("GET");

            // 创建连接
            int code = conn.getResponseCode();
            if (code == 200) {
                InputStream is = conn.getInputStream();
                FileOutputStream fos = new FileOutputStream(file);
                int len ;
                byte[] buf = new byte[1024];
                while ((len = is.read(buf)) != -1) {
                    fos.write(buf, 0, len);
                }
                fos.close();
                return BitmapFactory.decodeFile(file.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 上传图片工具方法
     *
     * @param url_path    路径
     * @param file        上传文件对象
     * @return            请求结果
     */
    public static String uploadImage(String url_path, File file) {
        DataOutputStream ds = null;
        try {
            URL url = new URL(url_path);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

			/* 允许Input、Output，不使用Cache */

            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);

			/* 设定传送的method=POST */
            con.setRequestMethod("POST");
            /* setRequestProperty */
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + "*****");

            ds = new DataOutputStream(con.getOutputStream());
            ds.writeBytes("--" + "*****" + "\r\n");
            ds.writeBytes("Content-Disposition: form-data; name=\"img\"; filename=\"" + file.getName() + "\"" + "\r\n");
            ds.writeBytes("\r\n");

			/* 取得文件的FileInputStream */
            FileInputStream fStream = new FileInputStream(file);
            /* 设定每次写入1024bytes */
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int length ;

			/* 从文件读取数据到缓冲区 */
            while ((length = fStream.read(buffer)) != -1) {
                /* 将数据写入DataOutputStream中 */
                ds.write(buffer, 0, length);
            }

            ds.writeBytes("\r\n");
            ds.writeBytes("--" + "*****" + "--" + "\r\n");
            fStream.close();
            ds.flush();

			/* 取得Response内容 */
            InputStream is = con.getInputStream();
            int ch;
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }

            System.out.println("上传成功");
            // Toast.makeText(RegisterActivity.this, "上传成功", Toast.LENGTH_LONG)
            //
            // .show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("上传失败" + e.getMessage());
            // Toast.makeText(MainActivity.this, "上传失败" + e.getMessage(),
            //
            // Toast.LENGTH_LONG).show();
        } finally {
            try {
                if (ds != null) {
                    ds.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 批量上传
     * @param url 路径
     * @param list 对象
     */
    public static void uploadBatch(String url, String text, List<String> list, View view, Callback callback){

        Map<String,String> headers = new HashMap<>();

        headers.put("Connection","Alive");
        headers.put("Charset", "UTF-8");
        headers.put("Content-Type", "multipart/form-data;boundary=\" + \"*****\"");

        PostFormBuilder builder = OkHttpUtils.post().url(url).headers(headers).addParams("text", text);

        for(String str : list){
            File file = new File(str);
            if(!file.exists()){
                return ;
            }

            builder.addFile("mfile", file.getName(), file);
        }

        builder.build().execute(callback);
    }


    public static String requestJourney_ByGet(String url_path, Object obj) {
        try {
            String data = "";
            if (obj instanceof User) {
                User user = (User) obj;
                data = "nickname=" + user.getNickname() + "&password=" + user.getPassword() +
                        "&email="
                        + user.getEmail() + "&phone=" + user.getPhone() + "&icon=" + user.getIcon();
            }
            if (obj instanceof Journey) {
                Journey journey = (Journey) obj;
                data = "?nickname=" + journey.getNickname() + "&date=" + journey.getDate()
                        + "&name=" + URLEncoder.encode(journey.getName(), "UTF-8") + "&describe="
                        + URLEncoder.encode(journey.getDescribe(), "UTF-8") + "&stime=" + journey
                        .getStime()
                        + "&etime=" + journey.getEtime() + "&location="
                        + URLEncoder.encode(journey.getLocation(), "UTF-8");
            }
            if (obj instanceof String) {
                data = "nickname=" + obj;
            }

            URL url = new URL(url_path + data);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;" +
                    "q=0.9,*/*;q=0.8");
            conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
            conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");

            conn.setConnectTimeout(10000);
            conn.setReadTimeout(5000);
            conn.setRequestMethod("GET");

            int code = conn.getResponseCode();
            if (code == 200) {
                InputStream is = conn.getInputStream();
                return getResult(is);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String requestJourney(String url_path, Object obj) {
        try {
            String data = "";
            if (obj instanceof User) {
                User user = (User) obj;
                data = "nickname=" + user.getNickname() + "&password=" + user.getPassword() +
                        "&email="
                        + user.getEmail() + "&phone=" + user.getPhone() + "&icon=" + user.getIcon();
            }
            if (obj instanceof Journey) {
                Journey journey = (Journey) obj;
                data = "nickname=" + URLEncoder.encode(journey.getNickname(), "UTF-8") + "&date="
                        + journey.getDate()
                        + "&name=" + journey.getName() + "&describe="
                        + URLEncoder.encode(journey.getDescribe(), "UTF-8") + "&stime=" + journey
                        .getStime()
                        + "&etime=" + journey.getEtime() + "&location="
                        + URLEncoder.encode(journey.getLocation(), "UTF-8");
            }
            if (obj instanceof String) {
                data = "nickname=" + obj;
            }

            URL url = new URL(url_path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", data.getBytes().length + "");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(5000);
            conn.setRequestMethod("POST");
            // 允许输出流
            conn.setDoOutput(true);
            // 将post参数放入输出提交
            OutputStream os = conn.getOutputStream();
            os.write(data.getBytes());
            // os.close();

            int code = conn.getResponseCode();
            if (code == 200) {
                InputStream is = conn.getInputStream();
                return getResult(is);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String requestByPost(String url_path, Object obj) {
        try {
            String data = "";
            if (obj instanceof User) {
                User user = (User) obj;
                data = "nickname=" + user.getNickname() + "&password=" + user.getPassword() +
                        "&email="
                        + user.getEmail() + "&phone=" + user.getPhone() + "&icon=" + user.getIcon();
            }
            if (obj instanceof Journey) {
                Journey journey = (Journey) obj;
                data = "nickname=" + journey.getNickname() + "&date=" + journey.getDate() + "&name="
                        + journey.getName() + "&describe=" + journey.getDescribe() + "&stime=" +
                        journey.getStime()
                        + "&etime=" + journey.getEtime() + "&location=" + journey.getLocation();
            }
            if (obj instanceof String) {
                data = "nickname=" + obj;
            }

            URL url = new URL(url_path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(5000);
            conn.setRequestMethod("POST");
            // 允许输出流
            conn.setDoOutput(true);
            // 将post参数放入输出提交
            OutputStream os = conn.getOutputStream();
            os.write(data.getBytes());
            os.close();

            int code = conn.getResponseCode();
            if (code == 200) {
                InputStream is = conn.getInputStream();
                return getResult(is);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String requestUser_ByOK(String url_path, String nickname, String password) {
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("nickname", nickname)
                .add("password", password)
                .build();

        Request request = new Request.Builder().url(url_path).post(body).build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String requestOkHttp(String url_path, Journey journey) {
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("id", String.valueOf(journey.getId()))
                .add("name", journey.getName())
                .add("describe", journey.getDescribe())
                .add("date", journey.getDate())
                .add("stime", journey.getStime())
                .add("etime", journey.getEtime())
                .add("location", journey.getLocation())
                .add("nickname", journey.getNickname()).build();

        Request request = new Request.Builder().url(url_path).post(body).build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String requestByPost_Client(String url_path, Object obj) {

        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(url_path);
            post.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            Journey journey = (Journey) obj;
            List<NameValuePair> list = new ArrayList<>();

            list.add(new BasicNameValuePair("name", journey.getName()));
            list.add(new BasicNameValuePair("describe", journey.getDescribe()));
            list.add(new BasicNameValuePair("date", journey.getDate()));
            list.add(new BasicNameValuePair("stime", journey.getStime()));
            list.add(new BasicNameValuePair("etime", journey.getEtime()));
            list.add(new BasicNameValuePair("location", journey.getLocation()));
            list.add(new BasicNameValuePair("nickname", journey.getNickname()));
            UrlEncodedFormEntity entity ;
            entity = new UrlEncodedFormEntity(list, "UTF-8");
            post.setEntity(entity);
            HttpResponse response = client.execute(post);
            int code = response.getStatusLine().getStatusCode();
            if (code == 200) {
                InputStream is = response.getEntity().getContent();
                return getResult(is);
            }
            client.getConnectionManager().shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String requestByGet_Client(String username, String password) {
        try {
            String data = "?username=" + username + "&password=" + password;
            // 创建Client对象
            HttpClient client = new DefaultHttpClient();
            // 创建get对象
            HttpGet get = new HttpGet("http://192.168.21.42:8080/ForAndroid/servlet1" + data);
            HttpResponse response = client.execute(get);
            int code = response.getStatusLine().getStatusCode();
            if (code == 200) {
                InputStream is = response.getEntity().getContent();
                return getResult(is);
            }
            client.getConnectionManager().shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

	/*public static String httpPostString(String url_path, Object obj){
        HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url_path);


		return null;
	}*/


    /**
     * 获取结果
     */
    public static String getResult(InputStream in) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int len ;
        byte[] buf = new byte[1024];
        try {
            while ((len = in.read(buf)) != -1) {
                bos.write(buf, 0, len);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String result = bos.toString();
        String charset = "UTF-8";
        // if(html.contains("gbk") || html.contains("gbk2312") ||
        // html.contains("GBK") || html.contains("GBK2312")){
        // charset = "GBK";
        // }
        try {
            result = new String(bos.toByteArray(), charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getXml(InputStream is) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            int len ;
            byte[] buf = new byte[1024];
            while ((len = is.read(buf)) != -1) {
                bos.write(buf, 0, len);
            }
            bos.flush();
            return new String(bos.toByteArray(), "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
