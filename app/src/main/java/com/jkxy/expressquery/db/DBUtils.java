package com.jkxy.expressquery.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jkxy.expressquery.entity.DetailInfoBean;
import com.jkxy.expressquery.entity.ListInfoBean;
import com.jkxy.expressquery.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zh on 2016/11/8.
 */

public class DBUtils {

    private static final String EXPRESS_TABLE_NAME = "expressDB";//表名

    private static final String ID = "_id";                            //id列
    private static final String DATE = "Date";                         //信息添加时间
    private static final String CUSTOMREMARK = "CustomRemark";         //用户备注
    private static final String ORDERCODE = "OrderCode";               //订单编号
    private static final String SHIPPERCODE = "ShipperCode";           //快递公司编号
    private static final String LOGISTICCODE = "LogisticCode";         //物流单号
    private static final String STATE = "State";                       //物流状态
    private static final String EACHINFO = "EachInfo";                 //每条物流信息详情

    private static ExpressDataBase mDataBase;

    public static ExpressDataBase getExpressDataBase(Context context) {
        if (mDataBase == null) {
            return new ExpressDataBase(context);
        } else {
            return mDataBase;
        }
    }

    /**
     * 获取所有物流集合的信息
     *
     * @param context
     * @return
     */
    public static List<ListInfoBean> getAllExpress(final Context context) {

        final List<ListInfoBean> expressInfo = new ArrayList<>();
        mDataBase = getExpressDataBase(context);
        SQLiteDatabase readableDatabase = mDataBase.getReadableDatabase();
        Cursor expressCursor = readableDatabase.query(EXPRESS_TABLE_NAME,
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
        return expressInfo;
    }


    /**
     * 添加物流信息到数据库
     *
     * @param context
     * @param date         时间
     * @param customRemark 用户备注
     * @param orderCode    订单编号  ""  空字符串
     * @param shipperCode  物流公司代号
     * @param logisticCode 物流运单号
     * @param state        物流状态: 2-在途中，3-签收,4-问题件
     * @param eachInfo
     * @return
     */
    public static long addExpressToDb(Context context, String date, String customRemark, String orderCode, String shipperCode, String logisticCode, String state, String eachInfo) {
        mDataBase = getExpressDataBase(context);
        SQLiteDatabase writableDatabase = mDataBase.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DATE, date);
        values.put(CUSTOMREMARK, customRemark);
        values.put(ORDERCODE, orderCode);
        values.put(SHIPPERCODE, shipperCode);
        values.put(LOGISTICCODE, logisticCode);
        values.put(STATE, state);
        values.put(EACHINFO, eachInfo);
        long id = writableDatabase.insert(EXPRESS_TABLE_NAME, null, values);
        mDataBase.close();
        return id;
    }

    /**
     * 更新主数据库中数据的备注信息
     * @param context
     * @param LogisticCode  快递单号
     * @param customRemark  备注信息
     */
    public static void updateExpressCustomRemarkToDb(Context context, String LogisticCode, String customRemark) {
        mDataBase = getExpressDataBase(context);
        SQLiteDatabase writableDatabase = mDataBase.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("CustomRemark", customRemark);
        writableDatabase.update(EXPRESS_TABLE_NAME,
                values,
                "LogisticCode = ?",
                new String[]{LogisticCode});
    }

    /**
     * 更新主数据库中数据的状态信息
     * @param context
     * @param LogisticCode 快递单号
     * @param customRemark 快递状态
     */
    public static void updateExpressStateToDb(Context context, String LogisticCode, String state) {
        mDataBase = getExpressDataBase(context);
        SQLiteDatabase writableDatabase = mDataBase.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("State", state);
        writableDatabase.update(EXPRESS_TABLE_NAME,
                values,
                "LogisticCode = ?",
                new String[]{LogisticCode});
    }



