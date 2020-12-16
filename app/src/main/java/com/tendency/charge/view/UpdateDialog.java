package com.tendency.charge.view;

import android.app.Activity;
import android.app.Dialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.tendency.charge.R;
import com.tendency.charge.utils.UIUtils;


/**
 * @author parry
 * @des ${TODO}
 */

public class UpdateDialog {


    private Activity context;
    private TextView dialogAffirm;
    private ImageView dialogCancel;
    private Dialog dialog;

    public UpdateDialog(Activity context) {
        this.context = context;
    }


    public void showCustomWindowDialog( String content, boolean isHideCancel) {
       dialog = new Dialog(context, R.style.TANCStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        View convertView = LayoutInflater.from(context)
                .inflate(R.layout.item_update_window, null);
        dialog.setContentView(convertView);

        TextView dialogContent = (TextView) convertView.findViewById(R.id.content_tv);
        dialogAffirm = (TextView) convertView.findViewById(R.id.button_confirm);
        dialogCancel = (ImageView) convertView.findViewById(R.id.button_cancel);

        if(isHideCancel){
            dialogCancel.setVisibility(View.GONE);
        }else {
            dialogCancel.setVisibility(View.VISIBLE);
        }
        dialogContent.setText(content);

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