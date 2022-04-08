package com.bjpowernode.microweb.view;


import com.bjpowernode.api.po.Product;

public class ProductView {
    private Integer id;
    private String allName;
    private String rate;
    private String cycle;
    private String productMoney;
    private String moneyDesc;


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
