 package cn.com.yusys.yusp.util;
 

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
 import javax.crypto.spec.SecretKeySpec;
 import org.apache.commons.codec.binary.Base64;
 
 public final class AESUtil
 {
   private static final String KEY_ALGORITHM = "AES";
   private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
//   private static final String AES_CIPHER_ALGORITHM = "AES";
   
   private  Cipher aESCipher ;
   
   public AESUtil() {
	   try {
		aESCipher = Cipher.getInstance( KEY_ALGORITHM );//NOSONAR
	} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
		// TODO 自动生成的 catch 块
		e.printStackTrace();
	}
   }
 
   public final String encrypt(String content, String key)
   {
     return encrypt(content, StandardCharsets.UTF_8.name(), key);
   }
 
   public final String encrypt(String content, String charset, String key)
   {
     try
     {
       return Base64.encodeBase64String(encrypt(content.getBytes(charset), key));
     } catch (Exception ex) {
       throw new RuntimeException(ex.getMessage(), ex);
     }
   }
 
   public final byte[] encrypt(byte[] content, String key)
   {
     try
     {
       Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);//NOSONAR
       cipher.init(1, getSecretKey(key));
       return cipher.doFinal(content);
     } catch (Exception ex) {
       
       throw new RuntimeException(ex.getMessage(), ex);
     }
   }
 
   public final String encryptAes(String content, String key) 
   {
	   try {
		   return encryptAes(content, StandardCharsets.UTF_8.name(), key);
	   }catch (Exception ex) {
		// TODO: handle exception
		   
		   throw new RuntimeException(ex.getMessage(), ex);
	}
     
   }
 
   public final String encryptAes(String content, String charset, String key) 
   {	
	   try {
		   return Base64.encodeBase64String(encryptAes(content.getBytes(charset), key));
	   } catch (Exception ex) {
		// TODO: handle exception
		   
		   throw new RuntimeException(ex.getMessage(), ex);
	}
       
   }
 
   public final byte[] encryptAes(byte[] content, String key)
   {
	   try {
		   aESCipher.init(1, getSecretKey(key));
	       return aESCipher.doFinal(content);
	   } catch (Exception ex) {
		// TODO: handle exception
		   
		   throw new RuntimeException(ex.getMessage(), ex);
	   }
       
   }
   //1
   public final String decrypt(String content, String key)throws  RuntimeException
   {
     return decrypt(content, StandardCharsets.UTF_8.name(), key);
   }
   //2
   public final String decrypt(String content, String charset, String key)
   {
     try
     {
       return new String(decrypt(Base64.decodeBase64(content), key), charset);
     } catch (Exception e) {
       throw new RuntimeException(e.getMessage(), e);
     }
   }
   //3
   public final byte[] decrypt(byte[] content, String key)
   {
     try
     {
       Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//NOSONAR
 
       cipher.init(2, getSecretKey(key));
 
       return cipher.doFinal(content);
     } catch (Exception ex) {
       throw new RuntimeException(ex.getMessage(), ex);
     }
   }
 
   public final String decryptAes(String content, String key)
   {
     return decryptAes(content, StandardCharsets.UTF_8.name(), key);
   }
 
   public final String decryptAes(String content, String charset, String key)
   {
     try
     {
       return new String(decryptAes(Base64.decodeBase64(content), key), charset);
     } catch (Exception ex) {
       
       throw new RuntimeException(ex.getMessage(), ex);
     }
   }
 
   public final byte[] decryptAes(byte[] content, String key)
   {
     try
     {
//       Cipher cipher = Cipher.getInstance("AES");
       aESCipher.init(2, getSecretKey(key));
       return aESCipher.doFinal(content);
     } catch (Exception ex) {
       
       throw new RuntimeException(ex.getMessage(), ex);
     }
   }
 
   private final SecretKeySpec getSecretKey(String key)
   {
     KeyGenerator kg = null;
     try {
       kg = KeyGenerator.getInstance("AES");//NOSONAR
       //修改AESLinx下的问题
       SecureRandom random = SecureRandom.getInstance("SHA1PRNG");//NOSONAR
       random.setSeed(key.getBytes());
       kg.init(128, random);
       SecretKey secretKey = kg.generateKey();
       return new SecretKeySpec(secretKey.getEncoded(), "AES");//NOSONAR
     } catch (Exception ex) {
       throw new RuntimeException(ex.getMessage(), ex);
     }
   }
 }