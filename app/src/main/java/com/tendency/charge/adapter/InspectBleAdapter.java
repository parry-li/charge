package com.tendency.charge.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.clj.fastble.data.BleDevice;
import com.tendency.charge.R;
import com.tendency.charge.bean.CheckBean;


import java.util.ArrayList;
import java.util.List;


public class InspectBleAdapter extends BaseQuickAdapter<BleDevice, BaseViewHolder> {


    private boolean isSHowX;

    private List<CheckBean> checkBeanList = new ArrayList<>();

    public InspectBleAdapter(List<BleDevice> data) {
        super(R.layout.item_inspect_ble, data);
        isSHowX = true;

    }


    @Override
    protected void convert(final BaseViewHolder helper, final BleDevice item) {
        final int position = helper.getLayoutPosition();
        helper.setText(R.id.item_name, item.getName());
        ImageView imageIv = helper.getView(R.id.item_iv);
        if (checkBeanList != null && checkBeanList.size() > position) {
            if (checkBeanList.get(position).isCheck()) {
                imageIv.setVisibility(View.VISIBLE);
            } else {
                imageIv.setVisibility(View.INVISIBLE);
            }
        } else {
            imageIv.setVisibility(View.INVISIBLE);
        }


        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCheckList(position);
                if (confirmClickListener != null) {
                    confirmClickListener.onItemClickListener(item);
                }

            }
        });
    }


    private void setCheckList(int position) {
        int msize = checkBeanList.size();
        for (int i = 0; i < msize; i++) {
            if (i == position) {
                checkBeanList.get(i).setCheck(true);
            } else {
                checkBeanList.get(i).setCheck(false);
            }

        }
        notifyDataSetChanged();
    }

    public void initCheckList() {
        int msize = checkBeanList.size();
        for (int i = 0; i < msize; i++) {
            checkBeanList.get(i).setCheck(false);


        }
        notifyDataSetChanged();
    }

    public List<CheckBean> getCheckList() {
        return checkBeanList;
    }

    public void addNewBle(BleDevice bleDevice) {
        checkBeanList.add(new CheckBean(checkBeanList.size(), false));
        addData(bleDevice);
    }

    public void setNewDataForSearch(List<BleDevice> bleDevices) {
        checkBeanList = new ArrayList<>();
        if (bleDevices != null && bleDevices.size() > 0) {
            int bleSize = bleDevices.size();
            for (int i = 0; i < bleSize; i++) {
                checkBeanList.add(new CheckBean(i,false));
            }
        }

        setNewData(bleDevices);
    }


    OnItemConfirmClickListener confirmClickListener;

    public void setOnItemClickListener(OnItemConfirmClickListener confirmClickListener) {
        this.confirmClickListener = confirmClickListener;
    }

    public interface OnItemConfirmClickListener {
        void onItemClickListener(BleDevice device);
    }

}
