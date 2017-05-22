package com.jkxy.expressquery.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jkxy.expressquery.Constants;
import com.jkxy.expressquery.R;
import com.jkxy.expressquery.db.DBUtils;
import com.jkxy.expressquery.entity.ExpressNumberCheckBean;
import com.jkxy.expressquery.component.SelectPicturePopupWindow;
import com.jkxy.expressquery.utils.AutoGetNumberUtils;
import com.jkxy.expressquery.utils.ExpressNumberCheck;
import com.jkxy.expressquery.utils.RegularUtils;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.IOException;

public class AddInfoActivity extends AppCompatActivity implements View.OnClickListener, SelectPicturePopupWindow.OnSelectedListener {

    private Button mBtnSubmit, mBtnCheck, mBtnCancel;
    private EditText mEtExpressNumber;
    private CoordinatorLayout coordinatorLayout;
    private int[] mRadioButtons;
    private String number;
    private RadioButton btnOne, btnTwo, btnThree, btnFour;
    private RadioButton[] buttons;
    private RadioGroup mGroup;

    private ImageView camera;

    private SelectPicturePopupWindow select;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_info);
        initView();
        mBtnCheck.setOnClickListener(this);
        mBtnSubmit.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);
        camera.setOnClickListener(this);
        String str;
        if ((str = getIntent().getStringExtra("code")) != null) {
            mEtExpressNumber.setText(str);
        }

    }

    //初始化组件
    private void initView() {
        buttons = new RadioButton[]{btnOne, btnTwo, btnThree, btnFour};
        mRadioButtons = new int[]{R.id.radio_btn_01, R.id.radio_btn_02, R.id.radio_btn_03, R.id.radio_btn_04};

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.layoutRoot);
        mBtnSubmit = (Button) findViewById(R.id.btn_submit);
        mBtnCheck = (Button) findViewById(R.id.btn_check);
        mBtnCancel = (Button) findViewById(R.id.btn_cancel);
        mEtExpressNumber = (EditText) findViewById(R.id.et_express_number);
        mGroup = (RadioGroup) findViewById(R.id.checkbox_group);

        camera = (ImageView) findViewById(R.id.camera);

        Toolbar toolbar = (Toolbar) findViewById(R.id.addinfo_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                Log.d("提交", "提交");
                if (DBUtils.checkNumberExists(this, number)) {
                    Toast.makeText(this, "单号已存在", Toast.LENGTH_SHORT).show();
                } else {
                    String code = RegularUtils.parseExpressCode(
                            ((RadioButton) findViewById(mGroup.getCheckedRadioButtonId()))
                                    .getText().toString());
                    if (code != null) {
                        Intent intent = new Intent(this, DetailInfoActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("code", code);
                        bundle.putString("customRemark", "暂无");
                        bundle.putString("number", number);
                        bundle.putBoolean("flag", true);
                        intent.putExtras(bundle);
                        Toast.makeText(this, code, Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "查询异常", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.btn_check:
                number = mEtExpressNumber.getText().toString();
                new AsyncGetResult().execute(number);
                break;
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.camera:
                select = new SelectPicturePopupWindow(this);
                select.setOnSelectedListener(this);
                select.showPopupWindow(this);
                break;

        }
    }

    @Override
    public void OnSelected(View v, int position) {
        switch (position) {
            case 0:
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri uri = Uri.fromFile(new File(Constants.IMAGE_CACHE_PATH));
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, Constants.REQUEST_ACTION_IMAGE_CAPTURE);
                select.dismissPopupWindow();
                break;
            case 1:
                Crop.pickImage(this);
                select.dismissPopupWindow();
                break;
            case 2:
                if (select != null) {
                    select.dismissPopupWindow();
                }
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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        switch (requestCode) {
            case Constants.REQUEST_ACTION_IMAGE_CAPTURE:
                Intent i = new Intent(AddInfoActivity.this, LoadingActivity.class);
                i.putExtra("data", Constants.IMAGE_CACHE_PATH);
                startActivityForResult(i, Constants.REQUEST_LOAD_ACTIVITY);
                break;
            case Constants.REQUEST_LOAD_ACTIVITY:
                beginCrop(data.getData());
                break;
            case Crop.REQUEST_PICK:
                beginCrop(data.getData());
                break;
            case Crop.REQUEST_CROP:
                handleCrop(resultCode, data);
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleCrop(int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                try {
                    Bitmap bp = MediaStore.Images.Media.getBitmap(getContentResolver(), Crop.getOutput(data));
                    String num = AutoGetNumberUtils.getNumber(bp);
                    mEtExpressNumber.setText(num);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case RESULT_CANCELED:

                break;
            case Crop.RESULT_ERROR:
                Toast.makeText(this, Crop.getError(data).getMessage(), Toast.LENGTH_SHORT).show();
                break;

        }
    }

    private void beginCrop(Uri data) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(data, destination).withAspect(10, 1).start(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && select != null && select.getPopupState()) {
            select.dismissPopupWindow();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
