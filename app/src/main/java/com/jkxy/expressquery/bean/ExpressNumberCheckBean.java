package com.jkxy.expressquery.bean;

import java.util.List;

/**
 * Created by zh on 16/9/22.
 */

public class ExpressNumberCheckBean {

    public String EBusinessID;
    public String LogisticCode;
    public boolean Success;
    public int Code;

    public List<Shippers> Shippers;

    public static class Shippers {
        public String ShipperCode;
        public String ShipperName;
    }

}
