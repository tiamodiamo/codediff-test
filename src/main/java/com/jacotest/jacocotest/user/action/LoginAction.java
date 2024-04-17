package com.jacotest.jacocotest.user.action;

import com.jacotest.jacocotest.threads.SubThread;
import com.jacotest.jacocotest.user.bean.UserLoginParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
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
            log.info("success:sum = " + sum + " addClass is " + new AddAction("running").toString());
            return "登录成功";
        }else {

            log.info("用户登录失败");
            SubThread subThread = new SubThread();
            new Thread(subThread).run();
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


    public String getMessage() {

        long timestamp = System.currentTimeMillis(); // 毫秒级时间戳

        Instant instant = Instant.ofEpochMilli(timestamp);
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = dateTime.format(formatter);

        return formattedDateTime;
    }


}
