package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.product.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件上传
 */
@RestController
@RequestMapping("/admin/product")
public class FileUploadController {

    @Autowired
    FileUploadService fileUploadService;

    /**
     * @RequestParam("file")MultipartFile file 接文件
     * @param file
     * @return
     */
    @PostMapping("/fileUpload")
    public Result fileUpload(@RequestPart("file")MultipartFile file) throws Exception {
        //收到前端的文件流,上传到Minio返回存储地址
        String url=fileUploadService.upload(file);

        return  Result.ok(url);
    }

    @PostMapping("/reg")
    public Result haha(@RequestParam("username")String username,
                       @RequestParam("password")String password,
                       @RequestParam("email")String email,
                       @RequestPart("header")MultipartFile[] header,
                       @RequestPart("sfz")MultipartFile sfz,
                       @RequestPart("shz")MultipartFile shz,
                       @RequestParam("ah")String[] ah,
                       @RequestHeader("Cache-Control") String cache
                       ){
        //1、用户名，密码，邮箱
        Map<String,Object> result = new HashMap<>();
        result.put("用户名：",username);
        result.put("密码：",password);
        result.put("邮箱：",email);

        result.put("头像文件大小？",header.length);
        result.put("生活照文件大小？",sfz.getSize());
        result.put("身份证文件大小？",shz.getSize());
        result.put("爱好", Arrays.asList(ah));
        result.put("cache",cache);

        return  Result.ok(result);
    }

}
