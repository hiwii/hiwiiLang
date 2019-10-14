package net.hiwii.db.bind;

import net.hiwii.cognition.Expression;
import net.hiwii.def.Declaration;
import net.hiwii.message.HiwiiException;
import net.hiwii.reg.EmptyString;
import net.hiwii.system.util.StringUtil;

import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;

public class DeclarationBinding extends TupleBinding<Declaration> {
	@Override
	public Declaration entryToObject(TupleInput arg0) {
		char type = arg0.readChar();

		if(type == 'a'){
			Declaration dec = new Declaration();
			String name = arg0.readString();
			dec.setName(name);

			String str = arg0.readString();
			Expression expr= StringUtil.parseString(str);
			if(expr instanceof HiwiiException){
				expr = new EmptyString();
			}
			dec.setStatement(expr);
			return dec;
		}
		return null;
	}

	@Override
	public void objectToEntry(Declaration arg0, TupleOutput arg1) {
		if(arg0 instanceof Declaration){
			char type = 'a';

			arg1.writeChar(type);
			arg1.writeString(arg0.getName());
			arg1.writeString(arg0.getStatement().toString());			
		}
	}
}
