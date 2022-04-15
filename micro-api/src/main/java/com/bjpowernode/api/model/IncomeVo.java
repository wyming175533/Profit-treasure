package com.bjpowernode.api.model;

import com.bjpowernode.api.po.IncomeRecord;

public class IncomeVo extends IncomeRecord {
    private  String productName;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
