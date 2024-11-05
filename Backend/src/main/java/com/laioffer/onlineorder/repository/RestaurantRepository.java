package com.laioffer.onlineorder.repository;


import com.laioffer.onlineorder.entity.RestaurantEntity;
import org.springframework.data.repository.ListCrudRepository;

/**
 *继承已有的 ListCrudRepository
 *
 *
 */
public interface RestaurantRepository extends ListCrudRepository<RestaurantEntity, Long> {
}

