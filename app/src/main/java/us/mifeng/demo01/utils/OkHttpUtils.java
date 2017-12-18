package us.mifeng.demo01.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import us.mifeng.demo01.bean.Pictures;

/**
 * Created by 黑夜之火 on 2017/10/30.
 */

public class OkHttpUtils {
    private Context context;
    private ILoadData loadData;
    private Handler handler;
    public OkHttpUtils(Context context,ILoadData loadData) {
        this.context = context;
        this.loadData = loadData;
        handler = new Handler();
    }
    //判断网络是否连接正常
    public boolean isConnected(){
        //获取管理网络的管理者对象
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        //获取网络的链接信息
        NetworkInfo info = manager.getActiveNetworkInfo();
        return info.isConnected();
    }
    public void postRequest(String path, HashMap<String,String>map,String imgPath){
        //第一步获取OkHttp对象
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(4, TimeUnit.SECONDS)
                .readTimeout(4,TimeUnit.SECONDS).build();
        //上传多文件
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        //向Body中添加参数
        for(String key:map.keySet()){
            //向Builder中添加参数
            builder.addFormDataPart(key,map.get(key));
        }
        File file = new File(imgPath);
        //添加文件对象
        RequestBody body = RequestBody.create(MediaType.parse("image/*"),file);
        builder.addFormDataPart("photo[]",file.getName(),body);
        //创建请求对象
        final Request request = new Request.Builder()
                .url(path)
                .post(builder.build())
                .build();
        //执行Call对象
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("tag","===========>"+e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String str = response.body().string();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        loadData.load(str);
                    }
                });
            }
        });

    }
    public void postRequestSJ(String path,
                              HashMap<String,String>map,
                              String picName,
                              ArrayList<Pictures>pics){
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(5,TimeUnit.SECONDS)
                .readTimeout(5,TimeUnit.SECONDS).build();
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for(String key: map.keySet()){
            builder.addFormDataPart(key,map.get(key));
        }
        //添加所有的图片
        for(Pictures pic: pics){
            File file = new File(pic.getPath());
            RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
            builder.addFormDataPart(picName,file.getName(),body);
        }
        Request request = new Request.Builder()
                .url(path)
                .post(builder.build())
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("tag","===========>"+e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String str = response.body().string();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        loadData.load(str);
                    }
                });
            }
        });
    }

    public interface ILoadData{
        public void load(String json);
    }
}
