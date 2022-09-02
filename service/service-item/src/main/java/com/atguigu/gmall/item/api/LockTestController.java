package com.atguigu.gmall.item.api;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.item.lock.RedisDistLock;
import org.redisson.api.RCountDownLatch;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@RestController
@RequestMapping("/lock")
public class LockTestController {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    RedisDistLock redisDistLock;

    @Autowired
    RedissonClient redissonClient;


    ReentrantLock lock =new ReentrantLock();
    int  i = 0;


    /**
     * 读写锁
     *  写数据：
     */
    @GetMapping("/rw/write")
    public Result readWriteValue() throws InterruptedException {
        //1、拿到读写锁
        RReadWriteLock lock = redissonClient.getReadWriteLock("rw-lock");

        //2、获取到写锁
        RLock rLock = lock.writeLock();

        //加写锁
        rLock.lock();
        //业务正在改数据
        Thread.sleep(20000);
        i = 888;

        rLock.unlock();

        return Result.ok();
    }

    /**
     * 闭锁
     */
    @GetMapping("/longzhu")
    public Result shoujilongzhu(){

        RCountDownLatch latch = redissonClient.getCountDownLatch("sl-lock");
        latch.countDown();
        return Result.ok("收集到1颗");
    }

    /**
     * 召唤神龙
     */
    @GetMapping("/shenlong")
    public Result shenlong() throws InterruptedException {
        RCountDownLatch latch = redissonClient.getCountDownLatch("sl-lock");
        latch.trySetCount(7); //设置数量


        latch.await(); //等待
        return Result.ok("一条龙 来了....");
    }

    /**
     * 读写锁
     * 读数据：
     */
    @GetMapping("/rw/read")
    public Result readValue(){

        //1、拿到读写锁
        RReadWriteLock lock = redissonClient.getReadWriteLock("rw-lock");

        RLock rLock = lock.readLock();
        rLock.lock();
        int x = i;
        rLock.unlock();
        return Result.ok(x);
    }

   @GetMapping("/common")
   public  Result redissonLock() throws InterruptedException {
       RLock lock = redissonClient.getLock("lock-hello");
       //加锁
       lock.lock();
       lock.lock(10, TimeUnit.MINUTES);
       lock.tryLock(); //立即抢锁 ,一下不等
       lock.tryLock(10,TimeUnit.SECONDS); //最多等10秒
       lock.tryLock(10,20,TimeUnit.SECONDS);
       //执行业务
       Thread.sleep(5000);
       System.out.println("执行结束");
       //解锁
       lock.unlock();
       return Result.ok();
   }



    @GetMapping("/incr")
    public Result increment(){
        //没加锁 1w-478
        //单机锁 : 1w -1w

        //lock.lock();
        String token = redisDistLock.lock();  //分布式锁
        String a = redisTemplate.opsForValue().get("a");
        int i = Integer.parseInt(a);

        i++;

        redisTemplate.opsForValue().set("a",i+"");
        //lock.unlock();
        redisDistLock.unlock(token);
        return  Result.ok();
    }


}
