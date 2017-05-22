package com.jkxy.expressquery.entity;

/**
 * Created by zh on 2017/2/22.
 */

public class HeadViewInfoBean {

    private String orderCode;
    private String customRemark;
    private String state;
    private String senderPhone;

    public HeadViewInfoBean(String orderCode, String customRemark, String state, String senderPhone) {
        this.orderCode = orderCode;
        this.customRemark = customRemark;
        this.state = state;
        this.senderPhone = senderPhone;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public String getCustomRemark() {
        return customRemark;
    }

    public String getState() {
        return state;
    }

    public String getSenderPhone() {
        return senderPhone;
    }
}
