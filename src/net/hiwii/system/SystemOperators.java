package net.hiwii.system;

import java.util.HashMap;
import java.util.Map;

import net.hiwii.system.exception.ApplicationException;
import net.hiwii.system.util.tuple.TwoTuple;

public class SystemOperators {
	public static Map<String, TwoTuple<String, Integer>> opmap = 
		new HashMap<String, TwoTuple<String, Integer>>();
	
	static{
		opmap.put("^", new TwoTuple<String, Integer>("pow", 60));
		opmap.put("!", new TwoTuple<String, Integer>("not", 60));
		
		opmap.put("@", new TwoTuple<String, Integer>("AT", 60));
		opmap.put("~", new TwoTuple<String, Integer>("tilde", 60));
		opmap.put("`", new TwoTuple<String, Integer>("FENG", 60));
		opmap.put("&&", new TwoTuple<String, Integer>("SCand", 60));
		opmap.put("||", new TwoTuple<String, Integer>("SCor", 60));
		opmap.put(">>", new TwoTuple<String, Integer>("RSHIFT", 60));
		opmap.put("<<", new TwoTuple<String, Integer>("LSHIFT", 60));
				
		opmap.put("*", new TwoTuple<String, Integer>("multiply", 50));
		opmap.put("%", new TwoTuple<String, Integer>("Percent", 50));
		opmap.put("/", new TwoTuple<String, Integer>("divide", 50));
		
		opmap.put("+", new TwoTuple<String, Integer>("plus", 40));
		opmap.put("-", new TwoTuple<String, Integer>("minus", 40));
		
		opmap.put("<>", new TwoTuple<String, Integer>("LTGT", 30));
		opmap.put(">", new TwoTuple<String, Integer>("GT", 30));
		opmap.put("<", new TwoTuple<String, Integer>("LT", 30));
		opmap.put(">=", new TwoTuple<String, Integer>("GE", 30));
		opmap.put("<=", new TwoTuple<String, Integer>("LE", 30));
		
//		opmap.put("=>", new TwoTuple<String, Integer>("lambda", 30));
		
		opmap.put("->", new TwoTuple<String, Integer>("belong", 25));
		opmap.put("<-", new TwoTuple<String, Integer>("contain", 25));
		
		opmap.put("::", new TwoTuple<String, Integer>("turn", 20));
		opmap.put("==", new TwoTuple<String, Integer>("LEQ", 20));
		
		opmap.put("!=", new TwoTuple<String, Integer>("NE", 20));
		
		opmap.put("&", new TwoTuple<String, Integer>("and", 10));
		opmap.put("|", new TwoTuple<String, Integer>("or", 0));
		
		opmap.put("?", new TwoTuple<String, Integer>("HOOK", -10));
		opmap.put("=", new TwoTuple<String, Integer>("EQ", -10));
		opmap.put(":=", new TwoTuple<String, Integer>("assign", -10));
					
		opmap.put(":", new TwoTuple<String, Integer>("describe", -20));
		
		//作为内部语句分隔符
		opmap.put(";", new TwoTuple<String, Integer>("SEMI", -30));
		//lambda定义， lambda 操作符被绑定到它后面的整个表达式。应用@，左结合
		opmap.put("=>", new TwoTuple<String, Integer>("lambda", -40));
		
	}
	public static boolean isOperator(String op){
		if (opmap.containsKey(op)){
			return true;
		}else{
			return false;
		}
	}
	
	public static int compare(String op1, String op2){
		return opmap.get(op1).getB() - opmap.get(op2).getB();
	}
	
	public static String getOperationName(String op) throws ApplicationException{
		if (opmap.containsKey(op)){
			return opmap.get(op).getA();
		}else{
			throw new ApplicationException("not operator!");
		}
	}
}
