package com.bjpowernode.microdataservice.mapper;

import com.bjpowernode.api.po.Product;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ProductMapper {
    /**
     * @param ProductType 产品类型
     * @param offset      分页起始值（掠过）
     * @param rows        行数
     * @return 产品集合
     */
    List<Product> selectListByType(@Param("ProductType") Integer ProductType,
                                   @Param("offset") Integer offset,
                                   @Param("rows") Integer rows);

    BigDecimal selectCompetAvgRate();

    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    Integer selectRecordsCount(Integer ptype);

    int updateProductMoney(Integer productId, BigDecimal bidMoney);

}