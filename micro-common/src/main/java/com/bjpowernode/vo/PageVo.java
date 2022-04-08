package com.bjpowernode.vo;

import com.bjpowernode.Consts.YLBConsts;

public class PageVo {
    private Integer PageNO;//第几页
    private Integer PageSize;//每页条数
    private Integer PageTotal;//总页数
    private Integer Records;//总记录数


    public Integer getPageNO() {
        return PageNO;
    }

    public Integer getPageSize() {
        return PageSize;
    }

    public Integer getPageTotal() {
        //计算总页数，判断除数不为0
        if(PageSize==null||PageSize<1)
            PageSize= YLBConsts.YLB_PRODUCT_PAGESIZE;
        PageTotal=Records % PageSize==0?Records/PageSize:Records/PageSize+1;
        return PageTotal;
    }

    public Integer getRecords() {
        return Records;
    }

    public PageVo() {
        this.PageNO = 1;
        this.PageSize=YLBConsts.YLB_PRODUCT_PAGESIZE;
        this.PageTotal=1;
        this.Records=1;

    }

    public PageVo(Integer pageNO, Integer pageSize, Integer records) {
        PageNO = pageNO;
        PageSize = pageSize;
        Records = records;
    }

    public void setPageNO(Integer pageNO) {
        PageNO = pageNO;
    }

    public void setPageSize(Integer pageSize) {
        PageSize = pageSize;
    }

    public void setPageTotal(Integer pageTotal) {
        PageTotal = pageTotal;
    }

    public void setRecords(Integer records) {
        Records = records;
    }
}
