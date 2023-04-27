package com.dengyun.baselibrary.utils.encode.base64;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * @author yangshu
 * @date 2020/9/22 17:55
 */
public class Demo {
    public static void main(String[] args) throws Exception {
        String publicKey= "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCaoZtaKupEiIgEzyMCWcJga0y2Oq5ctcGcD5f/u2RCnzAvoHkJ8UQu6coURlxDVISpOi79k7e7M0IjnscOC/BZS+LKbxwsBFdtcu8sxkIZyTZ3hgWEqN1wjTuBAHTmPkQP/2bcMrM0XWlxu0G2yXeqabQRV4T3LGGPizP2P1gC8QIDAQAB";
        System.out.println(encrypt( "admin",  publicKey));
    }
    /**
     * RSA公钥加密
     *
     * @param str 加密字符串
     * @param publicKey 公钥
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    public static String encrypt( String str, String publicKey) throws Exception{
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA")
                .generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
        return outStr;
    }
}
