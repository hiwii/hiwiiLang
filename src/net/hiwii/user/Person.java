package net.hiwii.user;

import net.hiwii.def.Definition;
import net.hiwii.prop.Property;

public class Person extends Definition {

	public Person() {
		super();
		setName("Person");
		setSignature("Person");
		
		Property prop1 = new Property();
		prop1.setName("asUser");
		prop1.setType("User");
		prop1.setNumber(1);
		prop1.setOption(false);
		prop1.setUnique(true);
//		getProps().put(prop1.getName(), prop1);
	}

}
