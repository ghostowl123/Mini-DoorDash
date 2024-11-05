package com.laioffer.onlineorder.entity;
//record use for data storage it will generate default getter setter estimate the override method
// Table 的意义在于
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;


@Table("customers")
public record CustomerEntity(
        @Id Long id,
        String email,
        String password,
        boolean enabled,
        String firstName,
        String lastName
) {
}
//数据库里面定义表是null 这里就不能 boolean 必须Boolean primitive type 不能为null
