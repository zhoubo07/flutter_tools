package com.dengyun.baselibrary.base.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import androidx.core.widget.NestedScrollView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.dengyun.baselibrary.R;
import com.dengyun.baselibrary.utils.ScreenUtil;
import com.dengyun.baselibrary.utils.ViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @titile  底部条目选择弹出框，（照相/选择图片，男女）
 * @desc Created by seven on 2018/3/30.
 */
public class BaseBottomItemDialog {


	private Builder builder;

	private BaseBottomItemDialog(Builder builder) {
	    this.builder = builder;
	}

    public static Builder newBuilder(Context context) {
        return new Builder(context);
    }

	public void show() {
		builder.dialog.show();
	}

	public interface OnSheetItemClickListener {
		void onClick(Dialog dialog, int which);
	}

	public static final class Builder{
        private Context context;
        private Dialog dialog;
        private TextView txt_title;
        private TextView txt_cancel;
        private LinearLayout lLayout_content;
        private NestedScrollView sLayout_content;
        private boolean showTitle = false;
        private List<SheetItem> sheetItemList;

	    public Builder(Context context){
            this.context = context;
            // 获取Dialog布局
            View view = LayoutInflater.from(context).inflate(
                    R.layout.base_dialog_ios_bottomitem, null);

            // 设置Dialog最小宽度为屏幕宽度
            view.setMinimumWidth(ScreenUtil.getScreenWidth());

            // 获取自定义Dialog布局中的控件
            sLayout_content = (NestedScrollView) view.findViewById(R.id.sLayout_content);
            lLayout_content = (LinearLayout) view
                    .findViewById(R.id.lLayout_content);
            txt_title = (TextView) view.findViewById(R.id.txt_title);
            txt_cancel = (TextView) view.findViewById(R.id.txt_cancel);
            txt_cancel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            // 定义Dialog布局和参数
            dialog = new Dialog(context, R.style.IosDialogBottomStyle);
            dialog.setContentView(view);
            Window dialogWindow = dialog.getWindow();
            dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.x = 0;
            lp.y = 0;
            lp.width = ScreenUtil.getScreenWidth();
            dialogWindow.setAttributes(lp);
        }

        /**
         * 设置标题
         * @param title
         * @return
         */
        public Builder setTitle(String title) {
            showTitle = true;
            txt_title.setVisibility(View.VISIBLE);
            txt_title.setText(title);
            return this;
        }

        /**
         * 设置dialog是否可取消
         * @param cancel
         * @return
         */
        public Builder setCancelable(boolean cancel) {
            dialog.setCancelable(cancel);
            return this;
        }

        /**
         * 设置dialog是否可以点击外面取消
         * @param cancel
         * @return
         */
        public Builder setCanceledOnTouchOutside(boolean cancel) {
            dialog.setCanceledOnTouchOutside(cancel);
            return this;
        }

        /**
         * @param strItem 条目名称
         * @param color 条目字体颜色，设置null则默认蓝色
         * @param listener
         * @return
         */
        public Builder addSheetItem(String strItem, SheetItemColor color,
                                                OnSheetItemClickListener listener) {
            if (sheetItemList == null) {
                sheetItemList = new ArrayList<SheetItem>();
            }
            sheetItemList.add(new SheetItem(strItem, color, listener));
            return this;
        }

        public BaseBottomItemDialog build(){
            setSheetItems();
            return new BaseBottomItemDialog(this);
        }

        /** 设置条目布局 */
        private void setSheetItems() {
            if (sheetItemList == null || sheetItemList.size() <= 0) {
                return;
            }

            int size = sheetItemList.size();

            // 添加条目过多的时候控制高度
            if (size >= 7) {
                LayoutParams params = (LayoutParams) sLayout_content
                        .getLayoutParams();
                params.height = ScreenUtil.getScreenHeight() / 2;
                sLayout_content.setLayoutParams(params);
            }

            // 循环添加条目
            for (int i = 1; i <= size; i++) {
                final int index = i;
                SheetItem sheetItem = sheetItemList.get(i - 1);
                String strItem = sheetItem.name;
                SheetItemColor color = sheetItem.color;
                final OnSheetItemClickListener listener = (OnSheetItemClickListener) sheetItem.itemClickListener;

                TextView textView = new TextView(context);
                textView.setText(strItem);
                textView.setTextSize(18);
                textView.setGravity(Gravity.CENTER);

                // 背景图片
                if (size == 1) {
                    if (showTitle) {
                        textView.setBackgroundResource(R.drawable.base_select_dialog_iosbottom_bottom);
                    } else {
                        textView.setBackgroundResource(R.drawable.base_select_dialog_iosbottom_single_selector);
                    }
                } else {
                    if (showTitle) {
                        if (i >= 1 && i < size) {
                            textView.setBackgroundResource(R.drawable.base_select_dialog_iosbottom_middle);
                        } else {
                            textView.setBackgroundResource(R.drawable.base_select_dialog_iosbottom_bottom);
                        }
                    } else {
                        if (i == 1) {
                            textView.setBackgroundResource(R.drawable.base_select_dialog_iosbottom_top);
                        } else if (i < size) {
                            textView.setBackgroundResource(R.drawable.base_select_dialog_iosbottom_middle);
                        } else {
                            textView.setBackgroundResource(R.drawable.base_select_dialog_iosbottom_bottom);
                        }
                    }
                }

                // 字体颜色
                if (color == null) {
                    textView.setTextColor(Color.parseColor(SheetItemColor.Blue
                            .getName()));
                } else {
                    textView.setTextColor(Color.parseColor(color.getName()));
                }

                // 高度
                float scale = context.getResources().getDisplayMetrics().density;
                int height = (int) (45 * scale + 0.5f);
                textView.setLayoutParams(new LayoutParams(
                        LayoutParams.MATCH_PARENT, height));

                // 点击事件
                textView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(null!=listener){
                            listener.onClick(dialog,index);
                        }else {
                            dialog.dismiss();
                        }
                    }
                });

                lLayout_content.addView(textView);
            }
        }

        public class SheetItem {
            String name;
            OnSheetItemClickListener itemClickListener;
            SheetItemColor color;

            public SheetItem(String name, SheetItemColor color,
                             OnSheetItemClickListener itemClickListener) {
                this.name = name;
                this.color = color;
                this.itemClickListener = itemClickListener;
            }
        }

        public enum SheetItemColor {
            Blue("#037BFF"), Red("#FD4A2E");

            private String name;

            private SheetItemColor(String name) {
                this.name = name;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }

}
