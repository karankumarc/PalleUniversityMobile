package com.techpalle.poc;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class CryptUtility {
	
	public static void encrypt(){
		//ENCRYPTING code
		try {
			FileInputStream fis = new FileInputStream("e:/data.mp4");
			FileOutputStream fos = new FileOutputStream("e:/satish.aes", false);
			SecretKeySpec sks = new SecretKeySpec("SATISH REDDY PAL".getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, sks, new IvParameterSpec("satishpasatishpa".getBytes()));
			CipherOutputStream cos = new CipherOutputStream(fos, cipher);
			int b;
			byte[] d = new byte[8];
			while((b = fis.read(d)) != -1){
				cos.write(d, 0, b);
			}
			cos.flush();
			cos.close();
			fis.close();
			System.out.println("ENCRYPTION DONE");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void decryptFast(String encPath, String destPath){
		try {
			SecretKeySpec sks = new SecretKeySpec("SATISH REDDY PAL".getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, 
					sks, 
					new IvParameterSpec("satishpasatishpa".getBytes()));
			int blockSize = cipher.getBlockSize();
			int outputSize = cipher.getOutputSize(blockSize);
			byte[] inBytes = new byte[blockSize*1024];
			byte[] outBytes = new byte[outputSize*1024];
	        FileInputStream in= new FileInputStream(encPath);
	        FileOutputStream out=new FileOutputStream(destPath);
	        
	        BufferedInputStream inStream = 
	        		new BufferedInputStream(in);
	        int inLength = 0;;
	        boolean more = true;
	        while (more)
	        {
	             inLength = inStream.read(inBytes);
	             if (inLength/1024 == blockSize)
	             {
	                int outLength 
	                   = cipher.update(inBytes, 
	                		   0, blockSize*1024, outBytes);
	                out.write(outBytes, 0, outLength);

	             }
	             else more = false;         
	          }
	          if (inLength > 0)
	             outBytes = cipher.doFinal(inBytes, 0, inLength);
	          else
	             outBytes = cipher.doFinal();

	          out.write(outBytes);

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ShortBufferException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static void decrypt(String encPath, String destPath){
		//DECRYPTING code
		 try {
			FileInputStream fis = new FileInputStream(encPath);
			BufferedInputStream bis = new BufferedInputStream(fis);//satish
			FileOutputStream fos = new FileOutputStream(destPath,false);
			//BufferedOutputStream bos = new BufferedOutputStream(fos);//satish
			SecretKeySpec sks = new SecretKeySpec("SATISH REDDY PAL".getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, sks, new IvParameterSpec("satishpasatishpa".getBytes()));
			CipherInputStream cis = new 
					CipherInputStream(/*fis*/bis, cipher);
			//BufferedReader br = new BufferedReader(in)
			int b;
			byte[] d = new byte[64*1024];
			while((b = cis.read(d)) != -1){
				fos.write(d, 0, b);
				//bos.write(d, 0, b);
			}
			
			fos.flush();
			fos.close();
			cis.close();
			//bis.close();//satish
			//bos.close();//satish
			
			System.out.println("DECRYPT DONE!!!");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
	}
	
}
