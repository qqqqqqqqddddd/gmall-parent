package com.atguigu.gmall.user.api;

import com.atguigu.gmall.common.auth.AuthUtils;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.user.UserAddress;
import com.atguigu.gmall.model.vo.user.UserAuthInfo;
import com.atguigu.gmall.user.service.UserAddressService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/inner/rpc/user")
public class UserApiController {


    @Autowired
    UserAddressService userAddressService;

    /**
     * 获取所有用户的收货地址集合
     * @return
     */
    @GetMapping("/address/list")
    public Result<List<UserAddress>> getUserAddressList(){
        UserAuthInfo authInfo = AuthUtils.getCurrentAuthInfo();
        Long userId = authInfo.getUserId();
        LambdaQueryWrapper<UserAddress> wrapper =new LambdaQueryWrapper<>();
        wrapper.eq(UserAddress::getUserId,userId);

        List<UserAddress> userAddresses = userAddressService.list(wrapper);


        return  Result.ok(userAddresses);
    }
}
