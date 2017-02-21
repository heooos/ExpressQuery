package com.jkxy.expressquery.bean;

/**
 * Created by zh on 16/9/23.
 * 所有的物流信息实体类
 */

public class ListInfoBean {


    private String id;  //id
    private String shipperCode;  //快递公司
    private String logisticCode; //物流单号
    private String date;        //添加时间
    private int shipperIcon;       //快递公司图标
    private String orderCode;     //订单编号 一般为""
    private String customRemark;    //备注
    private String state;           //状态 物流状态: 2-在途中，3-签收,4-问题件
    private String infoTableName;  //详细信息表名


    public String getId() {
        return id;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getInfoTableName() {
        return infoTableName;
    }

    public void setInfoTableName(String infoTableName) {
        this.infoTableName = infoTableName;
    }

    public String getShipperCode() {
        return shipperCode;
    }

    public void setShipperCode(String shipperCode) {
        this.shipperCode = shipperCode;
    }

    public String getLogisticCode() {
        return logisticCode;
    }

    public void setLogisticCode(String logisticCode) {
        this.logisticCode = logisticCode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getShipperIcon() {
        return shipperIcon;
    }

    public void setShipperIcon(int shipperIcon) {
        this.shipperIcon = shipperIcon;
    }

    public String getCustomRemark() {
        return customRemark;
    }

    public void setCustomRemark(String customRemark) {
        this.customRemark = customRemark;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


}
