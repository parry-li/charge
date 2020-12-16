package com.tendency.charge.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;


import com.parry.utils.code.ImageUtils;
import com.parry.utils.code.ToastUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by parry
 */
public class BitmapUtils {

    private static Bitmap orc_bitmap;

    public static Bitmap circleBitmap(Bitmap source) {
        int width = source.getWidth();
        Bitmap bitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawCircle(width / 2, width / 2, width / 2, paint);

        //设置图片相交情况下的处理方式
        //setXfermode：设置当绘制的图像出现相交情况时候的处理方式的,它包含的常用模式有：
        //PorterDuff.Mode.SRC_IN 取两层图像交集部分,只显示上层图像
        //PorterDuff.Mode.DST_IN 取两层图像交集部分,只显示下层图像
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //在画布上绘制bitmap
        canvas.drawBitmap(source, 0, 0, paint);

        return bitmap;

    }

    //实现图片的压缩处理
    //设置宽高必须使用浮点型，否则导致压缩的比例：0
    public static Bitmap zoom(Bitmap source, float width, float height) {

        Matrix matrix = new Matrix();
        //图片的压缩处理
        matrix.postScale(width / source.getWidth(), height / source.getHeight());

        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, false);
    }


    public static Bitmap imageViewCircleBit(ImageView view, String imgPath) {
        // Get the dimensions of the View
        int targetW = view.getWidth();
        int targetH = view.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imgPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
//        orc_bitmap = zoom(bitmap,480,800); //压缩图片
        orc_bitmap =  ImageUtils.compressByQuality(bitmap,50,false);
        orc_bitmap = ImgUpdateDirection(imgPath);
//        orc_bitmap =centerSquareScaleBitmap(orc_bitmap,480);

        view.setImageBitmap(orc_bitmap);
        return orc_bitmap;
    }

    /**截取中间图片
     * @param bitmap
     * @param edgeLength
     * @return
     */
    public static Bitmap centerSquareScaleBitmap(Bitmap bitmap, int edgeLength)
    {
        if(null == bitmap || edgeLength <= 0)
        {
            return  null;
        }

        Bitmap result = bitmap;
        int widthOrg = bitmap.getWidth();
        int heightOrg = bitmap.getHeight();

        if(widthOrg > edgeLength && heightOrg > edgeLength)
        {
            //压缩到一个最小长度是edgeLength的bitmap
            int longerEdge = (int)(edgeLength * Math.max(widthOrg, heightOrg) / Math.min(widthOrg, heightOrg));

            int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
            int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
            Bitmap scaledBitmap;

            try{
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
            }
            catch(Exception e){
                return null;
            }

            //从图中截取正中间的正方形部分。
            int xTopLeft = (scaledWidth - edgeLength) / 2;
            int yTopLeft = (scaledHeight - edgeLength) / 2;

            try{
                result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, edgeLength, edgeLength);
                scaledBitmap.recycle();
            }
            catch(Exception e){
                return null;
            }
        }

        return result;
    }

    /**
     * ImageView设置优化内存使用后的Bitmap
     * 返回一个等同于ImageView宽高的bitmap
     *
     * @param view     ImageView
     * @param imgPath  图像路径
     * @param
     */
    public static Bitmap imageViewSetPic(final ImageView view, final String imgPath) {


                // Get the dimensions of the View
                int targetW = view.getWidth();
                int targetH = view.getHeight();

                // Get the dimensions of the bitmap
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(imgPath, bmOptions);
                int photoW = bmOptions.outWidth;
                int photoH = bmOptions.outHeight;


                // Determine how much to scale down the image
                int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

                // Decode the image file into a Bitmap sized to fill the View
                bmOptions.inJustDecodeBounds = false;
                bmOptions.inSampleSize = scaleFactor;
                bmOptions.inPurgeable = true;

                Bitmap bitmap = BitmapFactory.decodeFile(imgPath, bmOptions);
//        Bitmap circleImage = BitmapUtils.circleBitmap(bitmap);
//        view.setBackground(new BitmapDrawable(bitmap));
                orc_bitmap =  ImageUtils.compressByQuality(bitmap,30,false);
//        orc_bitmap = zoom(bitmap,400,600); //压缩图片
                orc_bitmap = ImgUpdateDirection(imgPath);



        if(view != null){
            view.setBackground(new BitmapDrawable(orc_bitmap));
        }

        return orc_bitmap;
    }

    /**
     * ImageView设置优化内存使用后的Bitmap
     * 返回一个等同于ImageView宽高的bitmap
     *
     * @param view     ImageView
     * @param imgPath  图像路径
     * @param
     */
    public static Bitmap imageViewSetThreadPic(final ImageView view, final String imgPath) {



// 设置参数
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 只获取图片的大小信息，而不是将整张图片载入在内存中，避免内存溢出
        BitmapFactory.decodeFile(imgPath, options);
        int height = options.outHeight;
        int width= options.outWidth;
        int inSampleSize = 2; // 默认像素压缩比例，压缩为原图的1/2
        int minLen = Math.min(height, width); // 原图的最小边长
        if(minLen > 700) { // 如果原始图像的最小边长大于100dp（此处单位我认为是dp，而非px）
            float ratio = (float)minLen / 700.0f; // 计算像素压缩比例
            inSampleSize = (int)ratio;
        }
        options.inJustDecodeBounds = false; // 计算好压缩比例后，这次可以去加载原图了
        options.inSampleSize = inSampleSize; // 设置为刚才计算的压缩比例
        Bitmap bm = BitmapFactory.decodeFile(imgPath, options); // 解码文件
//        Log.w("TAG", "size: " + bm.getByteCount() + " width: " + bm.getWidth() + " heigth:" + bm.getHeight()); // 输出图像数据


        return bm;
    }




    private File outputImagepath;//存储拍完照后的图片
