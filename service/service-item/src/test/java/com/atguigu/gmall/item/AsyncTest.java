package com.atguigu.gmall.item;

import java.util.concurrent.*;

public class AsyncTest {

    static ExecutorService executor = Executors.newFixedThreadPool(4);  //可以使用指定线程池

    /**
     * 多任务组合
     * @param args
     */
    public static void allof(String[] args) throws InterruptedException {
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
            System.out.println("aaaa");
        });

        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> {
            return 2;
        });

        CompletableFuture<Void> future3 = CompletableFuture.runAsync(() -> {
            System.out.println("bbb");
        });

        //三个任务全部完成之后执行下面
        CompletableFuture.allOf(future1,future2,future3)
                .thenRunAsync(()->{
                    System.out.println("全部完成");
                });

        Thread.sleep(1000L);
    }
    public static void thenXXX(String[] args) throws InterruptedException {
//        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
//            System.out.println(Thread.currentThread().getName() + "计算1+1");
//            return 2;
//        });
//        /*
//        * thenRun:   不接收结果也不返回值
//        * thenAccept:  接收结果 不返回值
//        * thenApply   接收结果 有返回值
//        * */
//        future.thenApply((t)->{
//            System.out.println(Thread.currentThread().getName()+"222");
//            return  3;
//        });
        CompletableFuture.supplyAsync(() -> {
            return 1;
        }).thenApply((t) -> {
            return t + 2;
        }).thenApply((t) -> {
            return t * 3;
        }).thenAccept((t) -> {
            System.out.println(t + "保存到数据库");
        }).whenComplete((t, u) -> {

            if (u != null) {
                //记录日志
            }
            System.out.println("执行结束，记录日志");
        });

    }
    /**
     * 启动一个异步任务
     * @param args
     * @throws Exception
     */

        public static void startAsync(String[]args) throws Exception {


        System.out.println(Thread.currentThread().getName()+":主线程开始");
        //启动异步任务 --默认线程池
        CompletableFuture.runAsync(()->{
            System.out.println(Thread.currentThread().getName()+": haha");
        });

        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "哈哈");
            return 2;
        },executor);
        Integer integer = future.get(); //阻塞等待
        Integer integer1 = future.get(1, TimeUnit.MINUTES);  //限时等待
        System.out.println("异常结果"+integer);
        System.out.println("异常结果1"+integer1);

    }
    public static void main1(String[] args) throws InterruptedException {
        //查基本信息(skuId): skuInfo(spuId、c3Id)
        //   -- 查分类(c3Id):
        //   -- 查销售属性(skuId,spuId)
        //   -- 查skuvaluejson(spuId)
        //查图片(skuId)
        //查实时价格(skuId)
        //  同一层级异步执行，父子层级等待。

        System.out.println(Thread.currentThread().getName()+"呵呵呵");

        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "哈哈哈");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        future.whenComplete(( t,  u)->{
            if (u!=null){
                System.out.println("有异常");
            }
            //回调
            System.out.println(Thread.currentThread().getName() +"6666");
        });
        System.out.println(Thread.currentThread().getName()+"啦啦啦");

        Thread.sleep(10000);
    }
}
