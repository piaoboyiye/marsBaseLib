package com.mars.android.baselib.utils;


import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;

public class DataCleanManager {

    public static void cleanInternalCache(Context context) {
        deleteFilesByDirectory(context.getCacheDir());
    }

    public static void cleanExternalCache(Context context) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            deleteFilesByDirectory(context.getExternalCacheDir());
        }
    }

    public static void cleanFiles(Context context) {
        deleteFilesByDirectory(context.getFilesDir());
    }

    public static void cleanCustomCache(String filePath) {
        try {
            deleteFilesByDirectory(new File(filePath));
        } catch (Exception e) {
            LogUtil.printeException(e);
        }
    }

    public static void deleteFilesByDirectory(File directory) {
        if (directory != null) {
            File files[] = directory.listFiles();
            if (files != null)
                for (File f : files) {
                    if (f.isDirectory()) {
                        deleteFilesByDirectory(f);
                        try {
                            f.delete();
                        } catch (Exception e) {
                        }
                    } else {
                        if (f.exists()) { // 判断是否存在
                            deleteFilesByDirectory(f);
                            try {
                                f.delete();
                            } catch (Exception e) {
                            }
                        }
                    }
                }
        }
    }

    public static long getFolderSize(File file) {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            LogUtil.printeException(e);
        }
        return size;
    }

    public static String getFormatSize(long fileS) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#0.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    public static String getAppCacheSize(Context mContext, String customFilePath) {
        long internalCache = getFolderSize(mContext.getCacheDir());
        long externalCache = 0;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            externalCache = getFolderSize(mContext.getExternalCacheDir());
        }
        long fileCache = getFolderSize(mContext.getFilesDir());
        long customFile = 0;
        if (!TextUtils.isEmpty(customFilePath)) {
            customFile = getFolderSize(new File(customFilePath));
        }
        return getFormatSize(internalCache + externalCache + fileCache + customFile);
    }

    public static void cleanAppCache(Context mContext, String customFilePath) {
        cleanInternalCache(mContext);
        cleanExternalCache(mContext);
        cleanFiles(mContext);
        if (!TextUtils.isEmpty(customFilePath)) {
            cleanCustomCache(customFilePath);
        }
    }


}
