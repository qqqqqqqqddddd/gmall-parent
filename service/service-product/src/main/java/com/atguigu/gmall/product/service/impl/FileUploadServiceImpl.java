package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.common.util.DateUtil;
import com.atguigu.gmall.product.config.minio.MinioProperties;
import com.atguigu.gmall.product.service.FileUploadService;
import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Autowired
    MinioClient minioClient;

    @Autowired
    MinioProperties minioProperties;

    @Override
    public String upload(MultipartFile file) throws  Exception{


        // 使用putObject上传一个文件到存储桶中。
        String name = file.getName();
            //1.优化得到唯一文件名
        String dateStr = DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
        String filename = UUID.randomUUID().toString().replace("-","")+"_"+ file.getOriginalFilename();
        InputStream inputStream = file.getInputStream();
        String contentType = file.getContentType();
        PutObjectOptions options = new PutObjectOptions(file.getSize(), -1L);

        options.setContentType(contentType);
        minioClient.putObject(minioProperties.getBucketName(),
                dateStr+"/"+filename,
                inputStream,
                options);

         //返回url
        //2.优化 文件按时间归档
        String url = "http://192.168.200.100:9000/gmall/"+dateStr+"/"+filename;

        return url;
    }
}
