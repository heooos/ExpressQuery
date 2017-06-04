package com.jkxy.expressquery.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jkxy.expressquery.R;
import com.jkxy.expressquery.adapter.DetailInfoListRecAdapter;
import com.jkxy.expressquery.db.DBUtils;
import com.jkxy.expressquery.db.GetDate;
import com.jkxy.expressquery.entity.CacheBean;
import com.jkxy.expressquery.entity.DetailInfoBean;
import com.jkxy.expressquery.entity.DetailInfoChildBean;
import com.jkxy.expressquery.entity.DetailInfoGroupBean;
import com.jkxy.expressquery.entity.HeadViewInfoBean;
import com.jkxy.expressquery.entity.JsonRootBean;
import com.jkxy.expressquery.model.GetDetailDataFromList;
import com.jkxy.expressquery.model.GetExpressInfo;
import com.jkxy.expressquery.utils.DateUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DetailInfoActivity extends AppCompatActivity implements AbsListView.OnScrollListener {

    private String code;
    private String number;
    private String customRemark;
    private String state;
    private boolean flag;  //true-- 来自添加   false -- 来自主界面

    private ExpandableListView mList;

    private String eachExpressName;
    private TextView tvOrderCode;
    private TextView tvCustomRemark;
    private TextView tvState;
    private TextView tvSenderPhone;
    private View headView;

    private float currentY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_info);
        initData();
        initView();
        cacheMethod();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    /**
     * 初始化布局文件
     */
    private void initView() {
        headView = getHeadView();

        tvOrderCode = (TextView) headView.findViewById(R.id.order_code);
        tvCustomRemark = (TextView) headView.findViewById(R.id.custom_remark);
        tvState = (TextView) headView.findViewById(R.id.state);
        tvSenderPhone = (TextView) headView.findViewById(R.id.sender_phone);
//        mList = (ExpandableListView) findViewById(R.id.detail_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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


    //数据加载
    private void cacheMethod() {
        if (flag) {
            Log.d("进入查询", "查询");
            new AsyncGetInfo().execute(code, number); //来自添加界面的数据
        } else {
            // TODO: 2017/3/1 问题件处理
            if (state.equals("2")) {
                new AsyncGetInfo().execute(code, number);
            } else {
                showDataFromDb();
            }
        }
    }

    //直接从数据库读取数据加载列表
    private void showDataFromDb() {
        Log.d("直接加载", "直接加载");
        List<DetailInfoBean> list = DBUtils.queryData(DetailInfoActivity.this, eachExpressName);
        CacheBean cacheBean = GetDetailDataFromList.getMap(list);
//                showList(DetailInfoActivity.this, cacheBean.getGroupList(), cacheBean.getDataMap());
        showNewList(DetailInfoActivity.this, cacheBean.getGroupList(), cacheBean.getDataMap());
        HeadViewInfoBean bean = new HeadViewInfoBean(number, customRemark, state, cacheBean.getPhoneNumber());
        showDetailInfo(bean);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        View v = view.getChildAt(0);
        if (v != null) {
            float cache = v.getY();
            if ((cache - currentY) > 0) {
                currentY = cache;
            } else {
                currentY = cache;
                Log.d("测试", firstVisibleItem + "");
                if (firstVisibleItem != 0) {
                    getSupportActionBar().setTitle(tvCustomRemark.getText().toString());
                } else {
                    getSupportActionBar().setTitle("快递详情");
                }
            }
        }

    }


    class AsyncGetInfo extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result;
            try {
                result = new GetExpressInfo().queryInfo(strings[0], strings[1]);
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
                //当快递单号为未签收的时候  查询到最新结果 更新主数据库  删除老物流信息 添加新物流信息
                DBUtils.updateExpressStateToDb(DetailInfoActivity.this, b.getLogisticCode(), b.getState());
                DBUtils.deleteEachInfoByNumber(DetailInfoActivity.this, eachExpressName);
                for (JsonRootBean.Traces m : traces) {
                    Log.d("快递信息", "时间:" + m.AcceptTime + "\n" + "信息:" + m.AcceptStation);
                    DBUtils.addEachInfoToDb(DetailInfoActivity.this,
                            eachExpressName, m.AcceptTime, m.AcceptStation);
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
            showNewList(DetailInfoActivity.this,
                    cacheBean.getGroupList(),
                    cacheBean.getDataMap());
            HeadViewInfoBean bean = new HeadViewInfoBean(number,
                    customRemark,
                    state,
                    cacheBean.getPhoneNumber());
            showDetailInfo(bean);
            super.onPostExecute(s);
        }
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

    /**
     * 头部View
     *
     * @return
     */
    private View getHeadView() {
        View view = LayoutInflater.from(this).inflate(R.layout.head_layout, null);
        return view;
    }

    //测试
    private void showNewList(Context context, List<DetailInfoGroupBean> groupList,
                             Map<DetailInfoGroupBean, List<DetailInfoChildBean>> dataMap) {
        Collections.reverse(groupList);
        RecyclerView ryc = (RecyclerView) findViewById(R.id.ryc);
        ryc.setLayoutManager(new LinearLayoutManager(this));
        DetailInfoListRecAdapter adapter = new DetailInfoListRecAdapter(context, groupList, dataMap);
        ryc.setAdapter(adapter);

    }

}
