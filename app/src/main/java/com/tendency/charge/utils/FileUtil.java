package com.tendency.charge.utils;

import android.content.Context;

import com.parry.utils.code.FileUtils;

public class FileUtil {

    /**删除拍照缓存
     * @param context
     */
    public static void deleteImageFile(Context context){
        try{
            String VM_PATH = context.getExternalFilesDir("Pictures").getPath();
           if(FileUtils.isFileExists(VM_PATH)) {
               FileUtils.deleteAllInDir(VM_PATH);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
