package com.dengyun.baselibrary.utils.ocr;

/**
 * @Title 天网数据 ocr银行卡的实体
 * 地址：https://market.aliyun.com/products/?spm=5176.11065268.1996646101.searchclickresult.3e352af5zpp7k1&category=57126001&code=cmapi033791#sku=yuncode2779100001
 * @Author: zhoubo
 * @CreateDate: 2020-03-12 09:58
 */
public class BankCardBean {

    /**
     * msg :
     * success : true
     * code : 200
     * data : {"order_no":"57462827766736****","exif_orientation":null,"image_id":"8cb18aecc9bf4c8683f2c2d13119****","card_number":"622700251068005****","bank_name":"中国建设银行","bank_identification_number":"1050000","card_name":"龙卡储蓄卡(银联卡)","card_type":"借记卡"}
     */

    private String msg;
    private boolean success;
    private int code;
    private DataBean data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * order_no : 57462827766736****
         * exif_orientation : null
         * image_id : 8cb18aecc9bf4c8683f2c2d13119****
         * card_number : 622700251068005****
         * bank_name : 中国建设银行
         * bank_identification_number : 1050000
         * card_name : 龙卡储蓄卡(银联卡)
         * card_type : 借记卡
         */

        private String order_no;        //订单号
        private Object exif_orientation;//exif信息，暂时用不到
        private String image_id;        //图片上传云端后的id
        private String card_number;     //银行卡号
        private String bank_name;       //发卡行名称
        private String bank_identification_number;//发卡行标识代码
        private String card_name;       //卡片名称
        private String card_type;       //卡片类型

        public String getOrder_no() {
            return order_no;
        }

        public void setOrder_no(String order_no) {
            this.order_no = order_no;
        }

        public Object getExif_orientation() {
            return exif_orientation;
        }

        public void setExif_orientation(Object exif_orientation) {
            this.exif_orientation = exif_orientation;
        }

        public String getImage_id() {
            return image_id;
        }

        public void setImage_id(String image_id) {
            this.image_id = image_id;
        }

        public String getCard_number() {
            return card_number;
        }

        public void setCard_number(String card_number) {
            this.card_number = card_number;
        }

        public String getBank_name() {
            return bank_name;
        }

        public void setBank_name(String bank_name) {
            this.bank_name = bank_name;
        }

        public String getBank_identification_number() {
            return bank_identification_number;
        }

        public void setBank_identification_number(String bank_identification_number) {
            this.bank_identification_number = bank_identification_number;
        }

        public String getCard_name() {
            return card_name;
        }

        public void setCard_name(String card_name) {
            this.card_name = card_name;
        }

        public String getCard_type() {
            return card_type;
        }

        public void setCard_type(String card_type) {
            this.card_type = card_type;
        }
    }
}
