package com.dengyun.baselibrary.net.upload;

import java.util.List;

/**
 * @Title 多文件上传实体类
 * @Author: zhoubo
 * @CreateDate: 2020-03-24 08:39
 */
public class UploadListBean {
    public int code;
    public boolean success;
    public String msg;
    public Data data;

    public class Data{
        public List<String> filePathList;
    }
}
