package com.atguigu.gmall.search;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;


@EnableElasticsearchRepositories  //开启es的自动仓库功能  写bean 写接口 自动创建索引库
@SpringCloudApplication
public class SearchMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(SearchMainApplication.class,args);
    }
}
