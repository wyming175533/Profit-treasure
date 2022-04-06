package com.bjpowernode.microdataservice.service;

import com.bjpowernode.api.po.Product;
import com.bjpowernode.api.service.ProductService;
import com.bjpowernode.microdataservice.mapper.ProductMapper;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@DubboService(interfaceClass = ProductService.class,version="1.0")
public class ProductServiceImpl  implements ProductService {
    @Resource
    private ProductMapper productMapper;
    @Override
    public Product FindByProductId(Integer id) {
        return null;
    }

    @Resource
    private HelpService helpService;

    /**
     * @return 返回平均收益率
    */
    @Override
    public BigDecimal computeAvgRate() {
        return productMapper.selectCompetAvgRate();
    }

    /**
     * @return 返回计算的平均收益率
     * @ProductType 产品类型
     * @PageNo limit 第几页
     * @PageSize limit 页大小
     */
    @Override
    public List<Product> FindListByType(Integer ProductType, Integer PageNo, Integer PageSize) {
        List<Product> productList=new ArrayList<>();
        if(helpService.checkProductType(String.valueOf(ProductType))){
            //判断参数是否合法
            if(PageNo==null||PageNo<1){
                PageNo=1;
            }
            if(PageSize==null||PageSize<1||PageSize>100){
                PageSize=10;
            }
            int offset=(PageNo-1)*PageSize;
            productList=productMapper.selectListByType(ProductType,offset,PageSize);
        }

        return productList;
    }

    @Override
    public Integer queryRecordCount(Integer ptype) {
       Integer count= productMapper.selectRecordsCount(ptype);
        return count;
    }
}
