package net.hiwii.db.bind.func;

import java.util.ArrayList;
import java.util.List;

import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;

import net.hiwii.cognition.Expression;
import net.hiwii.def.decl.MappingDeclaration;
import net.hiwii.system.util.StringUtil;

/**
 * type = f; functionDeclaration
 * type = c; conditionDeclaration
 * @author Administrator
 *
 */
public class MappingDeclarationBinding extends TupleBinding<MappingDeclaration> {
	@Override
	public MappingDeclaration entryToObject(TupleInput arg0) {
		MappingDeclaration fd = new MappingDeclaration();
		int num = arg0.readInt();
		List<String> list = new ArrayList<String>();
		for(int n=0;n<num;n++){
			String str = arg0.readString();
			list.add(str);
		}
		fd.setArguments(list);
		String str = arg0.readString();
		Expression expr = StringUtil.parseString(str);
		fd.setStatement(expr);
		return fd;
	}

	@Override
	public void objectToEntry(MappingDeclaration arg0, TupleOutput arg1) {
		arg1.writeInt(arg0.getArguments().size());
		for(String arg:arg0.getArguments()){
			arg1.writeString(arg);
		}
		//statement
		arg1.writeString(arg0.getStatement().toString());
	}
}
