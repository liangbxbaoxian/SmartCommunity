package com.wb.sc.security;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import javax.crypto.Cipher;

import android.content.Context;
import android.text.TextUtils;

public class RSA {
	
	/**
	 * 证书类型X509
	 */
	private static final String CERT_TYPE = "X.509";

	/**
	 * 密钥库类型PCKS12
	 */
	private static final String STORE_TYPE = "PKCS12";

	/**
	 * 客户端私钥
	 */
//	private PrivateKey privateKey;

	/**
	 * 客户端证书
	 */
	private X509Certificate certificate;

	/**
	 * RSA最大加密明文大小
	 */
	private static final int MAX_ENCRYPT_BLOCK = 117;

	/**
	 * RSA最大解密密文大小
	 */
	private static final int MAX_DECRYPT_BLOCK = 256;
	
	/**
	 * 由Certificate获得公钥
	 * 
	 * @param certificatePath
	 *            证书路径
	 * @return PublicKey 公钥
	 * @throws Exception
	 */
	public PublicKey getPublicKeyByCertificate(Context context, String certificateName)
			throws Exception {
		// 获得证书
		Certificate certificate = getCertificate(context, certificateName);
		// 获得公钥
		return certificate.getPublicKey();

	}
	
	/**
	 * 获得Certificate
	 * 
	 * @param certificatePath
	 *            证书路径
	 * @return Certificate 证书
	 * @throws Exception
	 */
	private X509Certificate getCertificate(Context context, String certificateName)
			throws Exception {
		// 实例化证书工厂
		CertificateFactory certificateFactory = CertificateFactory.getInstance(CERT_TYPE);
		// 取得证书文件流
		InputStream in = context.getAssets().open(certificateName);
		// 生成证书
		Certificate certificate = certificateFactory.generateCertificate(in);
		// 关闭证书文件流
		in.close();
		return (X509Certificate) certificate;
	}
	
	/**
	 * 公钥加密
	 * 
	 * @param data
	 *            待加密数据
	 * @param certPath
	 *            服务器公钥文件路径
	 * @return byte[] 加密数据
	 * @throws Exception
	 */
	public byte[] encryptByPublicKey(PublicKey publicKey, byte[] data)
			throws Exception {
		
		if (publicKey == null) {
			return null;
		}
		// 对数据加密
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
		//Cipher cipher = Cipher.getInstance("RSA/ECB/NOPADDING");
//		Cipher cipher = Cipher.getInstance("RSA");

		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		int inputLen = data.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段加密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
				cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(data, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_ENCRYPT_BLOCK;
		}
		byte[] encryptedData = out.toByteArray();
		out.close();
		return encryptedData;
	}
	
	/**
	 * 由KeyStore获得私钥
	 * 
	 * @param keyStorePath
	 *            密钥库路径
	 * @param alias
	 *            别名
	 * @param password
	 *            密码
	 * @return PrivateKey 私钥
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public PrivateKey getCertInfoByKeyStore(Context context, String certificatePath, 
			String password) throws Exception {
		// 获得密钥库
		PrivateKey privateKey;
		KeyStore ks = getKeyStore(context, certificatePath, password);
		Enumeration keyenum = ks.aliases();
		String keyAlias = null;
		// 获得私钥
		if (keyenum.hasMoreElements()) {
			keyAlias = (String) keyenum.nextElement();
		}
		privateKey = (PrivateKey) ks.getKey(keyAlias, password.toCharArray());
			
		if (certificate == null) {
			certificate = (X509Certificate) ks.getCertificate(keyAlias);
		}
		return privateKey;
	}
	
	/**
	 * 获得KeyStore
	 * 
	 * @param keyStorePath
	 *            密钥库路径
	 * @param password
	 *            密码
	 * @return KeyStore 密钥库
	 * @throws Exception
	 */
	private KeyStore getKeyStore(Context context, String keyStorePath, String password) throws Exception {
		if (TextUtils.isEmpty(keyStorePath) || TextUtils.isEmpty(password)) {
			return null;
		}
		// 实例化密钥库
		KeyStore ks = KeyStore.getInstance(STORE_TYPE);
		// 获得密钥库文件流
		InputStream is = context.getAssets().open(keyStorePath);
		// 加载密钥库
		ks.load(is, password.toCharArray());
		// 关闭密钥库文件流
		is.close();
		return ks;
	}
	
	/**
	 * 私钥解密
	 * 
	 * @param data
	 *            待解密数据
	 * @param certificatePath
	 *            客户端证书路径
	 * @param alias
	 *            别名
	 * @param password
	 *            密码
	 * @return byte[] 解密数据
	 * @throws Exception
	 */
	public byte[] decryptByPrivateKey(PrivateKey privateKey, byte[] data) throws Exception {
		// 对数据加密
		//Cipher cipher = Cipher.getInstance("RSA/ECB/NOPADDING");
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
//		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		int inputLen = data.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段解密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
				cache = cipher.doFinal(data, offSet, MAX_DECRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(data, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_DECRYPT_BLOCK;
		}
		byte[] decryptedData = out.toByteArray();
		out.close();
		return decryptedData;
	}
}
