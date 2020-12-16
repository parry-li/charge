package com.tendency.charge.view;

import android.app.Activity;
import android.app.Dialog;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.parry.utils.code.KeyboardUtils;
import com.tendency.charge.R;
import com.tendency.charge.utils.AllCapTransformationMethod;
import com.tendency.charge.utils.ToastUtil;
import com.tendency.charge.utils.UIUtils;


/**
 * @author parry
 * @time 2017/6/7/007 14:20
 * @des ${TODO}
 */

public class ScanDialog {


    private Activity context;
    private Dialog dialog;

    public ScanDialog(Activity context) {
        this.context = context;
    }


    public void showCustomWindowDialog(String buttonName) {


        dialog = new Dialog(context, R.style.TANCStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        View convertView = LayoutInflater.from(context)
                .inflate(R.layout.dialog_input_plate, null);
        dialog.setContentView(convertView);

        final NullMenuEditText editContent = (NullMenuEditText) convertView.findViewById(R.id.edit_content);
        final NullMenuEditText editContentConfirm = (NullMenuEditText) convertView.findViewById(R.id.edit_contentConfirm);
        ImageView dialogCancel = (ImageView) convertView.findViewById(R.id.dialog_cancel);
        TextView dialogAffirm = (TextView) convertView.findViewById(R.id.dialog_affirm);
        TextView dialogTitle = (TextView) convertView.findViewById(R.id.dialog_title);
        editContent.setTransformationMethod(new AllCapTransformationMethod(true));
        editContentConfirm.setTransformationMethod(new AllCapTransformationMethod(true));

        if (!TextUtils.isEmpty(buttonName)) {
            dialogTitle.setText("请输入" + buttonName);
            editContent.setHint("请输入" + buttonName);
            editContentConfirm.setHint("请确认" + buttonName);
        }


        dialogAffirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardUtils.hideSoftInput(context);
                String contentStr = editContent.getText().toString().trim().toUpperCase();
                String contentConfirmStr = editContentConfirm.getText().toString().trim().toUpperCase();
                if (TextUtils.isEmpty(contentStr) || TextUtils.isEmpty(contentConfirmStr)) {
                    ToastUtil.showWX("请输入信息");
                } else {
                    if (contentStr.equals(contentConfirmStr)) {
                        dialog.dismiss();
                        if (onItemClickListener != null) {
                            onItemClickListener.onCustomDialogClickListener(contentStr);
                        }
                    } else {
                        ToastUtil.showWX("两次信息输入不一致，请重新输入");
                    }

                }

            }
        });

        dialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardUtils.hideSoftInput(context);
                dialog.dismiss();
                if (onCancelClickListener != null) {
                    onCancelClickListener.onCancelDialogClickListener();
                }
            }
        });

        WindowManager windowManager = context.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (display.getWidth() - UIUtils.dp2px(40)); //设置宽度
        dialog.getWindow().setAttributes(lp);
        dialog.setCancelable(false);
        dialog.show();

    }

    public boolean isShowing() {
        if (dialog == null) {
            return false;
        }
        return dialog.isShowing();
    }

    OnItemConfirmClickListener onItemClickListener;
    OnItemCancelClickListener onCancelClickListener;

    public void setOnCustomDialogClickListener(OnItemConfirmClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnCancelDialogClickListener(OnItemCancelClickListener onCancelClickListener) {
        this.onCancelClickListener = onCancelClickListener;
    }

    public interface OnItemConfirmClickListener {
        void onCustomDialogClickListener(String content);
    }

    public interface OnItemCancelClickListener {
        void onCancelDialogClickListener();
    }
}