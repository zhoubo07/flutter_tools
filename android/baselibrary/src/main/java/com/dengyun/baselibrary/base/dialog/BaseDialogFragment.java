package com.dengyun.baselibrary.base.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.dengyun.baselibrary.R;
import com.dengyun.baselibrary.base.dialog.listener.DialogViewHolder;
import com.dengyun.baselibrary.utils.ScreenUtil;
import com.dengyun.baselibrary.utils.SizeUtils;

/**
 * @titile 自定义dialog的基类，可以自定义view继承此类，按照fragment的方式操作
 * 此类需要实现使用，适合复杂布局
 * <p>
 * 如果要在继承的类中直接设置参数，可以重写初始化参数的方法initParams，但不能去掉super；
 * 例如：@Override protected void initParams() {
 * setWidthMarginDp(52);
 * setOutCancel(false);
 * super.initParams(); }
 * @desc Created by seven on 2018/3/8.
 */
public abstract class BaseDialogFragment extends DialogFragment {
    private static final String MARGIN = "widthMarginDp";
    private static final String WIDTH = "width";
    private static final String HEIGHT = "height";
    private static final String DIM = "dim_amount";
    private static final String BOTTOM = "show_bottom";
    private static final String TOP = "show_top";
    private static final String CANCEL = "out_cancel";
    private static final String BACK_CANCEL = "back_cancel";
    private static final String ANIM = "anim_style";
    private static final String LAYOUT = "layout_id";

    private OnDialogDismissListener mDialogDismissListener;

    private int widthMarginDp;//左右边距
    private int width;//宽度
    private int height;//高度
    private float dimAmount = 0.5f;//灰度深浅  0：全透明  --  1：全不透明
    private boolean showBottom;//是否底部显示
    private boolean showTop;//是否顶部显示
    private boolean outCancel = true;//是否点击外部取消
    private boolean backCancel = true;//点击back键是否取消
    @StyleRes
    private int animStyle;
    @LayoutRes
    protected int layoutId;

    public boolean isShowing;//是否正在展示

    public abstract int intLayoutId();

