package com.dengyun.baselibrary.base.fragment;

/**
 * @Title 适配 hide和show情况下 的fragment生命周期
 * @Desc: 生命周期onSume和onPause的监测通过onUserVisible和onUserInvisible方法，
 *        （整合：左右滑动切换，home，下一页返回）
 * @Author: zhoubo
 * @CreateDate: 2020-04-10 19:27
 */
public abstract class BaseHideShowFragment extends BaseFragment {

    //与show hide用到
    private boolean isRuningHiddenChanged = false;
    private boolean isUserVisible = false;

    @Override
    public void onResume() {
        super.onResume();
        if(!isRuningHiddenChanged){
            isRuningHiddenChanged = true;
            isUserVisible = true;
            onUserVisible();
        } else {
            //如果当前页面可见的
            if (isUserVisible) {
                onUserVisible();
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        //如果是隐藏
        if(hidden){
            if (isUserVisible){
                isUserVisible = false;
                onUserInvisible();
            }
        } else {
            //如果是显示
            onUserVisible();
            isUserVisible = true;
        }

        isRuningHiddenChanged = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        //如果当前页面可见的
        if (isUserVisible) {
            onUserInvisible();
        }
    }

    @Override
    public abstract int getLayoutId() ;

    protected abstract void onUserVisible();

    protected abstract void onUserInvisible();
}
