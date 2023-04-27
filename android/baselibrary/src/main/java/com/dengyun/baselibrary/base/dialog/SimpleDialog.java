package com.dengyun.baselibrary.base.dialog;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.dengyun.baselibrary.R;
import com.dengyun.baselibrary.base.dialog.listener.DialogViewHolder;
import com.dengyun.baselibrary.base.dialog.listener.OnCancelListener;
import com.dengyun.baselibrary.base.dialog.listener.OnConfirmListener;

/**
 * @titile 一个简单的dialog的实现，两个按钮
 * @desc Created by seven on 2018/3/8.
 */

public class SimpleDialog extends BaseDialog {
    private String type;
    private String title = "";
    private String message = "";
    private String cancelText = "";
    private String okText = "";
    private int buttonNum = 2;
    private OnCancelListener cancelClickListener;
    private OnConfirmListener confirmClickListener;

    public static SimpleDialog newInstance() {
        SimpleDialog simpleDialog = new SimpleDialog();
        return simpleDialog;
    }

    /**
     * @param title 设置标题
     */
    public SimpleDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * @param message 设置内容
     */
    public SimpleDialog setMessage(String message) {
        this.message = message;
        return this;
    }

    /**
     * @param cancelText 设置取消的文字
     */
    public SimpleDialog setCancelText(String cancelText) {
        this.cancelText = cancelText;
        return this;
    }

    /**
     * @param okText 设置确定的文字
     */
    public SimpleDialog setConfirmText(String okText) {
        this.okText = okText;
        return this;
    }

    /**
     * @param buttonNum 下方按钮的数量
     */
    public SimpleDialog setButtonNum(int buttonNum) {
        this.buttonNum = buttonNum;
        return this;
    }

    /**
     * @param cancelClickListener 设置按键取消监听
     * @return
     */
    public SimpleDialog setCancelListener(OnCancelListener cancelClickListener) {
        this.cancelClickListener = cancelClickListener;
        return this;
    }

    /**
     * @param confirmClickListener 设置按键确认监听
     * @return
     */
    public SimpleDialog setConfirmListener(OnConfirmListener confirmClickListener) {
        this.confirmClickListener = confirmClickListener;
        return this;
    }

    @Override
    public void onStart() {
        setWidthMarginDp(60);
        super.onStart();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        title = bundle.getString("title");
        message = bundle.getString("message");
    }

    @Override
    public int intLayoutId() {
        return R.layout.base_dialog_simple;
    }

    @Override
    public void convertView(final DialogViewHolder holder, final BaseDialogFragment dialog) {
        TextView tv_title = holder.getView(R.id.title);
        TextView tv_message = holder.getView(R.id.message);

        TextView tv_cancel = holder.getView(R.id.tv_simpledialog_cancel);
        TextView tv_ok = holder.getView(R.id.tv_simpledialog_ok);
        View tv_line = holder.getView(R.id.view_simpledialog_line);

        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(message)) {
            tv_title.setVisibility(View.VISIBLE);
            tv_title.setText(title);
            tv_message.setVisibility(View.VISIBLE);
            tv_message.setTextSize(14);
            tv_message.setText(message);
        } else {
            tv_title.setVisibility(View.GONE);
            tv_message.setTextSize(16);
            tv_message.setText(TextUtils.isEmpty(message) ? title : message);
        }

        switch (buttonNum) {
            case 1:
                tv_cancel.setVisibility(View.GONE);
                tv_line.setVisibility(View.GONE);
                break;
            case 2:
                tv_cancel.setVisibility(View.VISIBLE);
                tv_line.setVisibility(View.VISIBLE);
                break;
        }
        if (!TextUtils.isEmpty(cancelText)) {
            tv_cancel.setText(cancelText);
        }
        if (!TextUtils.isEmpty(okText)) {
            tv_ok.setText(okText);
        }


        holder.setOnClickListener(R.id.tv_simpledialog_cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == cancelClickListener) {
                    dialog.dismiss();
                } else {
                    cancelClickListener.onCancel(holder, dialog);
                }
            }
        });

        holder.setOnClickListener(R.id.tv_simpledialog_ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == confirmClickListener) {
                    dialog.dismiss();
                } else {
                    confirmClickListener.onConfirm(holder, dialog);
                }

            }
        });

    }
}
