package com.jkxy.expressquery.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.ArcMotion;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.Gson;
import com.jkxy.expressquery.R;
import com.jkxy.expressquery.adapter.DetailInfoAdapter;
import com.jkxy.expressquery.bean.DetailInfoBean;
import com.jkxy.expressquery.bean.JsonRootBean;
import com.jkxy.expressquery.db.DBUtils;
import com.jkxy.expressquery.db.GetDate;
import com.jkxy.expressquery.utils.DateUtils;
import com.jkxy.expressquery.utils.GetExpressInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DetailInfoActivity extends AppCompatActivity {

    private String code;
    private String number;
    private boolean flag;  //true-- 来自添加   false -- 来自主界面
    private ListView mList;
    private String state;
    private DetailInfoAdapter adapter;
    private String eachExpressName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_info);
        initData();
        setUpWindowTransition();
        mList = (ListView) findViewById(R.id.detail_list);
        if (flag) {
            new AsyncGetInfo().execute(code, number);
        }
        Log.d("进入查询", "查询");
    }

    private void setUpWindowTransition() {

        final ChangeBounds ts = new ChangeBounds();
        ts.setPathMotion(new ArcMotion());
        ts.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {

            }

            @Override
            public void onTransitionEnd(Transition transition) {
                Log.d("onTransitionEnd", "动画执行完毕");
                if (getIntent().getExtras().getString("state").equals("2")) {
                    new AsyncGetInfo().execute(code, number);
                } else {
                    Log.d("直接加载","直接加载");
                    showList(DBUtils.queryData(DetailInfoActivity.this, eachExpressName));
                }
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });
        getWindow().setSharedElementEnterTransition(ts);

    }

    /**
     * 初始化数据
     */
    private void initData() {
        Bundle info = getIntent().getExtras();
        code = info.getString("code");
        number = info.getString("number");
        flag = info.getBoolean("flag");
        eachExpressName = "s" + number;
    }


    class AsyncGetInfo extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result;
            try {
                result = new GetExpressInfo().queryInfo(strings[0], strings[1]);
                Log.d("doInBackground", "查询结果:" + result);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("onPostExecute", "查询结束");
            Gson g = new Gson();
            JsonRootBean b = g.fromJson(s, JsonRootBean.class);
            // TODO: 16/9/24 获取到快递信息
            List<JsonRootBean.Traces> traces = b.getTraces();
            if (flag) {
                //来自添加界面
                DBUtils.createEachInfoTable(DetailInfoActivity.this, eachExpressName);
                DBUtils.addExpressToDb(DetailInfoActivity.this, GetDate.getTime(), "", "", b.getShipperCode(), b.getLogisticCode(), b.getState(), "s" + number);
                for (JsonRootBean.Traces m : traces) {
                    Log.d("快递信息", "时间:" + m.AcceptTime + "\n" + "信息:" + m.AcceptStation);
                    DBUtils.addEachInfoToDb(DetailInfoActivity.this, eachExpressName, m.AcceptTime, m.AcceptStation);
                }
            } else {
                //来自主界面
                Log.d("物流数据个数:", traces.size() + "个");
                //查询数据库个数
                int count = DBUtils.queryDataCount(DetailInfoActivity.this, eachExpressName);
                Log.d("数据库数据数量", count + "个");
                if (!(count == traces.size())) {
                    // TODO: 2017/2/20 数据有更新  将更新的数据添加到数据库中
                    for (int i = 0; i < traces.size(); i++) {
                        if ((i + 1) > count) {
                            Log.d("快递信息", "时间:" + traces.get(i).AcceptTime + "\n" + "信息:" + traces.get(i).AcceptStation);
                            DBUtils.addEachInfoToDb(DetailInfoActivity.this, eachExpressName, traces.get(i).AcceptTime, traces.get(i).AcceptStation);
                        }
                    }
                }
            }
            // TODO: 2017/2/20 两个数据库已经创建好   显示物流信息 
            //在此处 表示程序完成单号的检查以及查询。返回了查询的结果。
            List<DetailInfoBean> beanList = new ArrayList<>();
            for (JsonRootBean.Traces m : traces) {
                String yearMonthDay = DateUtils.getYearMonthDay(m.AcceptTime);
                String time = DateUtils.getTime(m.AcceptTime);
                String week = DateUtils.getWeek(m.AcceptTime);
                DetailInfoBean bean = new DetailInfoBean();

                bean.setDateYearMonthDay(yearMonthDay);
                bean.setDateTime(time);
                bean.setDateWeek(week);
                bean.setInfo(m.AcceptStation);

                beanList.add(bean);
            }
            showList(beanList);
            super.onPostExecute(s);
        }
    }

    /**
     * 列表加载
     *
     * @param beanList
     */
    private void showList(List<DetailInfoBean> beanList) {
        Collections.reverse(beanList);
        adapter = new DetailInfoAdapter(DetailInfoActivity.this, beanList);
        mList.setAdapter(adapter);
        mList.setItemsCanFocus(false);

    }
}
