package net.hiwii.system;

import java.util.HashMap;
import java.util.Map;

import net.hiwii.prop.Property;
import net.hiwii.view.Entity;

/**
 * ϵͳ���ԣ��κζ��󶼿���ʹ��
 * @author a
 *
 */
public class SystemProperty extends Entity {
	private static Map<String, Property> sysprops = new HashMap<String, Property>();
	
	static{
		Property idprop = new Property();
		idprop.setName("IDENTIFIER");
		idprop.setType("String");
		sysprops.put("IDENTIFIER", idprop);   //�����Ψһ����
	}
	
	public static Property getProperty(String str){
		if(sysprops.containsKey(str)) {
			return sysprops.get(str);
		}else {
			return null;
		}
	}
}
