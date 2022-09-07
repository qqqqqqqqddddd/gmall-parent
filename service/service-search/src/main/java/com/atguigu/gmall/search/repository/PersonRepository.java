package com.atguigu.gmall.search.repository;

import com.atguigu.gmall.search.bean.Person;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository  extends ElasticsearchRepository<Person, Long> {


    //查询 address 在北京市的人
    List<Person> findAllByAddressLike(String 北京市);

    //查询 年龄小于等于 19的人

    List<Person> findAllByAgeLessThanEqual(Integer age);

    //查询 年龄 大于 18 且 在北京市的人
    List<Person> findAllByAgeGreaterThanAndAddressLike(Integer age,String beijing);


    //查询 年龄 大于 18 且 在北京市的人  或  id=3的人
    List<Person> findAllByAgeGreaterThanAndAddressLikeOrIdEquals(Integer age,String beijing,Long id);

    @Query("{\n" +
            "    \"match\": {\n" +
            "      \"address\": \"?0\"\n" +
            "    }\n" +
            "  }\n" +
            "}")
    List<Person> bbb(String 北京市);
}
