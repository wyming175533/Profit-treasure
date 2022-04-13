package com.bjpowernode.api.model;

import com.bjpowernode.api.po.BidInfo;

public class MyInvestVo extends BidInfo {
    private String productName;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
