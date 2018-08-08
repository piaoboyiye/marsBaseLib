package com.mars.android.baselib.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class AES {
    private IvParameterSpec ivSpec;
    private SecretKeySpec keySpec;

    public static final String AES_KEY = "sfe023f_9fd&fwfl";

    public AES(String key) {
        try {
            byte[] keyBytes = key.getBytes();
            byte[] buf = new byte[16];

            for (int i = 0; i < keyBytes.length && i < buf.length; i++) {
                buf[i] = keyBytes[i];
            }

            this.keySpec = new SecretKeySpec(buf, "AES");
            this.ivSpec = new IvParameterSpec(keyBytes);
        } catch (Exception e) {
            LogUtil.printeException(e);
        }
    }

    public byte[] encrypt(byte[] origData) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, this.keySpec, this.ivSpec);
            return cipher.doFinal(origData);
        } catch (Exception e) {
            LogUtil.printeException(e);
        }
        return null;
    }

    public byte[] decrypt(byte[] crypted) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, this.keySpec, this.ivSpec);
            return cipher.doFinal(crypted);
        } catch (Exception e) {
            LogUtil.printeException(e);
        }
        return null;
    }

}