package com.bjpowernode.microweb.view;


import com.bjpowernode.api.po.Product;

import java.math.BigDecimal;

public class ProductView {
    private Integer id;
    private String allName;  //商品全名称（多少期）
    private String rate; //利率，加%
    private String cycle; //周期 --天/月
    private String productMoney; //产品金额募集
    private String moneyDesc; //金额描述： 募集中/已满表


    private BigDecimal prate;   //利率不带%
    private Integer    pcycle; //周期，天数
    private Integer    productType; //  类型
    private BigDecimal leftProductMoney; //剩余可投金额
    private BigDecimal bidMinLimit; //单笔最小
    private BigDecimal bidMaxLimit;//单笔最大


    public BigDecimal getPrate() {
        return prate;
    }

    public void setPrate(BigDecimal prate) {
        this.prate = prate;
    }

    public Integer getPcycle() {
        return pcycle;
    }

    public void setPcycle(Integer pcycle) {
        this.pcycle = pcycle;
    }

    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }

    public BigDecimal getLeftProductMoney() {
        return leftProductMoney;
    }

    public void setLeftProductMoney(BigDecimal leftProductMoney) {
        this.leftProductMoney = leftProductMoney;
    }

    public BigDecimal getBidMinLimit() {
        return bidMinLimit;
    }

    public void setBidMinLimit(BigDecimal bidMinLimit) {
        this.bidMinLimit = bidMinLimit;
    }

    public BigDecimal getBidMaxLimit() {
        return bidMaxLimit;
    }

    public void setBidMaxLimit(BigDecimal bidMaxLimit) {
        this.bidMaxLimit = bidMaxLimit;
    }

    public ProductView(){

    }

    public static ProductView  turnView(Product product){
        ProductView  view  = new ProductView();
        view.setId(product.getId());
        view.setAllName( product.getProductName()+"("+product.getProductNo()+"期)");
        view.setCycle( product.getProductType() == 0 ? product.getCycle()+"天": product.getCycle()+"个月" );
        view.setMoneyDesc( product.getProductStatus() == 0  ?
                          "募集中,剩余募集金额 "+product.getLeftProductMoney()+"元":"已满标");
        view.setProductMoney(String.valueOf(product.getProductMoney()));
        view.setRate(product.getRate()+"%");

        view.setPcycle(product.getCycle());
        view.setPrate(product.getRate());
        view.setLeftProductMoney(product.getLeftProductMoney());
        view.setBidMinLimit(product.getBidMinLimit());
        view.setBidMaxLimit(product.getBidMaxLimit());
        view.setProductType(product.getProductType());

        return view;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAllName() {
        return allName;
    }

    public void setAllName(String allName) {
        this.allName = allName;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public String getProductMoney() {
        return productMoney;
    }

    public void setProductMoney(String productMoney) {
        this.productMoney = productMoney;
    }

    public String getMoneyDesc() {
        return moneyDesc;
    }

    public void setMoneyDesc(String moneyDesc) {
        this.moneyDesc = moneyDesc;
    }
}
