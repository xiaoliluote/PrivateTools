package com.killaxiao.library.bean;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import com.killaxiao.library.R;

public class WelcomeBean implements Parcelable{

    private String cls;
    private int img_bg;
    private int mCountdownTime;
    private int mRingColor;
    private int mProgessTextColor;
    private int mRingProgessTextSize;

    public final static int DEFAULT_IMG_BG= R.drawable.timg2;
    public final static int DEFAULT_COUNTDOWNTIME=5;
    public final static int DEFAULT_RINGCOLOR= Color.parseColor("#ffffff");
    public final static int DEFAULT_TEXTCOLOR=Color.parseColor("#ffffff");
    public final static int DEFAULT_TEXTSIZE=R.dimen.px40;

    public String getCls() {
        return cls;
    }

    public void setCls(String cls) {
        this.cls = cls;
    }

    public int getImg_bg() {
        return img_bg;
    }

    public void setImg_bg(int img_bg) {
        this.img_bg = img_bg;
    }

    public int getmCountdownTime() {
        return mCountdownTime;
    }

    public void setmCountdownTime(int mCountdownTime) {
        this.mCountdownTime = mCountdownTime;
    }

    public int getmRingColor() {
        return mRingColor;
    }

    public void setmRingColor(int mRingColor) {
        this.mRingColor = mRingColor;
    }

    public int getmProgessTextColor() {
        return mProgessTextColor;
    }

    public void setmProgessTextColor(int mProgessTextColor) {
        this.mProgessTextColor = mProgessTextColor;
    }

    public int getmRingProgessTextSize() {
        return mRingProgessTextSize;
    }

    public void setmRingProgessTextSize(int mRingProgessTextSize) {
        this.mRingProgessTextSize = mRingProgessTextSize;
    }

    public WelcomeBean(String cls,int img_bg,int mCountdownTime,int mRingColor,int mProgessTextColor,int mRingProgessTextSize){
        this.cls = cls;
        this.img_bg = img_bg;
        this.mCountdownTime = mCountdownTime;
        this.mRingColor = mRingColor;
        this.mProgessTextColor = mProgessTextColor;
        this.mRingProgessTextSize = mRingProgessTextSize;
    }

    protected WelcomeBean(Parcel in) {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cls);
        dest.writeInt(img_bg);
        dest.writeInt(mCountdownTime);
        dest.writeInt(mRingColor);
        dest.writeInt(mProgessTextColor);
        dest.writeInt(mRingProgessTextSize);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WelcomeBean> CREATOR = new Creator<WelcomeBean>() {
        @Override
        public WelcomeBean createFromParcel(Parcel in) {
            return new WelcomeBean(in.readString(),in.readInt(),in.readInt(),in.readInt(),in.readInt(),in.readInt());
        }

        @Override
        public WelcomeBean[] newArray(int size) {
            return new WelcomeBean[size];
        }
    };
}
