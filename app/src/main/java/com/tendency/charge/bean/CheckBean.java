package com.tendency.charge.bean;

public class CheckBean {
    private int position;
    private boolean isCheck;

    public CheckBean() {
    }

    public CheckBean(int position, boolean isCheck) {
        this.position = position;
        this.isCheck = isCheck;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
