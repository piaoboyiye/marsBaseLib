package com.mars.android.baselib.utils;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebSettings;

import com.mars.android.baselib.BaseApplication;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class BaseAppUtil {
    private static long backTime = 0;

    public static boolean exitApp() {
        if (System.currentTimeMillis() - backTime <= 2000) {
            return true;
        } else {
            backTime = System.currentTimeMillis();
            ToastAlone.show("再按一次退出程序");
            return false;
        }
    }

    public static String getAppVersion() {
        String appVersion = null;
        PackageManager manager = BaseApplication.mContext.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(BaseApplication.mContext.getPackageName(), 0);
            appVersion = "V " + info.versionName;
        } catch (Exception e) {
            LogUtil.printeException(e);
        }
        return appVersion;
    }


    public static String md5Encrypt(String string) {
        try {
            if (TextUtils.isEmpty(string)) {
                return null;
            }
            byte[] hash = null;
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
            StringBuilder hex = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                int i = (b & 0xFF);
                if (i < 0x10) hex.append('0');
                hex.append(Integer.toHexString(i));
            }
            return hex.toString();
        } catch (Exception e) {
            LogUtil.printeException(e);
        }
        return null;
    }

    public static int getStatusBarHeight(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static boolean checkPhoneNum(String phoneNum) {
        if (TextUtils.isEmpty(phoneNum)) {
            ToastAlone.show("手机号不能为空");
            return false;
        }
        Pattern p = Pattern.compile("^[1][0-9]{10}$");
        Matcher m = p.matcher(phoneNum);
        boolean b = m.matches();
        if (!b) {
            ToastAlone.show("请输入11位手机号");
        }
        return b;
    }

    public static boolean checkPhoneNumSingle(String phoneNum) {
        Pattern p = Pattern.compile("^[1][0-9]{10}$");
        Matcher m = p.matcher(phoneNum);
        return m.matches();
    }

    public static boolean checkPwd(String pwd, int minSize, int maxSize) {
        if (TextUtils.isEmpty(pwd)) {
            ToastAlone.show("密码不能为空");
            return false;
        }
        if (pwd.length() < minSize) {
            ToastAlone.show("密码不能少于" + minSize + "位");
            return false;
        }
        if (pwd.length() > maxSize) {
            ToastAlone.show("密码不能超过" + maxSize + "位");
            return false;
        }
        return true;
    }

    public static boolean checkCode(String code, int codeSize) {
        if (TextUtils.isEmpty(code)) {
            ToastAlone.show("验证码不能为空");
            return false;
        }
        if (code.length() != codeSize) {
            ToastAlone.show("请输入" + codeSize + "位验证码");
            return false;
        }
        return true;
    }

    public static String getDeviceModel() {
        String deviceModel = "";
        try {
            deviceModel = URLEncoder.encode(Build.MODEL, "utf-8");
        } catch (Exception e) {
            LogUtil.printeException(e);
        }
        return deviceModel;
    }

    public static String formateDoubleNum(double num) {
        return new DecimalFormat("#0.00").format(num);
    }

    public static double formateDouble(double num) {
        BigDecimal bg = new BigDecimal(num);
        return bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static String formateDate(long time, String formateStr) {
        SimpleDateFormat myFmt2 = new SimpleDateFormat(formateStr);
        return myFmt2.format(new Date(time));
    }

    public static String getDistance(double distance) {
        if (distance <= 1000) {
            return distance + "米";
        } else {
            return distance / 1000 + "公里";
        }
    }

    public static String getIntervalTime(long createtime) {
        String interval = null;

        long time = System.currentTimeMillis() - createtime;

        if (time / 1000 < 10 && time / 1000 >= 0) {
            //如果时间间隔小于10秒则显示“刚刚”time/10得出的时间间隔的单位是秒
            interval = "刚刚";

        } else if (time / 1000 < 60 && time / 1000 > 0) {
            //如果时间间隔小于60秒则显示多少秒前
            int se = (int) ((time % 60000) / 1000);
            interval = se + "秒前";

        } else if (time / 60000 < 60 && time / 60000 > 0) {
            //如果时间间隔小于60分钟则显示多少分钟前
            int m = (int) ((time % 3600000) / 60000);//得出的时间间隔的单位是分钟
            interval = m + "分钟前";

        } else if (time / 3600000 < 24 && time / 3600000 >= 0) {
            //如果时间间隔小于24小时则显示多少小时前
            int h = (int) (time / 3600000);//得出的时间间隔的单位是小时
            interval = h + "小时前";

        } else {
            //大于24小时，则显示正常的时间，但是不显示秒
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            interval = sdf.format(new Date(createtime));
        }
        return interval;
    }

    public static Map<String, String> bean2Map(Object bean) {
        Map<String, String> params = new HashMap<>();
        try {
            if (bean != null) {
                Class cls = bean.getClass();
                for (; cls != Object.class; cls = cls.getSuperclass()) {
                    Field[] fields = cls.getDeclaredFields();
                    for (Field f : fields) {
                        f.setAccessible(true);
                        String value = String.valueOf(f.get(bean));
                        if (!TextUtils.isEmpty(value)) {
                            params.put(f.getName(), value);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return params;
    }

    public static boolean checkBankCard(String cardId) {
        boolean isCheck;
        char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
        isCheck = cardId.charAt(cardId.length() - 1) == bit;
        if (!isCheck) {
            ToastAlone.show("卡号输入有误");
        }
        return isCheck;
    }

    public static void phoneCall(Context mContext, String number) {
        try {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
            mContext.startActivity(intent);
        } catch (Exception e) {
            LogUtil.printeException(e);
        }
    }

    public static boolean checkIdCard(String cardId) {
        boolean check = new IdcardValidator().isValidatedAllIdcard(cardId);
        if (!check) {
            ToastAlone.show("身份证输入有误");
        }
        return check;
    }

    public static String formateBankCard(String bankCardStr) {
        if (TextUtils.isEmpty(bankCardStr)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        try {
            sb.append(bankCardStr.substring(0, 4));
            sb.append(" **** ");
            sb.append("**** ");
            sb.append(bankCardStr.substring(bankCardStr.length() - 4, bankCardStr.length()));
        } catch (Exception e) {
            LogUtil.printeException(e);
        }
        return sb.toString();
    }

    public static String formateIdCard(String idCardStr) {
        if (TextUtils.isEmpty(idCardStr)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("**************");
            sb.append(idCardStr.substring(idCardStr.length() - 4, idCardStr.length()));
        } catch (Exception e) {
            LogUtil.printeException(e);
        }
        return sb.toString();
    }

    private static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            //如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }


    public static String getAppMetaData(String key) {
        try {
            ApplicationInfo ai = BaseApplication.mContext.getPackageManager().getApplicationInfo(BaseApplication.mContext.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            return bundle.getString(key);
        } catch (Exception e) {
        }
        return null;
    }

    public static String getUserAgent() {
        String userAgent = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            try {
                userAgent = WebSettings.getDefaultUserAgent(BaseApplication.mContext);
            } catch (Exception e) {
                userAgent = System.getProperty("http.agent");
            }
        } else {
            userAgent = System.getProperty("http.agent");
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0, length = userAgent.length(); i < length; i++) {
            char c = userAgent.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                sb.append(String.format("\\u%04x", (int) c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }


    public static boolean isForeground(Activity activity) {
        return isForeground(activity, activity.getClass().getName());
    }

    public static boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className))
            return false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName()))
                return true;
        }
        return false;
    }

}
