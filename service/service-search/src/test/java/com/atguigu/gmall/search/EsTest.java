package com.atguigu.gmall.search;

import com.atguigu.gmall.search.bean.Person;
import com.atguigu.gmall.search.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class EsTest {

    @Autowired
    PersonRepository personRepository;


    @Test
    void queryTest() {

//        Optional<Person> byId = personRepository.findById(1L);
//        System.out.println(byId);
        //1、查询 address 在北京市的人
         List<Person> beijing = personRepository.findAllByAddressLike("北京市");
//        for (Person person : beijing) {
//            System.out.println(person);
//        }
        //2、查询 年龄小于等于 19的人

        List<Person> lessThanEqual = personRepository.findAllByAgeLessThanEqual(19);
//        for (Person person : lessThanEqual) {
//            System.out.println(person);
//        }


        //3、查询 年龄 大于 18 且 在北京市的人
        List<Person> 北京市 = personRepository.findAllByAgeGreaterThanAndAddressLike(18, "北京市");


        //4、查询 年龄 大于 18 且 在北京市的人  或  id=3的人

        List<Person> 北京市1 = personRepository.findAllByAgeGreaterThanAndAddressLikeOrIdEquals(18, "北京市", 3L);
//        System.out.println("北京市1 = " + 北京市1);

        List<Person> aaaa= personRepository.bbb("北京市");
        System.out.println("aaaa = " + aaaa);


    }

    @Test
    void test() {

        Person person = new Person();
        person.setId(1L);
        person.setFirstName("李");
        person.setLastName("静");
        person.setAge(70);
        person.setAddress("北京市");

        personRepository.save(person);

        Person person1 = new Person();
        person1.setId(2L);
        person1.setFirstName("李");
        person1.setLastName("金");
        person1.setAge(15);
        person1.setAddress("西安市");

        personRepository.save(person1);

        Person person2 = new Person();
        person2.setId(3L);
        person2.setFirstName("李");
        person2.setLastName("工");
        person2.setAge(30);
        person2.setAddress("北京市");

        personRepository.save(person2);


        System.out.println("保存完成...");

    }


}
