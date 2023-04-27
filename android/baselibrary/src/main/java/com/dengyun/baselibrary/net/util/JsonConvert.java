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

import com.dengyun.baselibrary.base.ApiBean;
import com.dengyun.baselibrary.base.ApiSimpleBean;
import com.dengyun.baselibrary.config.AppConfig;
import com.dengyun.baselibrary.net.NetOption;
import com.dengyun.baselibrary.net.constants.ReturnCodeConstants;
import com.dengyun.baselibrary.net.exception.ApiException;
import com.dengyun.baselibrary.utils.AppLogUtil;
import com.dengyun.baselibrary.utils.GsonConvertUtil;
import com.dengyun.baselibrary.utils.ReflectUtils;
import com.dengyun.baselibrary.utils.phoneapp.AppUtils;
import com.google.gson.stream.JsonReader;
import com.lzy.okgo.convert.Converter;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
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
public class JsonConvert<T> implements Converter<T> {

    private NetOption netOption;
    private Type type;
    private Class<T> clazz;

    public JsonConvert(NetOption netOption){
        this.netOption = netOption;
        type = netOption.getType();
        clazz = netOption.getClazz();
    }

    /**
     * 该方法是子线程处理，不能做ui相关的工作
     * 主要作用是解析网络返回的 response 对象，生成onSuccess回调中需要的数据对象
     * 这里的解析工作不同的业务逻辑基本都不一样,所以需要自己实现,以下给出的时模板代码,实际使用根据需要修改
     */
    @Override
    public T convertResponse(Response response) throws Throwable {

        if(type == null && clazz == null){
            clazz = (Class<T>) String.class;
            return parseClass(response, clazz);
        }

        if(type==null){
            return parseClass(response,clazz);
        }else if (type instanceof ParameterizedType) {
            return parseParameterizedType(response, (ParameterizedType) type);
        } else {
            return parseType(response, type);
        }
    }

    private String codeFeflectName = "code";
    private String msgFeflectName = "msg";

    private T parseClass(Response response, Class<?> rawType) throws Exception {
        ResponseBody body = response.body();
        if (body == null) return null;
        JsonReader jsonReader = new JsonReader(body.charStream());
        T t;
        String code = "200",msg = "";
        boolean isNormalApiBean = true;//是否是标准的ApiBean的格式
        if (rawType == String.class) {
            t = (T) body.string();
            try {
                JSONObject jsonObject = new JSONObject(t.toString());
                code = jsonObject.getString(codeFeflectName);       //获取返回模板中的code值（根据业务返回的模板设置）
                msg = jsonObject.getString(msgFeflectName);  //获取返回模板中的message值（根据业务返回的模板设置）
            } catch (Exception e) {
//                AppLogUtil.setNetLog("返回信息非标准code、msg");
                isNormalApiBean = false;
            }
        }  else {
            try {
                t = GsonConvertUtil.fromJson(jsonReader, rawType);
            }catch (Exception e){
                if (AppUtils.isAppDebug()){
                    String json = GsonConvertUtil.fromJson(jsonReader,String.class);
                    printRealRequset(json);
                }
                response.close();
                throw e;
            }
            try {
                code = ReflectUtils.reflect(t).field(codeFeflectName).<String>get();    //反射获取返回的bean中的code字段，泛型不能传错
                msg = ReflectUtils.reflect(t).field(msgFeflectName).<String>get();   //反射获取返回的bean中的message字段，泛型不能传错
            } catch (Exception e) {
//                AppLogUtil.setNetLog("返回信息非标准code、msg");
                isNormalApiBean = false;
            }
        }
        response.close();

        if (AppUtils.isAppDebug()) {
            if(t instanceof String){
                printLog(t.toString());
            }else {
                String json = GsonConvertUtil.toJson(t);
                printLog(json);
            }
        }

        return setReturn(code,msg, (T) t,isNormalApiBean);

    }

    private T parseType(Response response, Type type) throws Exception {
        ResponseBody body = response.body();
        if (body == null) return null;
        JsonReader jsonReader = new JsonReader(body.charStream());

        // 泛型格式如下： new JsonCallback<任意JavaBean>(this)
        T t = null;
        try {
            t = GsonConvertUtil.fromJson(jsonReader, type);
        }catch (Exception e){
            if (AppUtils.isAppDebug()){
                String json = GsonConvertUtil.fromJson(jsonReader,String.class);
                printRealRequset(json);
            }
            throw e;
        }finally {
            response.close();
        }

        String code = null, msg = null;
        boolean isNormalApiBean = true;//是否是标准的ApiBean的格式
        try {
            code = ReflectUtils.reflect(t).field(codeFeflectName).<String>get();    //反射获取返回的bean中的code字段，泛型不能传错
            msg = ReflectUtils.reflect(t).field(msgFeflectName).<String>get();   //反射获取返回的bean中的message字段，泛型不能传错
        } catch (Exception e) {
//            AppLogUtil.setNetLog("返回信息非标准code、msg");
            isNormalApiBean = false;
        }
        if (AppUtils.isAppDebug()) {
            String json = GsonConvertUtil.toJson(t);
            printLog(json);
        }
        return setReturn(code,msg, (T) t,isNormalApiBean);
    }

