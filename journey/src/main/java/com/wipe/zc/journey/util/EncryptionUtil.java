package com.wipe.zc.journey.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * String 数据加密工具类
 */
public class EncryptionUtil {

    public static final String MD2 = "MD2";
    public static final String MD5 = "MD5";
    public static final String SHA_1 = "SHA-1";
    public static final String SHA_256 = "SHA-256";
    public static final String SHA_384 = "SHA-384";
    public static final String SHA_512 = "SHA-512";

    /**
     * 对字符串进行SHA256加密
     * @param text 待加密文本
     * @return  加密后文本
     */
    public static String encrypt(String text, String type){
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance(type);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if(digest != null){
            byte[] origBytes = text.getBytes();
            digest.update(origBytes);
            byte[] digestRes = digest.digest();
            return getDigestStr(digestRes);
        }
        return null;
    }

    /**
     * 通过byte[]获取String
     * @param origBytes byte[]数据
     * @return  转换后String数据
     */
    private static String getDigestStr(byte[] origBytes) {
        String tempStr;
        StringBuilder stb = new StringBuilder();
        for (int i = 0; i < origBytes.length; i++) {
            // 这里按位与是为了把字节转整时候取其正确的整数，java中一个int是4个字节
            // 如果origBytes[i]最高位为1，则转为int时，int的前三个字节都被1填充了
            tempStr = Integer.toHexString(origBytes[i] & 0xff);
            if (tempStr.length() == 1) {
                stb.append("0");
            }
            stb.append(tempStr);
        }
        return stb.toString();
    }
}
