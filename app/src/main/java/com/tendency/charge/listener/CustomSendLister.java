package com.tendency.charge.listener;

import android.graphics.Bitmap;

public interface CustomSendLister {
     void sendResult(Boolean isSuccess, int position, String value);
     void sendResultOri(Bitmap bitmap);
}
