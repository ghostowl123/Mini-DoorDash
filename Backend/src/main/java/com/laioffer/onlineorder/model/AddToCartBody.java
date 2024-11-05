package com.laioffer.onlineorder.model;

import com.fasterxml.jackson.annotation.JsonProperty;

//写信息一般都是在body里面
public record AddToCartBody(
        Long menuId
) {
}

