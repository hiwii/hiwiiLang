package net.hiwii.user;

import net.hiwii.def.Definition;
import net.hiwii.prop.Property;

public class UserDefinition extends Definition {

	public UserDefinition() {
		super();
		setName("User");
		setSignature("User");
//		Property prop1 = new Property();
//		prop1.setName("id");
//		prop1.setType("String");
//		prop1.setOption(false);
//		prop1.setUnique(true);
//		getProps().put(prop1.getName(), prop1);
//		Property prop2 = new Property();
//		prop2.setName("name");
//		prop2.setType("String");
//		prop2.setOption(false);
//		prop2.setUnique(true);
//		getProps().put(prop2.getName(), prop2);
		Property prop3 = new Property();
		prop3.setName("password");
		prop3.setType("String");
//		getProps().put(prop3.getName(), prop3);
//		Property prop4 = new Property();
//		prop4.setName("mail");
//		prop4.setType("String");
//		getProps().put(prop4.getName(), prop4);
	}

}
