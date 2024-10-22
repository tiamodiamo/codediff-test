package com.jacotest.jacocotest.vo.param;

import com.google.common.annotations.VisibleForTesting;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.junit.Test;

@Data
@ApiModel
public class UserLoginParamVO {
    private String username;
    /**
     * 代理类目录列表
     */

    private String password;
    /**
     * 代理端口号
     */
    private boolean ispass;
}
