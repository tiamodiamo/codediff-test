package com.jacotest.jacocotest.controller;

import com.jacotest.jacocotest.common.response.ApiResponse;

import com.jacotest.jacocotest.mapper.OrikaMapperUtils;
import com.jacotest.jacocotest.user.action.LoginAction;
import com.jacotest.jacocotest.user.bean.UserLoginParam;
import com.jacotest.jacocotest.vo.param.UserLoginParamVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.http.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


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

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        // 从请求头中获取需要的信息
        String headerValue = request.getHeader("caseid");
        System.out.println("headerValue = " + headerValue);
        UserLoginParam userLoginParam = OrikaMapperUtils.map(userLoginParamVo, UserLoginParam.class);
        String result = loginAction.userlogin(userLoginParam);

        return new ApiResponse<String>().success(result);
    }


    @ApiOperation("查询")
    @RequestMapping(value = "/getMessage", method = RequestMethod.GET)

    public ApiResponse<String> getMessage() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        // 从请求头中获取需要的信息
        String headerValue = request.getHeader("caseid");
        System.out.println("headerValue = " + headerValue);

        String result = loginAction.getMessage();

        return new ApiResponse<String>().success(result);
    }


}
