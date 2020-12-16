package com.tendency.charge.http.utils;


public class DdcResult<T>  {


    // 返回是否成功
    private Boolean success = false;
    // 返回信息
    private String msg = "操作成功";
    /**
     * 返回消息类型,用于复杂返回信息,0代表普通返回信息,1代表特殊返回信息
     */
    private Integer code = 0;

    private T data;

    private DdcPageInfo page;

    public DdcResult() {
    }

    public DdcResult(Boolean success, String msg, Integer code, T data, DdcPageInfo page) {
        this.success = success;
        this.msg = msg;
        this.code = code;
        this.data = data;
        this.page = page;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public DdcPageInfo getPage() {
        return page;
    }

    public void setPage(DdcPageInfo page) {
        this.page = page;
    }


}
