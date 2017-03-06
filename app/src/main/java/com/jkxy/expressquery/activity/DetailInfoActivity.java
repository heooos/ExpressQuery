package com.jkxy.expressquery.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.ArcMotion;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jkxy.expressquery.R;
import com.jkxy.expressquery.adapter.DetailInfoListAdapter;
import com.jkxy.expressquery.bean.CacheBean;
import com.jkxy.expressquery.bean.DetailInfoBean;
import com.jkxy.expressquery.bean.DetailInfoChildBean;
import com.jkxy.expressquery.bean.DetailInfoGroupBean;
import com.jkxy.expressquery.bean.HeadViewInfoBean;
import com.jkxy.expressquery.bean.JsonRootBean;
import com.jkxy.expressquery.db.DBUtils;
import com.jkxy.expressquery.db.GetDate;
import com.jkxy.expressquery.model.GetDetailDataFromList;
import com.jkxy.expressquery.model.GetExpressInfo;
import com.jkxy.expressquery.utils.DateUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DetailInfoActivity extends AppCompatActivity {

    private String code;
    private String number;
    private String customRemark;
    private String state;
    private boolean flag;  //true-- 来自添加   false -- 来自主界面

    private ExpandableListView mList;
    private DetailInfoListAdapter adapter;

    private String eachExpressName;
    private TextView tvOrderCode;
    private TextView tvCustomRemark;
    private TextView tvState;
    private TextView tvSenderPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_info);
        initData();
        initView();
        setUpWindowTransition();
        if (flag) {
            Log.d("进入查询", "查询");
            new AsyncGetInfo().execute(code, number); //来自添加界面的数据
        }
    }

    /**
     * 初始化布局文件
     */
    private void initView() {
        tvOrderCode = (TextView) findViewById(R.id.order_code);
        tvCustomRemark = (TextView) findViewById(R.id.custom_remark);
        tvState = (TextView) findViewById(R.id.state);
        tvSenderPhone = (TextView) findViewById(R.id.sender_phone);
        mList = (ExpandableListView) findViewById(R.id.detail_list);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        Bundle info = getIntent().getExtras();
        code = info.getString("code");
        number = info.getString("number");
        flag = info.getBoolean("flag");
        customRemark = info.getString("customRemark");
        state = info.getString("state");
        eachExpressName = "s" + number;
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
                if (flag) {
                    return;
                } else {
                    // TODO: 2017/3/1 问题件处理
                    if (state.equals("2")) {
                        new AsyncGetInfo().execute(code, number);
                    } else {
                        Log.d("直接加载", "直接加载");
                        String phoneNumber = "";
                        List<DetailInfoBean> list = DBUtils.queryData(DetailInfoActivity.this, eachExpressName);
                        CacheBean cacheBean = GetDetailDataFromList.getMap(list);
                        showList(DetailInfoActivity.this, cacheBean.getGroupList(), cacheBean.getDataMap());
                        HeadViewInfoBean bean = new HeadViewInfoBean(number, customRemark, state, cacheBean.getPhoneNumber());
                        showDetailInfo(bean);
                    }
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

            state = b.getState();

            // TODO: 16/9/24 获取到快递信息
            List<JsonRootBean.Traces> traces = b.getTraces();


            if (traces.size() == 0) {
                Toast.makeText(DetailInfoActivity.this, "未查询到快递信息", Toast.LENGTH_SHORT).show();
                return;
            }
            if (flag) {
                //来自添加界面
                DBUtils.createEachInfoTable(DetailInfoActivity.this, eachExpressName);
                DBUtils.addExpressToDb(DetailInfoActivity.this,
                        GetDate.getTime(),
                        "",
                        "",
                        b.getShipperCode(),
                        b.getLogisticCode(),
                        b.getState(),
                        "s" + number);
                for (JsonRootBean.Traces m : traces) {
                    Log.d("快递信息", "时间:" + m.AcceptTime + "\n" + "信息:" + m.AcceptStation);
                    DBUtils.addEachInfoToDb(DetailInfoActivity.this,
                            eachExpressName, m.AcceptTime, m.AcceptStation);
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
            List<DetailInfoBean> list = new ArrayList<>();
            for (JsonRootBean.Traces m : traces) {
                DetailInfoBean bean = new DetailInfoBean(
                        DateUtils.getYearMonthDay(m.AcceptTime),
                        DateUtils.getWeek(m.AcceptTime),
                        DateUtils.getTime(m.AcceptTime),
                        m.AcceptStation);
                list.add(bean);
            }


            //在此处 表示程序完成单号的检查以及查询。返回了查询的结果。
            CacheBean cacheBean = GetDetailDataFromList.getMap(list);
            showList(DetailInfoActivity.this, cacheBean.getGroupList(), cacheBean.getDataMap());
            HeadViewInfoBean bean = new HeadViewInfoBean(number, customRemark, state, cacheBean.getPhoneNumber());
            showDetailInfo(bean);
            super.onPostExecute(s);
        }
    }

    /**
     * 列表加载
     */
    private void showList(Context context, List<DetailInfoGroupBean> groupList, Map<DetailInfoGroupBean, List<DetailInfoChildBean>> dataMap) {
        Collections.reverse(groupList);
        adapter = new DetailInfoListAdapter(context, groupList, dataMap);
        mList.setAdapter(adapter);
        mList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
        int groupCount = mList.getCount();
        for (int i = 0; i < groupCount; i++) {
            mList.expandGroup(i);
        }
        ;
    }

    /**
     * headView显示具体信息
     */
    private void showDetailInfo(HeadViewInfoBean bean) {
        Log.d("显示头部消息", "头部");
        tvOrderCode.setText(bean.getOrderCode());
        if (bean.getCustomRemark().equals("")) {
            tvCustomRemark.setText("暂无");
        } else {
            tvCustomRemark.setText(bean.getCustomRemark());

        }
        tvState.setText(bean.getState());
        if (bean.getSenderPhone().equals("")) {
            tvSenderPhone.setText("暂无");
        } else {
            tvSenderPhone.setText(bean.getSenderPhone());
            tvSenderPhone.setTextColor(Color.BLUE);
            tvSenderPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(DetailInfoActivity.this, ((TextView) v).getText().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}
