package com.dengyun.baselibrary.utils.ocr;

/**
 * @Title 身份证识别的背面识别
 * @Desc: 描述
 * @Author: zhoubo
 * @CreateDate: 2020-03-12 09:20
 */
public class IdCardBackBean {
    /**
     * config_str : {\"side\":\"back\"}
     * start_date : 19700101
     * end_date : 19800101
     * issue : 杭州市公安局
     * success : true
     */

    private String config_str;  //#配置信息，同输入configure
    private String start_date;  //#有效期起始时间
    private String end_date;    //#有效期结束时间
    private String issue;       //#签发机关
    private boolean success;    //#识别结果，true表示成功，false表示失败

    public String getConfig_str() {
        return config_str;
    }

    public void setConfig_str(String config_str) {
        this.config_str = config_str;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
