package com.jkxy.expressquery.component;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.jkxy.expressquery.R;
import com.jkxy.expressquery.impl.IDialogButtonClickListener;

/**
 * Created by zh on 2017/3/6.
 */

public class ChangeCustomRemark {

    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private IDialogButtonClickListener listener;

    public void showDialog(final Activity activity) {
        builder = new AlertDialog.Builder(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog, null);
        final EditText tv = (EditText) view.findViewById(R.id.et_custom_remark);
        builder.setView(view);
        builder.setTitle("修改备注");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (listener != null) {
                    listener.onPositiveButtonClicked(tv.getText().toString());
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog = builder.create();

        alertDialog.show();
    }

    public void setListener(IDialogButtonClickListener l) {
        this.listener = l;
    }

    public void dismiss() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
            alertDialog = null;
        }
    }
}
