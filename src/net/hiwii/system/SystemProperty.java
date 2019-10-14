package net.hiwii.system;

import java.util.HashMap;
import java.util.Map;

import net.hiwii.prop.Property;
import net.hiwii.view.Entity;

/**
 * 系统属性，任何对象都可以使用
 * @author a
 *
 */
public class SystemProperty extends Entity {
	private static Map<String, Property> sysprops = new HashMap<String, Property>();
	
	static{
		Property idprop = new Property();
		idprop.setName("IDENTIFIER");
		idprop.setType("String");
		sysprops.put("IDENTIFIER", idprop);   //对象的唯一名字
	}
	
	public static Property getProperty(String str){
		if(sysprops.containsKey(str)) {
			return sysprops.get(str);
		}else {
			return null;
		}
	}
}
