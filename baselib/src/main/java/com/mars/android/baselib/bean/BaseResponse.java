package com.mars.android.baselib.bean;

public class BaseResponse<T> {
    public int code;
    public String message;
    public T data;
}
