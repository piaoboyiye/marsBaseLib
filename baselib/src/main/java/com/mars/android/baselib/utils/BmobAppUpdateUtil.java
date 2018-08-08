package com.mars.android.baselib.utils;


import android.app.Activity;
import android.content.Context;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.BmobDialogButtonListener;
import cn.bmob.v3.listener.BmobUpdateListener;
import cn.bmob.v3.update.BmobUpdateAgent;
import cn.bmob.v3.update.UpdateResponse;
import cn.bmob.v3.update.UpdateStatus;

public class BmobAppUpdateUtil {

    private boolean forceUpdate = false;

    public static void initBmob(Context context, String appid, boolean initTable) {
        try {
            Bmob.initialize(context.getApplicationContext(), appid);
            if (initTable) {
                BmobUpdateAgent.initAppVersion();
            }
        } catch (Exception e) {
            LogUtil.printeException(e);
        }
    }

    public void bmobUpdateApp(final boolean byUser, Activity activity, final UpdateCallBack callBack) {
//        if (!byUser) {
//            String updateDate = BaseSpDataUtil.getlocalJson(BaseSpConstants.APPUPDATE_TIME);
//            if (!TextUtils.isEmpty(updateDate)) {
//                try {
//                    long time = Long.valueOf(updateDate);
//                    if (Math.abs(System.currentTimeMillis() - time) < 24 * 60 * 60 * 1000) {
//                        LogUtil.i("bmobUpdateApp 24小时内不启动更新检查");
//                        return;
//                    }
//                } catch (Exception e) {
//                    LogUtil.printeException(e);
//                }
//            }
//        }
//        BaseSpDataUtil.saveLocalJson(BaseSpConstants.APPUPDATE_TIME, String.valueOf(System.currentTimeMillis()));
        LogUtil.i("bmobUpdateApp 启动更新检查");
        try {
            BmobUpdateAgent.update(activity);
            BmobUpdateAgent.setUpdateListener(new BmobUpdateListener() {
                @Override
                public void onUpdateReturned(int updateStatus, UpdateResponse updateResponse) {
                    if (updateStatus == UpdateStatus.Yes && updateResponse != null) {
                        forceUpdate = updateResponse.isforce;
                    } else {
                        if (byUser) {
                            ToastAlone.show("您现在已经是最新版本");
                        }
                    }
                }
            });
            BmobUpdateAgent.setDialogListener(new BmobDialogButtonListener() {

                @Override
                public void onClick(int status) {
                    switch (status) {
                        case UpdateStatus.Update:
                            ToastAlone.show("后台更新中,请稍后");
                            if (forceUpdate && callBack != null) {
                                callBack.forceUpdate();
                            }
                            break;
                    }
                }
            });
        } catch (Exception e) {
            LogUtil.printeException(e);
        }
    }

    public interface UpdateCallBack {
        void forceUpdate();
    }

}
