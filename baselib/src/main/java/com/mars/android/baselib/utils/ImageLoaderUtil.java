package com.mars.android.baselib.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.mars.android.baselib.constant.BaseConstants;
import com.mars.android.baselib.glideutil.GlideCircleTransform;
import com.mars.android.baselib.glideutil.GlideRoundTransform;

import java.io.File;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ImageLoaderUtil {

    public static void showImage(ImageView imageView, String url, int defaultImagRes) {
        Glide.with(imageView.getContext())
                .load(url)
                .dontAnimate()
                .centerCrop()
                .placeholder(defaultImagRes)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .error(defaultImagRes)
                .into(imageView);
    }
    public static void showImageInside(ImageView imageView, String url, int defaultImagRes) {
        Glide.with(imageView.getContext())
                .load(url)
                .dontAnimate()
                .fitCenter()
                .placeholder(defaultImagRes)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .error(defaultImagRes)
                .into(imageView);
    }

    public static void downloadImage(final Context mContext, final String url) {
        ToastAlone.show("保存中...");
        Observable.create(new Observable.OnSubscribe<File>() {

            @Override
            public void call(Subscriber<? super File> subscriber) {
                try {
                    Bitmap bitmap = Glide.with(mContext).load(url).asBitmap().into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
                    subscriber.onNext(FileUtil.saveImageToGallery(mContext, bitmap));
                } catch (Exception e) {
                    LogUtil.printeException(e);
                }
            }
        }).subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<File>() {
            @Override
            public void call(File file) {
                ToastAlone.show("保存成功,请到" + BaseConstants.APP_IMAGE_DIR + "相册中查看");
            }
        });

    }

    public static void showImage(ImageView imageView, Uri uri, int defaultImagRes) {
        Glide.with(imageView.getContext())
                .load(uri)
                .dontAnimate()
                .centerCrop()
                .thumbnail(0.1f)
                .placeholder(defaultImagRes)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .error(defaultImagRes)
                .into(imageView);
    }

    public static void showImageNormal(ImageView imageView, String url, int defaultImagRes) {
        Glide.with(imageView.getContext())
                .load(url)
                .dontAnimate()
                .placeholder(defaultImagRes)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .error(defaultImagRes)
                .into(imageView);
    }


    public static void showCircleImage(ImageView imageView, String url, int defaultImagRes) {
        Glide.with(imageView.getContext())
                .load(url)
                .dontAnimate()
                .centerCrop()
                .placeholder(defaultImagRes)
                .error(defaultImagRes)
                .transform(new GlideCircleTransform(imageView.getContext()))
                .into(imageView);
    }

    public static void showCircleImage(ImageView imageView, int resId) {
        Glide.with(imageView.getContext())
                .load(resId)
                .dontAnimate()
                .centerCrop()
                .transform(new GlideCircleTransform(imageView.getContext()))
                .into(imageView);
    }

    public static void showRoundImage(ImageView imageView, String url, int defaultImagRes) {
        Glide.with(imageView.getContext())
                .load(url)
                .dontAnimate()
                .centerCrop()
                .placeholder(defaultImagRes)
                .error(defaultImagRes)
                .transform(new GlideRoundTransform(imageView.getContext()))
                .into(imageView);
    }

    public static void showRoundImage(ImageView imageView, int resId) {
        Glide.with(imageView.getContext())
                .load(resId)
                .dontAnimate()
                .centerCrop()
                .transform(new GlideRoundTransform(imageView.getContext()))
                .into(imageView);
    }
}
