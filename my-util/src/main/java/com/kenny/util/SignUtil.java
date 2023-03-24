package cn.com.yusys.yusp.util;

import java.io.UnsupportedEncodingException;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.codehaus.jettison.json.JSONTokener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

/**
 * 
 * <p> Description: </p>
 * <p> Copyright: Copyright (c) 2018 </p>
 * <p> Create Date: 2018-5-17 </p>
 * <p> Company: CITIC BANK </p> 
 * @author liuchunshu
 * @version $Id: SignUtil.java,v 1.0 liuchunshu Exp $
 */
public class SignUtil {
    

    private static final String encode = "UTF-8";

    
    private Signature  verifySignSignature ;
    
    private Signature signSignature ;
    
    private  volatile static SignUtil instance = null ;
    
    public SignUtil(RSAPrivateKey privateKey,RSAPublicKey publicKey) {
//    	private SignUtil(RSAPrivateKey privateKey,RSAPublicKey publicKey) {
		try {
			signSignature = Signature.getInstance("SHA1WithRSA");
	    	verifySignSignature = Signature.getInstance("SHA1WithRSA");
	    	signSignature.initSign(privateKey);
	    	verifySignSignature.initVerify(publicKey);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
    }
    
	public static synchronized  SignUtil getInstance(RSAPrivateKey privateKey,RSAPublicKey publicKey) {
		if(instance == null) {
			instance = new SignUtil(privateKey, publicKey);
		}
		return instance;
		
	}
	
    public String verifySignBusiness(String business,String signData) throws RuntimeException, UnsupportedEncodingException, JSONException, org.codehaus.jettison.json.JSONException {
    	Object jsonBoject = new JSONTokener(business).nextValue();
    	  if(jsonBoject instanceof JSONObject){
				JSONObject jsonObject = (JSONObject) jsonBoject;
		        StringBuffer sbf = formaturlMap(jsonObject);
		        business = sbf.toString().replaceAll("\\}\\{", "\\},\\{").replaceAll("\r|\n", "");
			} 
        if (signData == null) {
            throw new RuntimeException("签名数据为空");
        }
        signData = signData.replace("\r|\n", "");
        boolean boo;
        try {
            boo = verifySign1(business.getBytes(encode), signData);
        } catch (Exception e) {
            throw new RuntimeException("验证签名失败");
        }
        if (boo) {
            return business;
        } else {
            throw new RuntimeException("验证签名失败");
        }
    }

    // 加签方法java原生
    public String sign1(String data) throws RuntimeException {
        try {
            data = data.replace("\r|\n", "");
            String reSbf = "";
            Object jsonBoject = new JSONTokener(data).nextValue();
            if(jsonBoject instanceof JSONObject){
				JSONObject jsonObject = (JSONObject) jsonBoject;
				 //对报文中的key字段进行ASCII排序
	            StringBuffer sbf = formaturlMap(jsonObject);
	            reSbf = sbf.toString().replaceAll("\\}\\{", "\\},\\{");
			} else {
				reSbf = data;
			}
            byte[] msg = reSbf.getBytes(encode);
            //签名数据
            signSignature.update(msg);
            byte[] bytarr = signSignature.sign();
            String signData = new String(java.util.Base64.getEncoder().encode(bytarr), encode);
            signData = signData.replace("\r|\n", "");
            return signData;
        } catch (Exception e) {
            throw new RuntimeException("数据签名失败");
        }
    }
    // 验签方法java原生
    public Boolean verifySign1(byte[] msg, String signData ) throws Exception {
        verifySignSignature.update(msg);
        signData.getBytes("UTF-8");
        boolean flag = verifySignSignature.verify(CBConverter.base64ToBytes(signData));
        return flag;
    }


    /**
     * 针对要签名验签的内容做ASCII排序 
     * @param paraMap
     * @return
     * @throws UnsupportedEncodingException
     */
    public StringBuffer formaturlMap(JSONObject jsonObject)
            throws UnsupportedEncodingException {
        if (jsonObject == null) {
            return null;
        }
        StringBuffer plain = new StringBuffer();
        @SuppressWarnings("unchecked")
        List<Map.Entry<String, Object>> infolds = new ArrayList<Map.Entry<String, Object>>(
                jsonObject.entrySet());
        //ASCII 排序（字典序）
        Collections.sort(infolds, new Comparator<Map.Entry<String, Object>>() {
            public int compare(Entry<String, Object> o1, Entry<String, Object> o2) {
                return (o1.getKey().compareTo(o2.getKey()));
            }
        });
        plain.append("{");
        for (Map.Entry<String, Object> item : infolds) {
            String key = item.getKey();
            if ("commonDataList".equalsIgnoreCase(key)) {
                continue;
            }
            plain.append("\"" + key + "\"");
            plain.append(":");
            Object val = item.getValue();
            if (val instanceof JSONObject) {
                StringBuffer str = formaturlMap((JSONObject) val);
                plain.append(str);
            } else if (val instanceof String) {
                plain.append("\"" + val + "\"");
            } else if (val instanceof JSONArray) {
                plain.append("[");
                JSONArray arr = (JSONArray) val;
                for (int i = 0; i < arr.size(); i++) {
                    StringBuffer tempstr = new StringBuffer();
                    Object obj = arr.get(i);
                    if (obj instanceof JSONObject) {
                        tempstr = formaturlMap((JSONObject) obj);
                    }
                    plain.append(tempstr);
                }
                plain.append("]");
            }
            plain.append(",");
        }
        plain.delete(plain.length() - 1, plain.length());
        if (plain.length() > 0) {
            plain.append("}");
        }
        return plain;
    }
}
