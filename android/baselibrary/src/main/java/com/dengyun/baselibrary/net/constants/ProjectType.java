package com.dengyun.baselibrary.net.constants;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @Title 项目类型枚举
 * @Author: zhoubo
 * @CreateDate: 2019-05-15 16:58
 */
@IntDef({ProjectType.NONE,ProjectType.DEFAULT})
@Retention(RetentionPolicy.SOURCE)
public @interface ProjectType {
    int NONE = -1;
    // 默认项目
    int DEFAULT = 0;
}
