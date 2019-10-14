package net.hiwii.db.bind.func;

import java.util.ArrayList;
import java.util.List;

import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;

import net.hiwii.cognition.Expression;
import net.hiwii.def.decl.FunctionDeclaration;
import net.hiwii.system.util.StringUtil;

/**
 * type = f; functionDeclaration
 * type = c; conditionDeclaration
 * @author Administrator
 *
 */
public class FunctionDeclarationBinding extends TupleBinding<FunctionDeclaration> {
	@Override
	public FunctionDeclaration entryToObject(TupleInput arg0) {
		FunctionDeclaration fd = new FunctionDeclaration();
		int num = arg0.readInt();
		List<String> list = new ArrayList<String>();
		for(int n=0;n<num;n++){
			String str = arg0.readString();
			list.add(str);
		}
		fd.setArguments(list);
		List<Expression> types = new ArrayList<Expression>();
		for(int n=0;n<num;n++){
			String str = arg0.readString();
			Expression exp = StringUtil.parseString(str);
			types.add(exp);
		}
		fd.setArgType(types);
		String str = arg0.readString();
		Expression expr = StringUtil.parseString(str);
		fd.setStatement(expr);
		return fd;
	}

	@Override
	public void objectToEntry(FunctionDeclaration arg0, TupleOutput arg1) {
		arg1.writeInt(arg0.getArguments().size());
		for(String arg:arg0.getArguments()){
			arg1.writeString(arg);
		}
		for(Expression arg:arg0.getArgType()){
			arg1.writeString(arg.toString());
		}
		//statement
		arg1.writeString(arg0.getStatement().toString());
	}
}
