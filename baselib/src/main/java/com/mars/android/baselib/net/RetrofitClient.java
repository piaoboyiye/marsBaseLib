package com.mars.android.baselib.net;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private Retrofit.Builder defaultClient;
    private Retrofit.Builder cacheClient;
    private Retrofit.Builder uploadClient;
    private Retrofit.Builder downloadClient;
    private Retrofit.Builder customClient;

    private static final int DEFALUT_CLIENT_TYPE = 0;
    private static final int UPLOAD_CLIENT_TYPE = 1;
    private static final int DOWNLOAD_CLIENT_TYPE = 2;
    private static final int CACHE_CLIENT_TYPE = 3;

    public static boolean showHttpLog = false;

    private static class ConverterHolder {
        private static GsonConverterFactory INSTANCE = GsonConverterFactory.create();
    }

    private static class CallAdapterHolder {
        private static RxJavaCallAdapterFactory INSTANCE = RxJavaCallAdapterFactory.create();
    }

    private static class SingletonHolder {
        private static RetrofitClient INSTANCE = new RetrofitClient();
    }

    public static RetrofitClient getInstance() {
        return SingletonHolder.INSTANCE;
    }


    private RetrofitClient() {
    }

    private OkHttpClient getOkHttpClient(OkHttpClient.Builder builder, int clientType) {
        if (builder == null) {
            builder = new OkHttpClient.Builder();
        }
        switch (clientType) {
            case UPLOAD_CLIENT_TYPE:
                builder.connectTimeout(5, TimeUnit.SECONDS);
                builder.writeTimeout(60, TimeUnit.SECONDS);
                builder.readTimeout(5, TimeUnit.SECONDS);
                break;
            case DOWNLOAD_CLIENT_TYPE:
                builder.connectTimeout(5, TimeUnit.SECONDS);
                builder.writeTimeout(5, TimeUnit.SECONDS);
                builder.readTimeout(60, TimeUnit.SECONDS);
                break;
            case CACHE_CLIENT_TYPE:
                builder.connectTimeout(5, TimeUnit.SECONDS);
                builder.writeTimeout(5, TimeUnit.SECONDS);
                builder.readTimeout(30, TimeUnit.SECONDS);
                break;
            default:
                builder.connectTimeout(20, TimeUnit.SECONDS);
                builder.writeTimeout(20, TimeUnit.SECONDS);
                builder.readTimeout(20, TimeUnit.SECONDS);
                break;
        }
        if (showHttpLog) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addNetworkInterceptor(interceptor);
        }
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        builder.retryOnConnectionFailure(true);
        return builder.build();
    }


    private Retrofit.Builder getBaseBuilder(String hostUrl) {
        return new Retrofit.Builder().addConverterFactory(ConverterHolder.INSTANCE)
                .addCallAdapterFactory(CallAdapterHolder.INSTANCE).baseUrl(hostUrl);
    }

    public Retrofit getCustomRetrofit(String hostUrl, OkHttpClient.Builder builder) {
        if (defaultClient == null) {
            defaultClient = getBaseBuilder(hostUrl)
                    .client(getOkHttpClient(builder, DEFALUT_CLIENT_TYPE));
        }
        return defaultClient.build();
    }

    public Retrofit getDefaultRetrofit(String hostUrl) {
        if (defaultClient == null) {
            defaultClient = getBaseBuilder(hostUrl)
                    .client(getOkHttpClient(null, DEFALUT_CLIENT_TYPE));
        }
        return defaultClient.build();
    }

    public Retrofit getCacheRetrofit(String hostUrl) {
        if (cacheClient == null) {
            cacheClient = getBaseBuilder(hostUrl)
                    .client(getOkHttpClient(null, CACHE_CLIENT_TYPE));
        }
        return cacheClient.build();
    }

    public Retrofit getUploadRetrofit(String hostUrl) {
        if (uploadClient == null) {
            uploadClient = getBaseBuilder(hostUrl)
                    .client(getOkHttpClient(null, UPLOAD_CLIENT_TYPE));
        }
        return uploadClient.build();
    }

    public Retrofit getDownloadRetrofit(String hostUrl) {
        if (downloadClient == null) {
            downloadClient = getBaseBuilder(hostUrl)
                    .client(getOkHttpClient(null, DOWNLOAD_CLIENT_TYPE));
        }
        return downloadClient.build();
    }

}
