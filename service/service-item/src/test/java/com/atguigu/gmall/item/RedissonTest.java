package com.atguigu.gmall.item;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RedissonTest {

    @Autowired
    RedissonClient redissonClient;


    @Test
    void test() {
        System.out.println(redissonClient);
    }


}
