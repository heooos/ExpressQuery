package com.jkxy.expressquery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import com.jkxy.expressquery.R;
import com.jkxy.expressquery.adapter.CustomAdapter;
import com.jkxy.expressquery.bean.ListInfoBean;
import com.jkxy.expressquery.component.ChangeCustomRemark;
import com.jkxy.expressquery.db.DBUtils;
import com.jkxy.expressquery.impl.IDialogButtonClickListener;
import com.jkxy.expressquery.impl.IOnRecyclerViewItemClickListener;
import com.jkxy.expressquery.impl.ISwipeMenuClickListener;

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
        setSupportActionBar(toolbar);
        //不允许截图
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);

        initViews();
        mDatas = new ArrayList<>(); //实例化数据源
        mAdapter = new CustomAdapter(this, mDatas);
        initData();
        mRecyclerView.setAdapter(mAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        initEvent();
        setupWindowAnimations();
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
                transitionToActivity(DetailInfoActivity.class, bundle, v.findViewById(R.id.img_logo));
            }
        });

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
                                DBUtils.updateExpressToDb(MainActivity.this,bean.getLogisticCode(),str);
                                initData();
                                remark.dismiss();
                            }
                        });
                        break;
                    case 2:
                        Toast.makeText(MainActivity.this, "添加到状态栏", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        DBUtils.deleteByLogisticCode(MainActivity.this,bean.getLogisticCode());
                        initData();
                        break;
                }
            }
        });


    }

    private void transitionToActivity(Class target, Bundle bundle, View view) {

        final Pair[] pairs = createSafeTransitionParticipants(this, false, view);
        beginActivity(target, bundle, pairs);
    }

    private void beginActivity(Class target, Bundle bundle, Pair[] pairs) {

        Intent i = new Intent(MainActivity.this, target);
        i.putExtras(bundle);
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, pairs);
        startActivity(i, transitionActivityOptions.toBundle());

    }

    private Pair[] createSafeTransitionParticipants(MainActivity mainActivity, boolean b, View view) {

        List<Pair> participants = new ArrayList<>(3);
        addNonNullViewToTransitionParticipants(view, participants);
        return participants.toArray(new Pair[participants.size()]);
    }

    private void addNonNullViewToTransitionParticipants(View view, List<Pair> participants) {

        if (view == null) {
            return;
        }
        participants.add(new Pair<>(view, view.getTransitionName()));
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

    /**
     * 设置，activity界面支持，左边一次滑出
     */
    private void setupWindowAnimations() {
        // Re-enter transition is executed when returning to this activity
        Slide slideTransition = new Slide();//滑出
        slideTransition.setSlideEdge(Gravity.LEFT);//滑出的方向
        slideTransition.setInterpolator(new DecelerateInterpolator());
        slideTransition.setDuration(500);//动画持续时间
        getWindow().setReenterTransition(slideTransition);//
        getWindow().setExitTransition(slideTransition);
    }

    @Override
    protected void onResume() {
        initData();
        super.onResume();
    }
}
