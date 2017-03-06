package com.jkxy.expressquery.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.jkxy.expressquery.component.SelectPicturePopupWindow;
import com.jkxy.expressquery.utils.AutoGetNumberUtils;
import com.jkxy.expressquery.utils.ExpressNumberCheck;
import com.jkxy.expressquery.utils.RegularUtils;
import com.soundcloud.android.crop.Crop;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static android.app.Activity.RESULT_CANCELED;

public class AddInfoActivity extends AppCompatActivity implements View.OnClickListener, SelectPicturePopupWindow.OnSelectedListener {

    private Button mBtnSubmit, mBtnCheck, mBtnCancel;
    private EditText mEtExpressNumber;
    private CoordinatorLayout coordinatorLayout;
    private int[] mRadioButtons;
    private String number;
    private RadioButton btnOne, btnTwo, btnStree, btnFour;
    private RadioButton[] buttons;
    private RadioGroup mGroup;

    private String sdPath;//SD卡的路径
    private String picPath;//图片存储路径

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

        sdPath = Environment.getExternalStorageDirectory().getPath();
        picPath = sdPath + "/" + "temp.jpg";
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
                Uri uri = Uri.fromFile(new File(picPath));
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

                FileInputStream fis;
                try {
                    fis = new FileInputStream(picPath);
                    Bitmap bitmap = BitmapFactory.decodeStream(fis);
                    Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null, null));
                    beginCrop(uri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                break;
            case Crop.REQUEST_PICK:
                beginCrop(data.getData());
                break;
            case Crop.REQUEST_CROP:
                handleCrop(resultCode, data);
                break;
        }

//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.aaa);
//        AutoGetNumberUtils.getNumber(bitmap);


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


    /**
     * 通过uri获取图片并进行压缩
     *
     * @param uri
     */
    public static Bitmap getBitmapFormUri(Activity ac, Uri uri) throws FileNotFoundException, IOException {
        InputStream input = ac.getContentResolver().openInputStream(uri);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        int originalWidth = onlyBoundsOptions.outWidth;
        int originalHeight = onlyBoundsOptions.outHeight;
        if ((originalWidth == -1) || (originalHeight == -1))
            return null;
        //图片分辨率以480x800为标准
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (originalWidth > originalHeight && originalWidth > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (originalWidth / ww);
        } else if (originalWidth < originalHeight && originalHeight > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (originalHeight / hh);
        }
        if (be <= 0)
            be = 1;
        //比例压缩
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = be;//设置缩放比例
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        input = ac.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();

        return compressImage(bitmap);//再进行质量压缩
    }

    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

}
