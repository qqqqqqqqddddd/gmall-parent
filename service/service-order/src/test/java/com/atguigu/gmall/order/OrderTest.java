package com.atguigu.gmall.order;

import com.atguigu.gmall.model.order.OrderInfo;
import com.atguigu.gmall.order.mapper.OrderInfoMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
public class OrderTest {

    @Autowired
    OrderInfoMapper orderInfoMapper;

    @Test
    void orderTest() {
        OrderInfo orderInfo = orderInfoMapper.selectById(205L);
        System.out.println("orderInfo = " + orderInfo);
    }


    @Test
    void testSplit() {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setTotalAmount(new BigDecimal("1.1"));
        orderInfo.setUserId(1l);
        orderInfoMapper.insert(orderInfo);

        System.out.println("1号用户订单插入完成...去1库1表里找");

        OrderInfo orderInfo2 = new OrderInfo();
        orderInfo2.setTotalAmount(new BigDecimal("2.1"));
        orderInfo2.setUserId(2l);
        orderInfoMapper.insert(orderInfo2);

        System.out.println("2号用户订单插入完成...去0库2表里找");


    }

    @Test
    public void testQuery(){

        QueryWrapper<OrderInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",2L);
        List<OrderInfo> infos = orderInfoMapper.selectList(wrapper);
        for (OrderInfo info : infos) {
            System.out.println(info.getTotalAmount());
        }

        System.out.println("=================");

        List<OrderInfo> infos2 = orderInfoMapper.selectList(wrapper);
        for (OrderInfo info : infos2) {
            System.out.println(info.getTotalAmount());
        }
    }

    /**
     * 不带分片键的查询会全库全表都查询，然后结果归并
     */
    @Test
    public void testALl(){
        List<OrderInfo> infos = orderInfoMapper.selectList(null);
        for (OrderInfo info : infos) {
            System.out.println(info.getId()+"==>"+info.getTotalAmount()+"== user:"+info.getUserId());
        }
    }



}
