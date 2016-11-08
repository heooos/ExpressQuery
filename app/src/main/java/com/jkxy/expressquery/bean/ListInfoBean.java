package com.jkxy.expressquery.bean;

/**
 * Created by zh on 16/9/23.
 * 所有的物流信息实体类
 */

public class ListInfoBean {


    private String id;  //id
    private String shipperCode;  //快递公司
    private String logisticCode; //物流单号
    private String data;  //添加时间
    private int shipperIcon;       //快递公司图标
    private String customRemark;    //备注
    private String state;           //状态
    private String infoTableName;  //详细信息表名


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
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
