package com.tendency.charge.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import com.tendency.charge.R;


/**
 * @author parry
 * @time 2017/6/7/007 14:20
 * @des ${TODO}
 */

public class CustomProgressDialog extends ProgressDialog {

    private static CustomProgressDialog d;

    public CustomProgressDialog(Context context) {
        super(context);
    }

    public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_progress);
    }

    public static CustomProgressDialog showDialog(Context ctx) {
        d = new CustomProgressDialog(ctx);
        d.setCanceledOnTouchOutside(false);
//        d.getWindow();
        d.getWindow().setBackgroundDrawableResource(R.color.transparent);
        d.show();
        return d;
    }

    public static void dismissDialog() {
        if (null != d && d.isShowing()) {
            d.dismiss();
        }
    }
    public static void setCanceledOnTouchOutsideDialog() {
        if (null != d && d.isShowing()) {
            d.setCanceledOnTouchOutside(false);
        }
    }
}