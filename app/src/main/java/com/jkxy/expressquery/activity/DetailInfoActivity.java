package com.jkxy.expressquery.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.ArcMotion;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.util.Log;

import com.google.gson.Gson;
import com.jkxy.expressquery.R;
import com.jkxy.expressquery.bean.JsonRootBean;
import com.jkxy.expressquery.utils.GetExpressInfo;

import java.util.List;

public class DetailInfoActivity extends AppCompatActivity {

    private String code;
    private String number;
    private boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_info);

          initData();

        if (flag) {
            Log.d("进入查询","查询");
            new AsyncGetInfo().execute(code, number);
        }

        setUpWindowTransition();
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
        System.out.println(code + ":" + number + flag);
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

            Gson g = new Gson();
            JsonRootBean b = g.fromJson(s, JsonRootBean.class);
            // TODO: 16/9/24 获取到快递信息
            List<JsonRootBean.Traces> traces = b.getTraces();
            for (JsonRootBean.Traces m :traces) {
                Log.d("快递信息","时间:"+m.AcceptTime +"\n"+"信息:"+m.AcceptStation);
            }
            super.onPostExecute(s);
        }
    }
}
