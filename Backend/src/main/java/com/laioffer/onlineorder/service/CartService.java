package com.laioffer.onlineorder.service;

import com.laioffer.onlineorder.entity.CartEntity;
import com.laioffer.onlineorder.entity.MenuItemEntity;
import com.laioffer.onlineorder.entity.OrderItemEntity;
import com.laioffer.onlineorder.model.CartDto;
import com.laioffer.onlineorder.model.OrderItemDto;
import com.laioffer.onlineorder.repository.CartRepository;
import com.laioffer.onlineorder.repository.MenuItemRepository;
import com.laioffer.onlineorder.repository.OrderItemRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {


    private final CartRepository cartRepository;
    private final MenuItemRepository menuItemRepository;
    private final OrderItemRepository orderItemRepository;


    public CartService(
            CartRepository cartRepository,
            MenuItemRepository menuItemRepository,
            OrderItemRepository orderItemRepository) {
        this.cartRepository = cartRepository;
        this.menuItemRepository = menuItemRepository;
        this.orderItemRepository = orderItemRepository;
    }


    @CacheEvict(cacheNames = "cart", key = "#p0")
    @Transactional
    public void addMenuItemToCart(long customerId, long menuItemId) {
        CartEntity cart = cartRepository.getByCustomerId(customerId);
        MenuItemEntity menuItem = menuItemRepository.findById(menuItemId).get();
        OrderItemEntity orderItem = orderItemRepository.findByCartIdAndMenuItemId(cart.id(), menuItem.id());

        Long orderItemId;
        int quantity;

        if (orderItem == null) {
            orderItemId = null;
            quantity = 1;
        } else {
            orderItemId = orderItem.id();
            quantity = orderItem.quantity() + 1;
        }
        OrderItemEntity newOrderItem = new OrderItemEntity(orderItemId, menuItemId, cart.id(), menuItem.price(), quantity);
        orderItemRepository.save(newOrderItem);
        cartRepository.updateTotalPrice(cart.id(), cart.totalPrice() + menuItem.price());
    }


    @Cacheable("cart")
    public CartDto getCart(Long customerId) {
        CartEntity cart = cartRepository.getByCustomerId(customerId);
        List<OrderItemEntity> orderItems = orderItemRepository.getAllByCartId(cart.id());
        List<OrderItemDto> orderItemDtos = getOrderItemDtos(orderItems);
        return new CartDto(cart, orderItemDtos);
    }


    @CacheEvict(cacheNames = "cart", key = "#p0")
    @Transactional
    public void clearCart(Long customerId) {
        CartEntity cartEntity = cartRepository.getByCustomerId(customerId);
        orderItemRepository.deleteByCartId(cartEntity.id());
        cartRepository.updateTotalPrice(cartEntity.id(), 0.0);
    }


    private List<OrderItemDto> getOrderItemDtos(List<OrderItemEntity> orderItems) {
        List<OrderItemDto> orderItemDtos = new ArrayList<>();
        for (OrderItemEntity orderItem : orderItems) {
            MenuItemEntity menuItem = menuItemRepository.findById(orderItem.menuItemId()).get();
            OrderItemDto orderItemDto = new OrderItemDto(orderItem, menuItem);
            orderItemDtos.add(orderItemDto);
        }
        return orderItemDtos;
    }
}


//restaurant / customer / operation team  -> 我们只做了 customer 端
//stripe 增加支付功能
//@的意义 ： 这个类有可能会被其他类依赖，将来需要被注入 -> 理解服务层数
//@Service
//public class CartService {
//
//
//    private final CartRepository cartRepository;
//    private final MenuItemRepository menuItemRepository;
//    private final OrderItemRepository orderItemRepository;
//
//
//    public CartService(
//            CartRepository cartRepository,
//            MenuItemRepository menuItemRepository,
//            OrderItemRepository orderItemRepository) {
//        this.cartRepository = cartRepository;
//        this.menuItemRepository = menuItemRepository;
//        this.orderItemRepository = orderItemRepository;
//    }
//
//    //有这个的注释就可以回滚 rollback - >  只要下行出错 上一行的执行也会被回滚 要么全成功 要么全失败
//    @Transactional
//    //为了保证cache consistency -> 我们需要保证有写操作的情况下 去更新cache 怎么做呢 只要有写操作 我们直接invalid相应的cache块
//    @CacheEvict(cacheNames = "cart", key = "#customerId")
//    public void addMenuItemToCart(long customerId, long menuItemId) {
//        CartEntity cart = cartRepository.getByCustomerId(customerId);
//        //这里需要写get() 因为Spring 默认包了一层
//        MenuItemEntity menuItem = menuItemRepository.findById(menuItemId).get();
//        OrderItemEntity orderItem = orderItemRepository.findByCartIdAndMenuItemId(cart.id(), menuItem.id());
//
//
//        Long orderItemId;
//        int quantity;
//
//        //加quantity 的数量
//        if (orderItem == null) {
//            orderItemId = null;
//            quantity = 1;
//        } else {
//            orderItemId = orderItem.id();
//            quantity = orderItem.quantity() + 1;
//        }
//        OrderItemEntity newOrderItem = new OrderItemEntity(orderItemId, menuItemId, cart.id(), menuItem.price(), quantity);
//        orderItemRepository.save(newOrderItem);
//        // save背后执行的SQL语句 id == null -> insert id !=null update
//        cartRepository.updateTotalPrice(cart.id(), cart.totalPrice() + menuItem.price());
//    }
//
//    //只有读操作没有写操作不用rollback
//    @Cacheable("cart")
//    public CartDto getCart(Long customerId) {
//        CartEntity cart = cartRepository.getByCustomerId(customerId);
//        List<OrderItemEntity> orderItems = orderItemRepository.getAllByCartId(cart.id());
//        List<OrderItemDto> orderItemDtos = getOrderItemDtos(orderItems);
//        return new CartDto(cart, orderItemDtos);
//    }
//
//
//    @Transactional
//    @CacheEvict(cacheNames = "cart", key = "#customerId")
//    public void clearCart(Long customerId) {
//        CartEntity cartEntity = cartRepository.getByCustomerId(customerId);
//        orderItemRepository.deleteByCartId(cartEntity.id());
//        cartRepository.updateTotalPrice(cartEntity.id(), 0.0);
//    }
//
//
//    private List<OrderItemDto> getOrderItemDtos(List<OrderItemEntity> orderItems) {
//        List<OrderItemDto> orderItemDtos = new ArrayList<>();
//        for (OrderItemEntity orderItem : orderItems) {
//            MenuItemEntity menuItem = menuItemRepository.findById(orderItem.menuItemId()).get();
//            OrderItemDto orderItemDto = new OrderItemDto(orderItem, menuItem);
//            orderItemDtos.add(orderItemDto);
//        }
//        return orderItemDtos;
//    }
//}

