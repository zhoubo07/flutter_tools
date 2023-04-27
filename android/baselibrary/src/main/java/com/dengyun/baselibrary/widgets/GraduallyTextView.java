package com.dengyun.baselibrary.widgets;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatEditText;

import com.orhanobut.logger.Logger;

/**
 * @titile  等待加载中的闪烁文字
 * @desc Created by seven on 2018/3/24.
 */

public class GraduallyTextView extends AppCompatEditText {
    private CharSequence text;//自定义的文本
    private int startY = 0;
    private float progress;//读取进度条
    private boolean isLoading;//判断是否正在读取
    private Paint mPaint;
    private int textLength;//设置文本长度
    private boolean isStop = true;
    private float scaleX;//设置缩放比例
    private int duration = 2000;
    private float sigleDuration;

    private ValueAnimator valueAnimator;//设置属性平移、缩放动画


    public GraduallyTextView(Context context) {
        super(context);
        init();
    }


    public GraduallyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    public GraduallyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    public void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        setBackground(null);
        setCursorVisible(false);
        setFocusable(false);
        setEnabled(false);
        setFocusableInTouchMode(false);
        //设置平移动画
        valueAnimator = ValueAnimator.ofFloat(0, 100).setDuration(duration);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.setRepeatCount(Animation.INFINITE);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.addUpdateListener(
                new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        //设置监听，当动画更新的时候触发重绘
                        progress = (Float) animation.getAnimatedValue();
                        GraduallyTextView.this.invalidate();
                    }
                });
    }

    //设置开始读取的方法
    public void startLoading() {
        post(new Runnable() {
            @Override
            public void run() {
                if (!isStop) {
                    return;
                }
                textLength = getText().length();
                if (TextUtils.isEmpty(getText().toString())) {
                    return;
                }
                isLoading = true;
                isStop = false;
                text = getText();
                scaleX = getTextScaleX() * 10;
                startY = getBaseline();
                mPaint.setColor(getCurrentTextColor());
                mPaint.setTextSize(getTextSize());
                setMinWidth(getWidth());
                setText("");
                setHint("");
                valueAnimator.start();
                sigleDuration = 100f / textLength;
            }
        });
    }

    //停止loading的方法
    public void stopLoading() {
        isLoading = false;
        valueAnimator.end();
        valueAnimator.cancel();
        isStop = true;
        setText(text);
    }

    //设置时长
    public void setDuration(int duration) {
        this.duration = duration;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (!isLoading) {
            return;
        }
        if (visibility == View.VISIBLE) {
            valueAnimator.resume();
        }
        else {
            valueAnimator.pause();
        }
    }

    //重写ondraw方法，如果还在loading，而且进度还小于1,则让它和透明度联动
    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isStop) {
            mPaint.setAlpha(255);
            if (progress / sigleDuration >= 1) {
                canvas.drawText(String.valueOf(text), 0,
                        (int) (progress / sigleDuration), scaleX, startY,
                        mPaint);
            }
            mPaint.setAlpha(
                    (int) (255 * ((progress % sigleDuration) / sigleDuration)));
            int lastOne = (int) (progress / sigleDuration);
            if (lastOne < textLength) {
                canvas.drawText(String.valueOf(text.charAt(lastOne)), 0, 1,
                        scaleX + getPaint().measureText(
                                text.subSequence(0, lastOne).toString()),
                        startY, mPaint);
            }
        }
    }
}
