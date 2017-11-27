package com.water.helper.bean;

import android.os.Parcel;
import android.os.Parcelable;

@SuppressWarnings("ALL")
public class GoodsModel implements Parcelable {

    private int id;
    private int num;
    private int num_wu;
    private String title;
    private String dj;
    private boolean focus;

    public GoodsModel() {
        super();
    }

    protected GoodsModel(Parcel in) {
        id = in.readInt();
        num = in.readInt();
        num_wu = in.readInt();
        title = in.readString();
        dj = in.readString();
        focus = in.readByte() != 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getNum_wu() {
        return num_wu;
    }

    public void setNum_wu(int num_wu) {
        this.num_wu = num_wu;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDj() {
        return dj;
    }

    public void setDj(String dj) {
        this.dj = dj;
    }

    public boolean isFocus() {
        return focus;
    }

    public void setFocus(boolean focus) {
        this.focus = focus;
    }

    public static final Creator<GoodsModel> CREATOR = new Creator<GoodsModel>() {
        @Override
        public GoodsModel createFromParcel(Parcel in) {
            return new GoodsModel(in);
        }

        @Override
        public GoodsModel[] newArray(int size) {
            return new GoodsModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(num);
        dest.writeInt(num_wu);
        dest.writeString(title);
        dest.writeString(dj);
        dest.writeByte((byte) (focus ? 1 : 0));
    }
}
