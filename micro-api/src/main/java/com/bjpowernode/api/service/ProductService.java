package com.bjpowernode.api.service;

import com.bjpowernode.api.po.Product;



import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    Product FindByProductId(Integer id);

    /**
     * @return 返回计算的平均收益率
     * @ProductType 产品类型
     * @PageNo limit 第几页
     * @PageSize limit 页大小
     */
    BigDecimal computeAvgRate();
    List<Product> FindListByType(Integer ProductType,
                                  Integer PageNo,
                                  Integer PageSize);

}
