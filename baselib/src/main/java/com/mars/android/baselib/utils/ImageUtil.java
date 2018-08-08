package com.mars.android.baselib.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

public class ImageUtil {

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;

        if (width < height) {
            int temp = width;
            width = height;
            height = temp;
        }
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static byte[] image2Bytes(String srcPath) {
        Bitmap bitmap = null;
        try {
            bitmap = getSampleBitmapFromFile(srcPath, 800, 480);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
            return stream.toByteArray();
        } catch (Error e) {
            LogUtil.printeException(e);
        } finally {
            if (bitmap != null) {
                bitmap.recycle();
            }
        }
        return null;
    }


    public static Bitmap getSampleBitmapFromFile(String bitmapFilePath, int reqWidth, int reqHeight) {
        try {
            File f = new File(bitmapFilePath);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, options);
            int scale = calculateInSampleSize(options, reqWidth, reqHeight);
            options.inJustDecodeBounds = false;
            options.inSampleSize = scale;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            return  BitmapFactory.decodeStream(new FileInputStream(f), null, options);
        } catch (Exception e) {
            LogUtil.printeException(e);
        } catch (Error e1) {
            LogUtil.printeException(e1);
        }
        return null;
    }

}
