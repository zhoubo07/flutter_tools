package com.dengyun.baselibrary.utils.ocr;

import java.util.List;

/**
 * @Title 身份证识别的正面返回实体
 * @Author: zhoubo
 * @CreateDate: 2020-03-12 09:19
 */
public class IdCardFaceBean {

    /**
     * address : 浙江省杭州市余杭区文一西路969号
     * config_str : {\"side\":\"face\"}
     * face_rect : {"angle":-90,"center":{"x":952,"y":325.5},"size":{"height":181.99,"width":164.99}}
     * card_region : [{"x":165,"y":657},{"x":534,"y":658},{"x":535,"y":31},{"x":165,"y":30}]
     * face_rect_vertices : [{"x":1024.6600341796875,"y":336.629638671875},{"x":906.6610717773438,"y":336.14801025390625},{"x":907.1590576171875,"y":214.1490478515625},{"x":1025.157958984375,"y":214.63067626953125}]
     * name : 张三
     * nationality : 汉
     * num : 1234567890
     * sex : 男
     * birth : 20000101
     * success : true
     */


    private String address; //#地址信息
    private String config_str;  //#配置信息，同输入configure
    private FaceRectBean face_rect;//#人脸位置
    private String name;    //#姓名
    private String nationality; //#民族
    private String num;     //#身份证号
    private String sex;     //#性别
    private String birth;   //#出生日期
    private boolean success; //#识别结果，true表示成功，false表示失败
    private List<CardRegionBean> card_region;  //#身份证区域位置，四个顶点表示，顺序是逆时针(左上、左下、右下、右上)
    private List<FaceRectVerticesBean> face_rect_vertices;//#人脸位置，四个顶点表示

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getConfig_str() {
        return config_str;
    }

    public void setConfig_str(String config_str) {
        this.config_str = config_str;
    }

    public FaceRectBean getFace_rect() {
        return face_rect;
    }

    public void setFace_rect(FaceRectBean face_rect) {
        this.face_rect = face_rect;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<CardRegionBean> getCard_region() {
        return card_region;
    }

    public void setCard_region(List<CardRegionBean> card_region) {
        this.card_region = card_region;
    }

    public List<FaceRectVerticesBean> getFace_rect_vertices() {
        return face_rect_vertices;
    }

    public void setFace_rect_vertices(List<FaceRectVerticesBean> face_rect_vertices) {
        this.face_rect_vertices = face_rect_vertices;
    }

    public static class FaceRectBean {
        /**
         * angle : -90
         * center : {"x":952,"y":325.5}
         * size : {"height":181.99,"width":164.99}
         */

        private double angle;   //#angle表示矩形顺时针旋转的度数
        private CenterBean center;  //#center表示人脸矩形中心坐标
        private SizeBean size;  //#size表示人脸矩形长宽

        public double getAngle() {
            return angle;
        }

        public void setAngle(double angle) {
            this.angle = angle;
        }

        public CenterBean getCenter() {
            return center;
        }

        public void setCenter(CenterBean center) {
            this.center = center;
        }

        public SizeBean getSize() {
            return size;
        }

        public void setSize(SizeBean size) {
            this.size = size;
        }

        public static class CenterBean {
            /**
             * x : 952
             * y : 325.5
             */

            private double x;
            private double y;

            public double getX() {
                return x;
            }

            public void setX(double x) {
                this.x = x;
            }

            public double getY() {
                return y;
            }

            public void setY(double y) {
                this.y = y;
            }
        }

        public static class SizeBean {
            /**
             * height : 181.99
             * width : 164.99
             */

            private double height;
            private double width;

            public double getHeight() {
                return height;
            }

            public void setHeight(double height) {
                this.height = height;
            }

            public double getWidth() {
                return width;
            }

            public void setWidth(double width) {
                this.width = width;
            }
        }
    }

    public static class CardRegionBean {
        /**
         * x : 165
         * y : 657
         */

        private double x;
        private double y;

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }
    }

    public static class FaceRectVerticesBean {
        /**
         * x : 1024.6600341796875
         * y : 336.629638671875
         */

        private double x;
        private double y;

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }
    }
}
