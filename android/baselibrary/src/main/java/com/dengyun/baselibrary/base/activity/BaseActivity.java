package com.dengyun.baselibrary.base.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.dengyun.baselibrary.R;
import com.dengyun.baselibrary.base.dialog.loading.LoadingDialog1;
import com.dengyun.baselibrary.base.view.BaseActivityView;
import com.dengyun.baselibrary.net.NetApi;
import com.dengyun.baselibrary.net.rx.RxManager;
import com.dengyun.baselibrary.utils.AppLogUtil;
import com.dengyun.baselibrary.utils.ObjectUtils;
import com.dengyun.baselibrary.utils.ToastUtils;
import com.dengyun.baselibrary.utils.bar.StatusBarUtil;
import com.dengyun.baselibrary.utils.phoneapp.KeyboardUtils;


/**
 * @titile
 * @desc Created by seven on 2018/2/24.
 */

public abstract class BaseActivity extends AppCompatActivity implements BaseActivityView {
    protected FragmentActivity activity;
    private boolean hasBus = false;
    private Fragment currentV4Fragment;
    private LoadingDialog1 loadingDialog;
    private String TAG = "BaseActivity";//本activity的tag，在onCreate方法中重置，为当前activity的包名+类名路径

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = ObjectUtils.getClassPath(this);
        activity = this;
        AppLogUtil.setActivityLCLog(TAG,"onCreate");
        onBeforSetContentView(savedInstanceState);
        setStatusBar();
        int layoutId = getLayoutId();
        if (layoutId != 0) {
            setContentView(layoutId);
        }

        onInitViews(savedInstanceState);
//        onInitData();
    }

    @Override
    protected void onStart() {
        AppLogUtil.setActivityLCLog(TAG,"onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppLogUtil.setActivityLCLog(TAG,"onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppLogUtil.setActivityLCLog(TAG,"onPause");
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        KeyboardUtils.hideInputWhenTouchOtherView(this,ev,null);
        return super.dispatchTouchEvent(ev);
    }

    private boolean isDestroyed = false;
    private void destroy()  {
        if (isDestroyed) {
            return;
        }
        isDestroyed = true;
        //回收资源
        onRelease();
    }

    /**
     * 释放资源
     */
    protected void onRelease(){
        AppLogUtil.setActivityLCLog(TAG,"onRelease");
        //本页的网络请求取消
        NetApi.cancelTag(TAG);
        //本页的observable取消
        RxManager.getInstance().clear(TAG);
    }


    @Override
    protected void onStop() {
        AppLogUtil.setActivityLCLog(TAG,"onStop");
        super.onStop();
        //在此生命周期中回收资源
        if (isFinishing()) {
            destroy();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppLogUtil.setActivityLCLog(TAG,"onDestroy");
        //需要在onDestroy方法中进一步检测是否回收资源等。
        destroy();
    }

    @Override
    public String getMyTag() {
        return TAG==null? ObjectUtils.getClassPath(this):TAG;
    }

    private View getContentView() {
        return ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
    }


    /**
     * @return 设置当前activity的layoutid
     */
    protected abstract int getLayoutId();

    /**
     * @param savedInstanceState 初始化view的操作，在此方法实现
     */
    protected abstract void initViews(Bundle savedInstanceState);

    /**
     * @param savedInstanceState
     */
    protected void onInitViews(Bundle savedInstanceState){
        AppLogUtil.setActivityLCLog(TAG,"onInitViews");
        initViews(savedInstanceState);
    }

    /**
     * 初始化数据的方法，在此实现
     */
//    protected  void initData(){};

    /*protected void onInitData(){
        AppLogUtil.setActivityLCLog(TAG,"onInitData");
        initData();
    }*/

    @Override
    public <T extends View> T findViewById(int id) {
        return super.findViewById(id);
    }

    /**
     * 设置状态栏，改变状态栏重写此方法
     */
    public void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, getResources().getColor(R.color.white));
        StatusBarUtil.setLightTextMode(this);
    }

    /**
     * 在setContentView之前设置东西
     */
    public void onBeforSetContentView(@Nullable Bundle savedInstanceState) {
    }


    /**
     * 跳转activity（不关闭本页面，无参数）
     */
    public void gotoActivity(Class<?> clz) {
        Intent intent = new Intent(activity, clz);
        startActivity(intent);
    }

    /**
     * Fragment替换视图
     *
     * @param resView        将要被替换掉的视图
     * @param targetFragment 用来替换的Fragment
     */
    public void changeFragment(int resView, Fragment targetFragment) {
        if (targetFragment.equals(currentV4Fragment)) {
            return;
        }
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        if (!targetFragment.isAdded()) {
            transaction.add(resView, targetFragment, targetFragment.getClass()
                    .getName());
        }
        if (targetFragment.isHidden()) {
            transaction.show(targetFragment);
            //targetFragment.onChange();
        }
        if (currentV4Fragment != null
                && currentV4Fragment.isVisible()) {
            transaction.hide(currentV4Fragment);
        }
        currentV4Fragment = targetFragment;
        transaction.commit();
    }

    @Override
    public void toast(String msg) {
        ToastUtils.showShort(msg);
    }

    @Override
    public void toast(int msgId) {
        ToastUtils.showShort(msgId);
    }

    @Override
    public void toastLong(String msg) {
        ToastUtils.showShort(msg);
    }

    @Override
    public void toastLong(int msgId) {
        ToastUtils.showShort(msgId);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public FragmentActivity getMyActivity() {
        return activity;
    }

}
