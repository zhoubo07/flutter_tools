package com.dengyun.baselibrary.utils.ocr;

/**
 * @Title 阿里云的银行卡识别
 * @Desc: 地址 https://market.aliyun.com/products/57124001/cmapi016870.html?spm=5176.730005.productlist.d_cmapi016870.59383524CAnLqZ&innerSource=search#sku=yuncode1087000000
 * @Author: zhoubo
 * @CreateDate: 2020-05-12 11:14
 */
public class BankCardBean2 {

    /**
     * bank_name : 中国邮政储蓄银行
     * card_num : 6217991000012564238
     * card_type :
     * inst_id :
     * request_id : 20200512133738_1297cbd63e16994a5ea3025d6fe88e35
     * success : true
     * valid_date :
     */

    private String bank_name;
    private String card_num;
    private String card_type;
    private String inst_id;
    private String request_id;
    private boolean success;
    private String valid_date;

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getCard_num() {
        return card_num;
    }

    public void setCard_num(String card_num) {
        this.card_num = card_num;
    }

    public String getCard_type() {
        return card_type;
    }

    public void setCard_type(String card_type) {
        this.card_type = card_type;
    }

    public String getInst_id() {
        return inst_id;
    }

    public void setInst_id(String inst_id) {
        this.inst_id = inst_id;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getValid_date() {
        return valid_date;
    }

    public void setValid_date(String valid_date) {
        this.valid_date = valid_date;
    }
}
