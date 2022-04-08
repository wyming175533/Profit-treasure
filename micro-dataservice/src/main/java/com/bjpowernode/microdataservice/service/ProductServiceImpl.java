package com.bjpowernode.microdataservice.service;

import com.bjpowernode.Util.YLBUtil;
import com.bjpowernode.Util.RedisOpreation;
import com.bjpowernode.Consts.YLBKEY;
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

    /**
     * @param id 主键
     * @return 单个产品或null
     */
    @Override
    public Product FindByProductId(Integer id) {

        if(YLBUtil.ifNullZero(id)){
            return null;
        }

        Product product=productMapper.selectByPrimaryKey(id);

        return product;
    }

    @Resource
    private RedisOpreation redisOpreation;

    /**
     * @return 返回平均收益率
    */
    @Override
    public BigDecimal computeAvgRate() {
        BigDecimal rate= (BigDecimal) redisOpreation.getKey(YLBKEY.PRODUCT_RATE);
        if(rate==null){
            synchronized (this){
                if((rate= (BigDecimal) redisOpreation.getKey(YLBKEY.PRODUCT_RATE))==null){
                    rate=productMapper.selectCompetAvgRate();
                    redisOpreation.setKey(YLBKEY.PRODUCT_RATE,rate,30);
                }
            }
        }

        return rate;
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
        if(redisOpreation.checkProductType(String.valueOf(ProductType))){
            //判断参数是否合法
            PageNo= YLBUtil.PageNO(PageNo);
            PageSize= YLBUtil.PageSize(PageSize);

            int offset= YLBUtil.offset(PageNo,PageSize);
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
