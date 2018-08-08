package com.mars.android.baselib.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.mars.android.baselib.BaseApplication;


public class SPUtil {

	private final static String NAME = "com.mars.android.baselib.sputil";
	private static SharedPreferences saveInfo;
	private static SharedPreferences.Editor saveEditor;

	private static SPUtil instance;

	private SPUtil() {
		if (saveInfo == null) {
			saveInfo = BaseApplication.mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
			saveEditor = saveInfo.edit();
		}
	}

	public static SPUtil getInstance() {
		if (instance == null) {
			instance = new SPUtil();
		}
		return instance;
	}

	public boolean putString(String key, String value) {
		saveEditor.putString(key, value);
		return saveEditor.commit();
	}

	public boolean putBoolean(String key, boolean value) {
		saveEditor.putBoolean(key, value);
		return saveEditor.commit();
	}

	public boolean putInt(String key, int value) {
		saveEditor.putInt(key, value);
		return saveEditor.commit();
	}

	public int getInt(String key) {
		return saveInfo.getInt(key, 0);
	}

	public int getInt(String key, int defaultValue) {
		return saveInfo.getInt(key, defaultValue);
	}

	public String getString(String key) {
		return saveInfo.getString(key, "");
	}

	public String getString(String key, String defaultValue) {
		return saveInfo.getString(key, defaultValue);
	}

	public boolean getBoolean(String key) {
		return saveInfo.getBoolean(key, false);
	}

	public boolean getBoolean(String key, boolean value) {
		return saveInfo.getBoolean(key, value);
	}


}