    private T parseParameterizedType(Response response, ParameterizedType type) throws Exception {
        ResponseBody body = response.body();
        if (body == null) return null;
        JsonReader jsonReader = new JsonReader(body.charStream());

        Type rawType = type.getRawType();                     // 泛型的实际类型
        Type typeArgument = type.getActualTypeArguments()[0]; // 泛型的参数

        boolean isNormalApiBean = true;//是否是标准的ApiBean的格式
        if (rawType != ApiBean.class) {
            // 泛型格式如下： new JsonCallback<外层BaseBean<内层JavaBean>>(this)
            T t = null;
            try {
                 t = GsonConvertUtil.fromJson(jsonReader, type);
            }catch (Exception e){
                if (AppUtils.isAppDebug()){
                    String json = GsonConvertUtil.fromJson(jsonReader,String.class);
                    printRealRequset(json);
                }
                throw e;
            }finally {
                response.close();
            }

            if (AppUtils.isAppDebug()) {
                String json = GsonConvertUtil.toJson(t);
                printLog(json);
            }

            String code = "200",msg = "";
            try {
                code = ReflectUtils.reflect(t).field(codeFeflectName).<String>get();    //反射获取返回的bean中的code字段，泛型不能传错
                msg = ReflectUtils.reflect(t).field(msgFeflectName).<String>get();   //反射获取返回的bean中的message字段，泛型不能传错
            } catch (Exception e) {
//                AppLogUtil.setNetLog("返回信息非标准code、msg");
                isNormalApiBean = false;
            }
            return setReturn(code,msg, (T) t,isNormalApiBean);
        } else {
            if (typeArgument == Void.class) {
                // 泛型格式如下： new JsonCallback<LzyResponse<Void>>(this)
                ApiSimpleBean simpleResponse = GsonConvertUtil.fromJson(jsonReader, ApiSimpleBean.class);
                response.close();
                if (AppUtils.isAppDebug()) {
                    String json = GsonConvertUtil.toJson(simpleResponse);
                    printLog(json);
                }
                return setReturn(simpleResponse.code,simpleResponse.msg, (T) simpleResponse.toApiBean(),true);
            } else {
                // 泛型格式如下： new JsonCallback<LzyResponse<内层JavaBean>>(this)
                ApiBean apiBean;
                try {
                    apiBean = GsonConvertUtil.fromJson(jsonReader, type);
                }catch (Exception e){
                    if (AppUtils.isAppDebug()){
                        String json = GsonConvertUtil.fromJson(jsonReader,String.class);
                        printRealRequset(json);
                    }
                    throw e;
                }finally {
                    response.close();
                }

                if (AppUtils.isAppDebug()) {
                    String json = GsonConvertUtil.toJson(apiBean);
                    printLog(json);
                }
                return setReturn(apiBean.code,apiBean.msg, (T) apiBean,true);
            }
        }
    }

    private void printLog(String json) {
        if (!AppUtils.isAppDebug()) return;
        AppLogUtil.setNetResultLog(netOption.getUrl(), netOption.getParams(), json);
    }

    private void printRealRequset(String json){
        Logger.t("real-request").json(json);
    }

    private T setReturn(String code,String msg,T t,boolean isNormalApiBean) throws ApiException {
        boolean isContentErrorCode = ReturnCodeConstants.isContainsDealCode(netOption,code);
        boolean isContainsGlobalDealCode = ReturnCodeConstants.isContainsGlobalDealCode(netOption,code);
        if(!isNormalApiBean){
            //返回信息不是标准实体的code、msg，做不了code判断拦截
            return (T) t;
        }else if(isContainsGlobalDealCode){
            //需要全局处理的code先传递到错误回调，例如token过期，然后会在错误回调区分，
            throw new ApiException(msg, code);
        }else if(!netOption.isInterceptErrorCode()){
            //不拦截错误code
            return (T) t;
        } else if (isContentErrorCode) {
            /*有需要返回处理的code*/
            return (T) t;
        } else {
            //其余的错误code传递到错误回调
            throw new ApiException(msg, code);
        }
    }

}
