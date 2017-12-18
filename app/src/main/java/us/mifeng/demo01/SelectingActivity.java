package us.mifeng.demo01;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import java.util.ArrayList;

import us.mifeng.demo01.adapter.GridAdapter;
import us.mifeng.demo01.bean.Pictures;

public class SelectingActivity extends AppCompatActivity {
    private Handler handler = new Handler();
    private GridAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecting);
        initView();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<Pictures> pictures = getPictures();
                        adapter.initPictures(pictures);
                    }
                });
                Looper.loop();
            }
        }).start();
    }

    private void initView() {
        GridView gridView = (GridView) findViewById(R.id.gridView);
        Button btn = (Button) findViewById(R.id.selectBtn);
        adapter = new GridAdapter(this);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.initSelecte(position);
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Pictures> pics = adapter.getSelectedPic();
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra("pics",pics);
                setResult(RESULT_OK,intent);
                SelectingActivity.this.finish();
            }
        });
    }
    //封装一个方法，用来获取本地当中的所有图片，
    public ArrayList<Pictures>getPictures(){
        ArrayList<Pictures>pictures = new ArrayList<>();
        //查询，并取出所有的照片
        String[]selection = {MediaStore.Images.Media.DATA,MediaStore.Images.Media._ID};
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                selection, null, null, null);
        if (cursor!=null){
            while (cursor.moveToNext()){
                String path = cursor.getString(
                        cursor.getColumnIndex(selection[0]));
                Pictures picture = new Pictures(path,false);
                pictures.add(picture);
            }
        }
        return pictures;
    }
}
