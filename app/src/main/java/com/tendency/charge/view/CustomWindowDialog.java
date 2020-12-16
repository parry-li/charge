package com.tendency.charge.view;

import android.app.Activity;
import android.app.Dialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.tendency.charge.R;
import com.tendency.charge.utils.UIUtils;


/**
 * @author parry
 * @time 2017/6/7/007 14:20
 * @des ${TODO}
 */

public class CustomWindowDialog {


    private Activity context;
    private TextView dialogAffirm;
    private TextView dialogCancel;
    private Dialog dialog;

    public CustomWindowDialog(Activity context) {
        this.context = context;
    }

    public void showCustomWindowDialog(String title, String content, boolean isHideCancel) {
        showCustomWindowDialog(title, content, isHideCancel, false);
    }

    public void showCustomWindowDialog(String title, String content, boolean isHideCancel, Boolean isHideAffirm) {


       dialog = new Dialog(context, R.style.TANCStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        View convertView = LayoutInflater.from(context)
                .inflate(R.layout.item_custom_window, null);
        dialog.setContentView(convertView);

        TextView dialogContent = (TextView) convertView.findViewById(R.id.dialog_content);
        dialogAffirm = (TextView) convertView.findViewById(R.id.dialog_affirm);
        dialogCancel = (TextView) convertView.findViewById(R.id.dialog_cancel);
        TextView dialogTitle = (TextView) convertView.findViewById(R.id.dialog_title);
        View bottomV = (View) convertView.findViewById(R.id.bottom_v);


        dialogTitle.setText(title);
        dialogContent.setText(content);


        if (isHideCancel) {
            dialogCancel.setVisibility(View.GONE);
            bottomV.setVisibility(View.GONE);
        } else {
            dialogCancel.setVisibility(View.VISIBLE);
        }

        if (isHideAffirm) {
            dialogAffirm.setVisibility(View.GONE);
            bottomV.setVisibility(View.GONE);
        } else {
            dialogAffirm.setVisibility(View.VISIBLE);
        }

        dialogAffirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (onItemClickListener != null) {
                    onItemClickListener.onCustomDialogClickListener();
                }
            }
        });

        dialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    public void setAffirmText(String text) {
        if(dialogAffirm!=null){
            dialogAffirm.setText(text);
        }
    }
    public void setCancelText(String text) {
        if(dialogCancel!=null){
            dialogCancel.setText(text);
        }
    }

    public boolean isDialogShowing(){

        if(dialog==null){
            return false;
        }
        return dialog.isShowing();
    }

    OnItemClickListener onItemClickListener;
    OnItemCancelClickListener onCancelClickListener;

    public void setOnCustomDialogClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnCancelDialogClickListener(OnItemCancelClickListener onCancelClickListener) {
        this.onCancelClickListener = onCancelClickListener;
    }

    public interface OnItemClickListener {
        void onCustomDialogClickListener();
    }

    public interface OnItemCancelClickListener {
        void onCancelDialogClickListener();
    }
}