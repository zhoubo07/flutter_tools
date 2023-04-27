package com.dengyun.baselibrary.widgets.toolbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dengyun.baselibrary.R;
import com.dengyun.baselibrary.utils.SizeUtils;

/**
 * @titile 右侧有一个按钮的toolbar（文字或者图片）
 * @desc Created by seven on 2018/3/21.
 */

public class BaseOneRightToolBar extends BaseToolBar {
    private OnClickListener rightClickListener;
    private ImageView iv_right;
    private TextView tv_right;
    private int right_text_size;
    private int right_text_color;
    private boolean is_right_bold;

    public BaseOneRightToolBar(Context context) {
        this(context, null);
    }

    public BaseOneRightToolBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseOneRightToolBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BaseOneRightToolBar);
        int right_icon = ta.getResourceId(R.styleable.BaseOneRightToolBar_right_icon, 0);
        String right_text = ta.getString(R.styleable.BaseOneRightToolBar_right_text);
        int right_text_sizePx = ta.getDimensionPixelSize(R.styleable.BaseOneRightToolBar_right_text_size, SizeUtils.sp2px(15));
        is_right_bold = ta.getBoolean(R.styleable.BaseOneRightToolBar_is_right_bold, false);
        right_text_size = SizeUtils.px2sp(right_text_sizePx);
        right_text_color = ta.getColor(R.styleable.BaseOneRightToolBar_right_text_color, getResources().getColor(R.color.gray333));
        ta.recycle();

        if (right_icon != 0) {
            /*设置了右侧按钮图标*/
            addRightIcon(right_icon);
        } else if (null != right_text) {
            /*设置了右侧文字*/
            addRightText(right_text);
        }

        /* toolbar.inflateMenu(R.menu.activity_tool_bar);//设置右上角的填充菜单
        //点击事件
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()){
                    case R.id.action_item1:
                        //........
                    case R.id.action_item2:
                        //........

                }

                return true;
            }
        });*/
    }

    /**
     * 添加右侧的图片按钮
     *
     * @param right_icon 右侧图片资源id
     */
    private void addRightIcon(int right_icon) {
        if (null != getTitleTextView()) getTitleTextView().setMaxEms(11);
        iv_right = new ImageView(getContext());
        LayoutParams layoutParamsIvRight =
                new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        iv_right.setLayoutParams(layoutParamsIvRight);
        iv_right.setImageResource(right_icon);
        iv_right.setPadding(0, 0, SizeUtils.dp2px(10), 0);
        iv_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != rightClickListener) {
                    rightClickListener.onClick(view);
                }
            }
        });
        addView(iv_right);
    }

    /**
     * 添加右侧的文字按钮
     *
     * @param right_text 右侧文字
     */
    private void addRightText(String right_text) {
        if (null != getTitleTextView()) getTitleTextView().setMaxEms(11);
        tv_right = new TextView(getContext());
        if (is_right_bold){
            tv_right.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        }
        LayoutParams layoutParamsIvRight =
                new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        tv_right.setLayoutParams(layoutParamsIvRight);
        tv_right.setText(right_text);
        tv_right.setTextSize(right_text_size);
        tv_right.setTextColor(right_text_color);
        tv_right.setPadding(0, 0, SizeUtils.dp2px(10), 0);
        tv_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != rightClickListener) {
                    rightClickListener.onClick(view);
                }
            }
        });
        addView(tv_right);
    }

    /**
     * 移除右侧按钮
     */
    public void removeRightView() {
        if (null != tv_right) {
            removeView(tv_right);
            tv_right = null;
        }
        if (null != iv_right) {
            removeView(iv_right);
            iv_right = null;
        }
    }


    /**
     * 设置右侧按钮为图片，设置图片资源id
     *
     * @param icon
     */
    public BaseOneRightToolBar setRightButtonIcon(int icon) {
        if (null != iv_right) {
            iv_right.setImageResource(icon);
        } else {
            removeRightView();
            addRightIcon(icon);
        }
        return this;
    }

    /**
     * 设置右侧按钮为文字，设置文字
     *
     * @param rightText
     */
    public BaseOneRightToolBar setRightButtonText(String rightText) {
        if (null != tv_right) {
            tv_right.setText(rightText);
        } else {
            removeRightView();
            addRightText(rightText);
        }
        return this;
    }

    public BaseOneRightToolBar setRightTextSize(float size) {
        right_text_size = (int) size;
        if (null == tv_right) {
            removeRightView();
            addRightText("");
        } else {
            tv_right.setTextSize(size);
        }
        return this;
    }

    public BaseOneRightToolBar setRightTextColor(int color) {
        right_text_color = color;
        if (null == tv_right) {
            removeRightView();
            addRightText("");
        } else {
            tv_right.setTextColor(color);
        }
        return this;
    }

    public CharSequence getRightText() {
        if (null != tv_right) return tv_right.getText();
        return null;
    }

    public View getRightView() {
        if (null != tv_right) {
            return tv_right;
        } else if (null != iv_right) {
            return iv_right;
        } else {
            return null;
        }
    }

    /**
     * 设置右侧按钮监听事件
     *
     * @param linster
     */
    public BaseOneRightToolBar setOnRightButtonClickLinster(OnClickListener linster) {
        this.rightClickListener = linster;
        return this;
    }
}
