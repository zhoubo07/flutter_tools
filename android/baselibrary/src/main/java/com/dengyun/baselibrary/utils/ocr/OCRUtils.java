package com.dengyun.baselibrary.utils.ocr;


import android.content.Context;
import android.util.Base64;

import com.dengyun.baselibrary.net.NetApi;
import com.dengyun.baselibrary.net.NetOption;
import com.dengyun.baselibrary.net.callback.JsonCallback;
import com.dengyun.baselibrary.net.constants.ProjectType;
import com.dengyun.baselibrary.net.constants.RequestMethod;
import com.dengyun.baselibrary.utils.ToastUtils;
import com.dengyun.baselibrary.utils.activity.ActivityUtils;
import com.lzy.okgo.model.Response;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


/**
 * @Title 阿里ocr识别工具类
 * @Author: zhoubo
 * @CreateDate: 2020年03月12日09:15:50
 */
public class OCRUtils {

    private static String appcode_bank = "438c8533687a4cae879fceb9379ec283";//星乐购--银行卡的appcode
    private static String appcode_id = "c88ac3ef5117409e860c7958e72f376c";//山森兰泰--身份证的appcode
    //private static String appcode_bank = "c88ac3ef5117409e860c7958e72f376c";//山森兰泰
    //private static String appcode_id = "524fb872670d4a508fb6b446d8ecf064";//妃子校

    /**
     * 识别身份证(正面)
     *
     * @param isLocal            是否是本地图片
     * @param imagePath          图片路径（本地就是本地路径，远程就是url）
     * @param onIdCardFaceResult
     */
    public static void recoIdCardFace(Context context,boolean isLocal, String imagePath, OnIdCardFaceResult onIdCardFaceResult) {
//        String url = "http://dm-51.data.aliyun.com/rest/160601/ocr/ocr_idcard.json";
        String url = "https://dm-51.data.aliyun.com/rest/160601/ocr/ocr_idcard.json";
        NetOption netOption = NetOption.newBuilder(url)
                .activity(ActivityUtils.getFragmentActivity(context))
                .headers("Authorization", "APPCODE " + appcode_id) //你自己的AppCode
                .params("configure", "{\"side\":\"face\"}") // 身份证正反面类型:face/back
                .params("image", isLocal ? conventImageBase64(imagePath) : imagePath) // 图片二进制数据的base64编码/图片url
                .clazz(IdCardFaceBean.class)
                .projectType(ProjectType.NONE)
                .build();
        NetApi.<IdCardFaceBean>getData(netOption, new JsonCallback<IdCardFaceBean>(netOption) {
            @Override
            public void onSuccess(Response<IdCardFaceBean> response) {
                IdCardFaceBean idCardFaceBean = response.body();
                if (idCardFaceBean.isSuccess()) {
                    onIdCardFaceResult.onResult(idCardFaceBean);
                } else {
                    ToastUtils.showShort("识别失败");
                }
            }

            @Override
            public void handleError(Response<IdCardFaceBean> response) {
                ToastUtils.showShort("识别失败");
            }
        });
    }

    /**
     * 识别身份证(反面)
     *
     * @param isLocal   是否是本地图片
     * @param imagePath 图片路径（本地就是本地路径，远程就是url）
     */
    public static void recoIdCardBack(Context context,boolean isLocal, String imagePath, OnIdCardBackResult onIdCardBackResult) {
//        String url = "http://dm-51.data.aliyun.com/rest/160601/ocr/ocr_idcard.json";
        String url = "https://dm-51.data.aliyun.com/rest/160601/ocr/ocr_idcard.json";
        NetOption netOption = NetOption.newBuilder(url)
                .activity(ActivityUtils.getFragmentActivity(context))
                .headers("Authorization", "APPCODE " + appcode_id) //你自己的AppCode
                .params("configure", "{\"side\":\"back\"}") // 身份证正反面类型:face/back
                .params("image", isLocal ? conventImageBase64(imagePath) : imagePath) // 图片二进制数据的base64编码/图片url
                .clazz(IdCardBackBean.class)
                .projectType(ProjectType.NONE)
                .build();
        NetApi.<IdCardBackBean>getData(netOption, new JsonCallback<IdCardBackBean>(netOption) {
            @Override
            public void onSuccess(Response<IdCardBackBean> response) {
                IdCardBackBean idCardBackBean = response.body();
                if (idCardBackBean.isSuccess()) {
                    onIdCardBackResult.onResult(idCardBackBean);
                } else {
                    ToastUtils.showShort("识别失败");
                }
            }

            @Override
            public void handleError(Response<IdCardBackBean> response) {
                ToastUtils.showShort("识别失败");
            }
        });
    }

