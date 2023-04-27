package com.dengyun.baselibrary.widgets.imageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import com.dengyun.baselibrary.R;


/**
 * 可以设置宽高比的ImageView
 * Created by seven on 2018/04/20.
 */

public class RatioImageView extends AppCompatImageView {
    //高/宽 比，由我们自己设定
    private float ratio;
    public RatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatioImageView);
        ratio = typedArray.getFloat(R.styleable.RatioImageView_image_ratio, 0.0f);
        typedArray.recycle();
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(ratio==0) {
            super.onMeasure(widthMeasureSpec,heightMeasureSpec);
            return;
        }
        //获取宽度的模式和尺寸
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        //获取高度的模式和尺寸
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if(widthMode == MeasureSpec.EXACTLY && heightMode!=MeasureSpec.EXACTLY){
            //宽确定，高不确定,按照宽度和比例算出高度
            heightSize = (int) (widthSize*ratio+0.5f);//根据宽度和比例计算高度
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        }else if(widthMode!=MeasureSpec.EXACTLY && heightMode==MeasureSpec.EXACTLY){
            //高确定，宽不确定，按照高度和比例算出宽度
            widthSize = (int) (heightSize/ratio+0.5f);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize,MeasureSpec.EXACTLY);
        }else{
            super.onMeasure(widthMeasureSpec,heightMeasureSpec);
            return;
//            throw new RuntimeException("无法设定宽高比");
        }
        //必须调用下面的两个方法之一完成onMeasure方法的重写，否则会报错
//        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec,heightMeasureSpec);
    }
    /**
     * 设置宽高比
     * @param ratio
     */
    public void setRatio(float ratio){
        this.ratio = ratio;
    }
}
