package com.dengyun.baselibrary.net.upload;

/**
 * Created by seven on 2016/5/9.
 */
public class UploadBean {

    public int code;
    public String message;
    public Data data;

    public class Data{
        public String fileName;
        public String fileUrl;
        public String originalFileName;
    }

}
