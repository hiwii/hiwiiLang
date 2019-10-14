package net.hiwii.system;

import java.util.Arrays;
import java.util.List;

/**
 * 系统字符分为英文字母、digit、特殊字符、不可见字符、其它字符
 * @author Administrator
 *
 */
public class SystemCharacter {
	public static String symbols = "`~@#$%^&*()-+={}[]|\\:;\"\'<>,./?";
//	static{
//		ts.add('`');
//		ts.add('~');
//		ts.add('@');
//		ts.add('#');
//		ts.add('$');
//		ts.add('%');
//		ts.add('^');
//		ts.add('&');
//		ts.add('&');
//		ts.add('*');
//		ts.add('(');
//		ts.add(')');
//		ts.add('-');
//		ts.add('_');
//		ts.add('+');
//		ts.add('=');
//		ts.add('|');
//		ts.add('\\');
//		
//		ts.add(':');
//		ts.add(';');
//		ts.add('\"');
//		ts.add('\'');
//		ts.add('<');
//		ts.add('>');
//		ts.add(',');
//		ts.add('.');
//		ts.add('-');
//		ts.add('_');
//		ts.add('?');
//		ts.add('/');
//	}
	
	public static boolean isChar(String code){
		if(code.length() == 1){
			char ch = code.charAt(0);
			return Character.isLetter(ch);
		}else if(code.length() == 2){
			char chh = code.charAt(0);
			char chl = code.charAt(1);
			int cd = Character.toCodePoint(chh, chl);
			return Character.isLetter(cd);
		}else{
			return false;
		}
	}
	public static boolean isSpecial(String code){
		if(code.length() != 1){
			return false;
		}
		if(symbols.indexOf(code) >= 0){
			return true;
		}else{
			return false;
		}
	}

	public static boolean isLetter(String code){
		if(code.length() != 1){
			return false;
		}
		char ch = code.charAt(0);
		if((ch >= 'A' && ch <= 'Z') || ((ch >= 'a' && ch <= 'z'))){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean isDigit(String code){
		if(code.length() != 1){
			return false;
		}
		char ch = code.charAt(0);
		if(ch >= '0' && ch <= '9'){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean isInvisiable(String code){
		if(code.length() != 1){
			return false;
		}
		char ch = code.charAt(0);
		List<Character> list = Arrays.asList(' ', '\n', '\r', '\t', '\f');
		if(list.contains(ch)){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 注意"_"属于others，不属于特殊字符。
	 * @param code
	 * @return
	 */
	public static boolean isOthers(String code){
		if(code.length() == 1){
			char ch = code.charAt(0);
			List<Character> list = Arrays.asList(' ', '\n', '\r', '\t', '\f');
			if(list.contains(ch)){
				return false;
			}
			if(ch >= '0' && ch <= '9'){
				return false;
			}
			if((ch >= 'A' && ch <= 'Z') || ((ch >= 'a' && ch <= 'z'))){
				return false;
			}
			if(symbols.indexOf(code) > 0){
				return false;
			}
			return Character.isLetter(ch);
		}else if(code.length() == 2){
			char chh = code.charAt(0);
			char chl = code.charAt(1);
			if(Character.isSurrogatePair(chh, chl)){
				return false;
			}
			int cd = Character.toCodePoint(chh, chl);
			return Character.isLetter(cd);
		}else{
			return false;
		}
	}
	
	public static boolean isIdentifierFirst(String code){
		if(code.length() == 1){
			char ch = code.charAt(0);
			if(ch == '_'){
				return true;
			}
			if(isSpecial(ch)){
				return false;
			}
			if(isInvisiable(ch)){
				return false;
			}
			if(isDigit(ch)){
				return false;
			}
			if(isLetter(ch) || isOthers(ch)){
				return true;
			}else{
				return false;
			}
		}else if(code.length() == 2){
			char chh = code.charAt(0);
			char chl = code.charAt(1);
			return isOthers(chh, chl);
		}else{
			return false;
		}
	}
	
	/**
	 * letter or digit or others
	 * @param code
	 * @return
	 */
	public static boolean isIdentifierPart(String code){
		if(code.length() == 1){
			char ch = code.charAt(0);
			if(ch == '_'){
				return true;
			}
			if(isSpecial(ch)){
				return false;
			}
			if(isInvisiable(ch)){
				return false;
			}
			if(isDigit(ch)){
				return true;
			}
			if(isLetter(ch) || isOthers(ch)){
				return true;
			}else{
				return false;
			}
		}else if(code.length() == 2){
			char chh = code.charAt(0);
			char chl = code.charAt(1);
			return isOthers(chh, chl);
		}else{
			return false;
		}
	}
	
	public static boolean isChar(char ch){
		if(isLetter(ch)){
			return true;
		}else if(isDigit(ch)){
			return true;
		}else if(isSpecial(ch)){
			return true;
		}else if(isInvisiable(ch)){
			return false;
		}else{
			return Character.isLetter(ch);
		}
	}
	
	public static boolean isSpecial(char ch){
		if(symbols.indexOf(ch) >= 0){
			return true;
		}else{
			return false;
		}
	}

	public static boolean isLetter(char ch){
		if((ch >= 'A' && ch <= 'Z') || ((ch >= 'a' && ch <= 'z'))){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean isDigit(char ch){
		if(ch >= '0' && ch <= '9'){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean isInvisiable(char ch){
		List<Character> list = Arrays.asList(' ', '\n', '\r', '\t', '\f');
		if(list.contains(ch)){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean isOthers(char ch){
		if(isLetter(ch)){
			return false;
		}else if(isDigit(ch)){
			return false;
		}else if(isSpecial(ch)){
			return false;
		}else if(isInvisiable(ch)){
			return false;
		}else{
			return Character.isLetter(ch);
		}
	}
	
	public static boolean isOthers(char chh, char chl){
		if(Character.isSurrogatePair(chh, chl)){
			return false;
		}
		int cd = Character.toCodePoint(chh, chl);
		return Character.isLetter(cd);
	}
	
	public static boolean isChar(int codePoint){
		String code = new String(Character.toChars(codePoint));
		return isChar(code);
	}
	public static boolean isSpecial(int codePoint){
		String code = new String(Character.toChars(codePoint));
		return isSpecial(code);
	}

	public static boolean isLetter(int codePoint){
		String code = new String(Character.toChars(codePoint));
		return isLetter(code);
	}
	
	public static boolean isDigit(int codePoint){
		String code = new String(Character.toChars(codePoint));
		return isDigit(code);
	}
	
	public static boolean isInvisiable(int codePoint){
		String code = new String(Character.toChars(codePoint));
		return isInvisiable(code);
	}
	
	/**
	 * 注意"_"属于others，不属于特殊字符。
	 * @param code
	 * @return
	 */
	public static boolean isOthers(int codePoint){
		String code = new String(Character.toChars(codePoint));
		return isOthers(code);
	}
	
	public static boolean isIdentifierFirst(int codePoint){
		String code = new String(Character.toChars(codePoint));
		return isIdentifierFirst(code);
	}
	
	/**
	 * letter or digit or others
	 * @param code
	 * @return
	 */
	public static boolean isIdentifierPart(int codePoint){
		String code = new String(Character.toChars(codePoint));
		return isIdentifierPart(code);
	}
}
