package com.bjpowernode.api.po;

import java.io.Serializable;

public class DicInfo implements Serializable {
    private static final long serialVersionUID = 2644130796409286031L;
    private Integer id;

    @Override
    public String toString() {
        return "DicInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", val='" + val + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    private String name;
    private  String category;
    private  String val;

}