    /*public static void recoIdCard(boolean isLocal, String imagePath){
        String url = "https://idcardocrc.shumaidata.com/getidcardocrc";
        NetOption.Builder netBuilder = NetOption.newBuilder(url)
                .headers("Authorization", "APPCODE " + appcode); //你自己的AppCode
        if (isLocal) {
            netBuilder.params("image", conventImageBase64(imagePath));// 图片二进制数据的base64编码
        } else {
            netBuilder.params("url", imagePath);//图片url
        }
        NetOption netOption = netBuilder.projectType(ProjectType.NONE).build();
        NetApi.getString(RequestMethod.POST_FORM,netOption, new JsonCallback<String>(netOption) {
            @Override
            public void onSuccess(Response<String> response) {

            }
        });
    }*/


    /**
     * 识别银行卡（天眼数据）
     *
     * @param isLocal   是否是本地图片
     * @param imagePath 图片路径（本地就是本地路径，远程就是url）
     */
    public static void recoBankCard(Context context,boolean isLocal, String imagePath, OnBankCardResult onBankCardResult) {
        String url = "http://bankocrb.shumaidata.com/getbankocrb";
        NetOption.Builder netBuilder = NetOption.newBuilder(url)
                .activity(ActivityUtils.getFragmentActivity(context))
                .headers("Authorization", "APPCODE " + appcode_bank); //你自己的AppCode
        if (isLocal) {
            netBuilder.params("image", conventImageBase64(imagePath));// 图片二进制数据的base64编码
        } else {
            netBuilder.params("url", imagePath);//图片url
        }
        NetOption netOption = netBuilder.clazz(BankCardBean.class)
                .projectType(ProjectType.NONE)
                .requestMethod(RequestMethod.POST_FORM)
                .build();
        NetApi.<BankCardBean>getData(netOption, new JsonCallback<BankCardBean>(netOption) {
            @Override
            public void onSuccess(Response<BankCardBean> response) {
                /*200	成功	成功
                400	参数错误	参数错误
                404	请求资源不存在	请求资源不存在
                500	系统内部错误，请联系服务商	系统内部错误，请联系服务商
                501	第三方服务异常	第三方服务异常
                601	服务商未开通接口权限
                602	账号停用
                603	余额不足请充值
                604	接口停用	接口停用
                1001	服务异常，会返回具体的错误原因	服务异常，会返回具体的错误原因*/
                BankCardBean bankCardBean = response.body();
                if (bankCardBean.isSuccess()) {
                    onBankCardResult.onResult(bankCardBean);
                } else {
                    ToastUtils.showShort("识别失败");
                }
            }

            @Override
            public void handleError(Response<BankCardBean> response) {
                ToastUtils.showShort("识别失败");
            }
        });
    }

    /**
     * 识别银行卡(阿里云计算)
     *
     * @param isLocal   是否是本地图片
     * @param imagePath 图片路径（本地就是本地路径，远程就是url）
     */
    public static void recoBankCard2(Context context,boolean isLocal, String imagePath, OnBankCardResult2 onBankCardResult) {
        String url = "http://yhk.market.alicloudapi.com/rest/160601/ocr/ocr_bank_card.json";
        NetOption netOption = NetOption.newBuilder(url)
                .activity(ActivityUtils.getFragmentActivity(context))
                .headers("Authorization", "APPCODE " + appcode_bank) //你自己的AppCode
                .params("image", isLocal ? conventImageBase64(imagePath) : imagePath) // 图片二进制数据的base64编码/图片url
                .clazz(BankCardBean2.class)
                .projectType(ProjectType.NONE)
                .build();
        NetApi.<BankCardBean2>getData(netOption, new JsonCallback<BankCardBean2>(netOption) {
            @Override
            public void onSuccess(Response<BankCardBean2> response) {
                BankCardBean2 bankCardBean = response.body();
                if (bankCardBean.isSuccess()) {
                    onBankCardResult.onResult(bankCardBean);
                } else {
                    ToastUtils.showShort("识别失败");
                }
            }

            @Override
            public void handleError(Response<BankCardBean2> response) {
                ToastUtils.showShort("识别失败");
            }
        });
    }


    // 对图像进行base64编码
    private static String conventImageBase64(String localImagePath) {
        String imgBase64Str = "";
        try {
            File file = new File(localImagePath);
            byte[] content = new byte[(int) file.length()];
            FileInputStream finputstream = new FileInputStream(file);
            finputstream.read(content);
            finputstream.close();
            try {
                imgBase64Str = Base64.encodeToString(content, Base64.DEFAULT);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return imgBase64Str;
    }

    public interface OnIdCardFaceResult {
        void onResult(IdCardFaceBean idCardFaceBean);
    }

    public interface OnIdCardBackResult {
        void onResult(IdCardBackBean idCardBackBean);
    }

    public interface OnBankCardResult {
        void onResult(BankCardBean bankCardBean);
    }

    public interface OnBankCardResult2 {
        void onResult(BankCardBean2 bankCardBean);
    }


}
