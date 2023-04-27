package com.dengyun.baselibrary.base.dialog;

import android.os.Parcel;
import android.os.Parcelable;

import com.dengyun.baselibrary.base.dialog.listener.DialogViewHolder;

/**
 * @titile  BaseDialog中的设置，设置此dialog中view的view操作
 * @desc Created by seven on 2018/3/8.
 */

public abstract class ViewConvertListener implements Parcelable {

    protected abstract void convertView(DialogViewHolder holder, BaseDialogFragment dialog);

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public ViewConvertListener() {
    }

    protected ViewConvertListener(Parcel in) {
    }

    public static final Creator<ViewConvertListener> CREATOR = new Creator<ViewConvertListener>() {
        @Override
        public ViewConvertListener createFromParcel(Parcel source) {
            return new ViewConvertListener(source){
                @Override
                protected void convertView(DialogViewHolder holder, BaseDialogFragment dialog) {

                }
            };
        }

        @Override
        public ViewConvertListener[] newArray(int size) {
            return new ViewConvertListener[size];
        }
    };
}
