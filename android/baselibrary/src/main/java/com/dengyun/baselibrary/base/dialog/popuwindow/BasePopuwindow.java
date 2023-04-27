package com.dengyun.baselibrary.base.dialog.popuwindow;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

/**
 * @Title 封装的popuwindow类
 * @Desc: 封装的popuwindow工具类
 * @Author: zhoubo
 * @CreateDate: 2018/12/7 10:46 AM
 */
public class BasePopuwindow extends PopupWindow {
    private Context mContext;
    private Builder builder;
    private View mContentView;

    public static Builder newBuilder(Context context) {
        return new Builder(context);
    }

    private BasePopuwindow(View contentView, int width, int height, boolean focusable, Builder builder) {
        super(contentView, width, height, focusable);
        this.builder = builder;
        this.mContext = builder.mContext;
        mContentView = builder.mConventView;
        initPopuwindow();
    }

    private void initPopuwindow() {

        //设置点击外部是否可以取消，必须和下面这个方法配合才有效
        setOutsideTouchable(builder.outsideTouchable);
        Drawable drawable ;
        if(builder.backgroundColor==0&&builder.backgroundRes==0){
            drawable = new ColorDrawable(Color.TRANSPARENT);
        }else if(builder.backgroundColor==0){
            Resources resources = mContext.getResources();
            drawable = resources.getDrawable(builder.backgroundRes);
        }else {
            Resources resources = mContext.getResources();
            drawable = new ColorDrawable(resources.getColor(builder.backgroundColor));
        }
        //设置一个空背景,设置了这个背景之后，设置点击外部取消才有效
        setBackgroundDrawable(drawable);
        if(builder.animStyle!=0){
            setAnimationStyle(builder.animStyle); //设置pop显示的动画效果
        }
    }


    /**
     * 根据id获取view
     *
     * @param viewId
     * @return
     */
    public View getItemView(int viewId) {
        if (mContentView != null) {
            return mContentView.findViewById(viewId);
        }
        return null;
    }

    /**
     * 根据id设置pop内部的控件的点击事件的监听
     *
     * @param viewId
     * @param listener
     */
    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getItemView(viewId);
        view.setOnClickListener(listener);
    }


    public static class Builder{
        private int width;//pop宽度
        private int height;//pop高度
        private int animStyle; //动画效果
        private int contentViewId; //pop的布局文件
        private boolean outsideTouchable;//是否点击外部取消
        private int backgroundColor;//背景色
        private int backgroundRes;//背景图片
        private Context mContext;
        private View mConventView;
        public Builder(Context context){
            mContext = context;
        }

        public Builder setWidth(int width) {
            this.width = width;
            return this;
        }

        public Builder setHeight(int height) {
            this.height = height;
            return this;
        }

        public Builder setAnimStyle(int animStyle) {
            this.animStyle = animStyle;
            return this;
        }

        public Builder setContentViewId(int contentViewId) {
            this.contentViewId = contentViewId;
            return this;
        }

        public Builder setContentView(View contentView){
            this.mConventView = contentView;
            return this;
        }

        public Builder setOutsideTouchable(boolean outsideTouchable) {
            this.outsideTouchable = outsideTouchable;
            return this;
        }

        public Builder setBackgroundColor(int backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public Builder setBackgroundRes(int backgroundRes) {
            this.backgroundRes = backgroundRes;
            return this;
        }

        public BasePopuwindow build(){
            if(null==mConventView && contentViewId !=0){
                mConventView = LayoutInflater.from(mContext).inflate(contentViewId, null);
            }
            return new BasePopuwindow(mConventView,
                    width==0?ViewGroup.LayoutParams.WRAP_CONTENT:width,
                    height==0?ViewGroup.LayoutParams.WRAP_CONTENT:height,
                    true,this);//倒数第二个参数fouceable，按back键首先让pop消失
        }

    }
}
