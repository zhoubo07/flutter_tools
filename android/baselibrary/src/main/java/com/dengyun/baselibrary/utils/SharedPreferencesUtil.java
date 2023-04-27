package com.dengyun.baselibrary.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @titile
 * @desc Created by seven on 2018/4/2.
 */

public class SharedPreferencesUtil {

    /**
     * sp保存值
     * @param context
     * @param fileName  保存的文件名称
     * @param key       保存的key
     * @param data      值
     */
    public static void saveData(Context context, String fileName, String key, Object data) {
        if (null != data) {
            String type = data.getClass().getSimpleName();
            SharedPreferences sharedPreferences = context
                    .getSharedPreferences(fileName, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            if ("Integer".equals(type)) {
                editor.putInt(key, (Integer) data);
            } else if ("Boolean".equals(type)) {
                editor.putBoolean(key, (Boolean) data);
            } else if ("String".equals(type)) {
                editor.putString(key, (String) data);
            } else if ("Float".equals(type)) {
                editor.putFloat(key, (Float) data);
            } else if ("Long".equals(type)) {
                editor.putLong(key, (Long) data);
            } else if ("List".equals(type)) {
                List list = (List) data;
                if (null == list || list.size() == 0) {
                    editor.putString(key, "");
                } else {
                    String value = GsonConvertUtil.toJson(list);
                    editor.putString(key, value);
                }
            }
            editor.apply();
        }
    }

    /**
     * 从sp获得保存的值的方法
     * @param context
     * @param fileName  sp文件名称
     * @param key   保存的key
     * @param defValue  取值的默认值
     * @param <T>
     * @return
     */
    public static <T extends Object> T getData(Context context, String fileName, String key, Object defValue) {

        String type = defValue.getClass().getSimpleName();
        SharedPreferences sharedPreferences = context.getSharedPreferences
                (fileName, Context.MODE_PRIVATE);

        //defValue为为默认值，如果当前获取不到数据就返回它
        if ("Integer".equals(type)) {
            return (T) (Integer) sharedPreferences.getInt(key, (Integer) defValue);
        } else if ("Boolean".equals(type)) {
            return (T) (Boolean) sharedPreferences.getBoolean(key, (Boolean) defValue);
        } else if ("String".equals(type)) {
            return (T) sharedPreferences.getString(key, (String) defValue);
        } else if ("Float".equals(type)) {
            return (T) (Float) sharedPreferences.getFloat(key, (Float) defValue);
        } else if ("Long".equals(type)) {
            return (T) (Long) sharedPreferences.getLong(key, (Long) defValue);
        }
        throw new RuntimeException("get SharedPrefrences error!!");
    }

    /**
     * sp保存实体类
     * @param context
     * @param bean  实体类
     * @param beanName  保存的文件名称
     * @param <T>
     */
    public static <T> void saveDataBean(Context context,String beanName, T bean) {
        SharedPreferences preferences = context.getSharedPreferences(beanName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Class c = bean.getClass();
        Field[] fields = c.getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);//反射时访问私有变量
                String key = field.getName();//获得类中属性key
                Object value = field.get(bean);//获得类中属性值
                String valueType = field.getType().getSimpleName();//获得属性值的类型
                if (!TextUtils.isEmpty(key)&&null != value&&!TextUtils.isEmpty(valueType)) {
                    if("String".equals(valueType)){
                        if(null==value){
                            editor.putString(key, "");
                        }else{
                            editor.putString(key, value.toString());
                        }
                    }else if("int".equals(valueType)){
                        editor.putInt(key, (int) value);
                    }else if("long".equals(valueType)){
                        editor.putLong(key, (long) value);
                    }else if("float".equals(valueType)){
                        editor.putFloat(key, (float) value);
                    }else if("double".equals(valueType)){
                        editor.putString(key, value.toString()+"");
                    }else if("boolean".equals(valueType)){
                        editor.putBoolean(key, (boolean) value);
                    }else if("List".equals(valueType)){
                        List list = (List) value;
                        if (null == list || list.size() == 0) {
                            editor.putString(field.getName(), "");
                        } else {
                            String valueJson = GsonConvertUtil.toJson(list);
                            editor.putString(key, valueJson);
                        }
                    }
                    editor.apply();
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    /**
     * sp删除指定key
     * @param context
     * @param fileName  文件名
     * @param keys  指定key，可以多个
     */
    public static void removeData(Context context, String fileName, String... keys) {
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (String key : keys) {
            editor.remove(key);
        }
        editor.apply();
    }

    /**
     * sp 清空文件
     * @param context
     * @param fileName  文件名
     */
    public static void removeAll(Context context, String fileName) {
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

}
