package com.dengyun.baselibrary.base.dialog;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import com.dengyun.baselibrary.base.dialog.listener.DialogViewHolder;

/**
 * BaseDialog.init()
     .setLayoutId(R.layout.dialog)     //设置dialog布局文件
     .setConvertListener(new ViewConvertListener() {     //进行相关View操作的回调
        @Override
        public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {}
        })
     .setDimAmount(0.3f)     //调节灰色背景透明度[0-1]，默认0.5f
     .setShowBottom(true)     //是否在底部显示dialog，默认flase
     .setMargin()     //dialog左右两边到屏幕边缘的距离（单位：dp），默认0dp
     .setWidth()     //dialog宽度（单位：dp），默认为屏幕宽度，-1代表WRAP_CONTENT
     .setHeight()     //dialog高度（单位：dp），默认为WRAP_CONTENT
     .setOutCancel(false)     //点击dialog外是否可取消，默认true
     .setAnimStyle(R.style.EnterExitAnimation)     //设置dialog进入、退出的动画style(底部显示的dialog有默认动画)
     .show(getSupportFragmentManager());     //显示dialog
  * */

/**
 * @titile  自定义dialog基类，可以添加布局，在convertview中可以设置view以及view中的监听
 *          此类可以直接链式调用创建（需要指定布局），简单易用适合简单布局
 * @desc Created by seven on 2018/3/8.
 */

public class BaseDialog extends BaseDialogFragment {
    private ViewConvertListener convertListener;

    public static BaseDialog init() {
        return new BaseDialog();
    }

    @Override
    public int intLayoutId() {
        return layoutId;
    }

    @Override
    public void convertView(DialogViewHolder holder, BaseDialogFragment dialog) {
        if (convertListener != null) {
            convertListener.convertView(holder, dialog);
        }
    }

    public BaseDialog setLayoutId(@LayoutRes int layoutId) {
        this.layoutId = layoutId;
        return this;
    }

    public BaseDialog setConvertListener(ViewConvertListener convertListener) {
        this.convertListener = convertListener;
        return this;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            convertListener = savedInstanceState.getParcelable("listener");
        }
    }

    /**
     * 保存接口
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("listener", convertListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        convertListener = null;
    }
}
