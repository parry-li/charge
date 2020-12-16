package com.tendency.charge.http.utils;



/**
 * Created by parry
 */


public class DdcPageInfo {

    private Integer total;

    private Integer pageNum = 1;

    private Integer pageSize = 20;

    private long count;

    public DdcPageInfo() {
    }

    public DdcPageInfo(Integer total, Integer pageNum, Integer pageSize, long count) {
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.count = count;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
