package com.jkxy.expressquery.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
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
import com.jkxy.expressquery.bean.ExpressNumberCheckBean;
import com.jkxy.expressquery.utils.AutoGetNumberUtils;
import com.jkxy.expressquery.utils.ExpressNumberCheck;
import com.jkxy.expressquery.utils.RegularUtils;
import com.jkxy.expressquery.utils.SelectPicturePopupWindow;

import java.io.File;
import java.util.Objects;

public class AddInfoActivity extends AppCompatActivity implements View.OnClickListener, SelectPicturePopupWindow.OnSelectedListener {

    private Button mBtnSubmit, mBtnCheck, mBtnCancel;
    private EditText mEtExpressNumber;
    private CoordinatorLayout coordinatorLayout;
    private int[] mRadioButtons;
    private String number;
    private RadioButton btnOne, btnTwo, btnStree, btnFour;
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

        camera = (ImageView) findViewById(R.id.camera);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                Log.d("提交", "提交");
                String code = RegularUtils.parseExpressCode(((RadioButton) findViewById(mGroup.getCheckedRadioButtonId())).getText().toString());
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
                String state = Environment.getExternalStorageState();
                if (Objects.equals(state, Environment.MEDIA_MOUNTED)) {
                    File path = Environment.getExternalStorageDirectory();
                    File file = new File(path, "ss.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                    startActivityForResult(intent, Constants.REQUEST_ACTION_IMAGE_CAPTURE);
                } else {
                    Toast.makeText(AddInfoActivity.this, "存储卡不可用", Toast.LENGTH_SHORT).show();
                }
                select.dismissPopupWindow();
                break;
            case 1:
                Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);
                getImage.addCategory(Intent.CATEGORY_OPENABLE);
                getImage.setType("image/*");
                startActivityForResult(getImage, Constants.REQUEST_ACTION_GET_CONTENT);
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

                String state = Environment.getExternalStorageState();
                if (state.equals(Environment.MEDIA_MOUNTED)) {
                    File path = Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                    File tempFile = new File(path, "ss.jpg");
                    startPhotoZoom(Uri.fromFile(tempFile));
                } else {
                    Toast.makeText(getApplicationContext(),
                            "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
                }
                break;
            case Constants.REQUEST_ACTION_GET_CONTENT:

                break;
            case Constants.REQUEST_CROP_CODE:
                if (data != null) {
                    getImageToView(data);
                }
                break;
        }

//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.aaa);
//        AutoGetNumberUtils.getNumber(bitmap);


        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getImageToView(Intent data) {

        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
//            Drawable drawable = new BitmapDrawable(this.getResources(), photo);
            String str = AutoGetNumberUtils.getNumber(photo);
            mEtExpressNumber.setText(str);
        }
    }

    private void startPhotoZoom(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 340);
        intent.putExtra("outputY", 340);
        intent.putExtra("return-data", false);
        startActivityForResult(intent, Constants.REQUEST_CROP_CODE);
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