    /**
     * 根据传入的表名 检查该表是否存在
     *
     * @param context
     * @param number
     * @return 如果存在 返回true  否则返回false
     */
    public static boolean checkNumberExists(Context context, String number) {
        // TODO: 2017/5/17
        mDataBase = getExpressDataBase(context);
        SQLiteDatabase readableDatabase = mDataBase.getReadableDatabase();
        Cursor cursor = readableDatabase.query(EXPRESS_TABLE_NAME, null, "LogisticCode = ?", new String[]{number}, null, null, null);
        if (cursor.moveToNext()) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }


    /**
     * 创建每条物流信息的详细数据表
     *
     * @param context
     * @param tableName 采用订单号作为表明 创建表、
     */
    public static void createEachInfoTable(Context context, String tableName) {

        mDataBase = getExpressDataBase(context);
        SQLiteDatabase writableDatabase = mDataBase.getWritableDatabase();
        writableDatabase.execSQL("CREATE TABLE " + tableName + "(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "AcceptTime TEXT DEFAULT \"\"," +
                "AcceptStation TEXT DEFAULT \"\")");

    }

    /**
     * 添加每一条物流的具体信息
     *
     * @param context
     * @param tableName     表名
     * @param acceptTime    物流时间
     * @param acceptStation 物流描述
     * @return
     */
    public static long addEachInfoToDb(Context context, String tableName, String acceptTime, String acceptStation) {
        mDataBase = getExpressDataBase(context);
        SQLiteDatabase writableDatabase = mDataBase.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("AcceptTime", acceptTime);
        values.put("AcceptStation", acceptStation);
        long id = writableDatabase.insert(tableName, null, values);
        mDataBase.close();
        return id;
    }

    /**
     * 查询数据库数据个数
     *
     * @param context
     * @param tableName
     * @return
     */
    public static int queryDataCount(Context context, String tableName) {
        mDataBase = getExpressDataBase(context);
        SQLiteDatabase readableDatabase = mDataBase.getReadableDatabase();
        Cursor cursor = readableDatabase.query(tableName, null, null, null, null, null, null);
        cursor.moveToLast();
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    /**
     * 查询所有数据
     *
     * @param context
     * @param tableName
     * @return
     */
    public static List<DetailInfoBean> queryData(Context context, String tableName) {
        mDataBase = getExpressDataBase(context);
        SQLiteDatabase readableDatabase = mDataBase.getReadableDatabase();
        Cursor cursor = readableDatabase.query(tableName, null, null, null, null, null, null);
        List<DetailInfoBean> beanList = new ArrayList<>();
        while (cursor.moveToNext()) {
            String date = cursor.getString(cursor.getColumnIndex("AcceptTime"));
            String yearMonthDay = DateUtils.getYearMonthDay(date);
            String week = DateUtils.getWeek(date);
            String time = DateUtils.getTime(date);
            String info = cursor.getString(cursor.getColumnIndex("AcceptStation"));
            DetailInfoBean bean = new DetailInfoBean(yearMonthDay, week, time, info);
            beanList.add(bean);
        }
        cursor.close();
        return beanList;
    }

    /**
     * 从总的数据库中删除单条物流信息
     *
     * @param context
     * @param logisticCode
     */
    public static void deleteByLogisticCode(Context context, String logisticCode) {

        DBUtils.deleteEachInfoFromDb(context, "s" + logisticCode);
        mDataBase = getExpressDataBase(context);
        SQLiteDatabase writableDatabase = mDataBase.getWritableDatabase();
        writableDatabase.delete(EXPRESS_TABLE_NAME, "LogisticCode = ?", new String[]{logisticCode});

    }

    public static void deleteEachInfoFromDb(Context context, String tableName) {
        mDataBase = getExpressDataBase(context);
        SQLiteDatabase writableDatabase = mDataBase.getWritableDatabase();
        writableDatabase.execSQL("DROP TABLE " + tableName);

    }

    /**
     * 根据提供的快递单号 删除该快递的信息
     *
     * @param context
     * @param name
     */
    public static void deleteEachInfoByNumber(Context context, String name) {
        mDataBase = getExpressDataBase(context);
        SQLiteDatabase writableDatabase = mDataBase.getWritableDatabase();
        writableDatabase.delete(name, null, null);
    }

}
