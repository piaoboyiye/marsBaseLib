package com.mars.android.baselib.share;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.mars.android.baselib.constant.BaseEventTag;
import com.mars.android.baselib.event.BaseEvent;
import com.mars.android.baselib.event.RxBus;
import com.mars.android.baselib.utils.ToastAlone;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;


public class WXPayCallBackActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = ShareUtil.wxApi;
        if (api == null) {
            ToastAlone.show("微信初始化失败");
            finish();
            return;
        }
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        BaseEvent event = new BaseEvent();
        event.requestTag = BaseEventTag.SHARE_PAYDONE;
        event.data = false;
        String messStr = "支付";
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    messStr += "成功";
                    event.data = true;
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    messStr += "已取消";
                    break;
                default:
                    messStr += "失败";
                    break;
            }
            ToastAlone.show(messStr);
        }
        RxBus.getDefault().post(event);
        finish();
    }

}