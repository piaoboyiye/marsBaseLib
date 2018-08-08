package com.mars.android.baselib.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.mars.android.baselib.BaseApplication;


public class NetUtil {

    public static String getNetworkType(Context context) {
        String strNetworkType = "";
        try {
            ConnectivityManager mConnectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = mConnectivity.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    strNetworkType = "wifi";
                } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                    String _strSubTypeName = networkInfo.getSubtypeName();
                    int networkType = networkInfo.getSubtype();
                    switch (networkType) {
                        case TelephonyManager.NETWORK_TYPE_GPRS:
                        case TelephonyManager.NETWORK_TYPE_EDGE:
                        case TelephonyManager.NETWORK_TYPE_CDMA:
                        case TelephonyManager.NETWORK_TYPE_1xRTT:
                        case TelephonyManager.NETWORK_TYPE_IDEN:
                            strNetworkType = "2g";
                            break;
                        case TelephonyManager.NETWORK_TYPE_UMTS:
                        case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        case TelephonyManager.NETWORK_TYPE_EVDO_A:
                        case TelephonyManager.NETWORK_TYPE_HSDPA:
                        case TelephonyManager.NETWORK_TYPE_HSUPA:
                        case TelephonyManager.NETWORK_TYPE_HSPA:
                        case TelephonyManager.NETWORK_TYPE_EVDO_B:
                        case TelephonyManager.NETWORK_TYPE_EHRPD:
                        case TelephonyManager.NETWORK_TYPE_HSPAP:
                            strNetworkType = "3g";
                            break;
                        case TelephonyManager.NETWORK_TYPE_LTE:
                            strNetworkType = "4g";
                            break;
                        default:
                            if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                                strNetworkType = "3g";
                            }
                            break;
                    }
                }
            }
        } catch (Exception e) {
            LogUtil.printeException(e);
        }
        return strNetworkType;
    }

    public static boolean isNetEnable() {
        try {
            ConnectivityManager manger = (ConnectivityManager) BaseApplication.mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = manger.getActiveNetworkInfo();
            return (info != null && info.isConnected());
        } catch (Exception e) {
            LogUtil.printeException(e);
        }
        return false;
    }

    public static boolean isWifiConnected() {
        try {
            ConnectivityManager mConnectivity = (ConnectivityManager) BaseApplication.mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = mConnectivity.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    return true;
                }
            }
        } catch (Exception e) {
            LogUtil.printeException(e);
        }
        return false;
    }
}
