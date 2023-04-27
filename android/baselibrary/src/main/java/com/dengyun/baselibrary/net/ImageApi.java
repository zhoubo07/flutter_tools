package com.dengyun.baselibrary.net;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.dengyun.baselibrary.R;

/**
 * @titile  加载图片的工具类
 *  封装了一些常用的api方法，调用时尽量使用此工具类调用，方便管理
 * @desc Created by seven on 2018/6/19.
 */

public class ImageApi {

    /**
     * 加载本地资源图片
     * @param pic 本地资源id
     */
    public static void displayImage(Context context, ImageView imageview, int pic) {
        Glide.with(context).load(pic).into(imageview);
    }

    public static void displayImage(Activity context, ImageView imageview, int pic) {
        Glide.with(context).load(pic).into(imageview);
    }

    public static void displayImage(Fragment context, ImageView imageview, int pic) {
        Glide.with(context).load(pic).into(imageview);
    }

    /**
     * 加载图片（不添加占位图）
     */
    public static void displayImage(Context context, ImageView imageview, String url) {
        displayImage(context, imageview, url, -1, -1);
    }

    public static void displayImage(Activity context, ImageView imageview, String url) {
        displayImage(context, imageview, url, -1, -1);
    }

    public static void displayImage(Fragment context, ImageView imageview, String url) {
        displayImage(context, imageview, url, -1, -1);
    }

    /**
     * 加载图片
     */
    public static void displayImage(Context context, ImageView imageview, String url, int placeholderPic, int errorPic) {
        if(TextUtils.isEmpty(url)){
            if(placeholderPic>0){
                imageview.setImageResource(placeholderPic);
            }
            return;
        }
        RequestOptions options = new RequestOptions();
        if (placeholderPic > 0) options.placeholder(placeholderPic);
        if (errorPic > 0) options.error(errorPic);
        Glide.with(context).load(url).apply(options).into(imageview);
    }
    /**
     * 异步加载图片
     */
    public static void asynchronousDisplayImage(Context context, ImageView imageview, String url, int placeholderPic, int errorPic) {
        if(TextUtils.isEmpty(url)){
            if(placeholderPic>0){
                imageview.setImageResource(placeholderPic);
            }
            return;
        }
        imageview.setTag(R.id.imageloader_uri,url);
        RequestOptions options = new RequestOptions();
        if (placeholderPic > 0) options.placeholder(placeholderPic);
        if (errorPic > 0) options.error(errorPic);
        Glide.with(context).load(url).apply(options).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable drawable, Transition<? super Drawable> transition) {

                if (drawable != null) {
                    String tag = (String) imageview.getTag(R.id.imageloader_uri);//获取控件的Tag进行比较
                    if (TextUtils.equals(url, tag)) {
                        imageview.setImageDrawable(drawable);
                    }
                }
            }
        });
    }

    public static void displayImage(Activity context, ImageView imageview, String url, int placeholderPic, int errorPic) {
        if(TextUtils.isEmpty(url)){
            if(placeholderPic>0){
                imageview.setImageResource(placeholderPic);
            }
            return;
        }
        RequestOptions options = new RequestOptions();
        if (placeholderPic > 0) options.placeholder(placeholderPic);
        if (errorPic > 0) options.error(errorPic);
        Glide.with(context).load(url).apply(options).into(imageview);
    }

    public static void displayImage(Fragment context, ImageView imageview, String url, int placeholderPic, int errorPic) {
        if(TextUtils.isEmpty(url)){
            if(placeholderPic>0){
                imageview.setImageResource(placeholderPic);
            }
            return;
        }
        RequestOptions options = new RequestOptions();
        if (placeholderPic > 0) options.placeholder(placeholderPic);
        if (errorPic > 0) options.error(errorPic);
        Glide.with(context).load(url).apply(options).into(imageview);
    }

    public static void displayImage(Context context, ImageView imageview, Object pic,int placeholderPic, int errorPic){
        if (null==pic){
            if(placeholderPic>0) imageview.setImageResource(placeholderPic);
            return;
        }
        RequestOptions options = new RequestOptions();
        if (placeholderPic > 0) options.placeholder(placeholderPic);
        if (errorPic > 0) options.error(errorPic);
        Glide.with(context).load(pic).apply(options).into(imageview);
    }

    public static void displayImage(Activity context, ImageView imageview, Object pic,int placeholderPic, int errorPic){
        if (null==pic){
            if(placeholderPic>0) imageview.setImageResource(placeholderPic);
            return;
        }
        RequestOptions options = new RequestOptions();
        if (placeholderPic > 0) options.placeholder(placeholderPic);
        if (errorPic > 0) options.error(errorPic);
        Glide.with(context).load(pic).apply(options).into(imageview);
    }

    public static void displayImage(Fragment context, ImageView imageview, Object pic,int placeholderPic, int errorPic){
        if (null==pic){
            if(placeholderPic>0) imageview.setImageResource(placeholderPic);
            return;
        }
        RequestOptions options = new RequestOptions();
        if (placeholderPic > 0) options.placeholder(placeholderPic);
        if (errorPic > 0) options.error(errorPic);
        Glide.with(context).load(pic).apply(options).into(imageview);
    }

    /**
     * 加载图片（添加加载中占位图）
     */
    public static void displayWithPlacehold(Context context, ImageView imageview, String url, int placeholderPic) {
        displayImage(context, imageview, url, placeholderPic, -1);
    }

    public static void displayWithPlacehold(Activity context, ImageView imageview, String url, int placeholderPic) {
        displayImage(context, imageview, url, placeholderPic, -1);
    }

    public static void displayWithPlacehold(Fragment context, ImageView imageview, String url, int placeholderPic) {
        displayImage(context, imageview, url, placeholderPic, -1);
    }

    /**
     * 加载图片（添加加载失败占位图）
     */
    public static void displayWithError(Context context, ImageView imageview, String url, int errorPic) {
        displayImage(context, imageview, url, -1, errorPic);
    }

    public static void displayWithError(Activity context, ImageView imageview, String url, int errorPic) {
        displayImage(context, imageview, url, -1, errorPic);
    }

    public static void displayWithError(Fragment context, ImageView imageview, String url, int errorPic) {
        displayImage(context, imageview, url, -1, errorPic);
    }

    public static void displayWithOptions(Context context, ImageView imageview, String url,RequestOptions options){
        Glide.with(context).load(url).apply(options).into(imageview);
    }
    public static void displayWithOptions(Activity context, ImageView imageview, String url,RequestOptions options){
        Glide.with(context).load(url).apply(options).into(imageview);
    }
    public static void displayWithOptions(Fragment context, ImageView imageview, String url, RequestOptions options){
        Glide.with(context).load(url).apply(options).into(imageview);
    }
}
