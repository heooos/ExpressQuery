package com.jkxy.expressquery.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zh on 16/9/23.
 */

public class ExpressDataBase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "EXPRESS_DB_LIST.db";
    public static final String TABLE_NAME = "expressDB";

    public ExpressDataBase(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    /**
     * _id               自增长id
     * Date              信息添加时间 格式:2016-11-09
     * CustomRemark      用户备注
     * OrderCode         订单编号
     * ShipperCode       快递公司编号
     * LogisticCode      物流运单号
     * State             状态  2-在途中,3-签收,4-问题件
     * EachInfo          单独物流信息表名
     *
     * @param db
     */

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Date TEXT DEFAULT \"\"," +
                "CustomRemark TEXT DEFAULT \"\"," +
                "OrderCode TEXT DEFAULT \"\"," +
                "ShipperCode TEXT DEFAULT \"\"," +
                "LogisticCode TEXT DEFAULT \"\"," +
                "State TEXT DEFAULT \"\"," +
                "EachInfo TEXT DEFAULT \"\")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
}
