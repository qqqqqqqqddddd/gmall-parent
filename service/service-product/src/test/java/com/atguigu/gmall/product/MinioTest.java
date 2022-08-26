package com.atguigu.gmall.product;

import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import io.minio.errors.*;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

//@SpringBootTest   //测试springboot所有组件功能
public class MinioTest {



    @Test
    public void uploadFileTest() throws Exception{
        try {
            //error:The difference between the request time and the server's time is too large
            // 时间敏感,配置linux时间同步
            // 使用MinIO服务的URL，端口，Access key和Secret key创建一个MinioClient对象
            MinioClient minioClient = new MinioClient("http://192.168.200.100:9000",
                    "admin",
                    "admin123456");

            // 检查存储桶是否已经存在
            boolean isExist = minioClient.bucketExists("gmall");
            if(isExist) {
                System.out.println("Bucket already exists.");
            } else {
                // 桶不存在,创建一个
                minioClient.makeBucket("asiatrip");
            }

            // 使用putObject上传一个文件到存储桶中。
            FileInputStream inputStream = new FileInputStream("D:\\java第六阶段雷神\\尚品汇\\资料\\03 商品图片\\品牌\\pingguo.png");
            PutObjectOptions options = new PutObjectOptions(inputStream.available(), -1L);
            options.setContentType("image/png");
            minioClient.putObject("gmall","pingguo.png",inputStream,options);
            System.out.println("上传成功");
        }catch (MinioException e){
            System.out.println("发生错误"+e);
        }
    }

}
