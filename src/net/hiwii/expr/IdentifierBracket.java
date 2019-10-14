package net.hiwii.expr;

import java.util.List;

import net.hiwii.cognition.Expression;
import net.hiwii.message.HiwiiException;
import net.hiwii.system.syntax.number.IntegerNumber;

/**
 * 用来表示数组，同名对象等
 * @author Administrator
 *
 */
public class IdentifierBracket extends Expression {
	private String name;
	private List<Expression> arguments;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Expression> getArguments() {
		return arguments;
	}
	public void setArguments(List<Expression> arguments) {
		this.arguments = arguments;
	}
	
	@Override
	public Expression getEnhancedArgument(List<Expression> args) {
		if(name.equals("argument")){
			if(arguments.size() != 1){
				return new HiwiiException();
			}
			if(!(arguments.get(0) instanceof IntegerNumber)){
				return new HiwiiException();
			}
			IntegerNumber in = (IntegerNumber) arguments.get(0);
			int n = Integer.parseInt(in.getValue());
			if(n < 1 || n > args.size()){
				return new HiwiiException();
			}
			return args.get(n - 1);
		}
		return this;
	}
	
	@Override
	public String toString() {
		String ret = name + "[";
		int i = 0;
		for(Expression exp:arguments){
			if(i == 0)
				ret = ret + exp.toString();
			else// if(i == size - 1)
				ret = ret + "," + exp.toString();
		}
		ret = ret + "]";
		return ret;
	}
}
