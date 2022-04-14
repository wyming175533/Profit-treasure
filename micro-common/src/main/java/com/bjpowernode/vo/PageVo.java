package com.bjpowernode.vo;

import com.bjpowernode.Consts.YLBConsts;

public class PageVo {
    private Integer pageNO;//第几页
    private Integer pageSize;//每页条数
    private Integer pageTotal;//总页数
    private Integer records;//总记录数


    public Integer getPageNO() {
        return pageNO;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Integer getPageTotal() {
        //计算总页数，判断除数不为0
        if(pageSize ==null|| pageSize <1)
            pageSize = YLBConsts.YLB_PRODUCT_PAGESIZE;
        pageTotal =records % pageSize ==0?records/ pageSize :records/ pageSize +1;
        return pageTotal;
    }

    public Integer getRecords() {
        return records;
    }

    public PageVo() {
        this.pageNO = 1;
        this.pageSize =YLBConsts.YLB_PRODUCT_PAGESIZE;
        this.pageTotal =1;
        this.records=1;

    }

    public PageVo(Integer pageNO, Integer pageSize, Integer records) {
        this.pageNO = pageNO;
        this.pageSize = pageSize;
        this.records = records;
    }

    public void setPageNO(Integer pageNO) {
        this.pageNO = pageNO;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setPageTotal(Integer pageTotal) {
        this.pageTotal = pageTotal;
    }

    public void setRecords(Integer records) {
        this.records = records;
    }
}
