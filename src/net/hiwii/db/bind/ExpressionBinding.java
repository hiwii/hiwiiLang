package net.hiwii.db.bind;

import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;

import net.hiwii.cognition.Expression;

public class ExpressionBinding extends TupleBinding<Expression> {

	@Override
	public Expression entryToObject(TupleInput arg0) {
		Expression expr = new Expression();
		char type = arg0.readChar();
		String str = arg0.readString();
		
		return expr;
	}

	@Override
	public void objectToEntry(Expression expr, TupleOutput arg1) {
		char type = 'i';
		
		arg1.writeChar(type);
//		arg1.writeString(str);
	}

}
