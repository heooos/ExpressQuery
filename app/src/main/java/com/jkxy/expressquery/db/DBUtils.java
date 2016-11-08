package com.jkxy.expressquery.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jkxy.expressquery.bean.ListInfoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zh on 2016/11/8.
 */

public class DBUtils {

    public static final String TABLE_NAME = "EXPRESS_DB_LIST";//表名
    public static final String ID = "_id";          //id列
    public static final String DATA = "Data";      //信息添加时间
    public static final String CUSTOMREMARK ="CustomRemark";  //用户备注
    public static final String ORDERCODE ="OrderCode"; //订单编号
    public static final String SHIPPERCODE ="ShipperCode"; //快递公司编号
    public static final String LOGISTICCODE = "LogisticCode";//物流单号
    public static final String STATE = "State";//物流状态
    public static final String EACHINFO = "EachInfo"; //每条物流信息详情

    private static ExpressDataBase mDataBase;

    public static ExpressDataBase getExpressDataBase(Context context){
        if (mDataBase == null) {
            return new ExpressDataBase(context);
        } else {
            return mDataBase;
        }
    }

    public static List<ListInfoBean> getAllExpress(final Context context){

        final List<ListInfoBean> expressInfo = new ArrayList<>();
        ExpressDataBase db = getExpressDataBase(context);
        SQLiteDatabase readableDatabase = db.getReadableDatabase();
        Cursor expressCursor = readableDatabase.query(TABLE_NAME,
                null, null, null, null, null, null);
        while (expressCursor.moveToNext()){

// TODO: 2016/11/8 完善数据库查询 
            int id = expressCursor.getInt(expressCursor.getColumnIndex(ID));
            String date = expressCursor.getString(expressCursor.getColumnIndex(DATA));
            String customRemark = expressCursor.getString(expressCursor.getColumnIndex(CUSTOMREMARK));



        }

        return null;
    }

}
