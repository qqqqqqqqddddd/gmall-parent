package com.atguigu.gmall.feign.ware.callback;

import com.atguigu.gmall.feign.ware.WareFeignClient;
import org.springframework.stereotype.Component;

@Component
public class WareFeignClientCallback implements WareFeignClient {

    @Override
    public String hasStock(Long skuId, Integer num) {

        return  "1";
    }
}