//    private Bitmap orc_bitmap;//拍照和相册获取图片的Bitmap
    /**
     * 拍完照和从相册获取玩图片都要执行的方法(根据图片路径显示图片)
     */
    private static void displayImage(String imagePath) {
        if (!TextUtils.isEmpty(imagePath)) {
            //orc_bitmap = BitmapFactory.decodeFile(imagePath);//获取图片

//            orc_bitmap = comp(BitmapFactory.decodeFile(imagePath)); //压缩图片
            orc_bitmap = zoom(BitmapFactory.decodeFile(imagePath),600,800); //压缩图片
            ImgUpdateDirection(imagePath);//显示图片,并且判断图片显示的方向,如果不正就放正
        } else {
//            Toast.makeText(this, "图片获取失败", Toast.LENGTH_LONG).show();
            ToastUtils.showShort( "图片获取失败");
        }


    }


    //改变拍完照后图片方向不正的问题
    public static Bitmap ImgUpdateDirection(String filepath) {
        int digree = 0;//图片旋转的角度
        //根据图片的URI获取图片的绝对路径
        Log.i("tag", ">>>>>>>>>>>>>开始");
        //String filepath = ImgUriDoString.getRealFilePath(getApplicationContext(), uri);
        Log.i("tag", "》》》》》》》》》》》》》》》" + filepath);
        //根据图片的filepath获取到一个ExifInterface的对象
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filepath);
            Log.i("tag", "exif》》》》》》》》》》》》》》》" + exif);
            if (exif != null) {

                // 读取图片中相机方向信息
                int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

                // 计算旋转角度
                switch (ori) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        digree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        digree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        digree = 270;
                        break;
                    default:
                        digree = 0;
                        break;

                }

            }
            //如果图片不为0
            if (digree != 0) {
                // 旋转图片
                Matrix m = new Matrix();
                m.postRotate(digree);
                orc_bitmap = Bitmap.createBitmap(orc_bitmap, 0, 0, orc_bitmap.getWidth(),
                        orc_bitmap.getHeight(), m, true);
            }
            if (orc_bitmap != null) {


//                ylMeHeadImage.setImageBitmap(BitmapUtils.circleBitmap(orc_bitmap));
//                //                imageView.setImageBitmap(orc_bitmap);
//                sendHeadImage(orc_bitmap);
            }
        } catch (IOException e) {
            e.printStackTrace();
            exif = null;
        }
        return orc_bitmap;
    }


}
