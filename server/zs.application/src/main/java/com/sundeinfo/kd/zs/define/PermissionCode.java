package com.sundeinfo.kd.zs.define;

import com.sundeinfo.kd.zs.utility.StringUtils;
import com.sundeinfo.kd.zs.utility.UserHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 权限标识定义
 */
public class PermissionCode {
    public static final String SCHOOL_RECRUIT_ADD = "school:recruit:counting:add";

    public static final String SCHOOL_RECRUIT_VIEW = "school:recruit:counting:view";

    public static final String SCHOOL_RECRUIT_VIEW_ALL = "school:recruit:counting:viewall";

}
