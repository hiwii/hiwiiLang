package net.hiwii.db.bind;

import java.util.ArrayList;
import java.util.List;

import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;

import net.hiwii.cognition.Expression;
import net.hiwii.expr.StringExpression;

public class ListExpressionBinding extends TupleBinding<List<Expression>> {

	@Override
	public List<Expression> entryToObject(TupleInput arg0) {
		int len = arg0.readInt();
		List<Expression> result = new ArrayList<Expression>();
		for(int i=0;i<len;i++){
			String str = arg0.readString();
			Expression expr = new StringExpression(str);
			result.add(expr);
		}
		return result;
	}

	@Override
	public void objectToEntry(List<Expression> arg0, TupleOutput arg1) {
		int len = arg0.size();
		arg1.writeInt(len);
		for(Expression expr:arg0) {
			if(expr instanceof StringExpression) {
				StringExpression se = (StringExpression) expr;
				arg1.writeString(se.getValue());
			}else {
				arg1.writeString(expr.toString());
			}
		}
	}

}
