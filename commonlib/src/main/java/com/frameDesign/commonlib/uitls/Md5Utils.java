package com.frameDesign.commonlib.uitls;

import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * md5加密
 * @author liyong
 * @date 2019-10-22.
 */
public class Md5Utils {

	private Md5Utils() {
	}

	/**
	 * Return a hash according to the MD5 algorithm of the given String.
	 * 
	 * @param s
	 *            The String whose hash is required
	 * @return The MD5 hash of the given String
	 */
	public static String md5(String s) {
		try {
			if(!TextUtils.isEmpty(s)) {
				StringBuilder sStringBuilder = new StringBuilder();
				sStringBuilder.setLength(0);
				if (!TextUtils.isEmpty(s)) {
					MessageDigest sMd5MessageDigest = MessageDigest.getInstance("MD5");
					sMd5MessageDigest.reset();
					sMd5MessageDigest.update(s.getBytes());

					byte digest[] = sMd5MessageDigest.digest();

					for (int i = 0; i < digest.length; i++) {
						final int b = digest[i] & 255;
						if (b < 16) {
							sStringBuilder.append('0');
						}
						sStringBuilder.append(Integer.toHexString(b));
					}
				}
				return sStringBuilder.toString();
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public static String md5(String s, int count) {
		if(!TextUtils.isEmpty(s)) {
			if (count > 0) {
				String md5 = md5(s);
				return md5(md5, --count);
			} else {
				return s;
			}
		}
		
		return null;
	}

	public static String md5Password(String s) {
		String pwd = md5(s, 5);
		return pwd;
	}
}
