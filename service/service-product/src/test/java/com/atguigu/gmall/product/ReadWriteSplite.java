package com.atguigu.gmall.product;


import com.atguigu.gmall.model.product.BaseTrademark;
import com.atguigu.gmall.product.mapper.BaseTrademarkMapper;
import org.apache.shardingsphere.infra.hint.HintManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ReadWriteSplite {

   @Autowired
   BaseTrademarkMapper baseTrademarkMapper;


    @Test
    public void test() {
        BaseTrademark baseTrademark = baseTrademarkMapper.selectById(4L);

        baseTrademark.setTmName("小米");
        baseTrademarkMapper.updateById(baseTrademark);


        HintManager.getInstance().setWriteRouteOnly();  //下次查询强制走主库
        BaseTrademark baseTrademark1= baseTrademarkMapper.selectById(4L);
        System.out.println("baseTrademark1 = " + baseTrademark1);

    }


    @Test
    public void testrw() {
        BaseTrademark baseTrademark = baseTrademarkMapper.selectById(4L);
        System.out.println("baseTrademark = " + baseTrademark);

        BaseTrademark baseTrademark1= baseTrademarkMapper.selectById(4L);
        System.out.println("baseTrademark1 = " + baseTrademark1);

        BaseTrademark baseTrademark2 = baseTrademarkMapper.selectById(4L);
        System.out.println("baseTrademark2 = " + baseTrademark2);

        BaseTrademark baseTrademark3 = baseTrademarkMapper.selectById(4L);
        System.out.println("baseTrademark3 = " + baseTrademark3);


    }

}
