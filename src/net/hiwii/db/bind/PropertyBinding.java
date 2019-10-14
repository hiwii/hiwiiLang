package net.hiwii.db.bind;

import java.util.ArrayList;
import java.util.List;

import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;

import net.hiwii.cognition.Expression;
import net.hiwii.prop.Property;
import net.hiwii.system.util.StringUtil;

public class PropertyBinding extends TupleBinding<Property> {

	@Override
	public Property entryToObject(TupleInput arg0) {
		Property prop = new Property();
		String name = arg0.readString();
		prop.setName(name);
		String dn = arg0.readString(); //definition name
		prop.setType(dn);
		int num = arg0.readInt();
		prop.setNumber(num);
		
		int len = arg0.readInt();
		List<Expression> limits = new ArrayList<Expression>();
		for(int i=0;i<len;i++){
			String str = arg0.readString();
			Expression expr = StringUtil.parseString(str);
			limits.add(expr);
		}
		prop.setLimits(limits);

		return prop;
		
	}

	@Override
	public void objectToEntry(Property arg0, TupleOutput arg1) {
		arg1.writeString(arg0.getName());
		arg1.writeString(arg0.getType());
		arg1.writeInt(arg0.getNumber());
		int len = arg0.getLimits().size();
		arg1.writeInt(len);
		for(Expression expr:arg0.getLimits()){
			arg1.writeString(expr.toString());
		}
	}

}
