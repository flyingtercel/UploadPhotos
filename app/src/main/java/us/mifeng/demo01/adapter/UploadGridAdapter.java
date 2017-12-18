package us.mifeng.demo01.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import us.mifeng.demo01.R;
import us.mifeng.demo01.bean.Pictures;

/**
 * Created by 黑夜之火 on 2017/10/31.
 */

public class UploadGridAdapter extends BaseAdapter {
    private ArrayList<Pictures>pictures ;
    private Context context;

    public UploadGridAdapter(ArrayList<Pictures> pictures, Context context) {
        this.pictures = pictures;
        this.context = context;
    }

    @Override
    public int getCount() {
        return pictures.size()+1;
    }

    @Override
    public Pictures getItem(int position) {
        return pictures.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.upload_grid_item,null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }
        if (position<pictures.size()){
            Pictures pics = getItem(position);
            Glide.with(context).load("file://"+pics.getPath()).into(vh.imageView);
        }else{
            vh.imageView.setImageResource(R.mipmap.gridview_addpic);
        }
        return convertView;
    }
    class ViewHolder{
        ImageView imageView;
        public ViewHolder(View view){
            imageView = (ImageView) view.findViewById(R.id.imageview);
        }
    }
}
