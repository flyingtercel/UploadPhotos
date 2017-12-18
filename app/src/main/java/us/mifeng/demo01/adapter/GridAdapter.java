package us.mifeng.demo01.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import us.mifeng.demo01.R;
import us.mifeng.demo01.bean.Pictures;

/**
 * Created by 黑夜之火 on 2017/10/31.
 */

public class GridAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Pictures>pictures = new ArrayList<>();
    //创建一个集合，用来保存我们选中的所有图片对象
    private ArrayList<Pictures>selected = new ArrayList<>();

    public GridAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return pictures.size();
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
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.grid_item,null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }
        Pictures pictures = getItem(position);
        Glide.with(context).load(
                "file://"+pictures.getPath()).into(vh.imageView);
        if (pictures.isSelected()){
            vh.selectImg.setImageResource(R.mipmap.icon_like_red_32);
        }else{
            vh.selectImg.setImageResource(R.mipmap.icon_like_white_34);
        }
        return convertView;
    }
    class ViewHolder{
        ImageView imageView,selectImg;
        public ViewHolder(View view){
            imageView = (ImageView) view.findViewById(R.id.imageView);
            selectImg = (ImageView) view.findViewById(R.id.select_Imageview);
        }
    }
    //写一个方法，传递图片数组，并跟新适配器
    public void initPictures(ArrayList<Pictures>pictures){
        if (pictures!=null && pictures.size()>0){
            this.pictures.clear();
            this.pictures.addAll(pictures);
            notifyDataSetChanged();
        }
    }
    //设置GridView的条目上的选中和没有选中
    public void initSelecte(int postion){
        Pictures pic = this.pictures.get(postion);
        if (pic.isSelected()){
            selected.remove(pic);
            pic.setSelected(false);
        }else{
           if (selected.size()<5){
               pic.setSelected(true);
               selected.add(pic);
           }else{
               Toast.makeText(context,"你最多能选择5张图片",Toast.LENGTH_SHORT).show();
           }
        }
        notifyDataSetChanged();
    }
    //当用户点击完成时，获取所有的图片信息
    public ArrayList<Pictures> getSelectedPic(){
        return selected;
    }
}
