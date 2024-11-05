package com.laioffer.onlineorder.service;

import com.laioffer.onlineorder.entity.MenuItemEntity;
import com.laioffer.onlineorder.entity.RestaurantEntity;
import com.laioffer.onlineorder.model.MenuItemDto;
import com.laioffer.onlineorder.model.RestaurantDto;
import com.laioffer.onlineorder.repository.MenuItemRepository;
import com.laioffer.onlineorder.repository.RestaurantRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Service
public class RestaurantService {


    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;


    public RestaurantService(
            RestaurantRepository restaurantRepository,
            MenuItemRepository menuItemRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
    }


    @Cacheable("restaurants")
    public List<RestaurantDto> getRestaurants() {
        List<RestaurantEntity> restaurantEntities = restaurantRepository.findAll();
        List<MenuItemEntity> menuItemEntities = menuItemRepository.findAll();
        Map<Long, List<MenuItemDto>> groupedMenuItems = new HashMap<>();
        for (MenuItemEntity menuItemEntity : menuItemEntities) {
            List<MenuItemDto> group = groupedMenuItems.computeIfAbsent(menuItemEntity.restaurantId(), k -> new ArrayList<>());
            MenuItemDto menuItemDto = new MenuItemDto(menuItemEntity);
            group.add(menuItemDto);
        }
        List<RestaurantDto> results = new ArrayList<>();
        for (RestaurantEntity restaurantEntity : restaurantEntities) {
            RestaurantDto restaurantDto = new RestaurantDto(restaurantEntity, groupedMenuItems.get(restaurantEntity.id()));
            results.add(restaurantDto);
        }
        return results;
    }
}


///**
// * Spring  boot 里面需要@Service 去决定 是否是Service
// *
// * */
//@Service
//public class RestaurantService {
//
//
//    private final MenuItemRepository menuItemRepository;
//    private final RestaurantRepository restaurantRepository;
//
///**
// *
// * RestaurantService 里面有两个dependence
// *
// *
// * */
//    public RestaurantService(
//            RestaurantRepository restaurantRepository,
//            MenuItemRepository menuItemRepository) {
//        this.restaurantRepository = restaurantRepository;
//        this.menuItemRepository = menuItemRepository;
//    }
//
//    //To do: 这里可以用paging/pagination的技术去优化
//    @Cacheable("restaurant")
//    public List<RestaurantDto> getRestaurants() {
//        //拿到所有restaurant 和所有的menuItems 创建MenuItemDTO 按Restaurant id分类 后 创建RestaurantDTO 再放MenuItemDTO进去
//        List<RestaurantEntity> restaurantEntities = restaurantRepository.findAll();
//        List<MenuItemEntity> menuItemEntities = menuItemRepository.findAll();
//        Map<Long, List<MenuItemDto>> groupedMenuItems = new HashMap<>();
//        for (MenuItemEntity menuItemEntity : menuItemEntities) {
//            List<MenuItemDto> group = groupedMenuItems.computeIfAbsent(menuItemEntity.restaurantId(), k -> new ArrayList<>());
//            //List<MenuItemDto> group = groupedMenuItem.get(menuItemEntity);
//            //这里不能直接用get得确保不为null
//            MenuItemDto menuItemDto = new MenuItemDto(menuItemEntity);
//            group.add(menuItemDto);
//        }
//        List<RestaurantDto> results = new ArrayList<>();
//        for (RestaurantEntity restaurantEntity : restaurantEntities) {
//            RestaurantDto restaurantDto = new RestaurantDto(restaurantEntity, groupedMenuItems.get(restaurantEntity.id()));
//            results.add(restaurantDto);
//        }
//        return results;
//    }
//}

