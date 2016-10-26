package com.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

/**
 * @description convenient to handle email string.
 * @author <a href="mailto:ou8zz@sina.com">OLE</a>
 * @date Jul 29, 2011  9:48:26 PM
 * @version 3.0
 */
public class RegexUtil {

	public static ArrayList<String> getMatches(Pattern pattern, String match, int group) {
		ArrayList<String> matches = new ArrayList<String>();
		Matcher matcher = pattern.matcher(match);
		while (matcher.find()) {
			matches.add(matcher.group(group));
		}
		return matches;
	}

	public static boolean verify(String regex, String str) {
		Pattern p = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
		return p.matcher(str).matches();
	}
	
	public static boolean notEmpty(Object str) {
		return (str == null || "".equals(str)) ? false:true;
	}
	
	public static boolean isEmpty(Object str) {
		return (str == null || str.equals("")) ? true:false;
	}
	
	/**
     * 格式化成小数点(#.##)
     * @param float
     * @param str
     * @return String
     */ 
	public static Object formatDecimal(Object f, String str) {
		DecimalFormat df = new DecimalFormat(str);
        return df.format(f);
    }
	
	/**
	 * MD5处理
	 * @param str
	 * @return
	 */
	public static String encodeMd5(String str) {
		Md5PasswordEncoder m = new Md5PasswordEncoder();
		String encodePassword = m.encodePassword(str, null);
		return encodePassword;
	}
	
}