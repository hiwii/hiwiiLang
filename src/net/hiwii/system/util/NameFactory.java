package net.hiwii.system.util;

public class NameFactory {

	public static String getNextKey(String key){
		if (key == null || key.length() == 0){
			return "0";
		}
		char end = key.charAt(key.length() - 1);
		if ( end == 'z'){
			return key + '0';
		} else {
			return key.substring(0, key.length() - 1) + getNextChar(end);
		}
	}
	
	public static char getNextChar(char ch){
		if (ch >= '0' && ch < '9'){
			return (char)(ch + 1);
		} else if (ch == '9'){
			return 'A';
		} else if (ch >= 'A' && ch < 'Z'){
			return (char)(ch + 1);
		}else if (ch == 'Z'){
			return 'a';
		}else if (ch >= 'a' && ch < 'z'){
			return (char)(ch + 1);
		}else {  //other chars. if all are letters, this can't happen.
			return '0';
		}
	}

}