    public abstract void convertView(DialogViewHolder holder, BaseDialogFragment dialog);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.BaseDialog);
        layoutId = intLayoutId();

        //恢复保存的数据
        if (savedInstanceState != null) {
            widthMarginDp = savedInstanceState.getInt(MARGIN);
            width = savedInstanceState.getInt(WIDTH);
            height = savedInstanceState.getInt(HEIGHT);
            dimAmount = savedInstanceState.getFloat(DIM);
            showBottom = savedInstanceState.getBoolean(BOTTOM);
            showTop = savedInstanceState.getBoolean(TOP);
            outCancel = savedInstanceState.getBoolean(CANCEL);
            backCancel = savedInstanceState.getBoolean(BACK_CANCEL);
            animStyle = savedInstanceState.getInt(ANIM);
            layoutId = savedInstanceState.getInt(LAYOUT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(layoutId, container, false);
        convertView(DialogViewHolder.create(view), this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initParams();
    }

    /**
     * 屏幕旋转等导致DialogFragment销毁后重建时保存数据
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(MARGIN, widthMarginDp);
        outState.putInt(WIDTH, width);
        outState.putInt(HEIGHT, height);
        outState.putFloat(DIM, dimAmount);
        outState.putBoolean(BOTTOM, showBottom);
        outState.putBoolean(TOP, showTop);
        outState.putBoolean(CANCEL, outCancel);
        outState.putInt(ANIM, animStyle);
        outState.putInt(LAYOUT, layoutId);
    }

    /**
     * 初始化参数（边距、宽高、显示方式、是否可取消）
     * 如果要在继承的类中直接设置参数，可以重写此方法，但不能去掉super；
     */
    protected void initParams() {
        Dialog dialog = getDialog();
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            //调节灰色背景透明度[0-1]，默认0.5f
            lp.dimAmount = dimAmount;
            //是否在底部显示
            if (showBottom) {
                lp.gravity = Gravity.BOTTOM;
                if (animStyle == 0) {
                    animStyle = R.style.base_window_bottom_animation;
                }
            }
            //是否在顶部显示
            if (showTop) {
                lp.gravity = Gravity.TOP;
                if (animStyle == 0) {
                    animStyle = R.style.base_window_top_animation;
                }
            }
            //设置dialog宽度
            if (width == 0) {
                lp.width = ScreenUtil.getScreenWidth() - 2 * SizeUtils.dp2px(widthMarginDp);
            } else if (width == -1) {
                lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            } else {
                lp.width = SizeUtils.dp2px(width);
            }

            //设置dialog高度
            if (height == 0) {
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            } else {
                lp.height = SizeUtils.dp2px(height);
            }

            //设置dialog进入、退出的动画
            if (animStyle != 0) window.setWindowAnimations(animStyle);
            window.setAttributes(lp);
        }

//        setCancelable(outCancel);
        dialog.setCanceledOnTouchOutside(outCancel);
        dialog.setCancelable(backCancel);
    }

    /**
     * 设置dialog的左右边距
     *
     * @param margin
     */
    public BaseDialogFragment setWidthMarginDp(int margin) {
        this.widthMarginDp = margin;
        return this;
    }

    /**
     * 设置dialog的宽度自适应
     */
    public BaseDialogFragment setWidthWrapContent() {
        this.width = -1;
        return this;
    }

    /**
     * 设置dialog的宽度为具体数值：默认为屏幕宽度
     *
     * @param width
     */
    public BaseDialogFragment setWidthDp(int width) {
        this.width = width;
        return this;
    }

    /**
     * 设置dialog的高度为具体数值：默认自适应
     *
     * @param height
     */
    public BaseDialogFragment setHeightDp(int height) {
        this.height = height;
        return this;
    }

    /**
     * 调节灰色背景透明度[0-1]，默认0.5f
     * 0：全透明  --  1：全不透明
     *
     * @param dimAmount
     */
    public BaseDialogFragment setDimAmount(float dimAmount) {
        this.dimAmount = dimAmount;
        return this;
    }

    /**
     * 设置是否从下方划出
     *
     * @param showBottom
     */
    public BaseDialogFragment setShowBottom(boolean showBottom) {
        this.showBottom = showBottom;
        return this;
    }

    /**
     * 设置是否从上方划出
     *
     * @param showTop
     */
    public BaseDialogFragment setShowTop(boolean showTop) {
        this.showTop = showTop;
        return this;
    }


    /**
     * 设置点击外面不消失true/false
     *
     * @param outCancel
     */
    public BaseDialogFragment setOutCancel(boolean outCancel) {
        this.outCancel = outCancel;
        return this;
    }

    /**
     * 设置点击back键是否消失
     *
     * @param backCancel
     * @return
     */
    public BaseDialogFragment setBackCancel(boolean backCancel) {
        this.backCancel = backCancel;
        return this;
    }


    /**
     * 设置动画
     *
     * @param animStyle
     */
    public BaseDialogFragment setAnimStyle(@StyleRes int animStyle) {
        this.animStyle = animStyle;
        return this;
    }

    public BaseDialogFragment setOnDismissListener(OnDialogDismissListener onDismissListener) {
        this.mDialogDismissListener = onDismissListener;
        return this;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        //support-26.0.0开始，Fragment以及FragmentManager提供了isStateSaved(), 可以判断宿主Activity是否已经执行过onSaveInstanceState().
        if (isShowing) return;
        if (!manager.isStateSaved() && !isAdded()) {
            try {
                super.show(manager, tag);
                isShowing = true;
            } catch (Exception ignore) {
                //  容错处理,不做操作
            }
        }
    }

    public BaseDialogFragment show(FragmentManager manager) {
        show(manager, String.valueOf(System.currentTimeMillis()));
        return this;
    }

    @Override
    public void dismiss() {
        dismissAllowingStateLoss();
    }

    @Override
    public void dismissAllowingStateLoss() {
        if (null != getFragmentManager()) super.dismissAllowingStateLoss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        isShowing = false;
        super.onDismiss(dialog);
        if (null != mDialogDismissListener) {
            mDialogDismissListener.onDismissListener();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}