package com.dengyun.baselibrary.utils.phoneapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.dengyun.baselibrary.utils.ListUtils;
import com.dengyun.baselibrary.utils.Utils;
import com.dengyun.baselibrary.utils.ViewUtil;

import java.lang.reflect.Field;
import java.util.List;

/**
 * showSoftInput                   : 动态显示软键盘
 * hideSoftInput                   : 动态隐藏软键盘
 * toggleSoftInput                 : 切换键盘显示与否状态
 * isSoftInputVisible              : 判断软键盘是否可见
 * setSoftInputChangeListener      : 设置软键盘显示隐藏的监听器
 * fixSoftInputLeaks               : 修复软键盘内存泄漏
 *
 * @titile 键盘 相关
 * @desc Created by seven on 2018/2/24.
 */
public final class KeyboardUtils {


    private KeyboardUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Show the soft input.
     *
     * @param activity The activity.
     */
    public static void showSoftInput(final Activity activity) {
        InputMethodManager imm =
                (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        View view = activity.getCurrentFocus();
        if (view == null) view = new View(activity);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    /**
     * Show the soft input.
     *
     * @param view The view.
     */
    public static void showSoftInput(final View view) {
        InputMethodManager imm =
                (InputMethodManager) Utils.getApp().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    /**
     * Hide the soft input.
     *
     * @param activity The activity.
     */
    public static void hideSoftInput(final Activity activity) {
        InputMethodManager imm =
                (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        View view = activity.getCurrentFocus();
        if (view == null) view = new View(activity);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Hide the soft input.
     *
     * @param view The view.
     */
    public static void hideSoftInput(final View view) {
        InputMethodManager imm =
                (InputMethodManager) Utils.getApp().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Toggle the soft input display or not.
     */
    public static void toggleSoftInput() {
        InputMethodManager imm =
                (InputMethodManager) Utils.getApp().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    /**
     * Return whether soft input is visible.
     * <p>The minimum height is 200</p>
     *
     * @param activity The activity.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isSoftInputVisible(final Activity activity) {
        return isSoftInputVisible(activity, 200);
    }

    /**
     * Return whether soft input is visible.
     *
     * @param activity             The activity.
     * @param minHeightOfSoftInput The minimum height of soft input.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isSoftInputVisible(final Activity activity,
                                             final int minHeightOfSoftInput) {
        return getContentViewInvisibleHeight(activity) >= minHeightOfSoftInput;
    }

    private static int getContentViewInvisibleHeight(final Activity activity) {
        final View contentView = activity.findViewById(android.R.id.content);
        Rect r = new Rect();
        contentView.getWindowVisibleDisplayFrame(r);
        return contentView.getRootView().getHeight() - r.height();
    }

    /**
     * set SoftBoard Change Listener with show and hide
     *
     * @param activity                     the activity
     * @param onSoftKeyBoardChangeListener the listener of soft show and hide
     */
    public static void setSoftInputChangeListener(Activity activity, SoftKeyboardListener.OnSoftKeyBoardChangeListener onSoftKeyBoardChangeListener) {
        SoftKeyboardListener softKeyBoardListener = new SoftKeyboardListener(activity);
        softKeyBoardListener.setOnSoftKeyBoardChangeListener(onSoftKeyBoardChangeListener);
    }

    /**
     * Fix the leaks of soft input.
     * <p>Call the function in {@link Activity# onDestroy()}.</p>
     *
     * @param context The context.
     */
    public static void fixSoftInputLeaks(final Context context) {
        if (context == null) return;
        InputMethodManager imm =
                (InputMethodManager) Utils.getApp().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) return;
        String[] strArr = new String[]{"mCurRootView", "mServedView", "mNextServedView"};
        for (int i = 0; i < 3; i++) {
            try {
                Field declaredField = imm.getClass().getDeclaredField(strArr[i]);
                if (declaredField == null) continue;
                if (!declaredField.isAccessible()) {
                    declaredField.setAccessible(true);
                }
                Object obj = declaredField.get(imm);
                if (obj == null || !(obj instanceof View)) continue;
                View view = (View) obj;
                if (view.getContext() == context) {
                    declaredField.set(imm, null);
                } else {
                    return;
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    /**
     * 触摸其他view的时候隐藏软键盘（默认排除EditText，其他类型view需要传入）
     * @param activity   要控制的activity
     * @param ev         分发的事件event
     * @param excludeViews 需要单独排除的view集合（不用加editText）
     */
    public static void hideInputWhenTouchOtherView(Activity activity, MotionEvent ev, @Nullable List<View> excludeViews) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 当手势焦点落在排除在外的View上时，不隐藏键盘
            if (!ListUtils.isEmpty(excludeViews)) {
                for (View excludeView : excludeViews) {
                   if (ViewUtil.isTouchView(excludeView,ev)){
                       return;
                   }
                }
            }

            View view = activity.getCurrentFocus();
            // 判断--如果当前获取焦点的View是EditText，则不处理
            if (view != null && (view instanceof EditText) && ViewUtil.isTouchView(view,ev)){

            }else {
                hideSoftInput(activity);
            }

        }
    }

}
