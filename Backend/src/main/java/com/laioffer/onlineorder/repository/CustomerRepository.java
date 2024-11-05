package com.laioffer.onlineorder.repository;

import com.laioffer.onlineorder.entity.CustomerEntity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;


import java.util.List;


public interface CustomerRepository extends ListCrudRepository<CustomerEntity, Long> {
    // 为什么用 JDBC
    // 规范Query 可以自定义Query action
    // SELECT * FROM customers WHERE first_name = : firstName;
    // 第一个 first_name -> Method name - > 详情见Spring JDBC doc  -> Repository Query Key Word
    List<CustomerEntity> findByFirstName(String firstName);
    List<CustomerEntity> findByLastName(String lastName);
    CustomerEntity findByEmail(String email);

    // modifying -> 一定要写 做query 操作
    // query 语句参数要跟 method 里面的参数匹配上 @Param 在此版本上JDK需要加上 @Param("id") @Param("firstName") @Param("lastName")
    @Modifying
    @Query("UPDATE customers SET first_name = :firstName, last_name = :lastName WHERE email = :email")
    void updateNameByEmail(@Param("email") String email, @Param("firstName") String firstName, @Param("lastName") String lastName);
}

