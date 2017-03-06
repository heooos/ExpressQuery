package com.jkxy.expressquery.component;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.jkxy.expressquery.R;

/**
 * Created by zh on 2017/2/26.
 */

public class SelectPicturePopupWindow implements View.OnClickListener {

    View mMenuView;
    Button takePhotoBtn, pickPictureBtn, cancelBtn;
    private OnSelectedListener listener;
    private PopupWindow popupWindow;
    private boolean popupWindowState = false;


    public SelectPicturePopupWindow(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        mMenuView = inflater.inflate(R.layout.layout_picture_selector, null);

        takePhotoBtn = (Button) mMenuView.findViewById(R.id.picture_selector_take_photo_btn);
        pickPictureBtn = (Button) mMenuView.findViewById(R.id.picture_selector_pick_picture_btn);
        cancelBtn = (Button) mMenuView.findViewById(R.id.picture_selector_cancel_btn);
        // 设置按钮监听
        takePhotoBtn.setOnClickListener(this);
        pickPictureBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.picture_selector_take_photo_btn:
                if (null != listener) {
                    listener.OnSelected(v, 0);
                }
                break;
            case R.id.picture_selector_pick_picture_btn:
                if (null != listener) {
                    listener.OnSelected(v, 1);
                }
                break;
            case R.id.picture_selector_cancel_btn:
                if (null != listener) {
                    listener.OnSelected(v, 2);
                }
                break;
        }
    }

    public interface OnSelectedListener {
        void OnSelected(View v, int position);
    }

    public void setOnSelectedListener(OnSelectedListener listener) {
        this.listener = listener;
    }

    /**
     * 把一个View控件添加到PopupWindow上并且显示
     *
     * @param activity
     */
    public void showPopupWindow(Activity activity) {
        popupWindow = new PopupWindow(mMenuView, // 添加到popupWindow
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.CENTER | Gravity.BOTTOM, 0, 0);
        popupWindow.setAnimationStyle(android.R.style.Animation_InputMethod); // 设置窗口显示的动画效果
        popupWindow.setFocusable(false); // 点击其他地方隐藏键盘 popupWindow
        popupWindow.update();
        popupWindowState = true;
    }

    /**
     * 移除PopupWindow
     */
    public void dismissPopupWindow() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
            popupWindowState = false;
        }
    }

    public boolean getPopupState(){
        return popupWindowState;
    }

}
