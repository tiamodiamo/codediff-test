package com.jacotest.jacocotest.user.action;

import com.jacotest.jacocotest.user.bean.UserLoginParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
@Slf4j
public class LoginAction {



    public  String userlogin(UserLoginParam userLoginParam){
        String username = userLoginParam.getUsername();
        String password = userLoginParam.getPassword();
        if (username.equals("admin")&&password.equals("123456")){
            log.info("用户登录成功");
            int sum = 0;
            for (int i = 0; i < 100; i++) {
                sum +=i;
            }
            log.info("success:sum = " + sum);
            return "登录成功";
        }else {

            log.info("用户登录失败");
            return "登录失败";
        }

    }

    public String doSomething(){
        int sum = 0;
        for (int i = 0; i < 100; i++) {
            sum +=i;
        }
        log.info("err:sum = " + sum);

        for (int i = 0; i < 100; i++) {
            sum +=i;
        }
        log.info("err:sum = " + sum);

        for (int i = 0; i < 100; i++) {
            sum +=i;
        }
        log.info("err:sum = " + sum);

        for (int i = 0; i < 100; i++) {
            sum +=i;
        }
        log.info("err:sum = " + sum);
        return "";
    }


}
