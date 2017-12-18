package us.mifeng.demo01;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.HashMap;

import us.mifeng.demo01.adapter.UploadGridAdapter;
import us.mifeng.demo01.bean.Pictures;
import us.mifeng.demo01.utils.FileUtils;
import us.mifeng.demo01.utils.OkHttpUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OkHttpUtils.ILoadData {
    private ArrayList<Pictures> pictures = new ArrayList<>();
    private Button openCarame;
    private Button openPhotos;
    private Button upload;
    private String filePath;
    private OkHttpUtils okHttpUtils;
    private GridView gridView;
    private UploadGridAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        gridView = (GridView) findViewById(R.id.gridView);
        adapter = new UploadGridAdapter(pictures, this);
        gridView.setAdapter(adapter);
        openCarame = (Button) findViewById(R.id.openCarame);
        openPhotos = (Button) findViewById(R.id.openPhotos);
        upload = (Button) findViewById(R.id.upload);
        Button openPicture = (Button) findViewById(R.id.openPictures);
        openCarame.setOnClickListener(this);
        openPhotos.setOnClickListener(this);
        upload.setOnClickListener(this);
        openPicture.setOnClickListener(this);
        okHttpUtils = new OkHttpUtils(this, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.openCarame:
                //打开相机
                openCarame();
                break;
            case R.id.openPhotos:
                openPhotos();
                break;
            case R.id.openPictures:
                //当选择openPictures时讲所有的照片都显示出来
                Intent intent = new Intent(this, SelectingActivity.class);
                startActivityForResult(intent, 0x113);
                break;
            case R.id.upload:
                upload();
                break;
        }
    }

    private void upload() {
        HashMap<String, String> map = new HashMap<>();
        map.put("title", "可口可乐");
        map.put("description", "拿到了可口可乐啊是");
        map.put("price", "4");
        map.put("mobile", "1");
        map.put("token", "185E18E52CA34AAAA0673D044B655B65");
        String path = "http://192.168.190.188/Goods/app/item/issue.json";
        if (pictures.size() > 0) {
            okHttpUtils.postRequestSJ(path, map, "photo[]", pictures);
        }
    }

    private void openPhotos() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, 0x112);
    }

    private void openCarame() {
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0x111);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == RESULT_OK) {
            if (requestCode == 0x111) {
                resultCarame(data);
            } else if (requestCode == 0x112) {
                resultPhotos(data);
            } else if (requestCode == 0x113) {
                ArrayList<Pictures> pics = data.getParcelableArrayListExtra("pics");
                pictures.addAll(pics);
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void resultPhotos(Intent data) {
        //获取系统的内容提供者对象
        ContentResolver re = getContentResolver();
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = re.query(data.getData(), projection, null, null, null, null);
        cursor.moveToNext();
        filePath = cursor.getString(cursor.getColumnIndex(projection[0]));
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        pictures.add(new Pictures(filePath, false));
        adapter.notifyDataSetChanged();
        Log.i("tag", "===================>>>>" + filePath);
    }

    private void resultCarame(Intent data) {
        Bitmap bitmap = null;
        Uri uri = data.getData();
        if (uri != null) {
            bitmap = BitmapFactory.decodeFile(uri.getPath());
            filePath = uri.getPath();
        }
        if (bitmap == null) {
            bitmap = (Bitmap) data.getExtras().get("data");
            //创建工具类，向文件中写入图片的工具
            FileUtils utils = new FileUtils(this);
            filePath = utils.writeFile(bitmap);
        }
        pictures.add(new Pictures(filePath, false));
        adapter.notifyDataSetChanged();
        Log.i("tag", "===================>>>>" + filePath);
    }

    @Override
    public void load(String json) {
        Log.i("tag", ">>>>>>>>>>>>>>>" + json);
    }
}
