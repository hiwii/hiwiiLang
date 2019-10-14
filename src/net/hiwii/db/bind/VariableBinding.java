package net.hiwii.db.bind;

import java.util.ArrayList;
import java.util.List;

import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;

import net.hiwii.cognition.Expression;
import net.hiwii.prop.VariableStore;
import net.hiwii.system.util.StringUtil;

public class VariableBinding extends TupleBinding<VariableStore> {
	@Override
	public VariableStore entryToObject(TupleInput arg0) {
		VariableStore prop = new VariableStore();
		String dn = arg0.readString(); //definition name
		prop.setType(dn);
		
		int len = arg0.readInt();
		List<Expression> limits = new ArrayList<Expression>();
		for(int i=0;i<len;i++){
			String str = arg0.readString();
			Expression expr = StringUtil.parseString(str);
			limits.add(expr);
		}
		prop.setLimits(limits);

		char valueType = arg0.readChar();
		prop.setValueType(valueType);
		String val = arg0.readString();
		prop.setValue(val);
		return prop;
		
	}

	@Override
	public void objectToEntry(VariableStore arg0, TupleOutput arg1) {
		arg1.writeString(arg0.getType());
		int len = arg0.getLimits().size();
		arg1.writeInt(len);
		for(Expression expr:arg0.getLimits()){
			arg1.writeString(expr.toString());
		}
		arg1.writeChar(arg0.getValueType());
		arg1.writeString(arg0.getValue());
	}

}
