package com.dengyun.baselibrary.widgets.toolbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dengyun.baselibrary.R;

/**
 * @Title 右侧有两个按钮的toolbar
 * @Author: zhoubo
 * @CreateDate: 2019-07-22 15:15
 */
public class BaseTwoIconRightToolbar extends BaseToolBar{

    private OnClickListener onClickListenerRight1;
    private OnClickListener onClickListenerRight2;
    private ImageView ivRight1;
    private ImageView ivRight2;

    public BaseTwoIconRightToolbar(Context context) {
        this(context,null,0);
    }

    public BaseTwoIconRightToolbar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BaseTwoIconRightToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addRightTwoButton(context);
    }

    public void setOnRight1ClickListener(OnClickListener onClickListenerRight1){
        this.onClickListenerRight1 = onClickListenerRight1;
    }

    public void setOnRight2ClickListener(OnClickListener onClickListenerRight2){
        this.onClickListenerRight2 = onClickListenerRight2;
    }

    public void setRight1Icon(int resId){
        ivRight1.setImageResource(resId);
    }
    public void setRight2Icon(int resId){
        ivRight2.setImageResource(resId);
    }

    /**
     * 添加右侧的分享和点击按钮的view
     *
     * @param context
     */
    private void addRightTwoButton(Context context) {
        View viewRight = LayoutInflater.from(context).inflate(R.layout.base_view_toolbar_righttwo, null);
        addRightListener(viewRight);
        LayoutParams layoutParamsIvRight =
                new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        addView(viewRight, layoutParamsIvRight);
    }

    /**
     * 添加右侧的分享和更多按钮的点击事件
     *
     * @param view
     */
    private void addRightListener( View view) {
        ivRight1 = (ImageView) view.findViewById(R.id.iv_right1);
        ivRight2 = (ImageView) view.findViewById(R.id.iv_right2);
        ivRight1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != onClickListenerRight1) onClickListenerRight1.onClick(view);
            }
        });
        ivRight2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != onClickListenerRight2) onClickListenerRight2.onClick(view);
            }
        });
    }
}
