package com.jkxy.expressquery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jkxy.expressquery.R;
import com.jkxy.expressquery.adapter.CustomAdapter;
import com.jkxy.expressquery.component.ChangeCustomRemark;
import com.jkxy.expressquery.db.DBUtils;
import com.jkxy.expressquery.entity.ListInfoBean;
import com.jkxy.expressquery.listener.IDialogButtonClickListener;
import com.jkxy.expressquery.listener.IOnRecyclerViewItemClickListener;
import com.jkxy.expressquery.listener.ISwipeMenuClickListener;
import com.jkxy.expressquery.service.ClipboardListenerService;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private FloatingActionButton mFab;
    private CustomAdapter mAdapter;
    private List<ListInfoBean> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        //不允许截图
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        startService(new Intent(this, ClipboardListenerService.class));
        initViews();
        mDatas = new ArrayList<>(); //实例化数据源
        mAdapter = new CustomAdapter(this, mDatas);
        initData();
        mRecyclerView.setAdapter(mAdapter);
//        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        GridLayoutManager manager = new GridLayoutManager(this,1);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        initEvent();
    }

    private void initEvent() {
        mFab.setOnClickListener(this);
        mAdapter.setOnItemClickListener(new IOnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(ListInfoBean bean, View v, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("state", bean.getState());
                bundle.putString("code", bean.getShipperCode());
                bundle.putString("customRemark", bean.getCustomRemark());
                bundle.putString("number", bean.getLogisticCode());
                bundle.putBoolean("flag", false);
                Intent i = new Intent(MainActivity.this, DetailInfoActivity.class);
                i.putExtras(bundle);
                startActivity(i);
            }
        });
//500454250776
        mAdapter.setOnSwipeMenuClickListener(new ISwipeMenuClickListener() {
            @Override
            public void onSwipeMenuClick(final ListInfoBean bean, int tag, int position) {
                switch (tag) {
                    case 1:
                        final ChangeCustomRemark remark = new ChangeCustomRemark();
                        remark.showDialog(MainActivity.this);
                        remark.setListener(new IDialogButtonClickListener() {
                            @Override
                            public void onPositiveButtonClicked(String str) {
                                DBUtils.updateExpressCustomRemarkToDb(MainActivity.this, bean.getLogisticCode(), str);
                                initData();
                                remark.dismiss();
                            }
                        });
                        break;
//                    case 2:
//                        Toast.makeText(MainActivity.this, "添加到状态栏", Toast.LENGTH_SHORT).show();
//                        break;
                    case 3:
                        DBUtils.deleteByLogisticCode(MainActivity.this, bean.getLogisticCode());
                        initData();
                        break;
                }
            }
        });


    }


    /**
     * 数据源初始化
     */
    private void initData() {
        mDatas.clear();
        List<ListInfoBean> data = DBUtils.getAllExpress(this);
        mDatas.addAll(data);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 界面视图初始化
     */
    private void initViews() {
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                Intent intent = new Intent(this, AddInfoActivity.class);
                startActivity(intent);
                break;


        }
    }


    @Override
    protected void onResume() {
        initData();
        super.onResume();
    }
}
