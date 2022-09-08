package com.atguigu.gmall.user.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.result.ResultCodeEnum;
import com.atguigu.gmall.model.user.UserInfo;
import com.atguigu.gmall.model.vo.user.LoginSuccessVo;
import com.atguigu.gmall.user.service.UserInfoService;
import io.swagger.annotations.ResponseHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/user")
public class UserController {


    @Autowired
    UserInfoService userInfoService;

    @PostMapping("/passport/login")
    public Result login(@RequestBody UserInfo info){

       LoginSuccessVo vo = userInfoService.login(info);
        if (vo!= null){
            return  Result.ok(vo);
        }
        ;
        Result<String> result = Result.build("", ResultCodeEnum.LOGIN_ERROR);

        return result;
    }

    @GetMapping("/passport/logout")
    public  Result logout(@RequestHeader("token") String  token ){

        userInfoService.logout(token);
        return Result.ok();
    }
}
