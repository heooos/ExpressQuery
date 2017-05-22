package com.jkxy.expressquery.entity;

import java.util.List;


public class JsonRootBean {

    private String EBusinessID;         //电商用户ID
    private String ShipperCode;         //快递公司编码
    private boolean Success;            //成功与否
    private String LogisticCode;        //物流运单号
    private String State;                  //物流状态: 2-在途中，3-签收,4-问题件
    private List<Traces> Traces;        //Traces 物流轨迹详情

    public List<JsonRootBean.Traces> getTraces() {
        return Traces;
    }

    public String getEBusinessID() {
        return EBusinessID;
    }

    public String getShipperCode() {
        return ShipperCode;
    }

    public boolean isSuccess() {
        return Success;
    }

    public String getLogisticCode() {
        return LogisticCode;
    }

    public String getState() {
        return State;
    }


    public static class Traces {
        public String AcceptTime;
        public String AcceptStation;
    }
}