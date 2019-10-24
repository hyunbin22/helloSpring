package com.kh.spring.common.encrypt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import org.springframework.stereotype.Component;

@Component
public class RSAEncrypto implements MyEncrypt{
	
	private PublicKey publicKey;
	private PrivateKey privateKey;

	public RSAEncrypto() {
		String path = this.getClass().getResource("/").getPath();
		path = path.substring(0,path.lastIndexOf("/target"));
		File f = new File(path + "/src/main/webapp/WEB-INF/keys.hb");
		if(f.exists()) {
			try(ObjectInputStream ois=new ObjectInputStream(new FileInputStream(f))){
				Map<String, Object> keys=((Map<String, Object>) ois.readObject());
				publicKey = (PublicKey)keys.get("public");
				privateKey = (PrivateKey)keys.get("private");
				
			}catch(Exception e) {
				e.printStackTrace();
			}
		} else {
			if(publicKey==null || privateKey==null) {
				try {
					getKey();
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private void getKey() throws NoSuchAlgorithmException {
		SecureRandom ser = new SecureRandom();
		//RAS방식으로 통신을 하기 위해서는 비대칭이기 때문에 두개의 key값 필요
		//1. publickey 2.privatekey
		KeyPairGenerator keygen;
		keygen = KeyPairGenerator.getInstance("RSA");
		keygen.initialize(2048, ser);
		//공개키, 개인키 생성
		KeyPair keypair = keygen.generateKeyPair();	//키생성
		publicKey = keypair.getPublic();
		privateKey = keypair.getPrivate();
		Map<String, Object> keys = new HashMap();
		keys.put("public", publicKey);
		keys.put("private", privateKey);
		String path = this.getClass().getResource("/").getPath();
		path = path.substring(0,path.lastIndexOf("/target"));
		File f = new File(path + "/src/main/webapp/WEB-INF/keys.hb");
		try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f))) {
			oos.writeObject(keys);
		} catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public String encrypt(String msg) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] encStr = cipher.doFinal(msg.getBytes());
		return Base64.getEncoder().encodeToString(encStr);
	}

	@Override
	public String decrypt(String msg) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] encStr = Base64.getDecoder().decode(msg.getBytes());
		byte[] result = cipher.doFinal(encStr);
		return new String(result, "UTF-8");
	}
}














