package com.jkxy.expressquery.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jkxy.expressquery.R;
import com.jkxy.expressquery.bean.ExpressNumberCheckBean;
import com.jkxy.expressquery.utils.ExpressNumberCheck;
import com.jkxy.expressquery.utils.RegularUtils;

public class AddInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtnSubmit, mBtnCheck, mBtnCancel;
    private EditText mEtExpressNumber;
    private CoordinatorLayout coordinatorLayout;
    private int[] mRadioButtons;
    private String number;
    private RadioButton btnOne, btnTwo, btnStree, btnFour;
    private RadioButton[] buttons;
    private RadioGroup mGroup;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_info);


        initView();

        mBtnCheck.setOnClickListener(this);
        mBtnSubmit.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);
    }
    //初始化组件
    private void initView() {
        buttons = new RadioButton[]{btnOne, btnTwo, btnStree, btnFour};
        mRadioButtons = new int[]{R.id.radio_btn_01, R.id.radio_btn_02, R.id.radio_btn_03, R.id.radio_btn_04};

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.layoutRoot);
        mBtnSubmit = (Button) findViewById(R.id.btn_submit);
        mBtnCheck = (Button) findViewById(R.id.btn_check);
        mBtnCancel = (Button) findViewById(R.id.btn_cancel);
        mEtExpressNumber = (EditText) findViewById(R.id.et_express_number);
        mGroup = (RadioGroup) findViewById(R.id.checkbox_group);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                Log.d("提交","提交");
                String code = RegularUtils.parseExpressCode(((RadioButton) findViewById(mGroup.getCheckedRadioButtonId())).getText().toString());
                if (code != null){
                    Intent intent = new Intent(this,DetailInfoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("code",code);
                    bundle.putString("number", number);
                    bundle.putBoolean("flag",true);
                    intent.putExtras(bundle);
                    Toast.makeText(this,code,Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(this,"查询异常",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_check:
                number = mEtExpressNumber.getText().toString();
                new AsyncGetResult().execute(number);
                break;
            case R.id.btn_cancel:
                finish();
                break;
        }
    }


    class AsyncGetResult extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            try {
                result = new ExpressNumberCheck(strings[0]).getOrderTracesByJson();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("s", s);
            Gson gson = new Gson();
            ExpressNumberCheckBean bean = gson.fromJson(s, ExpressNumberCheckBean.class);
            if (bean.Success) {
                int len = bean.Shippers.size();
                if (len != 0) {
                    mBtnSubmit.setEnabled(true);
                    for (int i = 0; i < len; i++) {
                        RadioButton rb = (RadioButton) findViewById(mRadioButtons[i]);
                        String info = bean.Shippers.get(i).ShipperName + ":" + bean.Shippers.get(i).ShipperCode;
                        rb.setText(info);
                        rb.setVisibility(View.VISIBLE);
                        buttons[i] = rb;
                    }
                    Snackbar.make(coordinatorLayout, "请选择快递公司并提交", Snackbar.LENGTH_SHORT).show();
                } else {
                    mBtnSubmit.setEnabled(false);
                    Snackbar.make(coordinatorLayout, "请检查快递单号是否正确", Snackbar.LENGTH_SHORT).show();
                }
            } else {
                mBtnSubmit.setEnabled(false);
                Snackbar.make(coordinatorLayout, "查询失败:" + bean.Code, Snackbar.LENGTH_SHORT).show();
            }


            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            super.onProgressUpdate(values);
        }
    }
}
