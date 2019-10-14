package net.hiwii.reg.atom;

import java.util.HashMap;
import java.util.Map;

import net.hiwii.reg.RegularExpression;
import net.hiwii.view.Entity;

public class SystemPattern extends Entity {
	public static Map<String,RegularExpression> patterns =
			new HashMap<String,RegularExpression>();
	static{
		patterns.put("IdentifierPattern", new IdentifierRegular());
	}

	public static RegularExpression getRegular(String name){
		if(patterns.containsKey(name)){
			return patterns.get(name);
		}else{
			return null;
		}
	}
}
