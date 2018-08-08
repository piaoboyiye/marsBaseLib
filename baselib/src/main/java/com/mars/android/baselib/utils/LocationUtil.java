package com.mars.android.baselib.utils;

public class LocationUtil {
//    private static AMapLocationClient mLocationClient;
//
//    public static void startLocation(Context mContext) {
//        if (mLocationClient == null) {
//            mLocationClient = new AMapLocationClient(mContext.getApplicationContext());
//            AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
//            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//            mLocationClient.setLocationOption(mLocationOption);
//        }
//        mLocationClient.setLocationListener(new AMapLocationListener() {
//            @Override
//            public void onLocationChanged(AMapLocation aMapLocation) {
//                LogUtil.i("LocationUtil", "onLocationChanged===" + aMapLocation);
//                if (aMapLocation != null && !TextUtils.isEmpty(aMapLocation.getCityCode())) {
//                    LocationBean bean = new LocationBean();
//                    bean.lat = aMapLocation.getLatitude();
//                    bean.lng = aMapLocation.getLongitude();
//                    bean.city = aMapLocation.getCity();
//                    bean.district = aMapLocation.getDistrict();
//                    bean.poiName = aMapLocation.getPoiName();
//                    bean.cityCode = aMapLocation.getCityCode();
//                    bean.areaCode = aMapLocation.getAdCode();
//                    BaseSpDataUtil.saveLocationInfo(bean);
//                    destoryLocation();
//                }
//            }
//        });
//        mLocationClient.startLocation();
//    }
//
//    public static void destoryLocation() {
//        if (mLocationClient != null) {
//            mLocationClient.stopLocation();
//            mLocationClient.onDestroy();
//        }
//    }

}