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

import com.jkxy.expressquery.R;
import com.jkxy.expressquery.adapter.CustomAdapter;
import com.jkxy.expressquery.bean.ListInfoBean;

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
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        initViews();
        initData();


        mAdapter = new CustomAdapter(this, mDatas);
        mRecyclerView.setAdapter(mAdapter);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        initEvent();
        setupWindowAnimations();
    }

    private void initEvent() {
        mFab.setOnClickListener(this);
        mAdapter.setOnItemClickListener(new CustomAdapter.onRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                // TODO: 16/9/20 RecyclerView点击事件
                //Toast.makeText(MainActivity.this, "" + (position) + ":" + mDatas.get(position), Toast.LENGTH_SHORT).show();
                // TODO: 2016/11/8 数据获取
                Bundle bundle = new Bundle();
                transitionToActivity(DetailInfoActivity.class, v.findViewById(R.id.img_logo));
            }
        });
    }

    private void transitionToActivity(Class target, View view) {

        final Pair[] pairs = createSafeTransitionParticipants(this, false, view);
        beginActivity(target, pairs);
    }

    private void beginActivity(Class target, Pair[] pairs) {

        Intent i = new Intent(MainActivity.this, target);

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
        mDatas = new ArrayList<>();
        // TODO: 2016/10/18 数据初始化

        for (int i = 0; i < 20; i++) {
            ListInfoBean infoBean = new ListInfoBean("SF", "123456789", "2016-09-24", 0, "袜子");
            mDatas.add(infoBean);
        }

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

}
