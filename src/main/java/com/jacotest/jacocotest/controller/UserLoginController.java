package com.jacotest.jacocotest.controller;

import com.jacotest.jacocotest.common.response.ApiResponse;

import com.jacotest.jacocotest.mapper.OrikaMapperUtils;
import com.jacotest.jacocotest.user.action.LoginAction;
import com.jacotest.jacocotest.user.bean.UserLoginParam;
import com.jacotest.jacocotest.vo.param.UserLoginParamVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(value = "/api", tags = "差异代码模块")
@RequestMapping("/api")
@Validated
public class UserLoginController {
    @Autowired
    private LoginAction loginAction;
    @ApiOperation("用户登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ApiResponse<String> getGitListToFile(
            @ApiParam(required = true, name = "用户登录")
            @RequestBody UserLoginParamVO userLoginParamVo) {
            UserLoginParam userLoginParam = OrikaMapperUtils.map(userLoginParamVo,UserLoginParam.class);
            String result = loginAction.userlogin(userLoginParam);

        return new ApiResponse<String>().success(result);
    }



}
