package com.bjpowernode.api.service;

import com.bjpowernode.api.po.Product;

public interface ProductService {

    Product FindByProductId(Integer id);
}
