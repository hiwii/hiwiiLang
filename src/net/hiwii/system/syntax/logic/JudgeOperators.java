package net.hiwii.system.syntax.logic;

import java.util.ArrayList;
import java.util.List;

import net.hiwii.system.parser.ScriptParserConstants;

public class JudgeOperators implements ScriptParserConstants{
//	private static String EQ = "==";
//	private static String GE = ">=";
//	private static String LE = "<=";
//	private static String GT = ">";
//	private static String LT = "<";
//	private static String NE = "!=";
	
	public static boolean isOperator(int op){
		if(op == EQ || op == GE || op == LE || op == GT || op == LT || op == NE){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean isOperator(String op){
		List<String> list = new ArrayList<String>();
		list.add("=");
		list.add("==");
		list.add(">=");
		list.add("<=");
		list.add(">");
		list.add("<");
		list.add("!=");
		list.add(">>");
		list.add("<<");
		list.add("+>");
		list.add("<+");
		list.add("->");
		list.add("<-");		
		list.add("^=");
		list.add("!>");
		list.add("<!");
		list.add("%=");
		list.add("~=");
		list.add("+=");
		list.add("-=");
		if(list.contains(op)){
			return true;
		}else{
			return false;
		}
	}
}
