package us.mifeng.demo01.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.SystemClock;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by 黑夜之火 on 2017/10/30.
 */

public class FileUtils {
    private Context context;

    public FileUtils(Context context) {
        this.context = context;
    }

    //判断SDCard是否存在
    public boolean isMounted(){
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            return true;
        }
        return false;
    }
    public File getRootFile(){
        File root;
        if (isMounted()){
            root = Environment.getExternalStorageDirectory();
        }else{
             root = context.getFilesDir();
        }
        File file = new File(root,"/saveImage");
        if (!file.exists()){
            file.mkdirs();
        }
        return file;
    }
    //生成文件名字：
    public String getFileName(){
        return "img"+SystemClock.currentThreadTimeMillis()+".png";
    }
    //向文件中写入数据
    public String writeFile(Bitmap bitmap){
        String name = getFileName();
        File root = getRootFile();
        File file = new File(root,name);
        try {
            FileOutputStream bos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,bos);
            bos.flush();
            bos.close();
            return file.getPath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
