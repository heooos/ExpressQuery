package com.jkxy.expressquery.bean;

import java.util.List;


public class JsonRootBean {

    public String EBusinessID;
    public String ShipperCode;
    public boolean Success;
    public String LogisticCode;
    public int State;
    public List<Traces> Traces;

    public static class Traces {
        public String AcceptTime;
        public String AcceptStation;
    }
}