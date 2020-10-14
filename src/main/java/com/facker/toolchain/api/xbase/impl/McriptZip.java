package com.facker.toolchain.api.xbase.impl;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * TODO 透传文件专用
 */
public class McriptZip {

	// 密钥
    private final static String secretKey = "tyyxscjinatcomxpenngo?#@" ;

    // 向量
    private final static String iv = "57985432" ;

    // 加解密统一使用的编码方式
    private final static String encoding = "utf-8" ;


    public static void main(String[] args) {
		try {
			//System.out.println(decode("vNwZ6wkUvtIDdF5rSo2j0lAFgfjKh3h+XMW9LLXzGrTSKtd0V1b/U408Msp4jBcXkWmxadp7f1FrSIUxKIKWGv2nkoz0mhUxMrpIT2iOiuX7zZLx1GZ5r6YaomFLo+u8PTlxh/ljAoS2hLBMmrYdtn2SDXu4PtN92zZ9n4kT+ACTMNM7z9R780rkZvsdnFYIeclqNaPUt4N4MF5F6eCF+RK+50lwAu9vpDBMTQ1pEvuAjA6PYeus5JmL4f6QxU5mYRZclnMTFiR2Ij4iYVgAyx1pHx9ALBnkZDWw+DocPSd2gNgjiwRxljF7caMdmhl3KyI/8/ASFiO7xkxCzH/UVb632P9RiryfFqWNyiphY2j227ScC9OvCj5lrPYQb7smO5WmJqTi0skcVb1OFFcZVsEvYFVQ+2UsNmJlx0qSPPveZUAnnMcArpTiXmzeMy+l9NtoAjpU5ev3xbAZ6FXJnQelkgionSeYlikEs2ELnCscjPq7V2SQkDVCOOEM+Q6VlBZJUKnKqwfD1S/xkfeZA6ZnCD0cak/xHXFOySFznZ3PyiuEsTtjb+nQNIMQ6sKI7q/6j77UEp3VdsoAWoVPVkvni8T4ou5h/xmw2hvHbL16O0Z9KQEf5EQYirOW/ml7GDEkPMwktJPtKxmG2y0zYzR/N5K0XB5G8DdzQOBwCtG8YenkMqLna5GMeUpSLSlisayXK+0UqLY9R+HOwKc3YjlADfXxDLD9nzrEZeYjyc1BNZTamfP2Ykn6QDURRLWlnCGauR0R/K3R0kle2bIUb+sPVSFhNM5sjvMWTxomejBTkeWbvlljLINb5hR6svEnpteJOyZk1XhM4qKH1h72uog8AfMO78TogaFPGtX9XBGcTBBpozVBnL99dzOz17TCeDNh7FZ26ly+BNhBwturAjjprybwhps1Gs30niZQiHgQUKNFVdFTQ4wlTEH1hm0eLLMG9cQVC1yQgGIUF+BfnvoyApik/x8Mne1SlCp++0dJJgjllvqy9oQam+1vdj2ZC3PVMhgZ4dh1VyycTqkVmT1puujwLfd18TcYozoDDUlJ4ktoe9upyAgchRiHRim+vqp/UKcbnRmzAgUbmj2yPOO+gLViHXzhCz1lubtXuQ7fdhapmKggz3SZUzj2kM8udghht8zHVEqBg8AlImoJLKEew5bS+JeDDtnBMyAuNa8yLCo60c7HXObWHxTv9CyAPLXmraOCi1i8ADUfHn8j+uALDXg3AZnfJN+Q466lAxWuxZD1t56LceeqhlM2ep9I8saKMW6Dlag9pTEu89rVnhqIcSccIjNucAQJof5C1BCwGK6u+rG+1SAcycVRu/HFiLx+6bsR6Lu9Y6v8dLS1Apa9TOgHcfwO3OCPBqEVQQqsc5Gyt5ho8kwk+FJTodijTSKaAhy8Ujd7F2mKcU0PKT8/xhRAY6V7gHWDH0HaqtZ9HgoXNPUJ9C5QQN4CEU1nryOf/pr+tFkrJ7DkoVzhEcOb4DlW9OMZOdW+QqjQvvZYoup/nNUs39Mi/NN8R09j0g2SxmFKqArc4ITTPIU1/GzOZD31qOMovAKL1M+rXzpzWjN1yjkuHgzor3h0TWZq5nhYzTY8zTnDoO6Z/b2GGC/u02P7bWq6+nb0GUpL2KnsOg8C3suYZUccj2VTjpXHPbjkKZqAgwmkTBAJGl3FvZ/3OgvNH4dL"));
			//String content = "{\"ch\":\"TG\",\"packageName\":\"com.technull.stickfight2\",\"appname\":\"SDKBridge\",\"zt\":\"31313013091\",\"dversion\":\"\",\"dname\":\"\",\"sdkversion\":\"1000\",\"tag\":\"activity\",\"startTimes\":\"1\",\"startStamp\":\"1541488679203\",\"diviceid\":\"862298035484572\",\"mobiletype\":\"M3X,23,6.0.1\",\"fire\":\"1\"}";
			//System.out.println(encode(content));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			//System.out.println(executeHttpGet(p));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(encryptionMD5("123".getBytes()));
	}

    public static String encryptionMD5(byte[] byteStr) {
        MessageDigest messageDigest = null;
        StringBuffer md5StrBuff = new StringBuffer();
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(byteStr);
            byte[] byteArray = messageDigest.digest();
//            return Base64.encodeToString(byteArray,Base64.NO_WRAP);
            for (int i = 0; i < byteArray.length; i++) {
                if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                    md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
                } else {
                    md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
                }
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5StrBuff.toString();
    }

    static String p = "KZLFUIjKnmHE1ZLJm00E1pIbJdohibPfnZUZmOhQq0VcqZWk2gtOEdhygE4MRoPsQuJUWpdzmw6deFx3kQt867m2Kw7smddXpl6lByK64Fy3hjSjO5syakBa2Me8MwWVshwj7e9dJ+Dyh1hHAcP8mqfBm/vJlTPwAOLjuvbmx3zc7llxydiRi0npw2V7aSoL6DCpd3/MV3e7UODbq/sX3FL7ll73Ys1azYSaTTD+7io1G1a+yto3bh3QUTpC3DbEq87GoRaU5XlXxo2k9UXXs+IU8UUeaaR3Br/OEWv/3d43L0WIdwndieWzqdJThRe+/JI8XyrRr1ktf/9qwcT7Wl8AQtQVxr6RhgE3qfRgepQ=";
    public static String executeHttpGet(String gmsclient) throws Exception {
		String result = null;
		URL url = null;
		HttpURLConnection connection = null;
		InputStreamReader in = null;
		try {
			String workUrl = "http://t.ianpei.com/ms/gms/";
			url = new URL(workUrl);
			connection = (HttpURLConnection) url.openConnection();
			connection.addRequestProperty("gmsclient", gmsclient);
			in = new InputStreamReader(connection.getInputStream());
			BufferedReader bufferedReader = new BufferedReader(in);
			StringBuffer strBuffer = new StringBuffer();
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				strBuffer.append(line);
			}
			result = strBuffer.toString();
		} catch (Exception e) {
			throw new Exception();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

    /**
     * 3DES加密
     * @param plainText 普通文本
     * @return
     * @throws Exception
     */
    public static String encode(String plainText) throws Exception {
        Key deskey = null ;
        DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance( "desede" );
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance( "desede/CBC/PKCS5Padding" );
        IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
        byte [] encryptData = cipher.doFinal(plainText.getBytes(encoding));
        return Base64.encode(encryptData);
    }

    public static byte[] encodeV2(String plainText) throws Exception {
        Key deskey = null ;
        DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance( "desede" );
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance( "desede/CBC/PKCS5Padding" );
        IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
        byte [] encryptData = cipher.doFinal(plainText.getBytes(encoding));
        return encryptData;
    }
    /**
     * 3DES解密
     *
     * @param encryptText 加密文本
     * @return
     * @throws Exception
     */
    public static String decode(String encryptText) throws Exception {
        Key deskey = null ;
        DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance( "desede" );
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance( "desede/CBC/PKCS5Padding" );
        IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
        byte [] decryptData = cipher.doFinal(Base64.decode(encryptText));

        return new String(decryptData, encoding);
    }

	public static String padding(String str) {
		byte[] oldByteArray;
		try {
			oldByteArray = str.getBytes("UTF8");
			int numberToPad = 8 - oldByteArray.length % 8;
			byte[] newByteArray = new byte[oldByteArray.length + numberToPad];
			System.arraycopy(oldByteArray, 0, newByteArray, 0,oldByteArray.length);
			for (int i = oldByteArray.length; i < newByteArray.length; ++i) {
				newByteArray[i] = 0;
			}
			return new String(newByteArray, "UTF8");
		} catch (UnsupportedEncodingException e) {
			System.out.println("Crypter.padding UnsupportedEncodingException");
		}
		return null;
	}

	/**
	  * Base64编码工具类
	  *
	  */
	public static class Base64 {
	     private static final char [] legalChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/" .toCharArray();

	     public static String encode( byte [] data) {
	         int start = 0 ;
	         int len = data.length;
	         StringBuffer buf = new StringBuffer(data.length * 3 / 2 );

	         int end = len - 3 ;
	         int i = start;
	         int n = 0 ;

	         while (i <= end) {
	             int d = (((( int ) data[i]) & 0x0ff ) << 16 ) | (((( int ) data[i + 1 ]) & 0x0ff ) << 8 ) | ((( int ) data[i + 2 ]) & 0x0ff );

	             buf.append(legalChars[(d >> 18 ) & 63 ]);
	             buf.append(legalChars[(d >> 12 ) & 63 ]);
	             buf.append(legalChars[(d >> 6 ) & 63 ]);
	             buf.append(legalChars[d & 63 ]);

	             i += 3 ;

	             if (n++ >= 14 ) {
	                 n = 0 ;
	                 buf.append( " " );
	             }
	         }

	         if (i == start + len - 2 ) {
	             int d = (((( int ) data[i]) & 0x0ff ) << 16 ) | (((( int ) data[i + 1 ]) & 255 ) << 8 );

	             buf.append(legalChars[(d >> 18 ) & 63 ]);
	             buf.append(legalChars[(d >> 12 ) & 63 ]);
	             buf.append(legalChars[(d >> 6 ) & 63 ]);
	             buf.append( "=" );
	         } else if (i == start + len - 1 ) {
	             int d = ((( int ) data[i]) & 0x0ff ) << 16 ;

	             buf.append(legalChars[(d >> 18 ) & 63 ]);
	             buf.append(legalChars[(d >> 12 ) & 63 ]);
	             buf.append( "==" );
	         }

	         return buf.toString();
	     }

	     private static int decode( char c) {
	         if (c >= 'A' && c <= 'Z' )
	             return (( int ) c) - 65 ;
	         else if (c >= 'a' && c <= 'z' )
	             return (( int ) c) - 97 + 26 ;
	         else if (c >= '0' && c <= '9' )
	             return (( int ) c) - 48 + 26 + 26 ;
	         else
	             switch (c) {
	             case '+' :
	                 return 62 ;
	             case '/' :
	                 return 63 ;
	             case '=' :
	                 return 0 ;
	             default :
	                 throw new RuntimeException( "unexpected code: " + c);
	             }
	     }

	     /**
	      * Decodes the given Base64 encoded String to a new byte array. The byte array holding the decoded data is returned.
	      */

	     public static byte [] decode(String s) {

	         ByteArrayOutputStream bos = new ByteArrayOutputStream();
	         try {
	             decode(s, bos);
	         } catch (IOException e) {
	             throw new RuntimeException();
	         }
	         byte [] decodedBytes = bos.toByteArray();
	         try {
	             bos.close();
	             bos = null ;
	         } catch (IOException ex) {
	             System.err.println( "Error while decoding BASE64: " + ex.toString());
	         }
	         return decodedBytes;
	     }

	     private static void decode(String s, OutputStream os) throws IOException {
	         int i = 0 ;

	         int len = s.length();

	         while ( true ) {
	             while (i < len && s.charAt(i) <= ' ' )
	                 i++;

	             if (i == len)
	                 break ;

	             int tri = (decode(s.charAt(i)) << 18 ) + (decode(s.charAt(i + 1 )) << 12 ) + (decode(s.charAt(i + 2 )) << 6 ) + (decode(s.charAt(i + 3 )));

	             os.write((tri >> 16 ) & 255 );
	             if (s.charAt(i + 2 ) == '=' )
	                 break ;
	             os.write((tri >> 8 ) & 255 );
	             if (s.charAt(i + 3 ) == '=' )
	                 break ;
	             os.write(tri & 255 );

	             i += 4 ;
	         }
	     }
	}
}
