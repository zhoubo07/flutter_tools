package com.dengyun.baselibrary.utils;

import android.content.res.Resources;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.tabs.TabLayout;

import java.lang.reflect.Field;

/**
 * @titile
 * @desc Created by seven on 2018/5/26.
 */

/**
 * setViewWidth  动态设置view的宽度
 * setViewHeight 动态设置view的高度
 * setMargins    动态设置view的margin
 * setTextOrGone 设置TextView的有值显示无值隐藏
 * isTouchView   判断手势触摸焦点是否在指定view上
 */

public class ViewUtil {

    /**
     * 动态设置view宽度
     *
     * @param v       要设置的view
     * @param widthPx 要设置的宽度像素
     */
    public static void setViewWidth(View v, int widthPx) {
        ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
        if (null!=layoutParams){
            layoutParams.width = widthPx;
            v.setLayoutParams(layoutParams);
        }
    }

    /**
     * 动态设置view高度
     *
     * @param v        要设置的view
     * @param heightPx 要设置的高度像素
     */
    public static void setViewHeight(View v, int heightPx) {
        ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
        if (null!=layoutParams){
            layoutParams.height = heightPx;
            v.setLayoutParams(layoutParams);
        }
    }

    /**
     * 动态设置控件的margin
     */
    public static void setMargins(View v, int left, int top, int right, int bottom) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            v.requestLayout();
        }
    }

    /**
     * 设置TextView判断指定字符串控制显示隐藏
     */
    public static void setTextOrGone(@NonNull TextView textView, CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
            textView.setText(text);
        }
    }

    /**
     * 判断手势触摸焦点是否在指定view上
     */
    public static final boolean isTouchView(View view, MotionEvent event) {
        if (view == null || event == null) {
            return false;
        }
        int[] leftTop = {0, 0};
        view.getLocationInWindow(leftTop);
        int left = leftTop[0];
        int top = leftTop[1];
        int bottom = top + view.getHeight();
        int right = left + view.getWidth();
        if (event.getRawX() > left && event.getRawX() < right
                && event.getRawY() > top && event.getRawY() < bottom) {
            return true;
        }
        return false;
    }

    /**
     * 已过时
     * 现在28的版本已经不用写这些东西了，google已经做了优化，添加了属性设置app:tabIndicatorFullWidth 为false时自适应文字的宽度，
     * 还有反射里面的mTabStrip也改成了slidingTabIndicator，如果是更高的版本再使用mTabStrip会报空指针
     * <p>
     * 设置tab指示器的宽度
     *
     * @param mTabLayout
     * @param marginleftDip  距离左端的距离
     * @param marginrightDip 距离端的距离
     */
    @Deprecated
    public static void setTabMargin(final TabLayout mTabLayout, final int marginleftDip, final int marginrightDip) {
        mTabLayout.post(new Runnable() {
            @Override
            public void run() {
                setIndicator(mTabLayout, marginleftDip, marginrightDip);
            }
        });
    }

    //动态设置tabIndicator的margin
    @Deprecated
    private static void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());
        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }

    }
}
