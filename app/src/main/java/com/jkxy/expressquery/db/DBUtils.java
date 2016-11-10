package com.jkxy.expressquery.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jkxy.expressquery.bean.ListInfoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zh on 2016/11/8.
 */

public class DBUtils {

    private static final String TABLE_NAME = "expressDB";//表名

    private static final String ID = "_id";          //id列
    private static final String DATE = "Date";      //信息添加时间
    private static final String CUSTOMREMARK = "CustomRemark";  //用户备注
    private static final String ORDERCODE = "OrderCode"; //订单编号
    private static final String SHIPPERCODE = "ShipperCode"; //快递公司编号
    private static final String LOGISTICCODE = "LogisticCode";//物流单号
    private static final String STATE = "State";//物流状态
    private static final String EACHINFO = "EachInfo"; //每条物流信息详情

    private static ExpressDataBase mDataBase;

    public static ExpressDataBase getExpressDataBase(Context context) {
        if (mDataBase == null) {
            return new ExpressDataBase(context);
        } else {
            return mDataBase;
        }
    }

    public static List<ListInfoBean> getAllExpress(final Context context) {

        final List<ListInfoBean> expressInfo = new ArrayList<>();
        mDataBase = getExpressDataBase(context);
        SQLiteDatabase readableDatabase = mDataBase.getReadableDatabase();
        Cursor expressCursor = readableDatabase.query(TABLE_NAME,
                null, null, null, null, null, null);
        while (expressCursor.moveToNext()) {
            ListInfoBean bean = new ListInfoBean();
            // TODO: 2016/11/8 完善数据库查询
            int id = expressCursor.getInt(expressCursor.getColumnIndex(ID));
            String date = expressCursor.getString(expressCursor.getColumnIndex(DATE));
            String customRemark = expressCursor.getString(expressCursor.getColumnIndex(CUSTOMREMARK));
            String orderCode = expressCursor.getString(expressCursor.getColumnIndex(ORDERCODE));
            String shipperCode = expressCursor.getString(expressCursor.getColumnIndex(SHIPPERCODE));
            String logisticCode = expressCursor.getString(expressCursor.getColumnIndex(LOGISTICCODE));
            String state = expressCursor.getString(expressCursor.getColumnIndex(STATE));
            String eachInfo = expressCursor.getString(expressCursor.getColumnIndex(EACHINFO));
            Log.d("信息查询", id + ":" + date + ":" + customRemark + ":" + orderCode + ":" + shipperCode + ":" + logisticCode + ":" + state + ":" + eachInfo);
            bean.setDate(date);
            bean.setOrderCode(orderCode);
            bean.setState(state);
            bean.setShipperCode(shipperCode);
            bean.setLogisticCode(logisticCode);
            bean.setInfoTableName(eachInfo);
            bean.setCustomRemark(customRemark);
            expressInfo.add(bean);
        }
        expressCursor.close();
        mDataBase.close();
        return null;
    }

    public static long addExpressToDb(Context context, String date, String customRemark, String orderCode, String shipperCode, String logisticCode, String state, String eachInfo) {
        mDataBase = getExpressDataBase(context);
        SQLiteDatabase writableDatabase = mDataBase.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DATE,date);
        values.put(CUSTOMREMARK,customRemark);
        values.put(ORDERCODE,orderCode);
        values.put(SHIPPERCODE,shipperCode);
        values.put(LOGISTICCODE,logisticCode);
        values.put(STATE,state);
        values.put(EACHINFO,eachInfo);
        long id = writableDatabase.insert(TABLE_NAME, null, values);
        mDataBase.close();
        return id;
    }

    /**
     * 创建每条物流信息的详细数据表
     * @param context
     * @param tableName
     */
    private static void createEachInfoTable(Context context,String tableName){
        mDataBase = getExpressDataBase(context);
        SQLiteDatabase writableDatabase = mDataBase.getWritableDatabase();
        writableDatabase.execSQL("CREATE TABLE " + tableName + "(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "AcceptTime TEXT DEFAULT \"\"," +
                "CustomRemark TEXT DEFAULT \"\"," +
                "ShipperCode TEXT DEFAULT \"\"," +
                "LogisticCode TEXT DEFAULT \"\"," +
                "State TEXT DEFAULT \"\")");

    }

}
