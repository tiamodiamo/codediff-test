package com.jacotest.jacocotest.controller;

import com.jacotest.jacocotest.common.response.ApiResponse;
import com.jacotest.jacocotest.controller.UserLoginController;
import com.jacotest.jacocotest.mapper.OrikaMapperUtils;
import com.jacotest.jacocotest.user.action.LoginAction;
import com.jacotest.jacocotest.user.bean.UserLoginParam;
import com.jacotest.jacocotest.vo.param.UserLoginParamVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;


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

        UserLoginParam userLoginParam = OrikaMapperUtils.map(userLoginParamVo, UserLoginParam.class);
        String result = loginAction.userlogin(userLoginParam);
        return new ApiResponse<String>().success(result);
    }


    @ApiOperation("查询")
    @RequestMapping(value = "/getMessage", method = RequestMethod.GET)

    public ApiResponse<String> getMessage() {



        String result = loginAction.getMessage();

        return new ApiResponse<String>().success(result);
    }


}
