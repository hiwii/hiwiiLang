package net.hiwii.user;

import net.hiwii.def.Definition;
import net.hiwii.prop.Property;

public class GroupDefinition extends Definition {

	public GroupDefinition() {
		super();
		setName("Group");
		setSignature("Group");
		
		Property prop1 = new Property();
		prop1.setName("member");
		prop1.setType("User");
		prop1.setNumber(-1);
		prop1.setOption(false);
		prop1.setUnique(true);
//		getProps().put(prop1.getName(), prop1);
		
	}
}
