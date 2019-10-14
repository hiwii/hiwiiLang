package net.hiwii.system.util;

import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.NavigableMap;

import net.hiwii.cognition.Expression;
import net.hiwii.cognition.result.JudgmentResult;
import net.hiwii.expr.FunctionExpression;
import net.hiwii.expr.IdentifierExpression;
import net.hiwii.expr.NullExpression;
import net.hiwii.expr.StringExpression;
import net.hiwii.system.SystemCharacter;
import net.hiwii.system.SystemDefinition;
import net.hiwii.system.exception.ApplicationException;
import net.hiwii.system.syntax.def.DeclaredExpression;
import net.hiwii.system.syntax.number.IntegerNumber;
import net.hiwii.view.Entity;

public class StringUtil {
	/**
	 * get part string of "#" separated string
	 * pos from 1 
	 */
	public static String getPart(String str, int pos){
		if(str == null || str.length() == 0){
			return str;
		}
		String[] res = str.split("#");
		if(pos <= res.length){
			return res[pos - 1];
		}else{
			return null;
		}
	}
	
	public static char negate(char sign){
		if(sign == '+'){
			return '-';
		}else{
			return '+';
		}
	}
	
	public static boolean hasPoint(String arg){
		int pos = arg.indexOf('.');
		if(pos >= 0){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * input a interger, add point by scale.
	 * scale > 0
	 * to get a floatNumber from bigDecimal
	 * num is intPart of bigDecimal
	 * @param num
	 * @return
	 */
	public static String scaleNumber(String num, int scale){
		String val = num;
		int pos = scale - num.length();
		if(pos >= 0){
			val = fillChar(pos, '0') + num;
			return "0." + val;
		}else{
			return val.substring(0, scale) + "." + val.substring(scale);
		}
	}
	/**
	 * ǰ����С������ַ�����
	 * �����Ǵ������ַ�����
	 * str0��long�ַ�����str1�Ƕ��ַ���
	 * �Ƿ�str0��ǰ������ȫƥ��str1
	 * @param strl
	 * @param strs
	 * @return
	 */
	public static boolean matched(String str0, String str1){
		if(str0 == null || str1 == null){
			return false;
		}
		if(str1.length() > str0.length()){
			return false;
		}
		if(str1.equals(str0.substring(0, str1.length()))){
			return true;
		}else{
			return false;
		}
	}
	
	public static char numberFormat(String value){
		if(value.matches("[1-9]([0-9])*/[1-9]([0-9])*")){
			return 'd';
		}else if(value.matches("[1-9]([0-9])*")){
			return 'i';//int
		}else if(value.matches("0 [xX] ([0-9a-fA-F])+")){
			return 'h';//int
		}else{
			// if(value.matches("[1-9]([0-9])*")){
			return 'f';//float or science
		}
	}
	
	public static String fillChar(int times, char ch){
		if(times <= 0){
			return "";
		}
		String ret = "";
		for(int i=0;i<times;i++){
			ret = ret + ch;
		}
		return ret;
	}
	
	public static String reverse(String val){
		String ret = "";
		for(int i=val.length() - 1;i>=0;i--){
			ret = ret + val.charAt(i);
		}
		return ret;
	}
	/**
	 * getС������������
	 */
	public static String getPointLeft(String value){
		int pos = value.indexOf(".");
		if(pos > 0){
			return value.substring(0, pos);
		}else{
			return value;
		}
	}
	
	/**
	 * getС����С������
	 */
	public static String getPointRight(String value){
		int pos = value.indexOf(".");
		if(pos > 0){
			return value.substring(pos + 1);
		}else{
			return "0";
		}
	}
	
	/**
	 * getС����С������,����ȷ����"."
	 */
	public static String getPointRight0(String value){
		int pos = value.indexOf(".");
		if(pos > 0){
			return value.substring(pos + 1);
		}else{
			return "";
		}
	}
	
	/**
	 * ��matchResult�У���Ҫn0.n1.n2......
	 */
	public static String head(String str){
		int pos = str.indexOf('.');
		if(pos > 0){
			return str.substring(0, pos);
		}else{
			return str;
		}
	}
	
	/**
	 * ��matchResult�У���Ҫn0.n1.n2......
	 * TODO ������Ч�Լ�飬�Ƿ����������ʽ��
	 */
	public static String tail(String str){
		int pos = str.indexOf('.');
		if(pos > 0){
			return str.substring(pos + 1);
		}else{
			return null;
		}
	}
	public static String getFractionUp(String value){
//		if(value.matches("[1-9]([0-9])*/[1-9]([0-9])*")){
//			int pos = value.indexOf("/");
//			return value.substring(0, pos);
//		}else{
//			return null;
//		}
		int pos = value.indexOf("/");
		return value.substring(0, pos);
	}
	
	public static String getFractionLow(String value){
		int pos = value.indexOf("/");
		return value.substring(pos + 1);
	}
	
	public static String getScientificValue(String value){
		int pos = value.indexOf("e");
		if(pos < 0){
			pos = value.indexOf("E");
		}
		return value.substring(0, pos);
	}
	
	public static String getScientificPower(String value){
		int pos = value.indexOf("e");
		if(pos < 0){
			pos = value.indexOf("E");
		}
		return value.substring(pos + 1);
	}
	
//	/**
//	 * declare �У��Բ���������Ϊ��Ҫ����
//	 * �ɱ�����������$��ʾ
//	 * @param name
//	 * @return
//	 */
//	public static String getIdentifierSignature(String name){
//		String ret = name + "#0";
//		return ret;
//	}
	
	/**
	 * String format: content%number.
	 * result = content
	 */
	public static String dropTail(String str){
		int pos = str.lastIndexOf('%');
		if(pos > 0){
			return str.substring(0, pos);
		}else{
			return str;
		}
	}
	
	public static boolean positionValid(String str, int pos){
		if(pos < 0){
			return false;
		}
		if(pos < str.length()){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * declare�ı��������ʽ��
	 * name(type[,condition])
	 * �����洢�ĸ�ʽ��
	 * name|arguments.size|id
	 * id��Ϊ�������ں�������ͬ������������/������ͬʱ�Ķ���
	 * @param name
	 * @param args
	 * @return
	 */
	public static DeclaredExpression getDeclareExpression(List<Expression> args) throws ApplicationException{
		if(args.size() < 1){
			throw new ApplicationException("wrong arg number.");
		}
		DeclaredExpression de = new DeclaredExpression();
		List<String> names = new ArrayList<String>();
		List<Expression> types = new ArrayList<Expression>();
		List<Expression> limits = new ArrayList<Expression>();
		for(Expression exp:args){
			if(exp instanceof IdentifierExpression){
				IdentifierExpression ie = (IdentifierExpression) exp;
				names.add(ie.getName());
				types.add(new NullExpression());
				limits.add(new NullExpression());
			}else if(exp instanceof FunctionExpression){
				FunctionExpression fe = (FunctionExpression) exp;
				names.add(fe.getName());
				List<Expression> la = fe.getArguments();
				if(la.size() == 1){
					Expression arg0 = la.get(0);
					types.add(arg0);
					limits.add(new NullExpression());
				}else if(la.size() == 2){
					types.add(la.get(0));
					limits.add(la.get(1));
				}else{
					throw new ApplicationException("wrong arg number.");
				}
			}
		}
		int size = names.size() - 1;
		int start = 0;
		while(start < size){
			String n0 = names.get(start);
			start++;
			for(int i=start;i<size;i++){
				String n1 = names.get(i);
				if(n0.equals(n1)){
					throw new ApplicationException("overlap name.");
				}
			}
		}
		de.setArguments(names);
		de.setLimits(limits);
		de.setTypes(types);
		return de;
	}
//		String ret = name + "#" + args.size() + "#" + "(";
//		String names = "";
//		String types = "";
//		String conds = "";//����
//		for(Expression exp:args){
//			String t = "";//type
//			String c = "";//condition
//			String n = "";//name
//			if(exp instanceof IdentifierExpression){
//				IdentifierExpression ie = (IdentifierExpression) exp;
//				n = ie.getName();
//				t = "";//��ʾ��������
//				c = "";
//			}else if(exp instanceof FunctionExpression){
//				FunctionExpression fe = (FunctionExpression) exp;
//				n = fe.getName();
//				List<Expression> la = fe.getArguments();
//				if(la.size() == 1){
//					Expression arg0 = la.get(0);
//					/*
//					 * ���Ϳ����Ƕ��ֱ��ʽ��
//					 * 1��identifier��ʾ����
//					 * 2������.modify(�������)
//					 * 3������@host
//					 * ����ƥ��ʱ��ҪSystemParser����
//					 */
//					t = arg0.toString();
////					c = "";
//				}else if(la.size() == 2){
//					//�ڶ��������Ǳ�������
//					c = la.get(1).toString();
//				}else{
//					throw new ApplicationException("wrong arg number.");
//				}
//			}
//			types = types + t + ",";//Ϊ�����жϣ����Ч�ʣ��������һ����������","
//			names = names + n + ",";
//			conds = conds + c + ",";
//		}
//		ret = ret + ")";
//		return ret;

	/**
	 * key format "type|name|argNum
	 * ���������У�ͬ��/ͬ���������ģ�����һ��id���Ա�־��ͬ
	 * ����������һ��������id��key
	 */
	public static String getNextKey(String key, NavigableMap<String,? extends Entity> map){
		String last = map.floorKey(key + "0");//��ֹ�ض�number���бȽϣ�3/30
		if(matched(last, key) && !matched(last, key + "0")){
			String next = NameFactory.getNextKey(last);
			return next;
		}else{
			//sessionKey use "#"
			return key + "%" + NameFactory.getNextKey("");
		}
	}
	
	public static String getKeyNumber(String key){
		int pos = key.lastIndexOf("#");
		if(pos > 0){
			return  key.substring(pos + 1);
		}else{
			return "";
		}
	}
	
	public static boolean containsKey(String name, NavigableMap<String,Expression> map, char type){
		String key = type + "#" + name + "#0";
		if(map.containsKey(key)){
			return true;
		}
		return false;
	}
	
	public static String getPropertyKey(String name, Entity from){
		
		return null;
	}
	
	public static boolean hasNoDup(List<String> list){
		int i=0,j=0;
		for(;i<list.size();i++){
			String a = list.get(i);
			for(j=i+1;j<list.size();j++){
				String b = list.get(j);
				if(a.equals(b)){
					return false;
				}
			}
		}
		return true;
	}
	public static boolean isDefinition(String def){
		Expression expr = SystemDefinition.doIsPositive("String", def);
		//TODO SystemDefinition.doIsPasitiveӦ�÷���boolean��definition�Ƿ�Entity
		if(expr instanceof JudgmentResult){
			JudgmentResult jr = (JudgmentResult) expr;
			return jr.isResult();
		}else{
			return false;
		}			
	}
	
	public static int getInteger(IntegerNumber in){
		if(in.getSign() == '+'){
			return Integer.parseInt(in.getValue());
		}else{
			int ret = Integer.parseInt(in.getValue());
			ret = -1 * ret;
			return ret;
		}
	}
	
	public static String getIPAddress(SocketAddress sa){
		String add = sa.toString();
		int pos = add.indexOf(':');
		if(pos > 0){
			return add.substring(1, pos);
		}else{
			return add;
		}
	}
	
	/**
	 * requestformat: [ac|op|po|ne]+sessionId+#
	 * @param req
	 * @return
	 */
	public static String getSessionId(String req){
		int pos = req.indexOf('#');
		if(pos >= 2){
			return req.substring(2, pos);
		}else{
			return "";
		}
	}
	
	public static String getRequestString(String req){
		int pos = req.indexOf('#');
		if(pos >= 2){
			return req.substring(pos + 1);
		}else{
			return "";
		}
	}
	
	public static String getMessageType(String req){
		if(req.length() >= 2){
			return req.substring(0, 2);
		}else{
			return "un";        //unknown
		}
	}
	
	public static Expression parseString(String str){
		if(str == null || str.trim().length() == 0){
			return null;
		}
		StringExpression se = new StringExpression(str);
		return se.toExpression();
	}
	
	public static boolean inString(String str, int pos, int len, boolean forward){
		if(pos < 0 || pos >= str.length()){
			return false;
		}
		if(forward){
			if(pos + len >= str.length()){
				return false;
			}
			return true;
		}else{
			if(pos - len < 0){
				return false;
			}
			return true;
		}
	}
	/**
	 * ��pos��ʼ����forward�����ȡlen���ַ���
	 * @param str
	 * @param len
	 * @param pos
	 * @param forward
	 * @return
	 * @throws ApplicationException
	 */
	public static String cut(String str, int pos, int len, boolean forward) throws ApplicationException{
		if(pos < 0 || pos >= str.length()){
			throw new ApplicationException("position or len error!");
		}
		if(forward){
			if(pos + len >= str.length()){
				throw new ApplicationException("position or len error!");
			}
			return str.substring(pos, pos + len);
		}else{
			if(pos - len < 0){
				throw new ApplicationException("position or len error!");
			}
			return str.substring(pos - len + 1, pos + 1);
		}
	}
	
	/**
	 * Unicode position
	 * @return String��ʾchar
	 */
	public static String charAt(String str, int pos){
		try {
			int off = str.offsetByCodePoints(0, pos);
			int code = str.codePointAt(off);
			return new String(Character.toChars(code));
		} catch (Exception e) {
			return null;
		}
	}
	
	public static boolean isIdentifier(String code){
		int len = code.codePointCount(0, code.length());
		if(len == 1){
			String ch = charAt(code, 0);
			return SystemCharacter.isIdentifierFirst(ch);
		}else if(len > 1){
			String ch = charAt(code, 0);
			if(SystemCharacter.isIdentifierFirst(ch)){
				for(int i=1;i<len;i++){
					ch = charAt(code, i);
					if(!SystemCharacter.isIdentifierPart(ch)){
						return false;
					}
				}
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	
	public static boolean isInteger(String code){
		int len = code.length();
		if(len == 0){
			return false;
		}
		String str = code.trim();
		for(int i=1;i<len;i++){
			char ch = str.charAt(i);
			if(!(ch >= '0' && ch <= '9')){
				return false;
			}
		}
		return true;
	}
	
	public static String fill(String str, int len){
		if(str.length() > len){
			return str.substring(0, len);
		}else if(str.length() == len){
			return str;
		}
		String ret = str;
		for(int i = str.length(); i < len; i++){
			ret = ret + " ";
		}
		return ret;
	}
	
	/**
	 * return a Regular String to java String
	 * //at least ""
	 * �������Ϸ����,��˵���ǰ���������Ϸ��Լ�顣
	 * @param str
	 * @return
	 */
	public static String transferString(String str){
		int len = str.length();

//		if((str.charAt(0) != '\"') || (str.charAt(len - 1) != '\"')){
//			return false;
//		}
		if(len == 2){
			return ""; //empty string
		}
		String ret = "";
		int pos = 1;
		while(pos <= len - 2){
			char ch = str.charAt(pos);
			if(ch == '\\'){
				pos++;
				char next = str.charAt(pos);
				if(next == 'r'){
					ret = ret + '\r';
				}else if(next == 'n'){
					ret = ret + '\n';
				}else if(next == 'f'){
					ret = ret + '\f';
				}else if(next == 'b'){
					ret = ret + '\b';
				}else if(next == 't'){
					ret = ret + '\t';
				}else if(next == '\\'){
					ret = ret + '\\';
				}
				pos++;
				continue;
			}
//			int code = str.codePointAt(pos);
//			ret = ret +  new String(Character.toChars(code));
//			pos = pos + Character.charCount(code);
			ret = ret + str.charAt(pos);
			pos++;
		}
		return ret;
	}
	
	public static String transferBack(String str){
		int len = str.length();

		String ret = "\"";
		int pos = 0;
		while(pos <= len - 1){
			char next = str.charAt(pos);
			if(next == '\r'){
				ret = ret + '\\' + 'r';
			}else if(next == '\n'){
				ret = ret + '\\' + 'n';
			}else if(next == '\f'){
				ret = ret + '\\' + 'f';
			}else if(next == '\b'){
				ret = ret + '\\' + 'b';
			}else if(next == '\t'){
				ret = ret + '\\' + 't';
			}else if(next == '\\'){
				ret = ret + '\\' + '\\';
			}else{
				ret = ret + str.charAt(pos);
			}
			pos++;
		}
		

		ret = ret + "\"";
		return ret;
	}
	
	public static String fetchIdentifier(String str, int pos){
		int code = str.codePointAt(pos);
		String ch = String.valueOf(Character.toChars(code));
		if(!SystemCharacter.isIdentifierFirst(ch)){
			return "";
		}
		String ret = ch;
		while(pos<str.length()){
			code = str.codePointAt(pos);
			if(!SystemCharacter.isIdentifierPart(code)){
				break;
			}
			ret = ret + String.valueOf(Character.toChars(code));
			pos = pos + Character.charCount(code);
		}
		
		return ret;
	}
	
	public static String fetchInteger(String str, int pos){
		String ret = "";
		while(pos<str.length()){
			char ch = str.charAt(pos);
			if(!SystemCharacter.isDigit(ch)){
				break;
			}
			ret = ret + ch;
			pos = pos++;
		}
		
		return ret;
	}
	
	public static String getTimeNow() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		String ret = sdf.format(cal.getTime());
		return ret;
	}
}
