package com.dengyun.baselibrary.net;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.dengyun.baselibrary.base.dialog.BaseDialogFragment;
import com.dengyun.baselibrary.base.dialog.loading.LoadingDialog1;
import com.dengyun.baselibrary.net.constants.ProjectType;
import com.dengyun.baselibrary.net.constants.RequestMethod;
import com.lzy.okgo.model.HttpHeaders;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @titile 本来要替换网络参数列表，参数不多，暂时不用此设计，多了再加
 * @desc Created by seven on 2018/4/24.
 */

/**
 * NetOption netOption = NetOption.newBuilder(HttpUrlConstants.login)
 * .params("account",userName)     //必传参数，参数的key-value，可以传多个，内部转成了json
 * .params("password", password)
 * .activity(mView.getMyActivity())   //和.fragment参数二选一，标识当前调用的是fragment或者activity
 * //.fragment()                     //和.activity参数二选一，标识当前调用的是fragment或者activity
 * .type(type)        //和.clazz参数二选一，标识返回bean的类型，推荐使用.type
 * //.clazz()        //和.type参数二选一，标识返回bean的类型，推荐使用.type
 * .isEncrypt(true)    //非必传，是否参数加密，默认true
 * .isShowDialog(true) //非必传，是否展示加载缓冲圈，默认false
 * .build();
 */

public class NetOption {

    private String url;
    private HttpHeaders headers;

    private final String tag;
    private final Fragment fragment;
    private final FragmentActivity activity;
    private final boolean isShowDialog;
    private BaseDialogFragment loadingDialog;
    private final Map params;
    private final boolean isEncrypt;//是否启动加密，默认为true
    private final Type type;
    private final Class clazz;
    private boolean isInterceptErrorCode = true;//是否拦截错误code
    private @RequestMethod
    int requestMethod;//网络请求方式：get/post_json/post_form
    private @ProjectType
    int projectType;//项目类型（由于不同的项目的网络参数配置可能不一致)，默认每天美耶

    private NetOption(Builder builder) {
        tag = builder.tag;
        headers = builder.headers;
        fragment = builder.fragment;
        activity = builder.activity;
        isShowDialog = builder.isShowDialog;
        loadingDialog = builder.loadingDialog;
        url = builder.url;
        params = builder.params;
        isEncrypt = builder.isEncrypt;
        type = builder.type;
        clazz = builder.clazz;
        isInterceptErrorCode = builder.isInterceptErrorCode;
        requestMethod = builder.requestMethod;
        projectType = builder.projectType;
    }

    public static Builder newBuilder(String url) {
        return new Builder(url);
    }

    public String getTag() {
        return tag;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public void addHeaders(HttpHeaders addHeaders) {
        if (null == headers) headers = new HttpHeaders();
        headers.put(addHeaders);
    }

    public void addHeaders(String key, String value) {
        if (null == headers) headers = new HttpHeaders();
        headers.put(key, value);
    }


    public Fragment getFragment() {
        return fragment;
    }

    public FragmentActivity getActivity() {
        return activity;
    }

    public boolean isShowDialog() {
        return isShowDialog;
    }

    public BaseDialogFragment getLoadingDialog() {
        if (isShowDialog
                && (null != getActivity() || null != getFragment())
                && null == loadingDialog) {
            loadingDialog = LoadingDialog1.getInstance(this);
        }
        return loadingDialog;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String newUrl) {
        url = newUrl;
    }

    public Map getParams() {
        return params;
    }

    public boolean isEncrypt() {
        return isEncrypt;
    }

    public boolean isInterceptErrorCode() {
        return isInterceptErrorCode;
    }

    public Type getType() {
        return type;
    }

    public Class getClazz() {
        return clazz;
    }

    public @ProjectType
    int getProjectType() {
        return projectType;
    }

    public @RequestMethod
    int getRequestMethod() {
        return requestMethod;
    }

    //临时放开这个设置，因为NetApi中有传RequestMethod的方法，
    // 之后的新项目删除这个设置，只在NetOption中配置RequestMethod，不在NetApi出传
    public void setRequestMethod(@RequestMethod int requestMethod) {
        this.requestMethod = requestMethod;
    }

    public static final class Builder {
        private String tag;
        private HttpHeaders headers;
        private Fragment fragment;
        private FragmentActivity activity;
        private boolean isShowDialog = true;
        private BaseDialogFragment loadingDialog;
        private String url;
        private Map<String, Object> params = new HashMap<>();
        private boolean isEncrypt = true;
        private Type type;
        private Class clazz;
        private boolean isInterceptErrorCode = true;//是否拦截错误code (继续拦截全局的错误code，例如token过期，只是设置是否不拦截其他非全局的错误code)
        private @RequestMethod
        int requestMethod = RequestMethod.POST_JSON;//网络请求方式：get/post_json/post_form
        private @ProjectType
        int projectType = ProjectType.DEFAULT;//项目类型（由于不同的项目的网络参数配置可能不一致)

        private Builder(String url) {
            this.url = url;
        }

        public Builder() {
        }

        public Builder tag(String tag) {
            this.tag = tag;
            return this;
        }

        public Builder headers(HttpHeaders headers) {
            if (null == this.headers) this.headers = new HttpHeaders();
            this.headers.put(headers);
            return this;
        }

        public Builder headers(String key, String value) {
            if (null == this.headers) this.headers = new HttpHeaders();
            headers.put(key, value);
            return this;
        }

        public Builder fragment(Fragment fragment) {
            this.fragment = fragment;
            return this;
        }

        public Builder activity(FragmentActivity activity) {
            this.activity = activity;
            return this;
        }

        public Builder isShowDialog(boolean isShowDialog) {
            this.isShowDialog = isShowDialog;
            return this;
        }

        public Builder loadingDialog(BaseDialogFragment loadingDialog) {
            this.loadingDialog = loadingDialog;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder params(Map val) {
            params = val;
            return this;
        }

        public Builder params(String key, Object value) {
            if (null != key && null != value) {
                this.params.put(key, value);
            }
            return this;
        }

        public Builder isEncrypt(boolean isEncrypt) {
            this.isEncrypt = isEncrypt;
            return this;
        }

        public Builder type(Type type) {
            this.type = type;
            return this;
        }

        public Builder clazz(Class clazz) {
            this.clazz = clazz;
            return this;
        }

        public Builder isInterceptErrorCode(boolean isInterceptErrorCode) {
            this.isInterceptErrorCode = isInterceptErrorCode;
            return this;
        }

        public Builder requestMethod(@RequestMethod int requestMethod) {
            this.requestMethod = requestMethod;
            return this;
        }

        public Builder projectType(@ProjectType int projectType) {
            this.projectType = projectType;
            return this;
        }

        public NetOption build() {
            if (null == type && null == clazz) {
                clazz = String.class;
            }
            //移除参数中value为空的数据
            removeMapEmptyValue();
            return new NetOption(this);
        }

        private void removeMapEmptyValue() {
            Set<String> set = params.keySet();
            Iterator<String> it = set.iterator();
            List<String> listKey = new ArrayList<String>();
            while (it.hasNext()) {
                String str = it.next();
                if (null == str || params.get(str) == null) {
                    listKey.add(str);
                }
            }
            for (String key : listKey) {
                params.remove(key);
            }
        }

    }
}
