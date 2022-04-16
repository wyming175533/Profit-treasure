package com.bjpowernode.microweb.view;

public class InvestTopBean {
    private String phone;
    private  Double bidMoney;

    public InvestTopBean(String phone, Double bidMoney) {
        this.phone = phone;
        this.bidMoney = bidMoney;
    }

    public InvestTopBean() {
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getBidMoney() {
        return bidMoney;
    }

    public void setBidMoney(Double bidMoney) {
        this.bidMoney = bidMoney;
    }
}
