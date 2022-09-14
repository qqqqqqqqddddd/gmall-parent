package com.atguigu.gmall.cart.service;

import com.atguigu.gmall.model.cart.CartInfo;
import com.atguigu.gmall.model.product.SkuInfo;

import java.util.List;

public interface CartService {


    SkuInfo addToCart(Long skuId, Integer num);

    String determinCartKey();

    SkuInfo addItemToCart(Long skuId, Integer num, String cartKey);


    List<CartInfo> getCartList(String cartKey);

    void updateItemNum(Long skuId, Integer num, String cartKey);

    void updateChecked(Long skuId, Integer status, String cartKey);

    void deleteCartItem(Long skuId, String cartKey);

    void deleteChecked(String cartKey);

    List<CartInfo> getCheckedItems(String cartKey);

    void mergeUserAndTempCart();

    void updateCartAllItemsPrice(String cartKey, List<CartInfo> infos);

}
