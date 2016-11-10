package com.jkxy.expressquery.bean;

import java.util.List;


public class JsonRootBean {

    private String EBusinessID;
    private String ShipperCode;
    private boolean Success;
    private String LogisticCode;
    private int State;
    private List<Traces> Traces;

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

    public int getState() {
        return State;
    }


    public static class Traces {
        public String AcceptTime;
        public String AcceptStation;
    }
}