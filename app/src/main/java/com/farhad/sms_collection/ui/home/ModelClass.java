package com.farhad.sms_collection.ui.home;

public class ModelClass {

    String mTime;
    String mData;

    public ModelClass() {}

    public ModelClass(String mTime, String mData) {
        this.mTime = mTime;
        this.mData = mData;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public String getmData() {
        return mData;
    }

    public void setmData(String mData) {
        this.mData = mData;
    }
}
