package cn.com.yusys.yusp.util;

import java.io.ByteArrayOutputStream;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.Key;
import java.security.Security;
import javax.crypto.Cipher;


public class RSAUtil {
	static {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	}
	
	private Cipher cipherEncrypt;
	
	private Cipher cipherDecode;
	
	private int step = 0;
	
	private int stepEncrypt = 0;
	
	private  volatile static RSAUtil instance = null ;
	
//	private RSAUtil(RSAPrivateKey privateKey,RSAPublicKey publicKey) {
	public RSAUtil(RSAPrivateKey privateKey,RSAPublicKey publicKey) {
		try {
			cipherDecode = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding","BC");
			cipherDecode.init(Cipher.DECRYPT_MODE, (Key)privateKey);
			step = privateKey.getModulus().bitLength() / 8;
			
			cipherEncrypt = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding","BC");
			cipherEncrypt.init(Cipher.ENCRYPT_MODE, publicKey);
			stepEncrypt = publicKey.getModulus().bitLength() / 8;
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	public static synchronized  RSAUtil getInstance(RSAPrivateKey privateKey,RSAPublicKey publicKey) {
		if(instance == null) {
			instance = new RSAUtil(privateKey, publicKey);
		}
		return instance;
		
	}
	/**
	 * 证书加密
	 * @param publicKey
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public String encrypt(String data) throws Exception {
		byte[] byt = data.getBytes("UTF-8");
		int n = byt.length / stepEncrypt;
		byte[] encryptedData = null;
		if (n > 0) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			for (int i = 0; i < n; i++) {
				baos.write( cipherEncrypt.doFinal(byt, i * stepEncrypt, stepEncrypt) );
			}
			if ((n = byt.length % stepEncrypt) != 0) {
				baos.write( cipherEncrypt.doFinal( byt , byt.length - n , n) );
			}
			encryptedData = baos.toByteArray();
		} else {
			encryptedData = cipherEncrypt.doFinal( byt );
		}
	  String returnDate = CBConverter.bytesToBase64( encryptedData );
	  return returnDate;
	}
	/**
	 * 证书解密
	 * @param privateKey
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public String decode(String data) throws Exception {
		byte[] raw;
		try {
			raw = CBConverter.base64ToBytes(data);
			int n = raw.length / step;
			if (n > 0){
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				for (int i = 0; i < n; i++) {
					baos.write(cipherDecode.doFinal(raw, i * step, step));
				}
				if ((n = raw.length % step) != 0) {
					baos.write( cipherDecode.doFinal( raw , raw.length - n , n) );
				}
				return new String(baos.toByteArray(), "UTF-8");
			}
			return new String(cipherDecode.doFinal(raw), "UTF-8");
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			throw  e;
		}
		
	}

}
