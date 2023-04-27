package com.dengyun.baselibrary.base.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.dengyun.baselibrary.base.dialog.loading.LoadingDialog1;
import com.dengyun.baselibrary.base.view.BaseFragmentView;
import com.dengyun.baselibrary.net.NetApi;
import com.dengyun.baselibrary.net.rx.RxManager;
import com.dengyun.baselibrary.utils.AppLogUtil;
import com.dengyun.baselibrary.utils.ObjectUtils;
import com.dengyun.baselibrary.utils.ToastUtils;

/**
 * @titile
 * @desc Created by seven on 2018/2/26.
 */

public abstract class BaseFragment extends Fragment implements BaseFragmentView {

    protected Context mContext;//activity的上下文对象
    protected Bundle mBundle;
    private LoadingDialog1 loadingDialog;
    private String TAG = "BaseFragment";//本fragment的tag，在onAttach方法中重置，为当前Fragment的包名+类名路径
    private View contentView;

    /**
     * 绑定activity
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        TAG = ObjectUtils.getClassPath(this);
        mContext = context;
        AppLogUtil.setFragmentLCLog(TAG,"onAttach");

    }

    /**
     * 运行在onAttach之后
     * 可以接受别人传递过来的参数,实例化对象.
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppLogUtil.setFragmentLCLog(TAG,"onCreate");
        //获取bundle,并保存起来
        if (savedInstanceState != null) {
            mBundle = savedInstanceState.getBundle("bundle");
        } else {
            mBundle = getArguments() == null ? new Bundle() : getArguments();
        }
    }

    /**
     * 运行在onCreate之后
     * 生成view视图
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AppLogUtil.setFragmentLCLog(TAG,"onCreateView");
        View view = inflater.inflate(getLayoutId(), container, false);
        initCreateView(inflater,container,savedInstanceState);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppLogUtil.setFragmentLCLog(TAG,"onViewCreated");
        this.contentView = view;
        onInitViews(view,savedInstanceState);
//        onInitData();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AppLogUtil.setFragmentLCLog(TAG,"onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        AppLogUtil.setFragmentLCLog(TAG,"onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        AppLogUtil.setFragmentLCLog(TAG,"onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        AppLogUtil.setFragmentLCLog(TAG,"onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        AppLogUtil.setFragmentLCLog(TAG,"onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        AppLogUtil.setFragmentLCLog(TAG,"onDestroyView");
        //本页的网络请求取消
        NetApi.cancelTag(TAG);
        //本页的observable取消
        RxManager.getInstance().clear(TAG);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppLogUtil.setFragmentLCLog(TAG,"onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        AppLogUtil.setFragmentLCLog(TAG,"onDetach");
    }

    @Override
    public String getMyTag() {
        return TAG==null? ObjectUtils.getClassPath(this):TAG;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mBundle != null) {
            outState.putBundle("bundle", mBundle);
        }
    }

    public <T extends View> T findViewById(int id) {
        if (null != contentView) return contentView.findViewById(id);
        return null;
    }

    /**
     * @return  设置fragment的布局
     */
    public abstract int getLayoutId();
    /**
     * 初始化Fragment应有的视图
     * @return
     */
    public abstract void initViews(@NonNull View view, @Nullable Bundle savedInstanceState);

    public void onInitViews(@NonNull View view, @Nullable Bundle savedInstanceState){
        AppLogUtil.setFragmentLCLog(TAG,"onInitViews");
        initViews(view, savedInstanceState);
    }

    /**
     * 一般不用重写，参数等同于onCreateView
     */
    public void initCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){}
   

    /**
     * 类似Activity的OnBackgress
     * fragment进行回退
     */
    public void onBack() {
        getFragmentManager().popBackStack();
    }

    public void gotoActivity(Class<?> clz){
        Intent intent = new Intent(getContext(),clz);
        startActivity(intent);
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public FragmentActivity getMyActivity() {
        return getActivity();
    }

    @Override
    public Fragment getMyFragment() {
        return this;
    }

    public Bundle getBundle() {
        return mBundle;
    }

    public BaseFragment getFragment() {
        return this;
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

}
