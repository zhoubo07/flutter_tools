package com.dengyun.baselibrary.widgets;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author zhoubo
 * @description: RecyclerView 自定义间隔
 * @date :2020年07月08日16:20:12
 */
public class RecycleViewDivider extends RecyclerView.ItemDecoration {

    private int space;
    private Drawable mDivider;
    private Paint mPaint;

    public RecycleViewDivider(int space) {
        this.space = space;
    }

    public RecycleViewDivider(int space, int color) {
        this.space = space;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(space);
    }

    public RecycleViewDivider(int space, Drawable mDivider) {
        this.space = space;
        this.mDivider = mDivider;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() != null) {
            if (parent.getLayoutManager() instanceof LinearLayoutManager && !(parent.getLayoutManager() instanceof GridLayoutManager)) {
                if (((LinearLayoutManager) parent.getLayoutManager()).getOrientation() == LinearLayoutManager.HORIZONTAL) {
                    // 横向的LinearLayoutManager
                    // 留间隔的顺序：每条的左侧留间隔，第一条不留间隔
                    if (parent.getChildLayoutPosition(view) != 0) {
                        outRect.set(space, 0, 0, 0);
                    }
                } else {
                    // 纵向的LinearLayoutManager
                    // 留间隔的顺序：每条的上方留间隔，第一条不留间隔
                    if (parent.getChildLayoutPosition(view) != 0) {
                        outRect.set(0, space, 0, 0);
                    }
                }
            } else if (parent.getLayoutManager() instanceof GridLayoutManager) {
                // GridLayoutManager
                GridLayoutManager gridLayoutManager = (GridLayoutManager) parent.getLayoutManager();

                // 是否位于 每行第一个(竖向布局) / 每列第一个(横向布局)
                boolean isRowFirst = parent.getChildLayoutPosition(view) % gridLayoutManager.getSpanCount() == 0;

                // 是否在第一列中
                boolean isInFirstRow = parent.getChildLayoutPosition(view) < gridLayoutManager.getSpanCount();

                if (gridLayoutManager.getOrientation() == LinearLayoutManager.HORIZONTAL) {
                    // gridLayout 横向布局
                    // 留间隔的顺序：每条的左方、上方留间隔，第一条左方不留间隔
                    outRect.set(isInFirstRow ? 0 : space, isRowFirst ? 0 : space, 0, 0);
                } else {
                    // gridLayout 竖向布局
                    // 留间隔的顺序：每条的左方、上方留间隔，第一条上方不留间隔
                    outRect.set(isRowFirst ? 0 : space, isInFirstRow ? 0 : space, 0, 0);
                }
            } else {
                super.getItemOffsets(outRect, view, parent, state);
            }
        }

    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (parent.getLayoutManager() != null) {
            if (parent.getLayoutManager() instanceof LinearLayoutManager && !(parent.getLayoutManager() instanceof GridLayoutManager)) {
                if (((LinearLayoutManager) parent.getLayoutManager()).getOrientation() == LinearLayoutManager.HORIZONTAL) {
                    // 横向的LinearLayoutManager
                    drawHorizontal(c, parent);
                } else {
                    // 纵向的LinearLayoutManager
                    drawVertical(c, parent);
                }
            } else {
                super.onDraw(c, parent, state);
            }
            // GridLayoutManager 只留间距就行，暂时不用绘制
        }
    }

    //绘制横向 item 分割线

    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getMeasuredHeight() - parent.getPaddingBottom();
        final int childSize = parent.getChildCount();
        // 绘制顺序：每条的右方绘制，最后一条不绘制
        for (int i = 0; i < childSize - 1; i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + layoutParams.rightMargin;
            final int right = left + space;
            if (mDivider != null) {
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(canvas);
            }
            if (mPaint != null) {
                canvas.drawRect(left, top, right, bottom, mPaint);
            }
        }
    }

    //绘制纵向 item 分割线
    private void drawVertical(Canvas canvas, RecyclerView parent) {
        int left = parent.getPaddingLeft();
        int right = parent.getMeasuredWidth() - parent.getPaddingRight();
        final int childSize = parent.getChildCount();
        // 绘制顺序：每条的下方绘制，最后一条不绘制
        for (int i = 0; i < childSize - 1; i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            int top = child.getBottom() + layoutParams.bottomMargin;
            int bottom = top + space;
            if (mDivider != null) {
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(canvas);
            }
            if (mPaint != null) {
                canvas.drawRect(left, top, right, bottom, mPaint);
            }

        }
    }
}