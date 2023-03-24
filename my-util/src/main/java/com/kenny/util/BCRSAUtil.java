package cn.com.yusys.yusp.util;

/**
 * 密钥工具类
 *
 * @author geql
 * @since 1.0.0
 */

import org.apache.commons.codec.binary.Base64;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.Base64Utils;

import io.netty.handler.codec.base64.Base64Decoder;

import javax.crypto.Cipher;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;

/**
 * BCRSAUtil
 *
 * @author geql
 * @author xiaodg
 * @since 1.0.0
 */
public class BCRSAUtil {


    private static RSAPublicKey pubKey = null;

    private BCRSAUtil() {

    }

    static {
        try {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            pubKey = (RSAPublicKey) readKey("cert/pwd_public.key");
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    private static Key readKey(String keyName) throws Exception {
    	Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        ObjectInputStream keyIn = new ObjectInputStream(BCRSAUtil.class.getClassLoader().getResourceAsStream(keyName));
        Key key = (Key) keyIn.readObject();
        keyIn.close();
        return key;
    }
    /**
     * 使用RSA公钥加密数据
     *
     * @param pubKeyInByte 打包的byte[]形式公钥
     * @param data         要加密的数据
     * @return 加密数据
     */
    public static byte[] encryptByRSA(byte[] pubKeyInByte, byte[] data) {
        try {
            KeyFactory mykeyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec pubSpec = new X509EncodedKeySpec(pubKeyInByte);
            PublicKey pubKey = mykeyFactory.generatePublic(pubSpec);
            Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding", "BC");//NOSONAR
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @方法名称:encryptByPublicKey
     * @方法描述:公钥加密
     * @参数与返回说明:加密结果
     * @算法描述:
     * // 服务端通过公钥加密密码
     * Base64.encodeBase64String(BCRSAUtil.encryptByPublicKey("admin".getBytes(), Base64.encodeBase64String(pubKey.getEncoded())));
     */
    public static byte[] encryptByPublicKey(byte[] data, String publicKey) throws Exception {
        byte[] keyBytes = Base64Utils.decodeFromString(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        return cipher.doFinal(data);
    }

    /**
     * 对指定密码加密
     * @param passWord 要加密的密码
     * @return
     * @throws Exception
     */
    public static String passwodString(String passWord) throws Exception {
    	String passwoString =Base64.encodeBase64String(
    			//调用公钥加密方法encryptByPublicKey
    			BCRSAUtil.encryptByPublicKey(passWord.getBytes(), Base64.encodeBase64String( pubKey.getEncoded() ))
    			) ;
    	return passwoString;
	}

}
