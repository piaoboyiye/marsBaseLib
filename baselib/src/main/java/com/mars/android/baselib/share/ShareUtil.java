package com.mars.android.baselib.share;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.mars.android.baselib.bean.ShareBean;
import com.mars.android.baselib.bean.WXPayBean;
import com.mars.android.baselib.constant.BaseEventTag;
import com.mars.android.baselib.event.BaseEvent;
import com.mars.android.baselib.event.RxBus;
import com.mars.android.baselib.utils.LogUtil;
import com.mars.android.baselib.utils.ToastAlone;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class ShareUtil {
    public static IWXAPI wxApi = null;
    private static UMShareAPI umShareAPI = null;

    public static void initSdk(Context context) {
        umShareAPI = UMShareAPI.get(context.getApplicationContext());
    }

    public static void initWx(Context context, String appKey, String appSecret) {
        PlatformConfig.setWeixin(appKey, appSecret);
        if (wxApi == null) {
            wxApi = WXAPIFactory.createWXAPI(context.getApplicationContext(), appKey, false);
            wxApi.registerApp(appKey);
        }
    }

//    public static void initQQ(String appKey, String appSecret) {
//        PlatformConfig.setQQZone(appKey, appSecret);
//    }
//
//    public static void initSina(String appKey, String appSecret, String redirect_url) {
//        PlatformConfig.setSinaWeibo(appKey, appSecret, redirect_url);
//    }

    public static void onActivityResult(int requestCode, int resultCode, Intent data) {
        umShareAPI.onActivityResult(requestCode, resultCode, data);
    }

    public static void login(Activity mContext, SHARE_MEDIA media) {
        umShareAPI.getPlatformInfo(mContext, media, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
                LogUtil.i("UMShareAPI", data.toString());
                BaseEvent baseEvent = new BaseEvent();
                ShareBean bean = new ShareBean();
                bean.union_id = data.get("unionid");
                bean.open_id = data.get("openid");
                bean.gender = data.get("gender");
                bean.iconurl = data.get("iconurl");
                bean.name = data.get("name");
                baseEvent.data = bean;
                baseEvent.requestTag = BaseEventTag.SHARE_LOGIN_SUCCESS;
                RxBus.getDefault().post(baseEvent);
            }

            @Override
            public void onError(SHARE_MEDIA platform, int action, Throwable t) {
                ToastAlone.show("失败:" + t.getMessage());
            }

            @Override
            public void onCancel(SHARE_MEDIA platform, int action) {
                ToastAlone.show("登录取消");
            }
        });
    }

    public static void logout(Activity mContext, SHARE_MEDIA media) {
        umShareAPI.deleteOauth(mContext, media, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
                BaseEvent baseEvent = new BaseEvent();
                baseEvent.requestTag = BaseEventTag.SHARE_LOGOUT_SUCCESS;
                RxBus.getDefault().post(baseEvent);
            }

            @Override
            public void onError(SHARE_MEDIA platform, int action, Throwable t) {
                ToastAlone.show("失败:" + t.getMessage());
            }

            @Override
            public void onCancel(SHARE_MEDIA platform, int action) {
                ToastAlone.show("注销取消");
            }
        });
    }

    public static void share(Activity mContext, SHARE_MEDIA media, String title, String desc, String url, int iconRes, String imgUrl) {
        UMWeb umWeb = new UMWeb(url);
        if (iconRes != 0) {
            umWeb.setThumb(new UMImage(mContext, iconRes));
        }
        if (!TextUtils.isEmpty(imgUrl)) {
            umWeb.setThumb(new UMImage(mContext, imgUrl));
        }
        umWeb.setTitle(title);
        umWeb.setDescription(desc);
        new ShareAction(mContext).setPlatform(media)
                .withMedia(umWeb)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                        BaseEvent baseEvent = new BaseEvent();
                        baseEvent.requestTag = BaseEventTag.SHARE_DONE;
                        RxBus.getDefault().post(baseEvent);
                        ToastAlone.show("分享成功");
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                        ToastAlone.show("失败:" + throwable.getMessage());
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {
                        ToastAlone.show("分享取消");
                    }
                })
                .share();
    }

    public static void wxPay(WXPayBean bean) {
        if (!wxApi.isWXAppInstalled()) {
            ToastAlone.show("您未安装微信");
            return;
        }
        PayReq request = new PayReq();
        request.appId = bean.appid;
        request.partnerId = bean.partnerid;
        request.prepayId = bean.prepayid;
        request.packageValue = bean.packagesign;
        request.nonceStr = bean.noncestr;
        request.timeStamp = bean.timestamp;
        request.sign = bean.sign;

        wxApi.sendReq(request);
    }

    public static void aliPay(String orderInfo, final Activity mContext) {
        Observable.just(orderInfo).map(new Func1<String, Map<String, String>>() {
            @Override
            public Map<String, String> call(String s) {
                PayTask alipay = new PayTask(mContext);
                return alipay.payV2(s, true);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Map<String, String>>() {
            @Override
            public void call(Map<String, String> result) {
                BaseEvent event = new BaseEvent();
                event.requestTag = BaseEventTag.SHARE_PAYDONE;
                LogUtil.i("payApi", result.toString());
                if (TextUtils.equals(result.get("resultStatus"), "9000")) {
                    event.data = true;
                    ToastAlone.show("支付成功");
                } else {
                    event.data = false;
                    ToastAlone.show("支付失败:" + result.get("memo"));
                }
                RxBus.getDefault().post(event);
            }
        });
    }
}
