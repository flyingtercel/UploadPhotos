package us.mifeng.demo01.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 黑夜之火 on 2017/10/31.
 */

public class Pictures implements Parcelable{
    private String path;
    private boolean selected;

    public Pictures(String path, boolean selected) {
        this.path = path;
        this.selected = selected;
    }

    protected Pictures(Parcel in) {
        path = in.readString();
        selected = in.readByte() != 0;
    }

    public static final Creator<Pictures> CREATOR = new Creator<Pictures>() {
        @Override
        public Pictures createFromParcel(Parcel in) {
            return new Pictures(in);
        }

        @Override
        public Pictures[] newArray(int size) {
            return new Pictures[size];
        }
    };

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(path);
        dest.writeByte((byte) (selected ? 1 : 0));
    }
}
