package com.jacotest.jacocotest.vo.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;

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
}
