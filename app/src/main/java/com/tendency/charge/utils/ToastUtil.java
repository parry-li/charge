package com.tendency.charge.utils;

import com.parry.utils.code.ToastUtils;

public class ToastUtil {
    public static void showFW(String msg) {
        ToastUtils.showShort("服务提示：" + msg);
    }

    public static void showWX(String msg) {
        ToastUtils.showShort("温馨提示：" + msg);
    }
}
