/*
 * Copyright 2016 jeasonlzy(廖子尧)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dengyun.baselibrary.net.util;

import android.text.TextUtils;

import com.dengyun.baselibrary.config.AppConfig;
import com.dengyun.baselibrary.net.NetOption;
import com.dengyun.baselibrary.net.constants.ReturnCodeConstants;
import com.dengyun.baselibrary.net.exception.ApiException;
import com.dengyun.baselibrary.utils.AppLogUtil;
import com.dengyun.baselibrary.utils.GsonConvertUtil;
import com.dengyun.baselibrary.utils.phoneapp.AppUtils;
import com.lzy.okgo.convert.Converter;
import org.json.JSONObject;

import java.lang.reflect.Type;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧）Github地址：https://github.com/jeasonlzy
 * 版    本：1.0
 * 创建日期：16/9/11
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class JsonConvert2<T> implements Converter<T> {

    private NetOption netOption;

    public JsonConvert2(NetOption netOption) {
        this.netOption = netOption;
    }

    /**
     * 该方法是子线程处理，不能做ui相关的工作
     * 主要作用是解析网络返回的 response 对象，生成onSuccess回调中需要的数据对象
     * 这里的解析工作不同的业务逻辑基本都不一样,所以需要自己实现,以下给出的时模板代码,实际使用根据需要修改
     */
    @Override
    public T convertResponse(Response response) throws Throwable {
        return parseClass(response);
    }

    private String codeFeflectName = "code";
    private String msgFeflectName = "msg";

    private T parseClass(Response response) throws Exception {
        ResponseBody body = response.body();
        if (body == null) return null;
        String responseJson = body.string();
        response.close();

        if (AppUtils.isAppDebug()) {
            printLog(responseJson);
        }

        //这里获得到的是类的泛型的类型
        Type returnType;
        if (null != netOption.getType()) {
            returnType = netOption.getType();
        } else if (null != netOption.getClazz()) {
            returnType = netOption.getClazz();
        } else {
            returnType = String.class;
        }

        JSONObject jsonObject = new JSONObject(responseJson);
        String code = "", msg = "";
        try {
            code = jsonObject.getString(codeFeflectName);       //获取返回模板中的code值（根据业务返回的模板设置）
        } catch (Exception e) {
        }

        try {
            msg = jsonObject.getString(msgFeflectName);  //获取返回模板中的message值（根据业务返回的模板设置）
        } catch (Exception e) {
        }

        if (TextUtils.isEmpty(code)) {
            //不是标准的code、msg
            return convertJson(responseJson,returnType);
        } else {
            boolean isContainsDealCode = ReturnCodeConstants.isContainsDealCode(netOption, code);
            boolean isContainsGlobalDealCode = ReturnCodeConstants.isContainsGlobalDealCode(netOption, code);
            if (isContainsGlobalDealCode) {
                //全局拦截code（类似token过期）
                throw new ApiException(msg, code);
            } else if (!netOption.isInterceptErrorCode() || isContainsDealCode) {
                //不拦截错误code 或者 code为需要正常返回的code
                return convertJson(responseJson,returnType);
            } else {
                //其余的错误code传递到错误回调
                throw new ApiException(msg, code);
            }
        }
    }

    private T convertJson(String responseJson,Type returnType){
        if (returnType.equals(String.class)){
            return (T) responseJson;
        }else {
            return GsonConvertUtil.fromJson(responseJson, returnType);
        }
    }

    private void printLog(String json) {
        if (!AppUtils.isAppDebug()) return;
        AppLogUtil.setNetResultLog(netOption.getUrl(), netOption.getParams(), json);
    }

}
