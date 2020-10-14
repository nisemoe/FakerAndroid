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
 * 专用
 */
public class McriptApi {

	// 密钥
    private final static String secretKey = "iyyxscjinatcomxpenngo?#@" ;

    // 向量
    private final static String iv = "67985432" ;

    // 加解密统一使用的编码方式
    private final static String encoding = "utf-8" ;


    public static void main(String[] args) {

		try {
			//System.out.println(decode("vNwZ6wkUvtKkzBtdK8wBw1g/hG6ikuqTMjj6guily1GT22izaBck19Myzi5Akxg4cGyM1luPJDuEEV+O/Kyztwy6m5O478K4GIXxXBa5MPI1A2T11r15i4OZtP3dKT5Cmgb2ox8y/hNwswhtJR+sEoROfxni6dj6h0SDEZRejXUEUtFzj3M1zA25VYuUCleIiGv7ZTAJi45gP7v8ebAGOp54ZIAcZZDIgD6XmQXLV5QDIcm4rowPZLsfYvhs6TCIhtF0gMjRE0g3X3xLigNTI9ASpjuIycmbg9zQdv6Yg6DSz7Pvxx8i1VvvHbFEPlVzG2E4QQeUOcDoUpS/4lhtlC5F1kigDvF3IpJjeDjj9ov54ZRDn578J6js+WlnMpgN37UQQ2yfV435Q06kljCBKu+HyzouJmaGFY4a1FSDgAHhgvdihvg4uVj6JPOUdBHYsrlTuMAITh/0BYGBpJx+0kA16eqofZYPmP1mK5f/RAbscVNJOw+Iuz/mc/hfip6WnTmOwxEyF8nMRoV2ykHTPptuwgUTMgZVZXQIzyI/H0d7J+bYSXwFfZKt5Md2B/qqdJZ9IShU+wITDmuBPIFzOBhbjEDnCp7xRcl36FQrwdQApCafUh5o95VF0cr25kK+myHocMEm9MC5g6rLNGHQT8lq27dfqaewSdvRj2jdnjL7trYy6pMCHH4Bsd+QrDy+hVsMWg0B0avp0w3pCciHS112UQZ8686PMZF1EGsUzzrAO6sTKkXmH9kvf9Rnt1wKYTuCvf9j0uk="));
			//String content = "{\"ch\":\"TG\",\"packageName\":\"com.technull.stickfight2\",\"appname\":\"SDKBridge\",\"zt\":\"31313013091\",\"dversion\":\"\",\"dname\":\"\",\"sdkversion\":\"1000\",\"tag\":\"activity\",\"startTimes\":\"1\",\"startStamp\":\"1541488679203\",\"diviceid\":\"862298035484572\",\"mobiletype\":\"M3X,23,6.0.1\",\"fire\":\"1\"}";
			//System.out.println(encode(content));
			System.out.println(decode("vNwZ6wkUvtKkzBtdK8wBw1g/hG6ikuqTMjj6guily1GT22izaBck19Myzi5Akxg4cGyM1luPJDsXkipR1rrj1EaZbIdhTpvTLJmQDGgTrWBHaqKD6Y/oXLTyL5wYA0zF8zEW6oBjZ+TWK6LNCEQiNWw/9X5WQ70WItbLFRTDiDKhCnpEKXnzg29c8jhXAVZZRe9ztKzjS01Z6NEsjrav5Mk+D8sjFuprB6vYjjr6Je0scJiL/yXCL6W6luCgQ93SyIfatF7yFszecVuPPtZFg1psxBhqwW03qp54/Z8oy4BG4CBQRtaHyeZEBH5+NmpBldg0/QUZcdB5Fq2cuKhMCvzPKXjWztSshdsj76UfTo2Z/wIZFtzHYNcmOwLxbssLuRE0qcRn0MlXEiTGKrHjJ6UnGB4zBpSaIcSGO3NbM0zyHsLrUwZAcTqJr3HSQ1KFfqm+BAUdPqWU/9uVFiz/APLi9vDXTlHkKJBOZyihWiEP0G809etSGeXY910y/XNGLYJf90tL4o0v6cKhl/vYa9j7cJsjaqWmRX8RTiO688es3w078/Dw7z19vW6JDHITDwR3GRHOplakByHu+RK7cYNYw37w7W6qtkXgi9LsxO0+wcQxoqoI9eVDegz2GO1pzehMhGLQv/2KbtfxCC45knXYFvSwdTnqDh4laQhwj7w30PTIC1d+un0mjgepEcfH/fBqE5oiKznWKQlbmC6PDeanExYSpU3Cdw+q4bGSKaxiF9rB2RiLjuwzTWHXJFp1oUuLkLvUPzUdLUMVBZLy3Mz3ZPnW801jvdv36BQfhTzeFfFPwV4ls7eLZLbUBJSVRE1QkKCZjcaO92LHK43kj74fceeCrlcY7fDk6UkXP/PniaMvO0ITlav24fghuz+AdawO5OzCjP81EAe9FFBHxPoeuPIQkDqTAnQ7Pfjc+X35V6/stdc5kxH5IVbZESnnmrMCOcE8M4bLCYAfkiAnSBT+iLZQnPP8RCfcsZJ+4Sxxdq7g0VGjXLRIUWmvcig5/xXJvBVsYjfvjTcth6pwNbTfcLQ+xALzO00vBTcAKaOBzkLw/9sAJZZesForjVETPFVud769EWXMwYYWZv30dw=="));

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

    static String p = "ELj4Nu7JJWPpfnqIHQijHlAr3Dwu/ZhNENE14iN+8wG0fBut03BeOB9VLrhqwBPGZpmqJ1KV7vxlT97lOzudmYhwuMKHish7UrKVxLEhamfxUaxWmSQzBz0rsukRV5/G1HQtsH6YgJ1Ldvv4npfh/srKhodIqMV2bsaljxqVa8swQqAMKmrolnMOs2wH42+47kpbbG6m74ketWVl0qn5cHc+monfTs0XMU3KuvpCnTeq7pXO0wPj+8rJG69a0plTajTROgrwkqL9vhj8GT/vWGprynlChsdmlFEed7QEWp74y1QW6xcnF9rl5E1QahWiAtklbLEPkxmRfWN21AW/frhf0lhz09Ir8GOYndhchRvyjYihrSDkDMv1FyDfk9XcwG7sjqiKsaHnChsq83sTPmf8HHyMaZH9V664+7ntxQ7GcmMLzqBX0P68x5h+c52J/2znmnliWRCwsqf6AovFSP+TGzVWBpBTbs5PkpySiX5pNCFYgg6pso5Ux/GqKBopLIukLnlKn5Fec3NqRaWtIJ6xueMXncGl5sZPftRpFe4=";
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
